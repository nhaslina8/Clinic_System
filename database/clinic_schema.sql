CREATE DATABASE IF NOT EXISTS clinic_system;
USE clinic_system;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'DOCTOR', 'PATIENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    date_of_birth DATE,
    phone VARCHAR(30),
    address VARCHAR(255),
    medical_record TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    specialization VARCHAR(120) NOT NULL,
    license_number VARCHAR(80) NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    billing_status ENUM('UNPAID', 'PAID', 'PARTIAL') DEFAULT 'UNPAID',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO users (full_name, email, password_hash, role)
VALUES
    ('Dr. Sarah Lim', 'sarah.lim@clinic.local', 'hashed_password_1', 'DOCTOR'),
    ('Amir Hakim', 'amir.hakim@clinic.local', 'hashed_password_2', 'PATIENT');

INSERT INTO doctors (user_id, specialization, license_number)
VALUES (1, 'General Medicine', 'DOC-001');

INSERT INTO patients (user_id, date_of_birth, phone, address, medical_record)
VALUES (2, '1998-07-14', '+601122334455', 'Shah Alam, Selangor', 'No known allergies.');

INSERT INTO appointments (patient_id, doctor_id, appointment_time, status, notes)
VALUES (1, 1, '2026-06-01 09:30:00', 'CONFIRMED', 'Follow-up consultation');

INSERT INTO bills (appointment_id, amount, billing_status)
VALUES (1, 120.00, 'UNPAID');
