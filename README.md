# Inventory + Billing System (POS & Stock Management)

A full-stack enterprise retail inventory, billing, and sales analytics software built for small/medium retail stores (Kirana, Medical Stores, Mobile Shops, Hardware Stores).

---

## 🚀 Key Features & Modules

1. **POS Billing Counter**:
   - Product search by name or barcode lookup.
   - Real-time stock validation (prevents overbilling beyond stock).
   - Auto-calculation of Subtotal, GST Tax %, and Grand Total.
   - Atomic `@Transactional` stock reduction + Invoice generation.
   - Dynamic OpenPDF PDF Invoice download & instant printing.

2. **Inventory & Product Management**:
   - Product CRUD operations (Add, Edit, Delete).
   - Category and Unit management (kg, pcs, strip, pouch, etc.).
   - Low-stock visual alerts when item quantity falls below `minStockAlert`.

3. **Role-Based Access Control (Spring Security + JWT)**:
   - **Store Owner (`ROLE_ADMIN`)**: Full access to Dashboard, Sales Analytics, Product CRUD, and Reports.
   - **Cashier (`ROLE_CASHIER`)**: Access restricted to POS Billing Counter and Product list.

4. **Business Analytics Dashboard**:
   - Daily Revenue, Total Orders Billed, Total Stock Count KPI Cards.
   - Interactive Sales Revenue Chart (Chart.js).
   - Low-stock action warning list.

---

## 🛠️ Tech Stack

* **Backend**: Spring Boot 3, Spring Data JPA, Spring Security (JWT), OpenPDF
* **Database**: H2 (In-memory zero-setup default) / MySQL (Production ready)
* **Frontend**: React 18, Vite, Tailwind CSS, Axios, Chart.js, Lucide Icons

---

## 🔑 Pre-seeded Demo Logins

Upon starting the application, the database is auto-seeded with test users and sample inventory items:

| Role | Username | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Store Owner (Admin)** | `admin` | `admin123` | Full Access (Dashboard, Products, Billing, Reports) |
| **Cashier** | `cashier` | `cashier123` | POS Billing & Product Lookup only |

---

## 💻 How to Run the Application

### 1. Run Backend (Spring Boot)
Ensure Java 17+ is installed.

```bash
cd backend
mvn spring-boot:run
```
* Backend REST API runs on: `http://localhost:8080`
* H2 Database Console available at: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:inventorydb`)

### 2. Run Frontend (React + Vite)
Ensure Node.js 18+ is installed.

```bash
cd frontend
npm install
npm run dev
```
* Access Frontend UI at: `http://localhost:5173`

---

## 🗄️ Database Configuration (Switching from H2 to MySQL)

By default, the application runs on **H2 In-Memory DB** for instant zero-configuration testing.

To switch to **MySQL**:
1. Open `backend/src/main/resources/application.yml`
2. Create database in MySQL:
   ```sql
   CREATE DATABASE inventory_billing_db;
   ```
3. Uncomment the MySQL configuration section in `application.yml` and enter your MySQL username & password:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/inventory_billing_db?useSSL=false&serverTimezone=UTC
       username: root
       password: your_mysql_password
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       database-platform: org.hibernate.dialect.MySQLDialect
       hibernate:
         ddl-auto: update
   ```
