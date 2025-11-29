-- Create database if not exists (optional, depending on setup)
-- CREATE DATABASE IF NOT EXISTS db_business_core_api;
-- USE db_business_core_api;

SET FOREIGN_KEY_CHECKS = 0;

-- Table: brands
DROP TABLE IF EXISTS brands;
CREATE TABLE brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    logo_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- Table: categories
DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- Table: metric_units
DROP TABLE IF EXISTS metric_units;
CREATE TABLE metric_units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10),
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- Table: roles
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Table: users
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_photo LONGTEXT,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

-- Table: user_roles
DROP TABLE IF EXISTS user_roles;
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- Table: models
DROP TABLE IF EXISTS models;
CREATE TABLE models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    brand_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_models_brand FOREIGN KEY (brand_id) REFERENCES brands (id)
);

-- Table: products
DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    category_id BIGINT,
    metric_unit_id BIGINT,
    weight DOUBLE,
    price DOUBLE,
    stock INT,
    sku VARCHAR(50) UNIQUE,
    brand_id BIGINT,
    model_id BIGINT,
    barcode VARCHAR(50),
    product_condition VARCHAR(50),
    image_base64 LONGTEXT,
    specs JSON,
    price_per_month DOUBLE,
    device_id VARCHAR(50) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_products_metric_unit FOREIGN KEY (metric_unit_id) REFERENCES metric_units (id),
    CONSTRAINT fk_products_brand FOREIGN KEY (brand_id) REFERENCES brands (id),
    CONSTRAINT fk_products_model FOREIGN KEY (model_id) REFERENCES models (id)
);

-- Table: carts
DROP TABLE IF EXISTS carts;
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Table: cart_items
DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    created_at DATETIME(6),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts (id),
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);

-- Table: db_connection_test
DROP TABLE IF EXISTS db_connection_test;
CREATE TABLE db_connection_test (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

-- Table: orders
DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total DOUBLE NOT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Table: order_items
DROP TABLE IF EXISTS order_items;
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    created_at DATETIME(6),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id)
);

-- Table: payment_logs
DROP TABLE IF EXISTS payment_logs;
CREATE TABLE IF NOT EXISTS payment_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    transaction_code VARCHAR(100),
    authorization_code VARCHAR(50),
    action_code VARCHAR(10),
    action_description VARCHAR(255),
    amount DOUBLE,
    card_brand VARCHAR(50),
    card_number VARCHAR(50),
    created_at DATETIME(6),
    CONSTRAINT fk_payment_logs_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

SET FOREIGN_KEY_CHECKS = 1;
