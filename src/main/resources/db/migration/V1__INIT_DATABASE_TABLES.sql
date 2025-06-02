CREATE SCHEMA IF NOT EXISTS lms;

SET search_path TO lms;

CREATE TABLE users
(
    id int NOT NULL,
    user_name varchar(50) NOT NULL,
    display_name varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    password varchar(50) NOT NULL,
    role varchar(25) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE SEQUENCE users_seq start with 1 increment by 1;

CREATE TABLE courses
(
    id int NOT NULL,
    title varchar(50) NOT NULL,

    CONSTRAINT pk_courses PRIMARY KEY (id)
);

CREATE SEQUENCE courses_seq start with 1 increment by 1;

CREATE TABLE classes
(
    id int NOT NULL,
    title varchar(50) NOT NULL,
    course_id int NOT NULL,

    CONSTRAINT pk_classes PRIMARY KEY (id),
    CONSTRAINT fk_classes_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE SEQUENCE classes_seq start with 1 increment by 1;

/* Joint Table */
CREATE TABLE users_classes
(
    user_id int NOT NULL,
    class_id int NOT NULL,

    CONSTRAINT pk_users_classes PRIMARY KEY (user_id, class_id),
    CONSTRAINT fk_users_classes_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_users_classes_class FOREIGN KEY (class_id) REFERENCES classes(id)
);

CREATE TABLE modules
(
    id int NOT NULL,
    title varchar(50) NOT NULL,

    CONSTRAINT pk_modules PRIMARY KEY (id)
);

CREATE SEQUENCE modules_seq start with 1 increment by 1;

/* Joint Table */
CREATE TABLE courses_modules
(
    course_id int NOT NULL,
    module_id int NOT NULL,

    CONSTRAINT pk_courses_modules PRIMARY KEY (course_id, module_id),
    CONSTRAINT fk_courses_modules_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_courses_modules_module FOREIGN KEY (module_id) REFERENCES modules(id)
);

CREATE TABLE submodules
(
    id int NOT NULL,
    title varchar(50) NOT NULL,

    CONSTRAINT pk_submodules PRIMARY KEY (id)
);

CREATE SEQUENCE submodules_seq start with 1 increment by 1;

/* Joint Table */
CREATE TABLE modules_submodules
(
    module_id int NOT NULL,
    submodule_id int NOT NULL,

    CONSTRAINT pk_modules_submodules PRIMARY KEY (module_id, submodule_id),
    CONSTRAINT fk_modules_submodules_module FOREIGN KEY (module_id) REFERENCES modules(id),
    CONSTRAINT fk_modules_submodules_submodule FOREIGN KEY (submodule_id) REFERENCES submodules(id)
);

CREATE TABLE codelabs
(
    id int NOT NULL,
    title varchar(50) NOT NULL,
    submodule_id int NOT NULL,

    CONSTRAINT pk_codelabs PRIMARY KEY (id),
    CONSTRAINT fk_codelabs_submodule FOREIGN KEY (submodule_id) REFERENCES submodules(id)
);

CREATE SEQUENCE codelabs_seq start with 1 increment by 1;

/* Joint Table */
CREATE TABLE users_codelabs
(
    user_id int NOT NULL,
    codelab_id int NOT NULL,
    progress varchar(25) NOT NULL DEFAULT 'NOT_STARTED',

    CONSTRAINT pk_users_codelabs PRIMARY KEY (user_id, codelab_id),
    CONSTRAINT fk_users_codelabs_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_users_codelabs_codelab FOREIGN KEY (codelab_id) REFERENCES codelabs(id)
);