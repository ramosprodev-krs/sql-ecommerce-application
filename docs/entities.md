# 📊 Database Entities (PostgreSQL)

This document describes the structure and relationships of the database entities used in the e-commerce system.

You can also check the classes diagram in the following file:
- 📑 [Class diagram (PostgreSQL entities)](diagram.md)

---

## 1. User Entity (`UserEntity`)
Represents a user in the system, utilizing Spring Security's `UserDetails` for authentication.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key (Auto-incremented). |
| `username` | String | Unique username (min 5, max 20 chars). |
| `password` | String | Hashed password (hidden in JSON responses). |
| `email` | String | Validated email address. |
| `userBalance` | BigDecimal | User credit balance for purchases. |
| `createdAt` | LocalDateTime| Timestamp of account creation. |

### Relationships:
* **One-to-One** with `CartEntity` (`cart_id`): Each user has one shopping cart.
* **One-to-Many** with `OrderEntity`: A user can have multiple orders.
* **One-to-Many** with `ProductEntity` (`creator_id`): A user (manager/admin) can create multiple products.
* **ElementCollection** (`userRoles`): Set of `UserRole` enums for RBAC.

---

## 2. Product Entity (`ProductEntity`)
Represents an item available for sale in the catalog.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key. |
| `name` | String | Name of the product (min 5, max 20 chars). |
| `description` | String | Product description. |
| `stockQuantity` | Integer | Quantity available in stock. |
| `price` | BigDecimal | Unit price of the product. |

### Relationships:
* **Many-to-One** with `UserEntity` (`creator_id`): The user who registered the product.

---

## 3. Cart Entity (`CartEntity`)
Represents the temporary storage of products a user intends to purchase.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key. |
| `cartPrice` | BigDecimal | Total value of items currently in the cart. |

### Relationships:
* **One-to-One** with `UserEntity` (`user_id`): The owner of the cart.
* **One-to-Many** with `CartItemEntity` (`cart_id`): The items added to the cart (with `orphanRemoval=true`).

---

## 4. Cart Item Entity (`CartItemEntity`)
Represents a specific product and its quantity within a cart.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key. |
| `quantity` | Integer | Quantity of the product. |
| `unitPrice` | BigDecimal | Price of the product at the time it was added. |
| `totalPrice` | BigDecimal | `unitPrice` * `quantity`. |

### Relationships:
* **Many-to-One** with `CartEntity` (`cart_id`).
* **Many-to-One** with `ProductEntity` (`product_id`): Utilizes `OnDeleteAction.CASCADE` to remove items if the product is deleted.

---

## 5. Order Entity (`OrderEntity`)
Represents a finalized purchase made by a user.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key. |
| `purchaseTotalPrice`| BigDecimal | Total price of the order. |
| `purchasedAt` | LocalDateTime| Timestamp when the order was placed. |

### Relationships:
* **Many-to-One** with `UserEntity` (`user_id`): The user who placed the order.
* **One-to-Many** with `OrderItemEntity` (`order_id`): The items included in this order.

---

## 6. Order Item Entity (`OrderItemEntity`)
Represents a snapshot of a product at the moment an order is finalized.

| Attribute | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | Primary Key. |
| `name` | String | Name of the product at the time of purchase. |
| `quantity` | Integer | Quantity purchased. |
| `currentPrice` | BigDecimal | Price at the time of purchase. |
| `totalPrice` | BigDecimal | `currentPrice` * `quantity`. |

### Relationships:
* **Many-to-One** with `OrderEntity` (`order_id`).

---

## 🔢 Enumerations

### `UserRole`
Defines the permissions within the system.
* `ADMIN` (ROLE_ADMIN)
* `USER` (ROLE_USER)
* `MANAGER` (ROLE_MANAGER)

---

#### Return to main documentation:
[🔙📖 Go back to README](https://github.com/ramosprodev-krs/sql-ecommerce-application)
