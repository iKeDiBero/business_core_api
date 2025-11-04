# API CRUD Endpoints - Ejemplos de uso con cURL

## Base URL
```
http://localhost:8080
```

---

## 1. CATEGORÍAS (Categories)

### 1.1 Crear una categoría
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Fruits",
    "description": "Fresh fruits and vegetables"
  }'
```

**Respuesta esperada:**
```json
{
  "status": "success",
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "Fruits",
    "description": "Fresh fruits and vegetables",
    "isActive": true,
    "createdAt": "2025-11-03T10:30:00",
    "updatedAt": "2025-11-03T10:30:00"
  }
}
```

### 1.2 Listar todas las categorías activas
```bash
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 1.3 Obtener una categoría por ID
```bash
curl -X GET http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 1.4 Actualizar una categoría
```bash
curl -X PUT http://localhost:8080/api/categories/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Fresh Fruits",
    "description": "Organic fresh fruits and vegetables"
  }'
```

### 1.5 Eliminar (desactivar) una categoría
```bash
curl -X DELETE http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 2. UNIDADES MÉTRICAS (Metric Units)

### 2.1 Crear una unidad métrica
```bash
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Kilogram",
    "symbol": "kg",
    "description": "Unit of mass in the metric system"
  }'
```

**Respuesta esperada:**
```json
{
  "status": "success",
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "Kilogram",
    "symbol": "kg",
    "description": "Unit of mass in the metric system",
    "isActive": true,
    "createdAt": "2025-11-03T10:35:00",
    "updatedAt": "2025-11-03T10:35:00"
  }
}
```

### 2.2 Crear más unidades métricas
```bash
# Litro
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Liter",
    "symbol": "L",
    "description": "Unit of volume"
  }'

# Unidad
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Unit",
    "symbol": "un",
    "description": "Individual unit"
  }'

# Gramo
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Gram",
    "symbol": "g",
    "description": "Unit of mass"
  }'
```

### 2.3 Listar todas las unidades métricas activas
```bash
curl -X GET http://localhost:8080/api/metric-units \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.4 Obtener una unidad métrica por ID
```bash
curl -X GET http://localhost:8080/api/metric-units/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.5 Actualizar una unidad métrica
```bash
curl -X PUT http://localhost:8080/api/metric-units/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Kilogram",
    "symbol": "kg",
    "description": "Standard unit of mass in SI system"
  }'
```

### 2.6 Eliminar (desactivar) una unidad métrica
```bash
curl -X DELETE http://localhost:8080/api/metric-units/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 3. PRODUCTOS (Products)

### 3.1 Crear un producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Red Apple",
    "description": "Fresh organic red apples",
    "categoryId": 1,
    "metricUnitId": 1,
    "weight": 0.2,
    "price": 1.5,
    "stock": 100,
    "barcode": "1234567890123",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA..."
  }'
```

**Respuesta esperada:**
```json
{
  "status": "success",
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "Red Apple",
    "description": "Fresh organic red apples",
    "categoryId": 1,
    "categoryName": "Fruits",
    "metricUnitId": 1,
    "metricUnitName": "Kilogram",
    "weight": 0.2,
    "price": 1.5,
    "stock": 100,
    "barcode": "1234567890123",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "isActive": true,
    "createdAt": "2025-11-03T10:40:00",
    "updatedAt": "2025-11-03T10:40:00"
  }
}
```

### 3.2 Crear más productos de ejemplo
```bash
# Banana
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Banana",
    "description": "Fresh yellow bananas",
    "categoryId": 1,
    "metricUnitId": 1,
    "weight": 0.15,
    "price": 0.8,
    "stock": 200,
    "barcode": "1234567890124",
    "imageBase64": ""
  }'

# Orange Juice
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Orange Juice",
    "description": "100% natural orange juice",
    "categoryId": 2,
    "metricUnitId": 2,
    "weight": 1.0,
    "price": 3.5,
    "stock": 50,
    "barcode": "1234567890125",
    "imageBase64": ""
  }'
```

### 3.3 Listar todos los productos activos
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 3.4 Obtener un producto por ID
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 3.5 Actualizar un producto
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Green Apple",
    "description": "Fresh organic green apples",
    "categoryId": 1,
    "metricUnitId": 1,
    "weight": 0.22,
    "price": 1.7,
    "stock": 80,
    "barcode": "1234567890123",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA..."
  }'
```

### 3.6 Eliminar (desactivar) un producto
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 4. FLUJO COMPLETO DE EJEMPLO

### Paso 1: Crear categorías
```bash
# Categoría Frutas
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Fruits", "description": "Fresh fruits"}'

# Categoría Bebidas
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Beverages", "description": "Drinks and juices"}'

# Categoría Lácteos
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Dairy", "description": "Dairy products"}'
```

### Paso 2: Crear unidades métricas
```bash
# Kilogramo
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Kilogram", "symbol": "kg", "description": "Unit of mass"}'

# Litro
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Liter", "symbol": "L", "description": "Unit of volume"}'

# Unidad
curl -X POST http://localhost:8080/api/metric-units \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"name": "Unit", "symbol": "un", "description": "Individual unit"}'
```

### Paso 3: Crear productos
```bash
# Producto 1: Manzana (categoryId=1, metricUnitId=1)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Red Apple",
    "description": "Fresh red apples",
    "categoryId": 1,
    "metricUnitId": 1,
    "weight": 0.2,
    "price": 1.5,
    "stock": 100,
    "barcode": "APP001"
  }'

# Producto 2: Leche (categoryId=3, metricUnitId=2)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "Fresh Milk",
    "description": "Whole fresh milk",
    "categoryId": 3,
    "metricUnitId": 2,
    "weight": 1.0,
    "price": 2.5,
    "stock": 50,
    "barcode": "MILK001"
  }'
```

---

## 5. NOTAS IMPORTANTES

### 5.1 Autenticación
Todos los endpoints (excepto `/api/auth/login` y `/api/auth/register`) requieren autenticación.
Primero debes hacer login para obtener el token:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario123",
    "password": "password123"
  }'
```

Copia el token de la respuesta y úsalo en el header `Authorization: Bearer TOKEN`.

### 5.2 Campos Obligatorios

**Category:**
- name (requerido)
- description (opcional)

**MetricUnit:**
- name (requerido)
- symbol (opcional)
- description (opcional)

**Product:**
- name (requerido)
- categoryId (requerido - debe existir)
- metricUnitId (requerido - debe existir)
- price (opcional)
- stock (opcional)
- weight (opcional)
- description (opcional)
- barcode (opcional)
- imageBase64 (opcional)

### 5.3 Formato de Imagen Base64
Para la imagen del producto, puedes usar:
- String base64 completo: `"data:image/png;base64,iVBORw0KGgo..."`
- Solo el string base64: `"iVBORw0KGgo..."`
- String vacío si no tienes imagen: `""`

### 5.4 Eliminación Lógica
Todos los endpoints de DELETE hacen eliminación lógica (soft delete):
- El registro no se elimina físicamente de la base de datos
- Se marca como inactivo (isActive = false)
- Los endpoints de listado solo muestran registros activos

### 5.5 Códigos de Respuesta HTTP
- `200 OK` - Operación exitosa
- `401 Unauthorized` - Token inválido o ausente
- `404 Not Found` - Recurso no encontrado o inactivo
- `500 Internal Server Error` - Error del servidor

---

## 6. TESTING CON POSTMAN

Si prefieres usar Postman en lugar de cURL:

1. Crea una nueva colección llamada "Business Core API"
2. Agrega una variable de entorno `baseUrl` con valor `http://localhost:8080`
3. Agrega una variable de entorno `token` para almacenar el JWT
4. Crea requests para cada endpoint usando `{{baseUrl}}` y `{{token}}`

Ejemplo de header en Postman:
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

---

## 7. SCRIPTS DE BASH PARA TESTING RÁPIDO

### script_test_categories.sh
```bash
#!/bin/bash
TOKEN="YOUR_TOKEN_HERE"
BASE_URL="http://localhost:8080"

# Crear categoría
echo "Creating category..."
curl -X POST $BASE_URL/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Test Category", "description": "Test description"}'

# Listar categorías
echo "\nListing categories..."
curl -X GET $BASE_URL/api/categories \
  -H "Authorization: Bearer $TOKEN"
```

### script_test_products.sh
```bash
#!/bin/bash
TOKEN="YOUR_TOKEN_HERE"
BASE_URL="http://localhost:8080"

# Crear producto
echo "Creating product..."
curl -X POST $BASE_URL/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Product",
    "description": "Test description",
    "categoryId": 1,
    "metricUnitId": 1,
    "price": 10.5,
    "stock": 100
  }'

# Listar productos
echo "\nListing products..."
curl -X GET $BASE_URL/api/products \
  -H "Authorization: Bearer $TOKEN"
```

---

## 8. ENDPOINTS RESUMIDOS

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/categories` | Crear categoría |
| GET | `/api/categories` | Listar categorías |
| GET | `/api/categories/{id}` | Obtener categoría |
| PUT | `/api/categories/{id}` | Actualizar categoría |
| DELETE | `/api/categories/{id}` | Eliminar categoría |
| POST | `/api/metric-units` | Crear unidad métrica |
| GET | `/api/metric-units` | Listar unidades métricas |
| GET | `/api/metric-units/{id}` | Obtener unidad métrica |
| PUT | `/api/metric-units/{id}` | Actualizar unidad métrica |
| DELETE | `/api/metric-units/{id}` | Eliminar unidad métrica |
| POST | `/api/products` | Crear producto |
| GET | `/api/products` | Listar productos |
| GET | `/api/products/{id}` | Obtener producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |

---

Fecha de creación: 2025-11-03
Versión: 1.0

