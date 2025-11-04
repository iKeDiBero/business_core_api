-- Script para agregar los nuevos campos a la tabla users
-- Ejecutar este script si ya tienes la tabla users creada

USE db_business_core_api;

-- Agregar columna para teléfono
ALTER TABLE users
ADD COLUMN telefono VARCHAR(20) NULL AFTER password;

-- Agregar columna para dirección
ALTER TABLE users
ADD COLUMN direccion VARCHAR(255) NULL AFTER telefono;

-- Agregar columna para ciudad
ALTER TABLE users
ADD COLUMN ciudad VARCHAR(100) NULL AFTER direccion;

-- Agregar columna para país
ALTER TABLE users
ADD COLUMN pais VARCHAR(100) NULL AFTER ciudad;

-- Agregar columna para foto de perfil (base64)
ALTER TABLE users
ADD COLUMN profile_photo LONGTEXT NULL AFTER pais;

-- Verificar los cambios
DESCRIBE users;

