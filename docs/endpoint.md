# 🔐 Endpoint Permissions (RBAC)
Access to the API is restricted based on user roles embedded in the JWT token.

| Controller | Endpoint | Method | Required Role |
| :--- | :--- | :--- | :--- |
| **Auth** | `/auth/register` | `POST` | Public |
| **Auth** | `/auth/login` | `POST` | Public |
| **User** | `/user` | `POST` | `ROLE_ADMIN` |
| **User** | `/user/read/all` | `GET` | `ROLE_ADMIN` |
| **User** | `/user/read/{id}` | `GET` | `ROLE_ADMIN` OR Owner |
| **User** | `/user/update/{id}`| `PATCH`| `ROLE_ADMIN` OR Owner |
| **User** | `/user/delete/{id}`| `DELETE`| `ROLE_ADMIN` OR Owner |
| **User** | `/user/{userId}/manager`| `PATCH`| `ROLE_ADMIN` |
| **User** | `/user/{userId}/admin` | `PATCH`| `ROLE_ADMIN` |
| **Product** | `/product` | `POST` | `ROLE_MANAGER`, `ROLE_ADMIN` |
| **Product** | `/product/read/all` | `GET` | Public |
| **Product** | `/product/read/{id}` | `GET` | Public |
| **Product** | `/product/user/get` | `GET` | `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN` |
| **Product** | `/product/update/{id}`| `PATCH`| `ROLE_ADMIN` OR (`MANAGER` and Owner)|
| **Product** | `/product/delete/{id}`| `DELETE`| `ROLE_ADMIN` OR (`MANAGER` and Owner)|
| **Cart** | `/cart/items/{productId}`| `POST` | `ROLE_USER`, `ROLE_ADMIN` |
| **Cart** | `/cart/items/{cartItemId}`| `DELETE`| `ROLE_USER`, `ROLE_ADMIN` |
| **Cart** | `/cart/read` | `GET` | `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN` |
| **Cart** | `/cart/clear` | `DELETE`| `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN` |
| **Purchase**| `/purchase/{userId}/deposit`| `POST` | `ROLE_ADMIN` |
| **Purchase**| `/purchase/complete` | `POST` | `ROLE_USER`, `ROLE_ADMIN` |
| **Purchase**| `/purchase/orders` | `GET` | `ROLE_USER`, `ROLE_ADMIN` |
| **Purchase**| `/purchase/balance` | `GET` | `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN` |

---

#### Return to API Documentation:
[🔙 Return to 📄 API Documentation (Swagger/OpenAPI)](/docs/api-doc.md)
