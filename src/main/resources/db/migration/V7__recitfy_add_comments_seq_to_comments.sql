SET search_path TO lms;

CREATE SEQUENCE comments_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Set the id column of comments to use the new sequence by default
ALTER TABLE comments
    ALTER COLUMN id SET DEFAULT nextval('comments_seq');

-- Set the sequence's current value to the max id in the table (or 1 if empty)
SELECT setval('comments_seq', COALESCE((SELECT MAX(id) FROM comments), 0) + 1, false);