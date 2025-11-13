# 🛍️ E-commerce Microservices Platform

A **microservices-based e-commerce backend** built with **Spring Boot 3.x**, **Kafka**, **MySQL**, and **Docker**. This platform is designed to handle user management, product catalog, shopping cart, orders, payments, inventory, and notifications.

---


## 📑 Table of Contents

- **Project Structure**
- **Features**
- **Tech Stack**
- **Architecture**
- **CI/CD Pipeline**
- **Setup Instructions**
- **API Documentation**
  - **User Service**
  - **Product Service**
  - **Cart Service**
  - **Order Service**
  - **Inventory Service**
  - **Payment Service**
  - **Notification Service**
- **Testing**
- **Notes**
- **Contributing**
- **Contact**

--- 

## 📂 Project Structure

```
ecommerce-microservices-platform/
├── 📁 backend-microservices/
│ ├── 🏗️ discovery-server/
│ ├── 🚪 api-gateway/
│ ├── 👤 user-service/
│ ├── 📦 product-service/
│ ├── 🛒 cart-service/
│ ├── 📋 order-service/
│ ├── 💳 payment-service/
│ ├── 📊 inventory-service/
│ ├── 📧 notification-service/
├── create-topics.sh
└── 🐳 docker-compose.yml
└── workflows/
  ├── ci-cd-discovery.yml # CI/CD for Discovery Server
  ├── ci-cd-user.yml # CI/CD for User Service
  └── ... # Other service pipelines
```

---

## ✨ Features

- **Microservices Architecture** with independent deployable services
- **Service Discovery** using Netflix Eureka
- **API Gateway** for routing and request handling
- **JWT-based Authentication** for secure access
- **Individual Databases** for each service (MySQL / MariaDB)
- **Messaging & Event Streaming** via Kafka
- **Dockerized Services** for easy deployment
- **CI/CD Integration via GitHub Actions** for automated builds and deployments  
- **RESTful APIs** for all business operations
- **Notification System** for emails and alerts
- **Scalable Design** for extending more services easily

---

## 🛠 Tech Stack

- **Backend Framework:** Spring Boot 3.x
- **Language:** Java 17+
- **Security:** Spring Security with JWT
- **Database:** MariaDB / MySQL
- **Messaging:** Apache Kafka
- **API Gateway:** Spring Cloud Gateway
- **Service Discovery:** Netflix Eureka
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose
- **CI/CD:** GitHub Actions  
- **Testing:** JUnit / Mockito
- **Documentation:** OpenAPI / Swagger
- **Email Notifications:** SMTP (configurable in environment variables)

---

## 🏗 Architecture

- **Discovery Server** – Registers all microservices for service discovery
- **API Gateway** – Routes incoming requests to appropriate services
- **User Service** – Handles authentication, registration, and profile management
- **Product Service** – Manages product catalog and queries
- **Cart Service** – Manages user shopping carts
- **Order Service** – Handles order creation, status, and history
- **Payment Service** – Processes payments and refunds
- **Inventory Service** – Tracks product stock levels
- **Notification Service** – Sends emails or alerts for orders and payments
- **Kafka & Zookeeper** – Messaging backbone for asynchronous events
- **Docker Compose** – Orchestrates all services and databases

---
## 🧩 CI/CD Pipeline (GitHub Actions)

This project includes **automated CI/CD pipelines** powered by **GitHub Actions**.

**Workflow Overview**

Each microservice (e.g., `discovery-server`, `user-service`, etc.) has a workflow file inside `.github/workflows/`.  

**CI/CD Flow**
- Triggered on push or PR to a specific service folder

- Builds the microservice using Maven

- Runs unit tests

- Builds Docker image

- Pushes image to DockerHub automatically

This ensures every service is built, tested, and deployed independently — achieving full CI/CD automation.

---
## ⚙️ Setup Instructions
**Prerequisites**

- Docker & Docker Compose

- Java 17

- Maven

**Run Application**
**Clone repo**
```
git clone <repository-url>
cd ecommerce-microservices-platform
```
**Build project**
```
mvn clean install
```
**Start all services with Docker Compose**
```
docker-compose up -d
```

**Access Services**

- [Eureka Dashboard](http://localhost:8761) - http://localhost:8761  
- [API Gateway](http://localhost:8080) - http://localhost:8080  


---

## 🚀 API Documentation

### 1️⃣ User Service APIs  
**Authentication & User Management**

| Method | Endpoint               | Description            |
|--------|------------------------|-----------------------|
| POST   | `/users/register`      | Register a new user    |
| POST   | `/users/login`         | User login             |
| GET    | `/users/profile`       | Get user profile       |
| PUT    | `/users/profile`       | Update user profile    |
| GET    | `/users`               | Get all users          |

**Request/Response Examples:**


// Register
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890"
}
```
// Login Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "CUSTOMER"
  }
}
```

### 2️⃣ Product Service APIs

**Product Management**

| Method | Endpoint                                   | Description                  |
|--------|-------------------------------------------|------------------------------|
| GET    | `/products`                                | Get all products             |
| GET    | `/products/{id}`                           | Get product by ID            |
| POST   | `/products`                                | Create a new product         |
| PUT    | `/products/{id}`                           | Update product by ID         |
| DELETE | `/products/{id}`                           | Delete product by ID         |
| GET    | `/products/category/{category}`            | Get products by category     |
| GET    | `/products/search?keyword={keyword}`      | Search products by keyword  |
| GET    | `/products/price-range?minPrice={min}&maxPrice={max}` | Get products in price range |


**Request Example:**
//Create Product
```json
{
  "name": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 999.99,
  "category": "Electronics",
  "imageUrl": "https://example.com/iphone15.jpg",
  "stockQuantity": 50
}
```

**3️⃣ Cart Management**

| Method | Endpoint                                         | Description                  |
|--------|-------------------------------------------------|------------------------------|
| GET    | `/carts/{userId}`                               | Get cart for a user          |
| POST   | `/carts/{userId}/add`                           | Add item to cart             |
| PUT    | `/carts/{userId}/update?productId={id}&quantity={qty}` | Update item quantity in cart |
| DELETE | `/carts/{userId}/remove?productId={id}`        | Remove item from cart        |
| DELETE | `/carts/{userId}/clear`                         | Clear all items from cart    |

**Request Example:**
```json
{
  "productId": 1,
  "quantity": 2,
  "productName": "iPhone 15",
  "price": 999.99
}
```
**Cart Response Example:**

```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "productName": "iPhone 15",
      "price": 999.99,
      "quantity": 2,
      "imageUrl": "https://example.com/iphone15.jpg"
    }
  ],
  "totalPrice": 1999.98
}
```

4️⃣ **Order Management**

| Method | Endpoint                                   | Description                  |
|--------|-------------------------------------------|------------------------------|
| POST   | `/orders`                                 | Create a new order           |
| GET    | `/orders/{orderId}`                       | Get order by ID              |
| GET    | `/orders/number/{orderNumber}`            | Get order by order number    |
| GET    | `/orders/user/{userId}`                   | Get all orders for a user    |
| PUT    | `/orders/{orderId}/status?status={status}` | Update order status         |
| GET    | `/orders`                                 | Get all orders               |

**Request Example:**
```json
{
  "userId": 1,
  "shippingAddress": {
    "recipientName": "John Doe",
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "phone": "+1234567890"
  },
  "items": [
    {
      "productId": 1,
      "productName": "iPhone 15",
      "price": 999.99,
      "quantity": 1
    }
  ]
}
```
5️⃣ **Inventory Service (Stock Management)**

| Method | Endpoint                                   | Description                        |
|--------|-------------------------------------------|------------------------------------|
| GET    | `/inventory/{productId}`                  | Get stock for a specific product   |
| PUT    | `/inventory/{productId}/update`          | Update stock quantity              |
| POST   | `/inventory/{productId}?initialQuantity={qty}` | Initialize stock for a product  |
| GET    | `/inventory`                              | Get all inventory                  |

**Request Example:**
```json
{
  "quantity": 100,
  "operation": "SET"  // Options: INCREMENT, DECREMENT, SET
}
```
6️⃣ **Payment Processing**

| Method | Endpoint                                   | Description                  |
|--------|-------------------------------------------|------------------------------|
| POST   | `/payments`                               | Process a new payment        |
| GET    | `/payments/{id}`                           | Get payment by ID            |
| GET    | `/payments/order/{orderId}`                | Get payments by order ID     |
| GET    | `/payments/user/{userId}`                  | Get payments by user ID      |
| PUT    | `/payments/{id}/status?status={status}`   | Update payment status        |
| POST   | `/payments/{id}/refund`                    | Refund a payment             |

**Request Example:**
```json
{
  "orderId": 1,
  "userId": 1,
  "amount": 999.99,
  "method": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "expiryDate": "12/25",
  "cvv": "123"
}
```
**Response Example:**

```json
{
  "success": true,
  "message": "Payment successful",
  "transactionId": "TXN-ABC123",
  "payment": {
    "id": 1,
    "orderId": 1,
    "status": "SUCCESS",
    "amount": 999.99
  }
}
```
7️⃣ **Notification Service (Notification Management)**

| Method | Endpoint                     | Description                       |
|--------|------------------------------|-----------------------------------|
| POST   | `/notifications/send`        | Send a notification               |
| GET    | `/notifications/user/{userId}` | Get notifications for a user    |
| GET    | `/notifications/pending`     | Get pending notifications         |
| POST   | `/notifications/retry-failed` | Retry failed notifications       |

**Request Example:**
```json
{
  "userId": 1,
  "type": "EMAIL",
  "recipient": "john@example.com",
  "subject": "Order Confirmation",
  "message": "Your order has been confirmed..."
}
```
---

## 🧪 Testing APIs

**Add to Cart:**

```bash
POST http://localhost:8080/carts/1/add
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "productId": 1,
  "quantity": 2,
  "productName": "iPhone 15",
  "price": 999.99,
  "imageUrl": "https://example.com/iphone15.jpg"
}
```

**Create Inventory:**
```bash
POST http://localhost:8080/inventory/1?initialQuantity=100
Content-Type: application/json
```

**Create Order:**
```bash
POST http://localhost:8080/orders
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "userId": 1,
  "shippingAddress": {...},
  "items": [...]
}
```

**Process Payment:**
```bash
POST http://localhost:8080/payments
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "orderId": 1,
  "userId": 1,
  "amount": 999.99,
  "method": "CREDIT_CARD",
  "cardNumber": "4111111111111111",
  "expiryDate": "12/25",
  "cvv": "123",
  "cardHolderName": "John Doe"
}

```
**Send Notification:**
```bash
POST http://localhost:8080/notifications/send
Content-Type: application/json

{
  "userId": 1,
  "type": "EMAIL",
  "recipient": "john@example.com",
  "subject": "Test Notification",
  "message": "This is a test notification"
}
```
---

## 📝 Notes

- Each microservice has its own application.yml, Dockerfile, and database schema.

- Kafka topics are initialized using create-topics.sh.

- Services can be started individually by using Docker Compose or Maven.

- Ensure ports defined in docker-compose.yml do not conflict with local services.

---

## 🤝 Contributing

We welcome contributions from the community! Follow these steps to contribute:

1. **Fork the Repository**  
   Click the "Fork" button on GitHub to create your copy.

2. **Clone your forked repository**  
   ```bash
   git clone <your-forked-repo-url>
   cd ecommerce-microservices-platform
   ```
3. **Create a new branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
4. **Make your changes**
   - Follow the existing project structure.

   - Write clean, readable, and maintainable code.

   - Update documentation if necessary.

5. **Commit your changes**

    ```bash
    git add .
    git commit -m "Add feature/describe-feature"
    ```

6. **Push your branch**

    ```bash
    git push origin feature/your-feature-name
    ```
7. **Open a Pull Request**

    - Go to the original repository.

    - Click "New Pull Request" and select your branch.

**Guidelines:**

- Use descriptive commit messages.

- Ensure all tests pass before submitting.

- Keep code consistent with existing style.

## 📫 Contact

**Project Maintainer:** Harshvardhan Singh  

- **Email:** [harshvardhan1483@gmail.com](mailto:harshvardhan1483@gmail.com)  
- **LinkedIn:** [https://www.linkedin.com/in/harshvardhansingh-in/](https://www.linkedin.com/in/harshvardhansingh-in/)  
- **GitHub:** [https://github.com/harshvardhansingh7](https://github.com/harshvardhansingh7)

