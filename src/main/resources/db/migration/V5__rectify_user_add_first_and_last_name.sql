SET search_path TO lms;

ALTER TABLE users
    ADD COLUMN first_name varchar(25) NOT NULL default 'default_first_name',
    ADD COLUMN last_name varchar(25) NOT NULL default 'default_last_name'