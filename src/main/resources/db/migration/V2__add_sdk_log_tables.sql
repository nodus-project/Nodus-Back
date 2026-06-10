CREATE TABLE site_activation_log
(
    id           UUID         NOT NULL PRIMARY KEY,
    site_id      UUID         NOT NULL,
    session_id   VARCHAR(255) NOT NULL,
    feature_name VARCHAR(255),
    current_page VARCHAR(255),
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6)
);

CREATE INDEX idx_site_activation_log_site_id_created_at
    ON site_activation_log (site_id, created_at);

CREATE TABLE site_revenue_log
(
    id           UUID         NOT NULL PRIMARY KEY,
    site_id      UUID         NOT NULL,
    session_id   VARCHAR(255) NOT NULL,
    tag          VARCHAR(255),
    current_page VARCHAR(255),
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6)
);

CREATE INDEX idx_site_revenue_log_site_id_created_at
    ON site_revenue_log (site_id, created_at);
