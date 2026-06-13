CREATE TABLE site_traffic_source
(
    id            UUID        NOT NULL PRIMARY KEY,
    site_id       UUID        NOT NULL,
    session_id    UUID        NOT NULL UNIQUE,
    channel       VARCHAR(30) NOT NULL,
    source        VARCHAR(255),
    medium        VARCHAR(100),
    campaign      VARCHAR(255),
    term          VARCHAR(255),
    content       VARCHAR(255),
    referrer      TEXT,
    referrer_host VARCHAR(255),
    landing_page  TEXT,
    landing_path  VARCHAR(500),
    click_id      VARCHAR(500),
    created_at    TIMESTAMP(6),
    updated_at    TIMESTAMP(6)
);
