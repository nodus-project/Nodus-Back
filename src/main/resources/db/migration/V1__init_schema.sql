CREATE TABLE user_account
(
    id          UUID         NOT NULL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    provider    VARCHAR(255) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    user_role   VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6),
    updated_at  TIMESTAMP(6),
    deleted_at  TIMESTAMP(6)
);

CREATE UNIQUE INDEX uk_user_account_provider_provider_id
    ON user_account (provider, provider_id);

CREATE TABLE site
(
    id         UUID         NOT NULL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    url        VARCHAR(255) NOT NULL,
    key        VARCHAR(255),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6)
);

CREATE UNIQUE INDEX uk_site_key
    ON site (key);

CREATE TABLE site_allowed_user
(
    id              UUID NOT NULL PRIMARY KEY,
    site_id         UUID NOT NULL,
    user_account_id UUID NOT NULL
);

CREATE INDEX idx_site_allowed_users_site_id
    ON site_allowed_user (site_id);

CREATE INDEX idx_site_allowed_users_user_account_id
    ON site_allowed_user (user_account_id);

CREATE TABLE site_visit_log
(
    id           UUID         NOT NULL PRIMARY KEY,
    site_id      UUID         NOT NULL,
    session_id   VARCHAR(255) NOT NULL,
    last_page    VARCHAR(255),
    current_page VARCHAR(255),
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6)
);

CREATE INDEX idx_site_visit_log_site_id_created_at
    ON site_visit_log (site_id, created_at);
