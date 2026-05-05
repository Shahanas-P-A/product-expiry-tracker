# 🏨 Hotel Reservation System

A web-based hotel booking system built with **HTML, CSS, PHP & MySQL**.

## Features
- Room browsing with availability status
- Customer registration & login
- Room booking with date validation & overlap check
- Booking history & cancellation
- Admin dashboard with stats & booking management

## Tech Stack
![HTML](https://img.shields.io/badge/HTML-E34F26?style=flat&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS-1572B6?style=flat&logo=css3&logoColor=white)
![PHP](https://img.shields.io/badge/PHP-777BB4?style=flat&logo=php&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)

## Setup Instructions

### Requirements
- XAMPP / WAMP / LAMP server
- PHP 7.4+
- MySQL 5.7+

### Steps
1. Clone the repo into your `htdocs` or `www` directory
2. Import `database.sql` into MySQL via phpMyAdmin
3. Open `db.php` and update credentials if needed
4. Navigate to `http://localhost/hotel-reservation-system`

### Admin Access
- URL: `http://localhost/hotel-reservation-system/admin/login.php`
- Username: `admin`
- Password: `admin123`

## Project Structure
```
hotel-reservation-system/
├── index.php          # Home page
├── rooms.php          # Room listing
├── booking.php        # Booking form
├── mybookings.php     # User booking history
├── login.php          # User login
├── register.php       # User registration
├── logout.php
├── db.php             # Database connection
├── style.css          # Stylesheet
├── database.sql       # DB schema & sample data
└── admin/
    ├── dashboard.php  # Admin panel
    ├── login.php      # Admin login
    └── logout.php
```

## Developed By
**Shahanas P A** — B.Tech CSE, College of Engineering, Thalassery
