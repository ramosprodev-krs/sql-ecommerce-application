# ⚙️ Application Configuration
This document outlines the configuration classes used to manage security, documentation, and external service integration.

---

## 1. Security Configuration (`SecurityConfiguration`)
Configures Spring Security to handle authentication and authorization.

* **Key Features:**
    * **JWT Authentication:** Uses a custom [`SecurityFilter`](../src/main/java/com/ramosprodev/sql_application/filter/SecurityFilter.java to validate JWT tokens.
    * **Stateless Session:** Configured to `SessionCreationPolicy.STATELESS` as the application uses JWT instead of HTTP sessions.
    * **Public Endpoints:** [`/auth/**`](../src/main/java/com/ramosprodev/sql_application/controller/AuthenticationController.java) and Swagger UI paths are open to public access.
    * **Protected Endpoints:** All other API paths require authentication.
    * **Password Encoding:** Uses `BCryptPasswordEncoder` for hashing user passwords.
    * **Method Security:** Enabled via `@EnableMethodSecurity` to allow `@PreAuthorize` annotations on controllers.

---

## 2. Swagger Configuration (`SwaggerConfiguration`)
Customizes the OpenAPI documentation to support JWT authentication.

* **Purpose:** Configures Swagger UI to include a "Bearer Token" input field, allowing testers to insert their JWT token directly into the Swagger UI for testing protected endpoints.                
* **Version:** API documentation is set for version "1.0".

---

## 3. SES Configuration (`SESConfiguration`)
Configures the Amazon Simple Email Service (SES) client for sending emails (e.g., welcome emails, deposit notifications).

* **Credentials:** Retrieves AWS Access Key, Secret Key, and Region from application properties (`application.properties` or environment variables).
* **Bean:** Creates a `SesClient` bean used by the [`EmailService`](../src/main/java/com/ramosprodev/sql_application/service/EmailService.java) to interact with AWS SES.

---

#### Return to main documentation:
[🔙📖 Go back to README](https://github.com/ramosprodev-krs/sql-ecommerce-application)
