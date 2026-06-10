ALTER TABLE site_allowed_user
    ADD COLUMN created_at TIMESTAMP(6);

ALTER TABLE site_allowed_user
    ADD COLUMN updated_at TIMESTAMP(6);
