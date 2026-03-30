---
type: task
project: NovaLedger
category: toDoList
status:
  - 🚧
start: 2026-01-26
due: 2026-04-30
up:
  - "[[0️⃣ Dashboard - 每日檢視中心/dashboard|dashboard]]"
  - "[[Nova dashboard]]"
  - "[[1_0_0_NovaLedger]]"
  - "[[NovaLedger_Progress]]"
---
# NovaLedger 開發 Checklist3（2026/01/31 - 2026/04/30）

---

## 🧱 Phase 0：資料表設計與 Schema 建立（1/31 - 2/26）

### Week 1-2：Phase 1 資料表設計（1/31 - 2/13）

**Day 1-3：RBAC 表設計（1/31 - 2/2）**
- [x] USERS 表設計（PK + 索引 + 欄位定義） 📅 2026-01-31 ✅ 2026-02-01
- [x] TENANTS 表設計（PK + 索引） 📅 2026-01-31 ✅ 2026-02-01
- [x] PERMISSIONS 表設計（資源 + 行為） 📅 2026-02-01 ✅ 2026-02-01
- [x] ROLES 表設計（系統預設 + 自訂角色） 📅 2026-02-01 ✅ 2026-02-01
- [x] ROLE_PERMISSIONS 表設計（多對多關聯） 📅 2026-02-02 ✅ 2026-02-01
- [x] USER_TENANTS 表設計（使用者與租戶關聯） 📅 2026-02-02 ✅ 2026-02-01

**Day 4-5：系統主檔表設計（2/3 - 2/4）**
- [x] CURRENCIES 表設計（幣別主檔） 📅 2026-02-03 ✅ 2026-02-02
- [x] INVESTMENT_TYPES 表設計（投資工具分類） 📅 2026-02-03 ✅ 2026-02-02
- [x] TRANSACTION_TYPES 表設計（交易類型定義） 📅 2026-02-03 ✅ 2026-02-02
- [x] BANKS 表設計（銀行主檔，無 tenant_id） 📅 2026-02-04 ✅ 2026-02-02
- [x] BANK_BRANCHES 表設計（銀行分行） 📅 2026-02-04 ✅ 2026-02-02
- [x] CATEGORIES 表設計（消費類別，支援系統預設） 📅 2026-02-04 ✅ 2026-02-02

**Day 6-7：訂閱管理表設計（2/5 - 2/6）**
- [x] SUBSCRIPTION_PLANS 表設計（方案規格與限制） 📅 2026-02-05 ✅ 2026-02-02
- [x] TENANT_SUBSCRIPTIONS 表設計（租戶訂閱狀態） 📅 2026-02-05 ✅ 2026-02-02
- [x] USER_LIMITS 表設計（資源使用限制與已使用數量） 📅 2026-02-06 ✅ 2026-02-02

**Day 8：投資工具表設計（2/7）**
- [x] INVESTMENT_PRODUCTS 表設計（投資產品主檔） 📅 2026-02-07 ✅ 2026-02-02

**Day 9-10：使用者啟用資料表設計（2/8 - 2/9）**
- [x] USER_ACCOUNTS 表設計（tenant-aware + deleted_at） 📅 2026-02-08 ✅ 2026-02-02
- [x] USER_CREDIT_CARDS 表設計（tenant-aware + deleted_at） 📅 2026-02-08 ✅ 2026-02-02

**Day 11-12：分類標籤與交易表設計（2/10 - 2/11）**
- [x] USER_TAGS 表設計（tenant-aware） 📅 2026-02-10 ✅ 2026-02-02
- [x] TRANSACTION_TAGS 表設計（多對多關聯） 📅 2026-02-10 ✅ 2026-02-02
- [x] TRANSACTIONS 表設計（tenant-aware + deleted_at） 📅 2026-02-11 ✅ 2026-02-02
- [x] TRANSACTION_ITEMS 表設計（交易明細） 📅 2026-02-11 ✅ 2026-02-02
- [x] ACCOUNT_BALANCES 表設計（餘額快照） 📅 2026-02-11 ✅ 2026-02-02
- [x] RECURRING_TRANSACTIONS 表設計（週期性交易模板） 📅 2026-02-11 ✅ 2026-02-02

**Day 13：預算表設計（2/12）**
- [x] USER_BUDGETS 表設計（tenant-aware） 📅 2026-02-12 ✅ 2026-02-02

**Day 14：Phase 1 表設計驗收（2/13）**
- [x] 檢查所有表的 tenant_id 索引 📅 2026-02-13 ✅ 2026-02-04
- [x] 檢查所有表的 deleted_at 索引 📅 2026-02-13 ✅ 2026-02-04
- [x] 檢查 FK 完整性與 on delete 策略 📅 2026-02-13 ✅ 2026-02-04
- [x] Phase 1 表設計完成（25 張表） 📅 2026-02-13 ✅ 2026-02-04

---

### Week 3：Phase 2 資料表設計（2/14 - 2/20）

**Day 15-16：上傳與解析表設計（2/14 - 2/15）**
- [x] UPLOAD_JOBS 表設計（tenant-aware） 📅 2026-02-14 ✅ 2026-02-04
- [x] UPLOAD_FILES 表設計（檔案元數據） 📅 2026-02-14 ✅ 2026-02-04
- [x] PARSED_RECORDS 表設計（解析暫存資料） 📅 2026-02-15 ✅ 2026-02-04
- [x] IMPORT_LOGS 表設計（匯入日誌） 📅 2026-02-15 ✅ 2026-02-04

**Day 17-18：報表與快照表設計（2/16 - 2/17）**
- [x] ASSET_SNAPSHOTS 表設計（tenant-aware） 📅 2026-02-16 ✅ 2026-02-06
- [x] REPORT_DEFINITIONS 表設計（報表定義） 📅 2026-02-16 ✅ 2026-02-06
- [x] REPORT_RESULTS 表設計（tenant-aware） 📅 2026-02-17 ✅ 2026-02-06

**Day 19：系統任務表設計（2/18）**
- [x] SCHEDULED_TASKS 表設計（排程任務） 📅 2026-02-18 ✅ 2026-02-04

**Day 20：匯率表設計（2/19）**
- [x] EXCHANGE_RATES 表設計（匯率歷史快照） 📅 2026-02-19 ✅ 2026-02-06

**Day 21：Phase 2 表設計驗收（2/20）**
- [x] 檢查所有表的索引設計 📅 2026-02-20 ✅ 2026-02-06
- [x] Phase 2 表設計完成（9 張表） 📅 2026-02-20 ✅ 2026-02-06
- [x] 總計 34 張表設計完成確認 📅 2026-02-20 ✅ 2026-02-06

---

### Week 4：Migration、Seed Data 與部署準備（2/21 - 2/26）

**Day 22-23：Migration 與 Seed Data（2/21 - 2/22）**
- [x] 建立 Flyway 目錄結構 📅 2026-02-21 ✅ 2026-03-16
- [x] 撰寫 V1__init.sql（所有 34張表的 DDL） 📅 2026-02-21 ✅ 2026-03-16
- [x] 撰寫 V2__seed_banks.sql（至少 20 家銀行） 📅 2026-02-22 ✅ 2026-03-16
- [x] 撰寫 V3__seed_categories.sql（10-15 個預設分類） 📅 2026-02-22 ✅ 2026-03-16
- [x] 撰寫 V4__seed_test_users.sql（admin + demo 帳號） 📅 2026-02-22 ✅ 2026-03-16

**Day 24：本機驗證（2/23）**
- [x] 設定 application.yml（MySQL 連線） 📅 2026-02-23 ✅ 2026-03-16
- [x] 執行 migration 驗證（DROP + CREATE + run） 📅 2026-02-23 ✅ 2026-03-16
- [x] 檢查 seed data 載入成功 📅 2026-02-23 ✅ 2026-03-16
- [x] 宣告 Schema Freeze 📅 2026-02-23 ✅ 2026-03-16

---

## 🔐 Phase 1：MCP 核心功能開發（2/27 - 4/8）

### A｜註冊與登入（3/3 - 3/13，共 11 天）

**跳過：2/27-3/2（228 連假）**

**Day 1-2：JWT 與 Security 基礎（3/3 - 3/4）**
- [x] 加入 springdoc-openapi 依賴 📅 2026-03-03 ✅ 2026-03-18
- [x] 設定 Swagger UI 路徑（/swagger-ui.html） 📅 2026-03-03 ✅ 2026-03-18
- [x] 加入 Spring Security 依賴 📅 2026-03-03 ✅ 2026-03-20
- [x] 加入 jjwt 依賴 📅 2026-03-04 ✅ 2026-03-20
- [x] 實作 JwtTokenProvider（generateToken, validateToken） 📅 2026-03-04 ✅ 2026-03-20
- [x] 實作 TenantContext（ThreadLocal 管理） 📅 2026-03-04 ✅ 2026-03-20

**Day 3-4：Entity 與 Repository（3/5 - 3/6）**
- [x] 建立 BaseEntity（id, created_at, updated_at） 📅 2026-03-05 ✅ 2026-03-20
- [x] 建立 BaseTenantEntity（extends BaseEntity, tenant_id） 📅 2026-03-05 ✅ 2026-03-20
- [x] 建立 User Entity 📅 2026-03-05 ✅ 2026-03-20
- [x] 建立 Tenant Entity 📅 2026-03-06 ✅ 2026-03-20
- [x] 建立 Role Entity 📅 2026-03-06 ✅ 2026-03-20
- [x] 建立 UserTenant Entity 📅 2026-03-06 ✅ 2026-03-20
- [x] 建立對應 Repository 介面 📅 2026-03-06 ✅ 2026-03-20

**Day 5-6：註冊與登入 API（3/7 - 3/8）**
- [x] 實作 AuthController.register（含 Swagger 註解） 📅 2026-03-07 ✅ 2026-03-20
- [x] 實作 UserService.register（建立 user + tenant + user_tenant） 📅 2026-03-07 ✅ 2026-03-20
- [x] 測試註冊流程（Postman） 📅 2026-03-07 ✅ 2026-03-20
- [x] 實作 AuthController.login（含 Swagger 註解） 📅 2026-03-08 ✅ 2026-03-20
- [x] 實作 UserService.login（驗證密碼 + 產生 token） 📅 2026-03-08 ✅ 2026-03-20
- [x] 測試登入流程（Postman） 📅 2026-03-08 ✅ 2026-03-20

**Day 7-8：Security Filter（3/9 - 3/10）**
- [x] 實作 JwtAuthenticationFilter（驗證 token + 設定 SecurityContext） 📅 2026-03-09 ✅ 2026-03-23
- [x] 實作 TenantContext.clear()（Filter 中） 📅 2026-03-09 ✅ 2026-03-23
- [x] 設定 SecurityConfig（路徑保護規則） 📅 2026-03-09 ✅ 2026-03-23
- [x] 測試 Filter 運作（Postman 帶 token） 📅 2026-03-10 ✅ 2026-03-23

**Day 9-10：前端 Login/Register（3/11 - 3/12）**
- [x] 建立 Login 頁面（UI + API 串接） 📅 2026-03-11 ✅ 2026-03-24
- [x] 建立 Register 頁面（UI + API 串接） 📅 2026-03-11 ✅ 2026-03-24
- [x] 實作 JWT token 儲存（localStorage） 📅 2026-03-12 → 不適用，改為 session，這條可以直接劃掉 ✅ 2026-03-24
- [x] 實作登入後 redirect 邏輯 📅 2026-03-12 ✅ 2026-03-26
- [x] 測試完整註冊登入流程 📅 2026-03-12 ✅ 2026-03-26 

**Day 11：A 系列驗收（3/13）**
- [x] 測試註冊 API（Swagger UI） 📅 2026-03-13 ✅ 2026-03-26
- [x] 測試登入 API（Swagger UI） 📅 2026-03-13 ✅ 2026-03-26
- [x] 測試前端完整流程 📅 2026-03-13 ✅ 2026-03-26
- [x] 測試 JWT 過期處理 📅 2026-03-13  ->MVP之後才處理 ✅ 2026-03-26
- [x] 測試租戶隔離（帳號 A token 不可存取帳號 B 資源）📅 2026-03-13  ->無法在 A 系列驗收階段執行，移至 B 系列驗收 ✅ 2026-03-26
- [x] A 系列驗收完成 📅 2026-03-13 ✅ 2026-03-26

**A 系列驗收後｜Docker 與 Railway 初始設定（A 完成後立即執行）**

- [x] 撰寫 Dockerfile ✅ 2026-03-27
- [x] 撰寫 docker-compose.yml（MySQL + App） ✅ 2026-03-27
- [x] 本機 Docker 測試 ✅ 2026-03-27
- [x] 註冊 Railway 帳號 ✅ 2026-03-27
- [x] 建立 Railway Project（NovaLedger） ✅ 2026-03-27
- [x] 新增 MySQL service ✅ 2026-03-27
- [x] 產生 JWT_SECRET ✅ 2026-03-27
- [x] 在 Railway 設定環境變數 ✅ 2026-03-27
- [x] 連接 GitHub repository 到 Railway ✅ 2026-03-27
- [x] 等待首次部署完成 ✅ 2026-03-27
- [x] 驗證 Migration 執行成功 ✅ 2026-03-27
- [x] 驗證 Seed Data 已載入 ✅ 2026-03-27
- [x] Railway 初始部署驗收完成 ✅ 2026-03-27
---
### B｜帳戶與信用卡（3/14 - 3/27，共 14 天）

**Day 1-2：Bank 與 Account Entity（3/14 - 3/15）**

- [x] 建立 Bank Entity ✅ 2026-03-29
- [x] 建立 BankBranch Entity ✅ 2026-03-29
- [x] 建立 UserAccount Entity（tenant-aware + deleted_at） ✅ 2026-03-29
- [x] 建立 AccountBalance Entity ✅ 2026-03-29
- [x] 建立對應 Repository 介面 ✅ 2026-03-29

**Day 3-4：Account API（3/16 - 3/17）**

- [x] 實作 BankController.getBanks（列出啟用銀行） ✅ 2026-03-30
- [x] 實作 AccountController.createAccount（含方案限制檢查） ✅ 2026-03-30
- [x] 實作 AccountController.getAccounts（列出帳戶） ✅ 2026-03-30
- [x] 實作 AccountController.updateAccount（更新帳戶） ✅ 2026-03-30
- [x] 實作 AccountController.deleteAccount（軟刪除） ✅ 2026-03-30
- [x] 測試 Account API（Postman） ✅ 2026-03-30

#### feature_b-credit-card

- [ ] 建立 UserCreditCard Entity（tenant-aware + deleted_at）
- [ ] 建立 CreditCard Repository
- [ ] 實作 CardController.createCard（含方案限制檢查）
- [ ] 實作 CardController.getCards（列出信用卡）
- [ ] 實作 CardController.updateCard（更新信用卡）
- [ ] 實作 CardController.deleteCard（軟刪除）
- [ ] 單元測試：CardService.createCard
- [ ] 單元測試：CardService.deleteCard（軟刪除邏輯）
- [ ] 單元測試：CardController（@WebMvcTest）
- [ ] 測試 Card API（Swagger UI）

---

#### feature_b-frontend

- [ ] 建立 Account List 頁面（表格 + 編輯刪除按鈕）
- [ ] 建立 Account Create 頁面（表單 + 銀行下拉選單）
- [ ] 實作初始餘額設定
- [ ] 測試帳戶 CRUD 完整流程
- [ ] 測試停用功能
- [ ] 建立 Card List 頁面（表格 + 編輯刪除按鈕）
- [ ] 建立 Card Create 頁面（表單 + 銀行下拉選單）
- [ ] 實作帳單週期設定（可選填）
- [ ] 測試信用卡 CRUD 完整流程
- [ ] 測試停用功能

---

#### feature_b-acceptance

- [ ] 測試租戶隔離（不同 tenant 看不到彼此帳戶）
- [ ] 測試 JWT filter 正確攔截未授權請求
- [ ] 測試軟刪除與恢復邏輯
- [ ] 檢查 Swagger UI 文件完整性
- [ ] B 系列驗收完成 📅 2026-03-27
- [ ] git commit -m "feat: B series complete - Account + CreditCard"
- [ ] git push origin main
- [ ] 等待 Railway 自動部署
- [ ] 檢查 Deployment logs
- [ ] 測試建立帳戶（線上環境）
- [ ] 測試建立信用卡（線上環境）
- [ ] B 系列線上驗證完成


---
### H｜Excel 匯入（3/28 - 4/2，共 6 天）

**Day 1：Schema 補強與 Entity（3/28）**

- [ ]  新增 Flyway migration：parsed_records 加 `IMPORT_STATUS VARCHAR(20) NOT NULL DEFAULT 'PENDING'`
- [ ]  新增索引 `idx_parsed_records_import (TENANT_ID, IMPORT_STATUS)`
- [ ]  建立 UploadJob Entity（tenant-aware）
- [ ]  建立 UploadFile Entity
- [ ]  建立 ParsedRecord Entity（含 IMPORT_STATUS，Java Enum：PENDING / IMPORTED / FAILED）
- [ ]  建立 ImportLog Entity
- [ ]  建立對應 Repository 介面

**Day 2：基礎 API（3/29）**

- [ ]  加入 Apache POI 依賴
- [ ]  實作 ImportController.uploadFile（檔案驗證）
- [ ]  建立 import_job 記錄（status=PENDING）

**Day 3-4：Excel 解析邏輯（3/30 - 3/31）**

- [ ]  實作存摺格式解析（日期、摘要、金額、餘額）
- [ ]  實作信用卡格式解析（消費日期、商店名稱、金額）
- [ ]  實作 ImportService.processImportJob（@Async）
- [ ]  實作逐列解析與驗證邏輯
- [ ]  解析成功 → PARSE_STATUS=SUCCESS, IMPORT_STATUS=PENDING
- [ ]  解析失敗 → PARSE_STATUS=FAILED，寫入 ImportLog 錯誤原因

**Day 5：狀態查詢 API（4/1）**

- [ ]  實作 ImportController.getJobStatus
- [ ]  實作 ImportController.getErrorRows（PARSE_STATUS=FAILED 的列）
- [ ]  實作 ImportController.getPreview（PARSE_STATUS=SUCCESS 的列，供使用者確認）
- [ ]  測試完整解析流程（Postman）

**Day 6：前端 Import 頁面（4/2）**

- [ ]  建立 Import 頁面（檔案選擇器 + 上傳按鈕）
- [ ]  實作進度顯示區（輪詢 job status）
- [ ]  實作解析結果預覽（成功列清單）
- [ ]  實作錯誤列顯示（哪列出錯 + 原因）
- [ ]  「確認匯入」按鈕（UI 先顯示，功能待 C 完成後串接）
- [ ]  測試上傳存摺 Excel
- [ ]  測試上傳信用卡 Excel
- [ ]  H 系列驗收完成 📅 2026-04-02

**H 系列驗收後｜Railway 部署驗證**

- [ ]  git commit -m "feat: H series complete - Excel Import"
- [ ]  git push origin main
- [ ]  等待 Railway 自動部署
- [ ]  檢查 Deployment logs
- [ ]  測試上傳真實存摺 Excel（線上環境）
- [ ]  測試上傳真實信用卡 Excel（線上環境）
- [ ]  確認解析結果預覽與錯誤列顯示正常
- [ ]  H 系列線上驗證完成

**跳過：4/3-4/6（清明連假）**

---

### 部署里程碑 ①（4/7 - 4/8，共 2 天）

**Day 1：整合驗證（4/7）**

- [ ]  確認線上環境 Migration 版本正確
- [ ]  完整 User Flow 測試：註冊 → 登入 → 建立帳戶 → 上傳 Excel → 預覽解析結果
- [ ]  測試租戶隔離（線上環境）
- [ ]  檢查 Deployment logs 無異常

**Day 2：第三方測試（4/8）**

- [ ]  第三方測試（給朋友測試 URL）
- [ ]  記錄測試回饋（test-feedback.md）
- [ ]  Phase 1 部署驗收完成 📅 2026-04-08
---

## ⚙️ Phase 2：MCP 進階功能開發（4/9 - 4/30）

### C｜交易記帳（4/9 - 4/12，共 4 天）

**Day 1-2：Transaction Entity 與 Service（4/9 - 4/10）**

- [ ]  建立 Transaction Entity（tenant-aware + deleted_at）
- [ ]  建立 TransactionItem Entity
- [ ]  建立 TransactionType Entity
- [ ]  建立對應 Repository 介面
- [ ]  實作 TransactionService.createTransaction（含 balance 更新）
- [ ]  實作 TransactionService.updateTransaction（計算差額）
- [ ]  實作 TransactionService.deleteTransaction（軟刪除 + 扣回 balance）

**Day 3：Transaction API + H 系列技術債收尾（4/11）**

- [ ]  實作 TransactionController.createTransaction
- [ ]  實作 TransactionController.getTransactions（分頁 + 篩選）
- [ ]  實作 TransactionController.updateTransaction
- [ ]  實作 TransactionController.deleteTransaction
- [ ]  測試 Transaction API（Postman）
- [ ]  實作 ImportService.confirmImport（IMPORT_STATUS=PENDING → 寫入 transactions → IMPORTED）
- [ ]  實作 ImportController.confirmImport API
- [ ]  串接前端「確認匯入」按鈕
- [ ]  測試上傳 → 預覽 → 確認 → transactions 寫入成功
- [ ]  測試 IMPORT_STATUS 正確更新（PENDING → IMPORTED）
- [ ]  測試重複確認不會重複寫入

**Day 4：前端 Transaction 頁面（4/12）**

- [ ]  建立 Transaction Create 頁面（收入/支出切換）
- [ ]  建立 Transaction List 頁面（表格 + 分頁 + 篩選）
- [ ]  實作編輯與刪除功能
- [ ]  測試 balance 更新邏輯
- [ ]  C 系列驗收完成 📅 2026-04-12

**C 系列驗收後｜Railway 部署驗證**

- [ ]  git commit -m "feat: C series complete - Transaction"
- [ ]  git push origin main
- [ ]  等待 Railway 自動部署
- [ ]  檢查 Deployment logs
- [ ]  測試手動新增交易（線上環境）
- [ ]  測試 Excel 確認匯入 → transactions 寫入成功（線上環境）
- [ ]  測試 balance 更新正確（線上環境）
- [ ]  C 系列線上驗證完成

---

### F｜匯率與換算（4/20，共 1 天）

**Day 1：固定匯率設定（4/20）**
- [ ] 建立 ExchangeRate Entity 📅 2026-04-20
- [ ] 建立 ExchangeRate Repository 📅 2026-04-20
- [ ] 手動建立固定匯率（USD/TWD = 31.5）📅 2026-04-20
- [ ] 實作 AdminExchangeRateController.updateRate（手動更新）📅 2026-04-20
- [ ] Dashboard 使用固定匯率測試 📅 2026-04-20
- [ ] F 系列驗收完成 📅 2026-04-20

---
### I｜管理員後台（4/22 - 4/23，共 2 天）

**Day 1：Admin Bank API（4/22）**
- [ ] 實作 AdminBankController.getBanks（@PreAuthorize）📅 2026-04-22
- [ ] 實作 AdminBankController.createBank 📅 2026-04-22
- [ ] 實作 AdminBankController.updateBank 📅 2026-04-22
- [ ] 測試 Admin Bank API（需 ADMIN token）📅 2026-04-22

**Day 2：前端 Admin 頁面（4/23）**
- [ ] 建立 Admin Layout（Sidebar + Content）📅 2026-04-23
- [ ] 實作權限檢查（role != ADMIN → redirect）📅 2026-04-23
- [ ] 建立 Admin Bank List 頁面 📅 2026-04-23
- [ ] 建立 Admin User List 頁面 📅 2026-04-23
- [ ] 測試權限保護（MEMBER 存取 /admin → 403）📅 2026-04-23
- [ ] I 系列驗收完成 📅 2026-04-23

---
### E｜Dashboard（4/16 - 4/19，共 4 天）

**Day 1-2：Dashboard Service（4/16 - 4/17）**
- [ ] 實作 DashboardService.getDashboardSummary 📅 2026-04-16
- [ ] 實作總資產計算（依幣別分組）📅 2026-04-16
- [ ] 實作本月收支計算 📅 2026-04-16
- [ ] 實作分類佔比計算 📅 2026-04-17
- [ ] 實作 DashboardController.getSummary 📅 2026-04-17
- [ ] 測試 Dashboard API（Postman）📅 2026-04-17

**Day 3-4：前端 Dashboard 頁面（4/18 - 4/19）**
- [ ] 建立 Dashboard Layout（卡片佈局）📅 2026-04-18
- [ ] 實作總資產卡片（TWD + USD 換算）📅 2026-04-18
- [ ] 實作本月收支卡片 📅 2026-04-18
- [ ] 實作分類消費圓餅圖（Chart.js 或 Recharts）📅 2026-04-19
- [ ] 實作 Empty State（無帳戶時）📅 2026-04-19
- [ ] 測試 Dashboard 完整顯示 📅 2026-04-19
- [ ] E 系列驗收完成 📅 2026-04-19


---


---
## MCP之後

### 部署里程碑 ②（4/27 - 4/30，共 4 天）

**MVP 後｜Redis + JWT Blacklist（部署前完成）**

- [ ] 加入 Redis 依賴（spring-boot-starter-data-redis） 📅 待定
- [ ] 設定 RedisConfig（連線、序列化） 📅 待定
- [ ] 設定 application.yml Redis 連線參數 📅 待定
- [ ] JwtTokenProvider 加入 jti claim（generateAccessToken 補 .setId(UUID)） 📅 待定
- [ ] JwtTokenProvider 加入 getJti() 方法 📅 待定
- [ ] JwtTokenProvider 加入 getRemainingSeconds() 方法 📅 待定
- [ ] 實作 RedisBlacklistService（blacklist 寫入 / 查詢） 📅 待定
- [ ] 更新 /auth/logout（寫入 Redis blacklist） 📅 待定
- [ ] 更新 JwtAuthenticationFilter（補 blacklist 檢查） 📅 待定
- [ ] 測試 logout 後 token 是否真的被擋（Postman） 📅 待定
- [ ] 測試 TTL 是否與 token 剩餘時間一致（Redis CLI） 📅 待定
- [ ] 驗收：登出後舊 token 回傳 401 ✅


**Day 1：部署（4/27）**
- [ ] git commit -m "Phase 2 complete: All MCP features" 📅 2026-04-27
- [ ] git push origin main 📅 2026-04-27
- [ ] 等待 Railway 自動部署 📅 2026-04-27
- [ ] 檢查 Deployment logs 📅 2026-04-27

**Day 2-3：功能驗證（4/28 - 4/29）**
- [ ] 驗證手動記帳功能 📅 2026-04-28
- [ ] 驗證分類與標籤管理 📅 2026-04-28
- [ ] 驗證 Dashboard 顯示 📅 2026-04-28
- [ ] 驗證月報表功能 📅 2026-04-29
- [ ] 驗證 Admin 後台 📅 2026-04-29
- [ ] 驗證預算管理 📅 2026-04-29
- [ ] 測試方案限制（FREE vs PRO）📅 2026-04-29

**Day 4：最終驗收（4/30）**
- [ ] 完整 User Flow 測試（註冊 → 帳戶 → Excel → Dashboard → 報表）📅 2026-04-30
- [ ] 第三方完整測試（給朋友測試所有功能）📅 2026-04-30
- [ ] 更新 README（URL + 測試帳號 + 功能說明）📅 2026-04-30
- [ ] 記錄所有測試回饋 📅 2026-04-30
- [ ] 確認所有 47 個情境可展示 📅 2026-04-30
- [ ] MCP 完整驗收完成 🎉 📅 2026-04-30

---

## 📊 開發統計

**Phase 0：** 27 天（1/31 - 2/26）
- 資料表設計：22 天（37 張表）
- Migration + Seed：3 天
- Railway 首次部署：3 天

**Phase 1：** 33 天（2/27 - 4/8，扣除 228 連假）
- A 系列（註冊登入）：11 天
- B 系列（帳戶信用卡）：14 天
- H 系列（Excel 匯入）：6 天
- 部署里程碑 ①：2 天

**Phase 2：** 18 天（4/9 - 4/30，扣除清明連假）
- C 系列（交易記帳）：4 天
- D 系列（分類標籤）：3 天
- E 系列（Dashboard）：4 天
- F 系列（匯率）：1 天
- G 系列（方案）：1 天
- I 系列（管理員）：2 天
- P 系列（預算）：1 天
- R 系列（報表）：2 天
- 部署里程碑 ②：4 天

**總計：** 78 工作日，約 156 開發小時

---

## 💡 執行提醒

1. **每日完成後打勾**：在 Obsidian 勾選完成項目
2. **彈性調整**：某天卡住可延後 1-2 天，但整週要達標
3. **週末驗收**：每個系列結束前確認驗收標準
4. **優先順序**：A, B, H, E 必做，其他可彈性調整
5. **Swagger 優先**：開發時善用 Swagger UI 測試 API