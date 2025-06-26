-- LMS Database Population Script
-- Clean data population for PostgreSQL database - Java Software Development Theme

SET search_path TO lms;

-- Insert 3 Coaches (role = 'COACH')
INSERT INTO users (id, user_name, display_name, email, role, first_name, last_name) VALUES
                                                                                        (nextval('users_seq'), 'jrodriguez', 'James Rodriguez', 'james.rodriguez@lms.edu', 'COACH', 'James', 'Rodriguez'),
                                                                                        (nextval('users_seq'), 'mthompson', 'Maria Thompson', 'maria.thompson@lms.edu', 'COACH', 'Maria', 'Thompson'),
                                                                                        (nextval('users_seq'), 'dkim', 'David Kim', 'david.kim@lms.edu', 'COACH', 'David', 'Kim');

-- Insert 3 Courses (each class will have a different course)
INSERT INTO courses (id, title) VALUES
                                    (nextval('courses_seq'), 'Java Core Programming'),
                                    (nextval('courses_seq'), 'Spring Boot Web Development'),
                                    (nextval('courses_seq'), 'Enterprise Java Applications');

-- Insert 3 Classes (each with a different course)
INSERT INTO classes (id, title, course_id) VALUES
                                               (nextval('classes_seq'), 'Java Fundamentals Bootcamp', 1),
                                               (nextval('classes_seq'), 'Spring Boot Intensive', 2),
                                               (nextval('classes_seq'), 'Enterprise Java Workshop', 3);

-- Assign coaches to classes (one coach per class)
INSERT INTO users_classes (user_id, class_id) VALUES
                                                  (1, 1), -- James -> Java Fundamentals
                                                  (2, 2), -- Maria -> Spring Boot
                                                  (3, 3); -- David -> Enterprise Java

-- Insert 3 Modules (one per course)
INSERT INTO modules (id, title) VALUES
                                    (nextval('modules_seq'), 'Java Language Fundamentals'),
                                    (nextval('modules_seq'), 'Spring Framework and Boot'),
                                    (nextval('modules_seq'), 'Enterprise Patterns and Architecture');

-- Link courses to modules (one module per course)
INSERT INTO courses_modules (course_id, module_id) VALUES
                                                       (1, 1), -- Java Core -> Java Language Fundamentals
                                                       (2, 2), -- Spring Boot -> Spring Framework and Boot
                                                       (3, 3); -- Enterprise Java -> Enterprise Patterns and Architecture

-- Insert Submodules for Module 1 (Java Language Fundamentals)
INSERT INTO submodules (id, title) VALUES
                                       (nextval('submodules_seq'), 'Java Syntax and Object-Oriented Programming'),
                                       (nextval('submodules_seq'), 'Collections Framework and Generics'),
                                       (nextval('submodules_seq'), 'Exception Handling and I/O'),
                                       (nextval('submodules_seq'), 'Concurrency and Multithreading'),
                                       (nextval('submodules_seq'), 'Testing with JUnit and Mockito');

-- Insert Submodules for Module 2 (Spring Framework and Boot)
INSERT INTO submodules (id, title) VALUES
                                       (nextval('submodules_seq'), 'Spring Core and Dependency Injection'),
                                       (nextval('submodules_seq'), 'Spring Boot Auto-Configuration'),
                                       (nextval('submodules_seq'), 'Spring MVC and REST APIs'),
                                       (nextval('submodules_seq'), 'Spring Data JPA and Databases'),
                                       (nextval('submodules_seq'), 'Spring Security and Authentication');

-- Insert Submodules for Module 3 (Enterprise Patterns and Architecture)
INSERT INTO submodules (id, title) VALUES
                                       (nextval('submodules_seq'), 'Microservices Architecture'),
                                       (nextval('submodules_seq'), 'Message Queues and Event Processing'),
                                       (nextval('submodules_seq'), 'Docker and Containerization'),
                                       (nextval('submodules_seq'), 'Monitoring and Observability'),
                                       (nextval('submodules_seq'), 'DevOps and CI/CD Pipelines');

-- Link modules to submodules
INSERT INTO modules_submodules (module_id, submodule_id) VALUES
-- Module 1 submodules (1-5)
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
-- Module 2 submodules (6-10)
(2, 6), (2, 7), (2, 8), (2, 9), (2, 10),
-- Module 3 submodules (11-15)
(3, 11), (3, 12), (3, 13), (3, 14), (3, 15);

-- Insert Codelabs for Java Syntax and Object-Oriented Programming (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Java Classes and Objects', 1, 'Create your first Java class with properties, constructors, and methods. Learn about object instantiation and basic OOP principles.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Inheritance and Polymorphism', 1, 'Implement inheritance hierarchies using extends keyword and explore polymorphism with method overriding and interfaces.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Encapsulation and Abstraction', 1, 'Apply encapsulation principles using access modifiers and create abstract classes to demonstrate abstraction concepts.');

-- Insert Codelabs for Collections Framework and Generics (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - ArrayList and LinkedList Operations', 2, 'Work with Java List implementations, compare ArrayList vs LinkedList performance, and implement common list operations.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - HashMap and HashSet Usage', 2, 'Utilize Java Map and Set collections for data storage and retrieval, understanding hash-based data structures.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Generic Types and Wildcards', 2, 'Create generic classes and methods, use bounded wildcards, and understand type safety in Java collections.');

-- Insert Codelabs for Exception Handling and I/O (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Try-Catch and Custom Exceptions', 3, 'Handle exceptions using try-catch blocks, create custom exception classes, and implement proper error handling strategies.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - File I/O with NIO.2', 3, 'Read and write files using Java NIO.2 API, work with Path and Files classes for modern file operations.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Streams and Serialization', 3, 'Process data using Java 8 Streams API and implement object serialization for data persistence.');

-- Insert Codelabs for Concurrency and Multithreading (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Thread Creation and Synchronization', 4, 'Create and manage threads using Thread class and Runnable interface, implement synchronization mechanisms.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Executor Framework and Thread Pools', 4, 'Use ExecutorService and thread pools for efficient concurrent task execution and resource management.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - CompletableFuture and Reactive Programming', 4, 'Implement asynchronous programming patterns using CompletableFuture and explore reactive programming concepts.');

-- Insert Codelabs for Testing with JUnit and Mockito (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - JUnit 5 Unit Testing', 5, 'Write comprehensive unit tests using JUnit 5 annotations, assertions, and test lifecycle methods.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Mockito for Test Doubles', 5, 'Create mock objects and stubs using Mockito framework to isolate units under test and verify interactions.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Integration Testing Strategies', 5, 'Implement integration tests using TestContainers and Spring Boot Test annotations for database and web layer testing.');

-- Insert Codelabs for Spring Core and Dependency Injection (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Spring IoC Container Setup', 6, 'Configure Spring IoC container using annotations and Java configuration, understand bean lifecycle and scopes.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Dependency Injection Patterns', 6, 'Implement constructor, setter, and field injection patterns, use @Autowired and @Qualifier annotations effectively.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Spring Profiles and Properties', 6, 'Manage application configuration using Spring profiles and external properties files for different environments.');

-- Insert Codelabs for Spring Boot Auto-Configuration (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Spring Boot Application Structure', 7, 'Create a Spring Boot application with proper package structure, main class, and configuration classes.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Auto-Configuration and Starters', 7, 'Leverage Spring Boot starters and auto-configuration features to quickly set up database connections and web servers.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Custom Auto-Configuration', 7, 'Build custom Spring Boot starter with auto-configuration for reusable components across multiple projects.');

-- Insert Codelabs for Spring MVC and REST APIs (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - REST Controller and Endpoints', 8, 'Build RESTful web services using @RestController, handle HTTP methods, and implement proper request/response handling.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Request Validation and Error Handling', 8, 'Implement input validation using Bean Validation API and create global exception handlers for consistent error responses.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - API Documentation with OpenAPI', 8, 'Generate interactive API documentation using OpenAPI 3.0 specifications and Swagger UI integration.');

-- Insert Codelabs for Spring Data JPA and Databases (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - JPA Entities and Repositories', 9, 'Create JPA entities with proper annotations and implement repository interfaces for database operations.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Custom Queries and Specifications', 9, 'Write custom JPQL queries and use JPA Criteria API for dynamic query building and complex data retrieval.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Database Migrations with Flyway', 9, 'Manage database schema changes using Flyway migrations and implement database versioning strategies.');

-- Insert Codelabs for Spring Security and Authentication (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - JWT Authentication Implementation', 10, 'Implement JWT-based authentication with Spring Security, create login endpoints and token validation filters.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Role-Based Authorization', 10, 'Configure method-level security and role-based access control using Spring Security annotations and expressions.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - OAuth2 and External Identity Providers', 10, 'Integrate with external OAuth2 providers like Google and GitHub for social login functionality.');

-- Insert Codelabs for Microservices Architecture (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Service Discovery with Eureka', 11, 'Implement service discovery using Netflix Eureka server and client for dynamic service registration and lookup.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - API Gateway with Spring Cloud Gateway', 11, 'Build an API gateway using Spring Cloud Gateway for request routing, load balancing, and cross-cutting concerns.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Circuit Breaker Pattern', 11, 'Implement circuit breaker pattern using Resilience4j to handle service failures and improve system resilience.');

-- Insert Codelabs for Message Queues and Event Processing (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Apache Kafka Integration', 12, 'Integrate Apache Kafka for asynchronous message processing, implement producers and consumers for event-driven architecture.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - RabbitMQ Message Patterns', 12, 'Use RabbitMQ for message queuing with different exchange types and routing patterns for reliable message delivery.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Event Sourcing Implementation', 12, 'Implement event sourcing pattern to store application state changes as a sequence of events for audit and recovery.');

-- Insert Codelabs for Docker and Containerization (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Dockerfile for Java Applications', 13, 'Create optimized Dockerfiles for Java applications using multi-stage builds and best practices for container security.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Docker Compose for Multi-Service Apps', 13, 'Orchestrate multi-container applications using Docker Compose with databases, message queues, and application services.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Kubernetes Deployment Strategies', 13, 'Deploy Java applications to Kubernetes cluster with proper resource management, health checks, and scaling policies.');

-- Insert Codelabs for Monitoring and Observability (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Micrometer and Prometheus Metrics', 14, 'Implement application metrics using Micrometer and expose them to Prometheus for monitoring and alerting.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - Distributed Tracing with Zipkin', 14, 'Add distributed tracing to microservices using Spring Cloud Sleuth and Zipkin for request flow visualization.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Centralized Logging with ELK Stack', 14, 'Implement centralized logging using Elasticsearch, Logstash, and Kibana for log aggregation and analysis.');

-- Insert Codelabs for DevOps and CI/CD Pipelines (3 codelabs)
INSERT INTO codelabs (id, title, submodule_id, details) VALUES
                                                            (nextval('codelabs_seq'), 'codelab01 - Jenkins Pipeline for Java Apps', 15, 'Create Jenkins CI/CD pipeline with automated testing, code quality checks, and deployment stages for Java applications.'),
                                                            (nextval('codelabs_seq'), 'codelab02 - GitHub Actions Workflow', 15, 'Implement GitHub Actions workflow for continuous integration with Maven/Gradle builds, testing, and artifact publishing.'),
                                                            (nextval('codelabs_seq'), 'codelab03 - Infrastructure as Code with Terraform', 15, 'Provision cloud infrastructure using Terraform for deploying Java applications with proper networking and security configurations.');


