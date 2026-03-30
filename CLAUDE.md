# NovaLedger — Claude Code 開發說明

## 啟動時必讀
專案背景、完整進度、Schema 定義在以下三個檔案，開始任何任務前必須先讀：

- `_claude_context/NovaLedger — Project Context for Claude.md`
- `_claude_context/NovaLedger CheckList v3.md`
- `_claude_context/NovaLedger - 專案架構.md`

---

## 技術棧
- Spring Boot 3.x
- MySQL 8.0 + Flyway 9.22.3
- Spring Security（session-based form login）+ JWT（Bearer token）
- springdoc-openapi / Swagger UI
- Docker + Railway 部署
- 本機測試指令：`mvn clean test "-Dskip.npm=true"`

---

## 模組結構
```
novaledger/
├── application/   # 啟動入口
├── auth/          # 登入、JWT、Security
├── common/        # BaseEntity、共用工具、Response wrapper
├── finance/       # 帳戶、信用卡、交易等功能模組
└── ...
```

### Module Dependency Rules
- `application` → 可依賴所有 module
- `auth` → 不可依賴 finance / tenant
- `common` → 可被所有 module 使用
- 禁止循環依賴

---

## Package 組織方式
finance 模組內按功能分包，不按技術層分包：
```
finance/
├── account/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── creditcard/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
└── ...
```

---

## Coding Style（強制）

**注入方式**
- Constructor Injection only
- 禁止 field injection（禁止在 field 上用 `@Autowired`）

**分層職責**
- Controller：只處理 request / response，不寫業務邏輯
- Service：所有業務邏輯放這裡，加 `@Transactional`
- Repository：只做資料存取，不寫業務邏輯

**DTO / Entity**
- 嚴格分離，Entity 不可直接回傳給 Controller
- Response DTO 用靜態工廠方法：`CardResponse.from(entity)`

**命名規則**
- 方法以動詞開頭：`createCard` / `getCards` / `deleteCard`

**Response 結構**
- 統一使用 `ApiResponse<T>` wrapper
- 有資料：`ApiResponse.ok(data)` → HTTP 200 或 201
- 無資料（刪除）：`ApiResponse.ok()` → HTTP 200
- 錯誤：`ApiResponse.fail(ApiErrorResponse)` → 由 GlobalExceptionHandler 處理

**例外處理**
- 統一由 `GlobalExceptionHandler` 處理
- Service 拋出 `BusinessException`（帶 `ErrorCode`），不要直接拋 `RuntimeException`

**userId 傳入方式**
- 目前統一用 `@RequestParam Long userId` 傳入，不從 SecurityContext 取，不用 `@AuthenticationPrincipal`

**DTO 設計慣例**
- create 和 update 是否共用同一個 Request DTO，以各功能模組現有寫法為準
- 新模組預設 create / update 分開（CreateXxxRequest / UpdateXxxRequest）
- 若欄位完全相同可共用，但需在 class 上加註解說明

**抽共用邏輯的原則**
- 相同邏輯出現兩次以上就考慮抽共用
- 不要讓同一個修改需要動到兩三個地方
- 有更簡潔的寫法時主動提出，不要只是照抄現有寫法複製貼上

---

## 多租戶設計（重要）
- 所有 tenant-aware 的 entity 繼承 `BaseTenantEntity`
- 所有 query 必須帶 `tenantId` 條件
- tenantId 從 `TenantContext.getTenantId()` 取得（ThreadLocal）
- 軟刪除欄位：`deletedAt`，查詢時條件加 `AND deleted_at IS NULL`


---

## Schema 參考
需要了解任何 table 的欄位定義時，直接讀 Flyway migration 檔案，不要假設欄位。
路徑：`application/src/main/resources/db/migration/`

各檔案對應內容：
- V1：users, tenants
- V2：rbac（roles, permissions, user_tenants）
- V3：system master data（banks, currencies, categories 等）
- V4：subscription_plans, tenant_subscriptions, user_limits
- V5：user_accounts, account_balances, investment 相關
- V6：transactions, transaction_items, transaction_tags
- V7：user_budgets, recurring_transactions
- V8：asset_snapshots, exchange_rates
- V9：upload_jobs, upload_files, parsed_records, import_logs
- V10：report_definitions, report_results, scheduled_tasks
- V11：seed bank_branches
- V12：seed test users
- V13：seed categories
- V14：seed user_tenants

需要某張 table 的 DDL 時，先對照上表找到對應檔案再去讀，不要掃描全部檔案。
---

## Entity 規則
- 繼承 `BaseEntity`（id, createdAt, updatedAt）
- tenant-aware entity 繼承 `BaseTenantEntity`（extends BaseEntity，含 tenantId）
- 使用 Lombok
- 日期欄位用 `LocalDateTime`
- 新功能需要新增錯誤碼時，統一加到 `ErrorCode.java`
  路徑：`common/src/main/java/com/example/novaledger/common/exception/ErrorCode.java`
  命名慣例：`模組縮寫_流水號`，例如 `ACCOUNT_001`、`CARD_001`
  不要自己建新的 enum 或 class

**Lombok 使用規則**
- DTO / Request / Response 類別：使用 `@Data`
- Entity：預設不用 `@Data`（會影響 JPA lazy loading、equals/hashCode），改用手動 getter / setter
- 例外：Entity 欄位單純、無關聯、無繼承時可用 `@Data`，但有疑慮時一律改回手動

---

## 測試規則
- 使用 `@ExtendWith(MockitoExtension.class)`
- 使用 `@Mock` + `@InjectMocks`，不用 `@SpringBootTest`
- `@WebMvcTest` 的 test class 需加 `@MockitoBean JwtTokenProvider jwtTokenProvider`，否則 context 載入失敗
- 只測試有邏輯值得保護的地方：純 CRUD entity / repository 不強制寫測試
- Assert 用 AssertJ：`assertThat(...).isEqualTo(...)`
- 測試方法命名：`should_[預期結果]_when_[條件]()`
- `@WebMvcTest` 遇到 Security context 載入失敗時，
  除了 `@MockitoBean JwtTokenProvider` 之外，
  可能還需要 `@Import(SecurityConfig.class)`，
  實際以能跑通為準，確認後更新此文件

測試範例風格：
```java
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void should_set_deleted_at_when_delete_card() {
        // arrange
        // act
        // assert
    }
}
```

---

## 注意事項
- Flyway migration 檔案一旦 commit 後視為 read-only，schema 變更必須新增版本
- 新增 public API endpoint 需同步更新：
  1. `SecurityConfig.PUBLIC_PATHS`
  2. `TenantInterceptor.PUBLIC_ENDPOINTS`
- `/api/cards` 等需要認證的 endpoint 不加入 public whitelist
- 方案限制（FREE plan 上限檢查）目前跳過，待 G 系列實作
- root `pom.xml` 已設定 `maven-compiler-plugin` 加上 `-parameters` flag，
  目的是讓 Swagger 正確顯示 `@RequestParam` 參數名稱，新增模組不需要重複設定

## 遇到問題時的處理原則
- 預設以「立即修好」為目標，不累積技術債
- 以下情況例外，改為提建議並記錄 Linear：
  - 影響範圍跨多個模組
  - 需要大量修改測試
  - 評估無法在當天開發時間內修好
- 回報格式：
  1. 說明問題點
  2. 評估影響範圍
  3. 是否會影響當前進度
  4. 建議：立即修 / 記 Linear 待 MVP 後處理