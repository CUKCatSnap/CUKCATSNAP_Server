ALTER TABLE feed_comment
DROP CONSTRAINT IF EXISTS fk_feed_comment__model,
    DROP COLUMN IF EXISTS member_id;

ALTER TABLE feed_comment
DROP CONSTRAINT IF EXISTS fk_feed_comment__photograph,
    DROP COLUMN IF EXISTS photographer_id;

ALTER TABLE feed_comment
    ADD COLUMN user_id BIGINT;

ALTER TABLE feed_comment
    ADD CONSTRAINT fk_feed_comment__user_id FOREIGN KEY (user_id)
        REFERENCES "users"(id);

ALTER TABLE feed_like
DROP CONSTRAINT IF EXISTS fk_feed_like__model,
    DROP COLUMN IF EXISTS member_id;

ALTER TABLE feed_like
DROP CONSTRAINT IF EXISTS fk_feed_like__photograph,
    DROP COLUMN IF EXISTS photographer_id;

ALTER TABLE feed_like
    ADD COLUMN user_id BIGINT;

ALTER TABLE feed_like
    ADD CONSTRAINT fk_feed_like__user_id FOREIGN KEY (user_id)
        REFERENCES "users"(id);