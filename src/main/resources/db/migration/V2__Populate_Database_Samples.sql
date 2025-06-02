-- Use the schema
SET search_path TO lms;

-- USERS
INSERT INTO users (id, user_name, display_name, email, password, role) VALUES
                                                                           (nextval('users_seq'), 'jdoe', 'John Doe', 'jdoe@example.com', 'password123',  'STUDENT'),
                                                                           (nextval('users_seq'), 'asmith', 'Alice Smith', 'asmith@example.com', 'securepass', 'COACH'),
                                                                           (nextval('users_seq'), 'bwayne', 'Bruce Wayne', 'bwayne@example.com', 'batpass', 'STUDENT');

-- COURSES
INSERT INTO courses (id, title) VALUES
                                    (nextval('courses_seq'), 'Introduction to Programming'),
                                    (nextval('courses_seq'), 'Web Development 101');

-- CLASSES
INSERT INTO classes (id, title, course_id) VALUES
                                               (nextval('classes_seq'), 'July Class', 1),
                                               (nextval('classes_seq'), 'May Class', 1),
                                               (nextval('classes_seq'), 'October Class', 2);

-- USERS_CLASSES
INSERT INTO users_classes (user_id, class_id) VALUES
                                                  (1, 1),
                                                  (2, 1),
                                                  (3, 2);

-- MODULES
INSERT INTO modules (id, title) VALUES
                                    (nextval('modules_seq'), 'Basics'),
                                    (nextval('modules_seq'), 'Advanced Topics');

-- COURSES_MODULES
INSERT INTO courses_modules (course_id, module_id) VALUES
                                                       (1, 1),
                                                       (1, 2),
                                                       (2, 1);

-- SUBMODULES
INSERT INTO submodules (id, title) VALUES
                                       (nextval('submodules_seq'), 'Variables and Data Types'),
                                       (nextval('submodules_seq'), 'Control Flow'),
                                       (nextval('submodules_seq'), 'Object-Oriented Programming');

-- MODULES_SUBMODULES
INSERT INTO modules_submodules (module_id, submodule_id) VALUES
                                                             (1, 1),
                                                             (1, 2),
                                                             (2, 3);

-- CODELABS
INSERT INTO codelabs (id, title, submodule_id) VALUES
                                                   (nextval('codelabs_seq'), 'Hello World', 1),
                                                   (nextval('codelabs_seq'), 'If Statements', 2),
                                                   (nextval('codelabs_seq'), 'Classes and Objects', 3);

-- USERS_CODELABS
INSERT INTO users_codelabs (user_id, codelab_id, progress) VALUES
                                                               (1, 1, 'DONE'),
                                                               (1, 2, 'BUSY'),
                                                               (3, 1, 'NOT_STARTED'),
                                                               (3, 3, 'REFACTORING');

