# 📦 Product Expiry Tracker

A desktop application to track product expiry dates and manage inventory, built with **Java Swing & MySQL**.

## Features
- Add, update, delete product records
- Automatic expiry status: **SAFE / WARNING / CRITICAL / EXPIRED**
- Color-coded table for easy visualization
- Filter products expiring within next 30 days
- MySQL database integration via JDBC

## Status Legend
| Status | Days Left | Color |
|--------|-----------|-------|
| 🟢 SAFE | > 30 days | Green |
| 🟡 WARNING | 8–30 days | Yellow |
| 🟠 CRITICAL | 1–7 days | Orange |
| 🔴 EXPIRED | Expired | Red |

## Tech Stack
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=flat)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)

## Setup Instructions

### Requirements
- Java JDK 11+
- MySQL 5.7+
- `mysql-connector-java` JAR (download from MySQL website)

### Steps
1. Import `database.sql` into MySQL
2. Download `mysql-connector-java-x.x.x.jar`
3. Compile:
   ```bash
   javac -cp .;mysql-connector-java-x.x.x.jar ProductExpiryTracker.java
   ```
4. Run:
   ```bash
   java -cp .;mysql-connector-java-x.x.x.jar ProductExpiryTracker
   ```
   *(Use `:` instead of `;` on Linux/Mac)*

### Database Config
Edit `connectDB()` in `ProductExpiryTracker.java`:
```java
conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expiry_db", "root", "your_password");
```

## Project Structure
```
product-expiry-tracker/
├── ProductExpiryTracker.java   # Main application
├── database.sql                # DB schema & sample data
└── README.md
```

## Developed By
**Shahanas P A** — B.Tech CSE, College of Engineering, Thalassery
