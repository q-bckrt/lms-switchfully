SET search_path TO lms;

TRUNCATE TABLE comments,
    users_codelabs,
    codelabs,
    modules_submodules,
    submodules,
    courses_modules,
    modules,
    users_classes,
    classes,
    courses,
    users
    RESTART IDENTITY CASCADE;

ALTER TABLE users DROP COLUMN password;