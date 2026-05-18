-- V19: system_configs table and seed data
-- 用途：儲存可由 Admin 後台動態調整的系統設定。
-- 與 application.properties 的區別：只存「應用程式運行中可動態變更」的值，
-- 啟動必要的設定（DB 連線、JWT、Flyway）仍在 properties 檔案。

CREATE TABLE IF NOT EXISTS system_configs (
    config_key      VARCHAR(100)    NOT NULL,
    config_value    VARCHAR(500)    NOT NULL,
    value_type      VARCHAR(20)     NOT NULL DEFAULT 'STRING' COMMENT 'STRING | BOOLEAN | INTEGER',
    description     VARCHAR(200)    NULL,
    is_public       TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '0=Admin only, 1=可暴露給前端',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      VARCHAR(50)     NULL COMMENT 'Admin user who last updated',
    PRIMARY KEY (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Seed data
-- ============================================================

INSERT INTO system_configs (config_key, config_value, value_type, description, is_public) VALUES

-- 功能開關
('registration.enabled',            'true',       'BOOLEAN', '是否開放新用戶註冊',                      0),
('maintenance.mode',                'false',      'BOOLEAN', '維護模式（開啟時所有請求回傳 503）',        0),
('email.verify.enabled',            'true',       'BOOLEAN', '註冊時是否寄送 email 驗證信',              0),
('parser.auto.detect.enabled',      'false',      'BOOLEAN', '是否啟用自動偵測銀行 parser',              0),

-- 業務參數
('import.max.file.size.mb',         '20',         'INTEGER', '上傳檔案大小上限（MB）',                   0),
('import.max.files.per.batch',      '10',         'INTEGER', '單次批量上傳檔案數限制',                   0),
('auth.token.expire.minutes',       '15',         'INTEGER', 'Email 驗證 token 有效分鐘數',              0),
('auth.resend.cooldown.seconds',    '60',         'INTEGER', '重新發送驗證信冷卻秒數',                   0),
('auth.login.max.attempts',         '5',          'INTEGER', '登入失敗鎖定前最多嘗試次數',               0),
('auth.login.lockout.minutes',      '15',         'INTEGER', '登入鎖定持續分鐘數',                       0),

-- 公開設定（可暴露給前端）
('app.name',                        'NovaLedger', 'STRING',  '應用程式名稱',                             1),
('app.contact.email',               '',           'STRING',  '客服聯絡 email',                           1),
('app.version',                     '1.0.0',      'STRING',  '當前版本號',                               1);
