-- Insertar roles por defecto si no existen
INSERT INTO roles (name, description)
SELECT 'ROLE_USER', 'Usuario estándar'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name, description)
SELECT 'ROLE_ADMIN', 'Administrador del sistema'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name, description)
SELECT 'ROLE_MODERATOR', 'Moderador del sistema'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_MODERATOR');

