# 🏪 SmartStore - Retail Inventory & POS Billing System

A modern, full-stack **Point of Sale (POS), Inventory Management, and Billing Software** designed for small to medium retail stores (Kirana, Medical Stores, Electronics, Hardware shops). Built with **Spring Boot 3 (Java)** and **React 18 (Vite + Tailwind CSS)**.

---

## ✨ Features

- **🛒 Point of Sale (POS) Billing Counter**:
  - Live product search by name or barcode scanner.
  - Real-time stock validation (prevents overbilling beyond available inventory).
  - Product thumbnail previews for quick visual identification.
  - Auto-calculation of Subtotal, GST Tax %, Discounts, and Grand Total.
  - One-click **PDF Tax Invoice** generation and instant printing.
  - Atomic `@Transactional` database operations (bill generation & stock deduction commit together).

- **📦 Product & Inventory Management**:
  - Full Product CRUD (Create, Read, Update, Delete) with image URLs, categories, units, and barcode.
  - **Low-Stock Alert Badges** (highlight items running low on stock).
  - Categorization (Grocery, Medicines, Electronics, etc.).

- **📊 Business Analytics Dashboard**:
  - Daily & Monthly Revenue, Total Bills Billed, Total Items Tracked.
  - Interactive **Sales Revenue Graph** (Chart.js).
  - Low-stock action warning list.
  - Recent transactions table with PDF re-print options.

- **🔒 Role-Based Access Control (Spring Security + JWT)**:
  - **Store Owner (`ROLE_ADMIN`)**: Full access to Analytics Dashboard, Product CRUD, Reports, and POS.
  - **Cashier (`ROLE_CASHIER`)**: Dedicated access to POS Billing Screen and Product search.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3, Spring Data JPA, Spring Security (JWT), OpenPDF (PDF Generator), Validation
- **Database**: PostgreSQL / MySQL / H2 (Zero-setup in-memory default)
- **Frontend**: React 18, Vite, Tailwind CSS, Lucide React Icons, Chart.js, Axios

---

## 📋 Prerequisites

Make sure you have the following installed on your machine:
- **Java 17 or higher** (`java -version`)
- **Node.js 18 or higher** and **npm** (`node -v`, `npm -v`)
- *Optional*: PostgreSQL / MySQL (if not using default in-memory database)

---

## 🚀 Quick Start Guide (Run Locally)

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/MukundKumar902/Inventory-Billing-System.git
cd Inventory-Billing-System
```

---

### 2️⃣ Start the Backend Server (Spring Boot)

Open a terminal in the `backend/` folder:

```bash
cd backend
```

- **On Windows**:
  ```bash
  .\mvn.bat spring-boot:run
  ```
  *(or if you have Maven installed globally: `mvn spring-boot:run`)*

- **On Linux / macOS**:
  ```bash
  mvn spring-boot:run
  ```

> 🟢 **Backend will start on:** `http://localhost:8080`

---

### 3️⃣ Start the Frontend (React + Vite)

Open another terminal in the `frontend/` folder:

```bash
cd frontend
npm install
npm run dev
```

> 🌐 **Open in your browser:** `http://localhost:5173`

---

## 🔑 Default Login Credentials

The application automatically seeds default accounts and sample products with images on first startup:

| Role | Username | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Store Owner (Admin)** | `admin` | `admin123` | Full Access (Dashboard, Products CRUD, Billing, Reports) |
| **Cashier** | `cashier` | `cashier123` | POS Billing & Product Lookup |

*(Quick auto-fill buttons for both roles are also available directly on the login screen for testing!)*

---

## 🗄️ Database Configuration

By default, the backend connects to your configured database. You can customize the database settings in:
`backend/src/main/resources/application.yml`

### Example: Running with Local PostgreSQL
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/inventory_db
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
```

### Example: Running with Local MySQL
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/inventory_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
```

### Example: Zero-Setup In-Memory H2 DB
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:inventorydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

---

## 🔌 API Endpoints Summary

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Authenticate user & get JWT token | Public |
| `GET` | `/api/products` | Get list of all products with stock & photos | Authenticated |
| `POST` | `/api/products` | Add new product with image URL | Admin only |
| `PUT` | `/api/products/{id}` | Update product details or stock | Admin only |
| `DELETE` | `/api/products/{id}` | Delete product | Admin only |
| `GET` | `/api/products/low-stock` | Get low stock warning list | Authenticated |
| `POST` | `/api/invoices` | Create invoice, update stock (@Transactional) | Authenticated |
| `GET` | `/api/invoices` | Get transaction history | Authenticated |
| `GET` | `/api/invoices/{id}/pdf`| Download dynamic PDF Tax Invoice | Authenticated |
| `GET` | `/api/reports/today` | Get today's sales KPI & revenue stats | Admin only |

---

## 📜 License & Contributions
This project is open-source and available under the [MIT License](LICENSE). Contributions, feedback, and pull requests are welcome!
