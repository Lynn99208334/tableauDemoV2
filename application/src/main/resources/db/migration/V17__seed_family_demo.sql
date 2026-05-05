-- ========================================
-- V17 - Family Demo Seed（含 ROLE_ID）
-- ========================================

-- ========================================
-- 1. 建立 tenant（家庭帳）
-- ========================================
INSERT INTO tenants (
    CODE,
    NAME,
    TYPE,
    PLAN,
    OWNER_USER_ID,
    STATUS,
    CREATED_AT,
    UPDATED_AT
)
SELECT
    'alice-family',
    'Alice Family',
    'FAMILY',
    'FREE',
    u.ID,
    'ACTIVE',
    NOW(),
    NOW()
FROM users u
WHERE u.USERNAME = 'alice'
  AND NOT EXISTS (
    SELECT 1 FROM tenants t WHERE t.CODE = 'alice-family'
);


-- ========================================
-- 2. 建立 user_tenants（家庭成員）
-- ========================================
INSERT INTO user_tenants (
    USER_ID,
    TENANT_ID,
    ROLE_ID,
    STATUS,
    JOINED_AT
)
SELECT
    u.ID,
    t.ID,
    r.ID,
    'ACTIVE',
    NOW()
FROM users u
         JOIN tenants t ON t.CODE = 'alice-family'
         JOIN roles r ON r.CODE =
                         CASE
                             WHEN u.USERNAME = 'alice' THEN 'OWNER'
                             ELSE 'MEMBER'
                             END
WHERE u.USERNAME IN ('alice', 'bob', 'son')
  AND NOT EXISTS (
    SELECT 1 FROM user_tenants ut
    WHERE ut.USER_ID = u.ID
      AND ut.TENANT_ID = t.ID
      AND ut.DELETED_AT IS NULL
);


-- ========================================
-- 3. 建立帳戶
-- ========================================
INSERT INTO user_accounts (
    TENANT_ID,
    USER_ID,
    ACCOUNT_TYPE,
    BANK_CODE,
    NAME,
    CURRENCY_CODE,
    CURRENT_BALANCE,
    INITIAL_BALANCE,
    CREATED_AT,
    UPDATED_AT
)
SELECT
    t.ID,
    u.ID,
    'BANK',
    '004',
    CONCAT(u.USERNAME, ' 主帳戶'),
    'TWD',
    10000,
    10000,
    NOW(),
    NOW()
FROM users u
         JOIN tenants t ON t.CODE = 'alice-family'
WHERE u.USERNAME IN ('alice', 'bob')
  AND NOT EXISTS (
    SELECT 1 FROM user_accounts ua
    WHERE ua.USER_ID = u.ID
      AND ua.TENANT_ID = t.ID
      AND ua.NAME = CONCAT(u.USERNAME, ' 主帳戶')
);