-- ========================================
-- NovaLedger Seed Data
-- Flyway Migration V17
-- 家庭帳 Demo Seed
-- 目的：供 Dashboard 面試 Demo 使用
--   - 新增子帳號 son
--   - 建立 Family Tenant（alice 為 owner）
--   - alice + son 同屬 Family Tenant
--   - son 有幾筆帳戶（TWD + USD），讓 Dashboard 可展示多幣別聚合
-- 所有帳號密碼均為：password123
-- ========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 1. 新增子帳號 son
-- ========================================

INSERT INTO users (
    USERNAME, EMAIL, PASSWORD,
    ENABLED, STATUS, EMAIL_VERIFIED, VERIFIED_AT,
    EMAIL_VERIFY_TOKEN, EMAIL_VERIFY_EXPIRED_AT, EMAIL_VERIFY_LAST_SENT_AT,
    IS_SYSTEM_ADMIN, LAST_LOGIN_AT,
    CREATED_AT, UPDATED_AT, DELETED_AT
) VALUES (
    'son',
    'son@novaledger.dev',
    '$2a$10$8R0nwfQG.dSGE9UemN0JveorFQPg6QzlTyWg/WNFnnGQyi6eCgn4C',
    TRUE, 'ACTIVE', TRUE, '2026-01-01 00:00:00',
    NULL, NULL, NULL,
    FALSE, NULL,
    NOW(), NOW(), NULL
);

-- ========================================
-- 2. 建立 son 的個人帳本
-- ========================================

INSERT INTO tenants (
    CODE, NAME, TYPE, PLAN,
    OWNER_USER_ID, STATUS,
    CREATED_AT, UPDATED_AT, DELETED_AT
) VALUES (
    'son-personal',
    'Son 的個人帳本',
    'PERSONAL', 'FREE',
    (SELECT ID FROM users WHERE USERNAME = 'son'),
    'ACTIVE',
    NOW(), NOW(), NULL
);

-- ========================================
-- 3. 建立 Family Tenant（alice 為 owner）
-- ========================================

INSERT INTO tenants (
    CODE, NAME, TYPE, PLAN,
    OWNER_USER_ID, STATUS,
    CREATED_AT, UPDATED_AT, DELETED_AT
) VALUES (
    'alice-family',
    'Alice 的家庭帳本',
    'FAMILY', 'FREE',
    (SELECT ID FROM users WHERE USERNAME = 'alice'),
    'ACTIVE',
    NOW(), NOW(), NULL
);

-- ========================================
-- 4. USER_TENANTS：son 加入個人帳本 + alice & son 加入 Family Tenant
-- ========================================

INSERT INTO user_tenants (USER_ID, TENANT_ID, ROLE_ID, STATUS, JOINED_AT)
VALUES
-- son 加入 son-personal
(
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    (SELECT ID FROM tenants WHERE CODE     = 'son-personal'),
    (SELECT ID FROM roles   WHERE CODE     = 'MEMBER'),
    'ACTIVE',
    NOW()
),
-- alice 加入 alice-family（OWNER 角色）
(
    (SELECT ID FROM users   WHERE USERNAME = 'alice'),
    (SELECT ID FROM tenants WHERE CODE     = 'alice-family'),
    (SELECT ID FROM roles   WHERE CODE     = 'MEMBER'),
    'ACTIVE',
    NOW()
),
-- son 加入 alice-family（MEMBER 角色）
(
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    (SELECT ID FROM tenants WHERE CODE     = 'alice-family'),
    (SELECT ID FROM roles   WHERE CODE     = 'MEMBER'),
    'ACTIVE',
    NOW()
);

-- ========================================
-- 5. son 的個人帳戶（有餘額）
-- ========================================

INSERT INTO user_accounts (
    TENANT_ID, USER_ID,
    ACCOUNT_TYPE, BANK_CODE, BRANCH_ID,
    ACCOUNT_NUMBER, NAME,
    CURRENCY_CODE, CURRENT_BALANCE, INITIAL_BALANCE,
    NOTES, CREATED_AT, UPDATED_AT, DELETED_AT
)
VALUES
-- son 的台幣帳戶
(
    (SELECT ID FROM tenants WHERE CODE = 'son-personal'),
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    'BANK', '004', NULL,
    NULL,
    '台灣銀行 活期存款',
    'TWD', 85000.00, 85000.00,
    'Son Demo 帳戶 - TWD',
    NOW(), NOW(), NULL
),
-- son 的美元帳戶
(
    (SELECT ID FROM tenants WHERE CODE = 'son-personal'),
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    'BANK', '004', NULL,
    NULL,
    '台灣銀行 外幣存款',
    'USD', 1200.00, 1200.00,
    'Son Demo 帳戶 - USD',
    NOW(), NOW(), NULL
);

-- ========================================
-- 6. alice-family tenant 下的帳戶（聚合用）
--    alice 與 son 各自的帳戶掛在 family tenant 下
-- ========================================

INSERT INTO user_accounts (
    TENANT_ID, USER_ID,
    ACCOUNT_TYPE, BANK_CODE, BRANCH_ID,
    ACCOUNT_NUMBER, NAME,
    CURRENCY_CODE, CURRENT_BALANCE, INITIAL_BALANCE,
    NOTES, CREATED_AT, UPDATED_AT, DELETED_AT
)
VALUES
-- alice 在家庭帳本下的台幣帳戶
(
    (SELECT ID FROM tenants WHERE CODE = 'alice-family'),
    (SELECT ID FROM users   WHERE USERNAME = 'alice'),
    'BANK', '808', NULL,
    NULL,
    '玉山銀行（家庭帳）',
    'TWD', 120000.00, 120000.00,
    'Alice Family Demo - TWD',
    NOW(), NOW(), NULL
),
-- son 在家庭帳本下的台幣帳戶
(
    (SELECT ID FROM tenants WHERE CODE = 'alice-family'),
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    'BANK', '004', NULL,
    NULL,
    '台灣銀行（家庭帳）',
    'TWD', 85000.00, 85000.00,
    'Son Family Demo - TWD',
    NOW(), NOW(), NULL
),
-- son 在家庭帳本下的美元帳戶
(
    (SELECT ID FROM tenants WHERE CODE = 'alice-family'),
    (SELECT ID FROM users   WHERE USERNAME = 'son'),
    'BANK', '004', NULL,
    NULL,
    '台灣銀行 外幣（家庭帳）',
    'USD', 1200.00, 1200.00,
    'Son Family Demo - USD',
    NOW(), NOW(), NULL
);

SET FOREIGN_KEY_CHECKS = 1;
