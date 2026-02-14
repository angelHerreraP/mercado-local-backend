CREATE DATABASE IF NOT EXISTS finalProject;

USE finalProject;

-- =====================
-- TABLA ROLES
-- =====================


CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;


-- =====================
-- TABLA USERS
-- =====================
CREATE TABLE users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
)ENGINE=InnoDB;

-- =====================
-- TABLA PRODUCT_TYPE
-- =====================
CREATE TABLE product_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
)ENGINE=InnoDB;


-- =====================
-- TABLA PRODUCTS
-- =====================
CREATE TABLE products(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    id_type INT NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_type FOREIGN KEY (id_type) REFERENCES product_type(id)
)ENGINE=InnoDB;


-- =====================
-- TABLA SALES
-- =====================
CREATE TABLE sales(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sales_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_sales_product FOREIGN KEY (product_id) REFERENCES products(id)
)ENGINE=InnoDB;