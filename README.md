# Match Management System
This is a Match Management System built with Java Spring and Thymeleaf. The system allows users to view match details, with specific actions available based on their role (administrator or regular user). Administrators can add, delete, and edit matches, while regular users have view-only access and the ability to follow a certain match.

## Features
* User roles:
  * Administrators: Full CRUD (Create, Read, Update, Delete) capabilities for matches.
  * Regular users: View-only access to matches, follow a match.

* Match Management:
  * Matches are associated with specific locations.
  * Role-based access ensures proper functionality separation.

## Technology Stack
* Backend: Java Spring Boot
* Frontend: Thymeleaf
* Database: h2
* Testing: Comprehensive testing suite (details below)

## Tests
The project includes a detailed and robust testing suite to ensure the system's functionality, reliability, and security. The following testing methods have been implemented:

1. Unit Testing

Unit testing is a fundamental approach that focuses on testing individual components or functions in isolation. This method allows us to verify that each unit of our code, such as methods, classes, or modules, functions correctly according to their specifications. By isolating these units from the rest of the application, we can ensure that they produce the expected results.

2. Input Space Partitioning

This technique is used to systematically test different combinations and boundary inputs. By categorizing inputs into equivalent classes and testing representative values from each class, we ensure that our software can effectively handle various scenarios, ranging from typical inputs to edge cases.

4. Logic Coverage
   
Logic coverage involves examining the code coverage within the logic of our application (typically focusing on functions that return boolean values). This method helps improve the reliability and efficiency of our software by ensuring that all logical conditions and branches are tested.

4. Graph Coverage
   
Graph-based testing evaluates the flow and path coverage of our application's execution. It includes mapping out possible paths and transitions in our code to ensure that all branches are tested and that no potential issues are left unnoticed. This method helps identify and fix logical or flow-related problems within our application.

5. Integration Testing with Mockito and MockMVC

Integration testing is crucial for verifying the interactions between different parts of our Spring Boot application. Mockito helps us create mock objects to simulate dependencies, ensuring that the integration of various components works seamlessly. MockMVC, designed specifically for testing Spring MVC controllers, allows us to evaluate the functionality and behavior of our web endpoints.

6. Selenium for Web Testing

Selenium is a robust tool that we use to automate web browser interactions. It allows us to verify the functionality of our web-based components by simulating user actions and interactions. Selenium ensures that our web application works smoothly across different browsers and platforms.

## Installation
1. Clone the repository:

    ```sh
    git clone https://github.com/vladimir028/Software-Quality-Assurance-Project
    ```

2. Build the project:

    ```sh
    mvn clean install
    ```
3. Access the application:

   http://localhost:8080

## Contributing
Contributions to improve testing or other aspects of the project are welcome. Feel free to submit pull requests or report issues.
