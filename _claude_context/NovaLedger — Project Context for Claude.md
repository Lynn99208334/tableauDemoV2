---
type:
project: NovaLedger
module:
date: 2026-03-24
status: open
tags: []
up:
  - "[[NL_dev_plan]]"
---
# NovaLedger — Project Context for Claude
> 更新日期：2026-03-24
> 此文件為 Claude 專案唯一真實來源（Single Source of Truth）
> 每次有重大進度變更時更新此文件並重新上傳至 Claude Project
---
## 一、技術棧 & 架構

|層級|技術|
|---|---|
|框架|Spring Boot 3.x，Modular Monolith|
|資料庫|MySQL 8.0 + Flyway 9.22.3|
|認證|Spring Security（session-based form login）+ JWT（Bearer token）|
|API 文件|springdoc-openapi / Swagger UI|
|容器化|Docker（multi-stage Dockerfile + docker-compose）|
|部署|Railway（已部署，初始驗證完成）|
|測試工具|Postman|                                                  |


### 模組結構

```

novaledger/

├── auth/         # 登入、JWT、Security
├── user/         # 使用者管理
├── tenant/       # 多租戶邏輯
├── common/       # BaseEntity、共用工具
└── ...           # B~R 系列待開發

```

### Module Dependency Rules
- `application` → 可依賴所有 module
- `auth` → 不可依賴 tenant
- `tenant` → 不可依賴 auth
- `common` → 可被所有 module 使用
- ❗ 禁止循環依賴

---
## 二、AI 行為規則（必須遵守）

Claude 在此專案中必須遵守以下規則，不得違反：

**架構約束**
- MUST 使用 Modular Monolith，MUST NOT 拆成 Microservices
- MUST 保持 module 邊界清晰，MUST NOT 讓模組互相亂依賴
- MUST NOT 在 Controller 寫 business logic
- MUST NOT 在 Repository 寫業務邏輯
- MUST 透過 Service 層進行跨模組呼叫
- MUST 所有查詢帶 tenant_id（多租戶隔離不可省略）
  

**回覆風格**
- 優先給「可執行解法」，避免長篇理論
- 若有錯誤，直接指出問題點
- 不建議引入不必要的 framework 或過度抽象設計  

---

## 三、Coding Style（強制）

| 規則 | 說明 |
|------|------|
| 注入方式 | Constructor Injection，禁止 field injection（`@Autowired` on field） |
| 分層職責 | Controller 僅處理 request/response，Service 處理業務，Repository 僅存取資料 |
| DTO / Entity | 嚴格分離，Entity 不暴露給 Controller |
| 例外處理 | 統一使用 `GlobalExceptionHandler` |
| Transaction | 使用 `@Transactional` 控制，放在 Service 層 |
| 命名規則 | 方法以動詞開頭：`createUser` / `getTenant` / `deleteRole` |
| Response 結構 | Controller 回傳統一 Response wrapper |

---

## 四、多租戶設計
- 每張 table 含 `TENANT_ID` 欄位
- 所有 query 必須帶 `TENANT_ID` 條件
- 使用 `TenantInterceptor` 注入 tenant context
- 使用 `ThreadLocal` 儲存當前 tenant（`TenantContext`）
- 複合唯一鍵：`UNIQUE(TENANT_ID, ID)`
---
## 五、資料庫 Migration 進度

| Migration | 內容 | 狀態 |
|-----------|------|------|
| V1～V13 | 34 張 table DDL + sys_admin/alice/bob seed data | ✅ 完成 |

---

## 六、開發進度

### ✅ 已完成

- Phase 0 全部（Schema / Flyway V1–V13 / Seed Data / 架構地基層）
- A 系列全部（springdoc / Swagger / Spring Security / jjwt / JwtTokenProvider / TenantContext / 全 Entity & Repository / AuthController register+login / UserService / 前端 Login/Register / JWT redirect / Postman 測試通過）
- Docker / Railway 初始部署（Dockerfile / docker-compose / Railway 環境變數設定 / Migration 驗證）

### 🔄 進行中
- B 系列（帳戶與信用卡）

### ⏳ 未開始
- H｜Excel 匯入
- C｜交易記帳
- D｜分類與標籤
- E｜Dashboard
- F｜匯率與換算
- G｜方案與訂閱
- I｜管理員後台
- P｜預算
- R｜報表

---
## 七、目前開發重點（最高優先）

> Claude 回答時請優先協助以下任務


---
## 八、已知問題

| 問題                                   | 處理方式     | 日期         | 狀態    |
| ------------------------------------ | -------- | ---------- | ----- |
| [bug] 手動更新密碼未經 encode 導致 BCrypt 比對失敗 | Linear紀錄 | 2026/03/26 | 🔴未解決 |


---
## 九、設計決策紀錄

| 決策 | 選擇 | 原因 |
|------|------|------|
| 架構模式 | Modular Monolith | 初期複雜度控制，未來可拆分 |
| 認證機制 | Session-based + JWT 並存 | 支援 Web 與 API client 兩種情境 |
| ORM | JPA + Hibernate | Spring Boot 標準整合 |
| Migration 工具 | Flyway | 版本控制友善，適合多環境部署 |
| 未來 AI 功能 | Python/FastAPI microservice（規劃中） | NL2SQL、費用分類、RAG 建議 |

---
## 十、Error Handling Strategy

- 所有 Exception 統一由 `GlobalExceptionHandler` 處理
- 禁止在 Controller 內 try-catch business exception
- Service 層可拋出自定義 exception（如 `BusinessException`）
- 禁止寫 `catch(Exception e) { return "fail"; }` 這類吞掉例外的寫法
- 回傳格式統一包含：`errorCode` / `message` / `timestamp`

---
## 十一、Design Priority
> 當有多種解法時，Claude 依此優先順序選擇：

1. Simplicity > Flexibility
2. Readability > Abstraction
3. Current Phase Fit > Future Scalability
---
## 十二、Guardrails（禁止事項）

Claude 不得主動建議：

- 改成 Microservices 架構
- 引入 MapStruct、Lombok（optional）等非必要 framework
- 過度抽象（多一層 interface 沒有實際好處的情況）
- 省略 tenant 隔離條件
- 在 Controller 層寫邏輯
---
## 十三、更新紀錄

| 日期 | 更新內容 |
|------|----------|
| 2026-03-24 | v3：新增 Error Handling Strategy、Design Priority |
| 2026-03-24 | v2：新增 AI 行為規則、Coding Style、開發重點、Guardrails |
| 2026-03-24 | v1：初版建立 |
| 2026-03-27 | V4: 更新開發進度(進行中:B系列)
