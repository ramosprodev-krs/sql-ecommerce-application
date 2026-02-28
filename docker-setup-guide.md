# 🐳 Running the application with Docker (Guide)
In this guide you will learn the first steps of this application, including:
- Locally running the app with Docker
- Registering your first user
- Logging in with your user
- Authorizing endpoints with your JWT Token


### Setting things first
Before proceeding, ensure you have the following tools properly installed on your system:  
- [Java Development Kit (JDK 21)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)  
- [Docker Engine / Docker Desktop](https://www.docker.com/products/docker-desktop/)  
- [Insomnia](https://insomnia.rest/download) or [Postman](https://www.postman.com/downloads/) (use if you judge necessary, as Swagger is already included).

---

## The use of "docker-compose.yml"
This project includes a **docker-compose.yml** file designed to orchestrate the **PostgreSQL** database and the **Spring Boot** application containers seamlessly.

### 1. **Clone the repository** 
```bash
git clone [https://github.com/KalRSilva/sql-ecommerce-application.git](https://github.com/KalRSilva/sql-ecommerce-application.git)
cd sql-ecommerce-application
```

### 2. Start the containers Use Docker Compose to start both the Application and the Database containers:
```bash
docker-compose up --build
```

### 3. Available ports After successfully starting containers, the following ports are now available:
```bash
Port 8080 -> Runs the application itself (API)
Port 5432 -> Runs the PostgreSQL Database
```

### 4. Accessing the endpoints We are going to access the authentication and protected endpoints locally.
#### 4.1. Accessing localhost:8080/swagger-ui.html

You can authenticate directly by this endpoint via Swagger, by selecting the "Authentication controller".
Next, you can click the **/auth/register** api, and register you account by providing a JSON on the following pattern:
```
{
  "username" : "user",
  "password" : "password"  
}
```

The JWT Token will then be returned, ensuring the "login" and "authentication" methods are properly working.
Practical example - Swagger

Here's an example of this functionality, by using Swagger:
<img src="https://i.imgur.com/0OUuhlQ.png" width="1100">

#### 4.2. Accessing Protected Endpoints (e.g., /product)
Now that you have successfully registered your account, you can authorize the next protected 
requests by providing the generated token in the locker icon:

(image to be added)

Once you provided the token, you have complete access to the Protected Endpoints (if your role ensures they are accessible).

Now that you’ve finished this guide, you can return to the main documentation:  
[🔙📖 Go back to README](https://github.com/ramosprodev-krs/sql-ecommerce-application)
