SET search_path TO lms;

ALTER TABLE classes
ALTER COLUMN course_id DROP NOT NULL;
