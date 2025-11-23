
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

-- Insertar Marcas (Brands)
INSERT INTO brands (name, description, is_active, created_at, updated_at) VALUES 
('Apple', 'Apple Inc.', TRUE, NOW(), NOW()),
('Samsung', 'Samsung Electronics', TRUE, NOW(), NOW()),
('Dell', 'Dell Technologies', TRUE, NOW(), NOW()),
('HP', 'Hewlett-Packard', TRUE, NOW(), NOW()),
('Lenovo', 'Lenovo Group', TRUE, NOW(), NOW()),
('Asus', 'ASUSTek Computer', TRUE, NOW(), NOW());

-- Insertar Categorías (Categories)
INSERT INTO categories (name, description, is_active, created_at, updated_at) VALUES 
('Laptops', 'Computadoras portátiles', TRUE, NOW(), NOW()),
('Smartphones', 'Teléfonos inteligentes', TRUE, NOW(), NOW()),
('Desktops', 'Computadoras de escritorio', TRUE, NOW(), NOW()),
('Tablets', 'Tabletas', TRUE, NOW(), NOW()),
('Monitors', 'Monitores y pantallas', TRUE, NOW(), NOW());

-- Insertar Unidades de Medida (Metric Units)
INSERT INTO metric_units (name, symbol, description, is_active, created_at, updated_at) VALUES 
('Unidad', 'u', 'Unidad individual', TRUE, NOW(), NOW());

-- Insertar Modelos (Models)
-- Asumiendo IDs: Apple=1, Samsung=2, Dell=3, HP=4, Lenovo=5, Asus=6
INSERT INTO models (name, description, brand_id, is_active, created_at, updated_at) VALUES 
('MacBook Pro 16', 'Laptop de alto rendimiento', 1, TRUE, NOW(), NOW()),
('MacBook Air 13', 'Laptop ultraligera', 1, TRUE, NOW(), NOW()),
('iPhone 15 Pro', 'Smartphone insignia', 1, TRUE, NOW(), NOW()),
('Galaxy S24 Ultra', 'Smartphone Android premium', 2, TRUE, NOW(), NOW()),
('Galaxy Book4', 'Laptop Samsung', 2, TRUE, NOW(), NOW()),
('XPS 15', 'Laptop Dell premium', 3, TRUE, NOW(), NOW()),
('Spectre x360', 'Laptop HP convertible', 4, TRUE, NOW(), NOW()),
('ThinkPad X1 Carbon', 'Laptop empresarial', 5, TRUE, NOW(), NOW()),
('ROG Strix', 'Laptop gaming', 6, TRUE, NOW(), NOW());

-- Insertar Productos (Products)
-- Asumiendo IDs: 
-- Categories: Laptops=1, Smartphones=2, Desktops=3, Tablets=4
-- MetricUnits: Unidad=1
-- Models: MacBook Pro 16=1, MacBook Air 13=2, iPhone 15 Pro=3, Galaxy S24 Ultra=4, Galaxy Book4=5, XPS 15=6, Spectre x360=7, ThinkPad X1 Carbon=8, ROG Strix=9
INSERT INTO products (name, description, category_id, metric_unit_id, weight, price, stock, sku, brand_id, model_id, barcode, product_condition, is_active, created_at, updated_at) VALUES 
('MacBook Pro 16 M3 Max', 'Apple MacBook Pro 16 pulgadas con chip M3 Max, 32GB RAM, 1TB SSD', 1, 1, 2.1, 3499.99, 50, 'MBP16-M3MAX', 1, 1, '194253000001', 'new', TRUE, NOW(), NOW()),
('MacBook Air 13 M2', 'Apple MacBook Air 13 pulgadas con chip M2, 8GB RAM, 256GB SSD', 1, 1, 1.24, 1099.99, 100, 'MBA13-M2', 1, 2, '194253000002', 'new', TRUE, NOW(), NOW()),
('iPhone 15 Pro Max 256GB', 'Apple iPhone 15 Pro Max Titanio Natural', 2, 1, 0.22, 1199.99, 200, 'IPH15PM-256', 1, 3, '194253000003', 'new', TRUE, NOW(), NOW()),
('Samsung Galaxy S24 Ultra 512GB', 'Samsung Galaxy S24 Ultra Titanium Gray', 2, 1, 0.23, 1299.99, 150, 'SGS24U-512', 2, 4, '880609000001', 'new', TRUE, NOW(), NOW()),
('Dell XPS 15 9530', 'Dell XPS 15 con Intel Core i9, RTX 4060, 32GB RAM, 1TB SSD', 1, 1, 1.86, 2499.00, 30, 'DXPS15-9530', 3, 6, '884116000001', 'new', TRUE, NOW(), NOW()),
('HP Spectre x360 14', 'HP Spectre x360 2-in-1 Laptop OLED', 1, 1, 1.45, 1599.99, 40, 'HPSP14-OLED', 4, 7, '196068000001', 'new', TRUE, NOW(), NOW()),
('Lenovo ThinkPad X1 Carbon Gen 11', 'Lenovo ThinkPad X1 Carbon Intel Core i7, 16GB RAM', 1, 1, 1.12, 1899.00, 60, 'LTPX1-G11', 5, 8, '195890000001', 'new', TRUE, NOW(), NOW()),
('Asus ROG Strix G16', 'Asus ROG Strix G16 Gaming Laptop, RTX 4070, Intel i9', 1, 1, 2.50, 1999.99, 25, 'ASROG-G16', 6, 9, '471108000001', 'new', TRUE, NOW(), NOW());

-- Actualizar Specs (JSON)
UPDATE products SET specs = '{"processor": "Apple M3 Max", "ram": "32GB Unified Memory", "storage": "1TB SSD", "display": "16.2-inch Liquid Retina XDR", "os": "macOS Sonoma"}' WHERE sku = 'MBP16-M3MAX';
UPDATE products SET specs = '{"processor": "Apple M2", "ram": "8GB Unified Memory", "storage": "256GB SSD", "display": "13.6-inch Liquid Retina", "os": "macOS Sonoma"}' WHERE sku = 'MBA13-M2';
UPDATE products SET specs = '{"processor": "A17 Pro chip", "ram": "8GB", "storage": "256GB", "display": "6.7-inch Super Retina XDR", "camera": "48MP Main | Ultra Wide | Telephoto", "os": "iOS 17"}' WHERE sku = 'IPH15PM-256';
UPDATE products SET specs = '{"processor": "Snapdragon 8 Gen 3 for Galaxy", "ram": "12GB", "storage": "512GB", "display": "6.8-inch Dynamic AMOLED 2X", "camera": "200MP Main", "os": "Android 14"}' WHERE sku = 'SGS24U-512';
UPDATE products SET specs = '{"processor": "Intel Core i9-13900H", "ram": "32GB DDR5", "storage": "1TB PCIe SSD", "graphics": "NVIDIA GeForce RTX 4060 8GB", "display": "15.6-inch OLED 3.5K", "os": "Windows 11 Pro"}' WHERE sku = 'DXPS15-9530';
UPDATE products SET specs = '{"processor": "Intel Core Ultra 7 155H", "ram": "16GB LPDDR5x", "storage": "1TB PCIe Gen4 SSD", "display": "14-inch 2.8K OLED Touch", "os": "Windows 11 Home"}' WHERE sku = 'HPSP14-OLED';
UPDATE products SET specs = '{"processor": "Intel Core i7-1355U", "ram": "16GB LPDDR5x", "storage": "512GB SSD", "display": "14-inch WUXGA IPS", "os": "Windows 11 Pro"}' WHERE sku = 'LTPX1-G11';
UPDATE products SET specs = '{"processor": "Intel Core i9-13980HX", "ram": "16GB DDR5", "storage": "1TB PCIe SSD", "graphics": "NVIDIA GeForce RTX 4070", "display": "16-inch QHD+ 240Hz", "os": "Windows 11 Home"}' WHERE sku = 'ASROG-G16';


