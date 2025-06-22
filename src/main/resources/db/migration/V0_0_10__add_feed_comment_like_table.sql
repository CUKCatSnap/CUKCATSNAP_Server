CREATE TABLE feed_comment_like
(
    feed_comment_like_id BIGSERIAL PRIMARY KEY,
    user_id              BIGINT,
    feed_comment_id      BIGINT,
    CONSTRAINT fk_feed_comment_like__user FOREIGN KEY (user_id) REFERENCES "users" (id),
    CONSTRAINT fk_feed_comment_like__comment FOREIGN KEY (feed_comment_id) REFERENCES feed_comment (feed_comment_id)
);