# Nocturne Archive: Rare Book Inventory System
**CSCE 548 - Software Engineering Project 2**

## Project Overview
The Nocturne Archive is a multi-tiered Java application designed to manage rare book inventories and sales logs. It utilizes a professional service-oriented architecture to ensure separation of concerns between the user interface, business logic, and data persistence.

## Technical Architecture
This project implements a classic 3-tier architectural model:
* **Presentation Layer (`App.java`):** A console-based interface that invokes the Service Layer to perform CRUD operations.
* **Service Layer (`BookService.java`):** Acts as the primary interface for external consumers, delegating requests to the underlying business logic.
* **Business Layer (`BookBusinessLogic.java`):** Implements domain-specific validation rules and logic (e.g., price validation).
* **Data Access Layer (`DatabaseManager.java`):** Manages low-level JDBC connections and SQL execution against a MySQL database.

## Cloud Deployment & Hosting
To model professional development practices, the service is containerized and hosted in the cloud:
* **Hosting Platform:** Render (using Docker for container orchestration).
* **Database Provider:** Aiven Managed MySQL (Cloud-hosted).
* **CI/CD:** Continuous deployment via GitHub integration.

## Environment Configuration
The application is configured to be environment-agnostic using system variables:
* `DB_URL`: JDBC connection string for the Aiven instance.
* `DB_USER`: Database administrative username.
* `DB_PASSWORD`: Secure service password.

## Testing
The repository includes an automated test suite within `App.java` that verifies full CRUD functionality across all architectural layers upon deployment.