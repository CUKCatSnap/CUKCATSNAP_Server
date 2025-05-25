CREATE TABLE photographer_introduction
(
    id              BIGSERIAL PRIMARY KEY,
    photographer_id BIGINT UNIQUE,
    content         TEXT,
    created_at      TIMESTAMP(6) NOT NULL,
    updated_at      TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_introduction_photographer__photographer
        FOREIGN KEY (photographer_id) REFERENCES photograph (id)
);