
The goal of this documentation is to provide an overview of how the app works and is implemented, as well as some important key points.
More detailed documentation about specific methods and classes can be found as javadoc.

---

### Table of Contents

1. Project Overview
2. Roles & Authorization
3. System Architecture
4. Backend & API Functionalities by Domain
    - User
    - Class
    - Courses & Content Structure
	    - Courses
	    - Modules & Submodules
	    - Codelabs
    - Linking Courses & Classes
    - Statistics & Progress Tracking
5. Technologies Used
6. Backend Project Structure

---

### 1. Project Overview

This Learning Management System (LMS) is a full-stack web application that allows administrators to manage users, coaches to assign courses and track student progress, and students to follow a course structure comprising courses, modules, submodules, and codelabs. This project was developed as our final Java training assignment.

The app offers:

- Structured course organization
- Multi-level content hierarchy (Courses > Modules > Submodules > Codelabs)
- User roles and permissions
- Admin and coach dashboards
- Progress tracking features

---

### 2. Roles & Authorization

**Coach**:

- Can create Courses, Modules, Submodules, and Codelabs
- Can manage Classes
- Can link Courses to Classes
- Can leave comments on codelabs

**Student**:

- Can view their assigned Classes
- Can follow course structure and mark codelabs as complete
- Can track their own progress in the codelabs
- Can leave comments on codelabs

**Authorization is enforced with OAuth2 via Keycloak. Keycloak integration supports:**

- Login and registration via Keycloak endpoints
- Token-based session management
- Role-based access

---

### 3. System Architecture

- **Backend**: Java + Spring Boot
- **Frontend**: Angular + BootStrap
- **Database**: PostgreSQL
- **Authentication**: Keycloak
- **Test DB**: JUnit, AssertJ, H2

REST APIs and their services are split by domain (User, Course, Module, etc.), with their associated DTOs and mappers.

---

### 4. Backend & API Functionalities by Domain

Here are descriptions of the most important points of the different domain objects.
For a clear view of the way they depend and interact on each other, please refer to the database diagram.

#### 4.1 User

This contains both students and coaches. It has has 9 fields:
- Some basic ones (including `firstName`, `username`, etc...)
- A `role` class that's an enum and separates coaches from students
- A `class` list, which is based on a *ManyToMany* relationship with the `class` domain object, and contains all the classes associated with a user.

This list is initiated empty, as a user (either coach or student) needs to add himself to which class(es) he belongs to. A student can only belong to one class while a coach can belong to several.

**Here are the main endpoints for User:**

- **registerAsStudent:** Used to add a new user to both Keycloak and the database. It was decided to start with adding the student on Keycloak first to ensure that if the addition is not successful (which is more likely the case due to the several problems and constant evolution of keycloak), it won't be added to the database. The database is queried to check for already existing email/username because if it was added on it but not on keycloak, that would be a problem for the user if he tries to register again.
- **getProfileInfo:** Get the basic profile informations of a specific user.
- **updatedProfileInfo:** Update the password and display names of a user, the other fields cannot be modified at the moment.
- **getClassOverview:** Get an overview of the classes associated with a specific user.
- **updateClassInfo:** Add one class to the user's list of associated classes.

**Some additional services:**

- **validateStudentInput:** Validate that the information provided by the user are correct, username and email should not already be present in the database and the email should have the right format.
- **getClassDtoListUser:** Private helper method that gets additional fields used in the class mapper method `ClassOutputDtoList`, also handles null pointers.

**DTOs**

The User domain object requires more DTOs than just a basic Input & Output.

- **UserInputDto:** Used to register a new student. Since only students can register at the moment, in the mapper, the `inputToUser` method has the role “`STUDENT`” hardcoded in it.

- **UserInputEditDto:** Used to update the profile information of the user, as mentioned before only the password and the display name can be changed at the moment.

- **UserOutputDto:** Provides basic information about the user: username, display name and email. It's also used in the class service, to avoid an infinite loop between the list of classes of the `User` entity and the list of users of the `Class` entity.

- **UserOutputDtoList:** Provides all of the information from above + a list of all the classes associated with that user. Used for the profile overview.

**Mappers**

User's mappers also have some specific cases.  

- **InputToUser:** As mentioned above, the `inputToUser` has the role hardcoded there, but it also is where the display name is defined (It's the concatenation of the First Name and Last Name with a space between the two). 

- **userInputToKeycloakUser and userEditToKeycloak:** Used to convert the `userInputDto` and `userInputEditDto` into a KeycloakUser to allow its upload to the Keycloak server. Like for the `inputToUser`, the “`STUDENT`” role is hardcoded.

- **userToOutputList and userToOutput:** Used to produce the output DTOs, but one with the list of classes and one without.

#### 4.2 Class

A class represents a single 'instance' of a course given (and so associated to) a group of students during a period of time. Has fields, it has:
- A simple `title`
- A course field representing the course that's given in this class and is based on a *ManyToOne* relationship with the `Course` domain object, as they can be given in many classes but a class only gives one course
- A list representing the users associated to this class, both coaches and students. It's based on a *ManyToMany* relationship with the User domain object, as coaches can give multiple classes (but students can still only follow one at a time)

**DTOs**

There's an InputDto, OutputDto, and OutputDtoList for Classes.
The difference between those last two is that OutputDto only contains the title and the id, while OutputDtoList also contains the associated Course object and the list of associated Users.

That list of associated user is condensed and does not contain its list of associated classes to avoid
a self-referencing infinite loop that would cause a stack overflow, which is the other side of the same problem mentioned in User above.

#### 4.2 Courses & Content Structure

The three next domain objects works together as a hierarchy, so we'll group them together here.
They're only proper field is a title, except for `Codelab` which also has a description. The other fields represents their relationship with other objects.

The structure is simple: Courses have modules, modules have submodules, and submodules have codelabs (exercises).

Next is an explanation of how they work together.
##### **4.2.1 Courses**

Courses don’t have parents, and don’t need to be linked to a `Class` at creation. So they are just created through their API with their title, and that’s it. It's the role of a new class to assign the course to it, obviously.
##### **4.2.2 Modules and Submodules**

They are both very similar.

From the backend perspective, they don’t need (and **can't** get, anyway) a parent at creation, just a title.

The first reason for that are that conceptually, their parents are the owner of the relationships so it’s a better practice for it to be established parent-side. The other reason is that it makes the code simpler and more straightforward that way.

**_However, the specs requires at least one parent at their creation._**  

So how it works, in practice:
- In the Backend, the Course API has an `addChildModule` endpoint, and the Module API has an `addChildSubModule` endpoint.  
- In the Frontend, when the user wants to create a Module and selects an associated course:  
- Angular firsts makes a requests to the Modules API to create a new Module with just a `title`, and gets its `id` in return.  
- Angular then must make a requests to the Course API to add that new Module to the appropriate parent course, using their IDs.  
- Same process for creating a Submodule and associating it to its Module parent.
##### **4.2.3 Codelabs**

Codelabs also require a submodule parent at creation. However, they have a _ManyToOne_ relationship with a parent submodule, so they also have a **non-nullable** `parent_id` column in the database (instead of using a joint table), meaning **they must have a parent at creation, right away**.

In the Backend, the Create Codelab endpoint requires a `courseId` that it will save both as a parent in the codelab, and as a child in the submodule.  
- In the Frontend, Angular just makes a single request to the `createCodelab` endpoint with its `title`, `description` and `parentSubmoduleId` (edited)

---

### 5. Technologies Used

- Java 23
- Spring Boot
- Angular 17
- PostgreSQL
- Maven
- Keycloak

---

### 6. Backend Project Structure


- `webapi/`: REST API endpoints (controllers)
- `domain/`: JPA entities and enums
- `repository/`: Spring Data interfaces
- `service/`: Business logic
- `dto/`: Data Transfer Objects
- `mapper/`: Mappers (Entities <-> DTO)
