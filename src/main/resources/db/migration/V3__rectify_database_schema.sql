SET search_path TO lms;

ALTER TABLE codelabs
ADD details text;

CREATE TABLE comments
(
    id int NOT NULL,
    user_id int NOT NULL,
    codelab_id int NOT NULL,
    comment text,

    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_usercodelab FOREIGN KEY (user_id,codelab_id) REFERENCES users_codelabs(user_id, codelab_id)
);