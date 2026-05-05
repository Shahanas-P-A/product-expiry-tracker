-- Product Expiry Tracker Database Schema
CREATE DATABASE IF NOT EXISTS expiry_db;
USE expiry_db;

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    quantity INT DEFAULT 1,
    expiry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data
INSERT INTO products (name, category, quantity, expiry_date) VALUES
('Milk', 'Dairy', 5, DATE_ADD(CURDATE(), INTERVAL 3 DAY)),
('Yogurt', 'Dairy', 10, DATE_ADD(CURDATE(), INTERVAL 7 DAY)),
('Bread', 'Bakery', 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY)),
('Cheese', 'Dairy', 8, DATE_ADD(CURDATE(), INTERVAL 20 DAY)),
('Orange Juice', 'Beverages', 12, DATE_ADD(CURDATE(), INTERVAL 45 DAY)),
('Chicken', 'Meat', 6, DATE_ADD(CURDATE(), INTERVAL -2 DAY)),
('Pasta Sauce', 'Canned', 15, DATE_ADD(CURDATE(), INTERVAL 180 DAY)),
('Eggs', 'Dairy', 24, DATE_ADD(CURDATE(), INTERVAL 14 DAY)),
('Butter', 'Dairy', 4, DATE_ADD(CURDATE(), INTERVAL 60 DAY)),
('Canned Corn', 'Canned', 20, DATE_ADD(CURDATE(), INTERVAL 365 DAY));
