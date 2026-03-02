# 📄 API Documentation (Swagger/OpenAPI)
The application provides an interactive **Swagger UI** (the same as we used initially) documentation, allowing us to visualize and test all endpoints directly from the browser.

## 1. Accessing the Documentation
As mentioned before, once the application is running via Docker, access the documentation at:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 2. 🛠️ API Structure & Implementation
The API is organized into functional controllers. The business logic for these endpoints is located in the **Services** directory.  
Each correspondent service is linked next to the mentioned controller, there it is possible to further visualize how each method was  
structured.

If you would wish to see a detailed list of each one of the endpoints,  
you can check this file:
- 🔐 [Endpoint Permissions(RBAC)](endpoint.md)

### 2.1. Authentication Controller (`/auth`)
Handles user registration and login.
* **Implementation:** [UserService](../src/main/java/com/ramosprodev/sql_application/service/UserService.java), [TokenService](../src/main/java/com/ramosprodev/sql_application/service/TokenService.java) and [EmailService](../src/main/java/com/ramosprodev/sql_application/service/EmailService.java)
<img src="https://i.imgur.com/hj4LscS.png">

### 2.2. User Controller (`/user`)
Handles user account management.
* **Implementation:** [UserService](../src/main/java/com/ramosprodev/sql_application/service/UserService.java)
<img src="https://i.imgur.com/O9N6ziK.png">

### 2.3. Cart Controller (`/cart`)
Handles user account management.
* **Implementation:** [CartService](../src/main/java/com/ramosprodev/sql_application/service/CartService.java)
<img src="https://i.imgur.com/oHBPzJF.png">

### 2.4. Product Controller (`/product`)
Manages the product catalog.
* **Implementation:** [ProductService](../src/main/java/com/ramosprodev/sql_application/service/ProductService.java)
<img src="https://i.imgur.com/l1Pv7mu.png">

### 2.5. Purchase Controller (`/purchase`)
Handles purchase operations.
* **Implementation:** [PurchaseService](../src/main/java/com/ramosprodev/sql_application/service/PurchaseService.java) and [EmailService](../src/main/java/com/ramosprodev/sql_application/service/EmailService.java)
<img src="https://i.imgur.com/IC3Zg96.png">

---

## 3. Testing Protected Endpoints

To test restricted endpoints, don't forget to authorize your session in Swagger:

1.  **Generate a Token:** Use `POST /auth/login`.
2.  **Authorize:** Click the **Authorize** (lock icon) button.
3.  **Input Token:** Enter `Bearer <your_token>`.  

Locker icon:  
<img src="https://i.imgur.com/l2OBUm6.png">

Token screen:  
<img src="https://i.imgur.com/COXKn2l.png">

---

#### Return to main documentation:
[🔙📖 Go back to README](https://github.com/ramosprodev-krs/sql-ecommerce-application)
