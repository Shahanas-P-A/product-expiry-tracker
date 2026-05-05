-- Hotel Reservation System Database Schema
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    type ENUM('Single', 'Double', 'Suite', 'Deluxe') NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL DEFAULT 1,
    description TEXT,
    status ENUM('Available', 'Booked', 'Maintenance') DEFAULT 'Available',
    image VARCHAR(255)
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Confirmed', 'Cancelled', 'Completed') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Sample data
INSERT INTO rooms (room_number, type, price_per_night, capacity, description, status) VALUES
('101', 'Single', 2500.00, 1, 'Cozy single room with city view', 'Available'),
('102', 'Double', 4000.00, 2, 'Spacious double room with balcony', 'Available'),
('201', 'Suite', 8000.00, 3, 'Luxury suite with living area', 'Available'),
('202', 'Deluxe', 6000.00, 2, 'Deluxe room with pool view', 'Available'),
('301', 'Single', 2500.00, 1, 'Standard single room', 'Available'),
('302', 'Double', 4500.00, 2, 'Premium double room', 'Available');

INSERT INTO admins (username, password) VALUES
('admin', MD5('admin123'));
