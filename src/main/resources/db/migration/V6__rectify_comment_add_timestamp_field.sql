SET search_path TO lms;

ALTER TABLE comments
    ADD COLUMN time_stamp timestamp;