# API Endpoints - Brands y Models

## Base URL
```
http://localhost:8080
```

---

## 1. BRANDS (Marcas)

### 1.1 Crear una marca
```bash
curl -X POST http://localhost:8080/api/brands \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "HP",
    "description": "Hewlett-Packard Inc.",
    "logoUrl": "/assets/logos/hp.png"
  }'
```

### 1.2 Listar todas las marcas
```bash
curl -X GET http://localhost:8080/api/brands \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 1.3 Obtener una marca por ID
```bash
curl -X GET http://localhost:8080/api/brands/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 1.4 Actualizar una marca
```bash
curl -X PUT http://localhost:8080/api/brands/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "HP Inc.",
    "description": "Hewlett-Packard Corporation",
    "logoUrl": "/assets/logos/hp-new.png"
  }'
```

### 1.5 Eliminar una marca (soft delete)
```bash
curl -X DELETE http://localhost:8080/api/brands/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 2. MODELS (Modelos)

### 2.1 Crear un modelo
```bash
curl -X POST http://localhost:8080/api/models \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "EliteBook 840 G10",
    "description": "Professional business laptop",
    "brandId": 1
  }'
```

### 2.2 Listar todos los modelos
```bash
curl -X GET http://localhost:8080/api/models \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.3 Listar modelos por marca
```bash
curl -X GET "http://localhost:8080/api/models?brandId=1" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.4 Obtener un modelo por ID
```bash
curl -X GET http://localhost:8080/api/models/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.5 Actualizar un modelo
```bash
curl -X PUT http://localhost:8080/api/models/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "EliteBook 840 G11",
    "description": "Latest generation professional laptop",
    "brandId": 1
  }'
```

### 2.6 Eliminar un modelo (soft delete)
```bash
curl -X DELETE http://localhost:8080/api/models/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 3. PRODUCTS (Actualizado)

### 3.1 Crear un producto con brandId y modelId
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "name": "HP EliteBook 840 G10",
    "description": "Professional business laptop",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brandId": 1,
    "modelId": 1,
    "categoryId": 1,
    "metricUnitId": 1,
    "productCondition": "new",
    "price": 1200.00,
    "pricePerMonth": 120,
    "stock": 15,
    "weight": 1.3,
    "barcode": "HP-840-G10-001",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD"
    }
  }'
```

### Respuesta esperada:
```json
{
  "status": "success",
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "HP EliteBook 840 G10",
    "description": "Professional business laptop",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brandId": 1,
    "brandName": "HP",
    "modelId": 1,
    "modelName": "EliteBook 840 G10",
    "categoryId": 1,
    "categoryName": "Laptops",
    "metricUnitId": 1,
    "metricUnitName": "Unit",
    "productCondition": "new",
    "price": 1200.00,
    "pricePerMonth": 120,
    "stock": 15,
    "weight": 1.3,
    "barcode": "HP-840-G10-001",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD"
    },
    "isActive": true,
    "createdAt": "2025-11-05T10:30:00",
    "updatedAt": "2025-11-05T10:30:00"
  }
}
```

---

## 4. FLUJO COMPLETO DE EJEMPLO

### Paso 1: Crear marcas
```bash
# HP
curl -X POST http://localhost:8080/api/brands \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name": "HP", "description": "Hewlett-Packard Inc."}'

# Dell
curl -X POST http://localhost:8080/api/brands \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name": "Dell", "description": "Dell Technologies Inc."}'

# Apple
curl -X POST http://localhost:8080/api/brands \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name": "Apple", "description": "Apple Inc."}'
```

### Paso 2: Crear modelos
```bash
# HP EliteBook 840 G10
curl -X POST http://localhost:8080/api/models \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "EliteBook 840 G10",
    "description": "14-inch business laptop",
    "brandId": 1
  }'

# Dell XPS 13
curl -X POST http://localhost:8080/api/models \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "XPS 13",
    "description": "13-inch premium ultrabook",
    "brandId": 2
  }'

# MacBook Pro 14
curl -X POST http://localhost:8080/api/models \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "MacBook Pro 14",
    "description": "14-inch professional laptop",
    "brandId": 3
  }'
```

### Paso 3: Crear productos
```bash
# Producto con brandId y modelId
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "HP EliteBook 840 G10",
    "description": "Professional laptop",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brandId": 1,
    "modelId": 1,
    "categoryId": 1,
    "metricUnitId": 1,
    "productCondition": "new",
    "price": 1200.00,
    "pricePerMonth": 120,
    "stock": 15,
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD"
    }
  }'
```

---

## 5. CAMBIOS IMPORTANTES

### Campos eliminados de Product:
- ❌ `imageUrl` - Ya no se usa, solo `imageBase64`
- ❌ `brand` (String) - Ahora es `brandId` (Long) + `brandName` en respuesta
- ❌ `model` (String) - Ahora es `modelId` (Long) + `modelName` en respuesta

### Nuevos campos en ProductRequest:
- ✅ `brandId` (Long) - ID de la marca
- ✅ `modelId` (Long) - ID del modelo

### Nuevos campos en ProductResponse:
- ✅ `brandId` (Long) - ID de la marca
- ✅ `brandName` (String) - Nombre de la marca
- ✅ `modelId` (Long) - ID del modelo
- ✅ `modelName` (String) - Nombre del modelo

---

## 6. NUEVOS ENDPOINTS AGREGADOS

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/brands` | Crear marca |
| GET | `/api/brands` | Listar marcas |
| GET | `/api/brands/{id}` | Obtener marca |
| PUT | `/api/brands/{id}` | Actualizar marca |
| DELETE | `/api/brands/{id}` | Eliminar marca |
| POST | `/api/models` | Crear modelo |
| GET | `/api/models` | Listar modelos |
| GET | `/api/models?brandId={id}` | Listar modelos por marca |
| GET | `/api/models/{id}` | Obtener modelo |
| PUT | `/api/models/{id}` | Actualizar modelo |
| DELETE | `/api/models/{id}` | Eliminar modelo |

---

Fecha de actualización: 2025-11-05
Versión: 2.0

