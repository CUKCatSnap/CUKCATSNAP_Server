CREATE TABLE notification_last_read
(
    notification_last_read_id BIGSERIAL PRIMARY KEY,
    user_id                   BIGINT UNIQUE,
    last_read                 TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_notification_last_read__user
        FOREIGN KEY (user_id) REFERENCES users (id)
);
