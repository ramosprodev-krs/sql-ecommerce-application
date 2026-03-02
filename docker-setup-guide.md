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
#### 4.1. Registering with Swagger.

In order to register your first account, we need are accessing localhost:8080/swagger-ui.html.
Next, you can visualize the **register** and **login** endpoints, by clicking the **"Authentication controller"** tab.

Finally, you can click the **/auth/register** api, and register you account by providing a JSON on the following pattern:
```
{
  "username" : "username",
  "password" : "password",
  "email" : "example@gmail.com"
}
```

In a few seconds, you will receive a confirmation from AWS SES, if the provided e-mail is
authenticated in the service.

Here's an example of this functionality, by using Swagger:
<img src="https://i.imgur.com/A8vK0yA.png" width="1100">

Confirmation e-mail successfully received:
<img src="https://i.imgur.com/YBezApx.png" width="1100">

#### 4.2 Logging in with Swagger
Now, you can login by accesing the **/auth/login** endpoint, and simply providing the following JSON pattern:
``` 
{
  "username" : "username",
  "password" : "password"
}
```

Here's an example:
<img src="https://i.imgur.com/SJPjNcC.png" width="1100">

Your token is now returned, for further authorization management:
<img src="https://i.imgur.com/6OkwopZ.png">

#### 4.2. Accessing Protected Endpoints (e.g., /product)
Now that you have successfully registered your account, you can authorize the next protected 
requests by providing the generated token in the locker icon:

(image to be added)

Once you provided the token, you have complete access to the Protected Endpoints (if your role ensures they are accessible).

Now that you’ve finished this guide, you can return to the main documentation:  
[🔙📖 Go back to README](https://github.com/ramosprodev-krs/sql-ecommerce-application)
