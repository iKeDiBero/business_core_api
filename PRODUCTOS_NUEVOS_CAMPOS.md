# Ejemplos de cURL para Productos con Nuevos Campos

## Crear un Producto (con todos los campos nuevos)

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "name": "HP EliteBook 840 G10",
    "description": "Laptop profesional de alto rendimiento",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brand": "HP",
    "model": "EliteBook 840 G10",
    "categoryId": 1,
    "metricUnitId": 1,
    "condition": "new",
    "price": 1200.00,
    "pricePerMonth": 120,
    "stock": 15,
    "weight": 1.3,
    "imageUrl": "/assets/devices/hp-840.png",
    "barcode": "HP-840-G10-001",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD"
    }
  }'
```

## Respuesta Esperada

```json
{
  "status": "success",
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "name": "HP EliteBook 840 G10",
    "description": "Laptop profesional de alto rendimiento",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brand": "HP",
    "model": "EliteBook 840 G10",
    "categoryId": 1,
    "categoryName": "Laptops",
    "metricUnitId": 1,
    "metricUnitName": "Unit",
    "condition": "new",
    "price": 1200.00,
    "pricePerMonth": 120,
    "stock": 15,
    "weight": 1.3,
    "imageUrl": "/assets/devices/hp-840.png",
    "barcode": "HP-840-G10-001",
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD"
    },
    "isActive": true,
    "createdAt": "2025-11-03T14:30:00",
    "updatedAt": "2025-11-03T14:30:00"
  }
}
```

## Crear Varios Productos de Ejemplo

### Ejemplo 1: Dell XPS 13

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "name": "Dell XPS 13",
    "description": "Ultrabook premium con pantalla 4K",
    "deviceId": "dev_002",
    "sku": "DELL-XPS-13",
    "brand": "Dell",
    "model": "XPS 13",
    "categoryId": 1,
    "metricUnitId": 1,
    "condition": "refurbished",
    "price": 999.00,
    "pricePerMonth": 99.90,
    "stock": 8,
    "weight": 1.0,
    "imageUrl": "/assets/devices/dell-xps.png",
    "barcode": "DELL-XPS-13-001",
    "specs": {
      "cpu": "i5-1335U",
      "ram": "8GB",
      "storage": "256GB SSD"
    }
  }'
```

### Ejemplo 2: MacBook Pro 14

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "name": "MacBook Pro 14",
    "description": "Laptop profesional con chip M3",
    "deviceId": "dev_003",
    "sku": "APPLE-MBP-14",
    "brand": "Apple",
    "model": "MacBook Pro 14",
    "categoryId": 1,
    "metricUnitId": 1,
    "condition": "new",
    "price": 1999.00,
    "pricePerMonth": 199.90,
    "stock": 5,
    "weight": 1.6,
    "imageUrl": "/assets/devices/macbook-pro.png",
    "barcode": "APPLE-MBP-14-001",
    "specs": {
      "cpu": "M3 Max",
      "ram": "16GB",
      "storage": "512GB SSD"
    }
  }'
```

### Ejemplo 3: ThinkPad X1 Carbon (usado)

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "name": "ThinkPad X1 Carbon",
    "description": "Laptop corporativa robusta",
    "deviceId": "dev_004",
    "sku": "LENOVO-X1C",
    "brand": "Lenovo",
    "model": "ThinkPad X1 Carbon",
    "categoryId": 1,
    "metricUnitId": 1,
    "condition": "used",
    "price": 599.00,
    "pricePerMonth": 59.90,
    "stock": 12,
    "weight": 1.13,
    "imageUrl": "/assets/devices/thinkpad-x1.png",
    "barcode": "LENOVO-X1C-001",
    "specs": {
      "cpu": "i7-10510U",
      "ram": "16GB",
      "storage": "512GB SSD"
    }
  }'
```

## Actualizar un Producto

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "name": "HP EliteBook 840 G10 - Actualizado",
    "description": "Laptop profesional actualizada",
    "deviceId": "dev_001",
    "sku": "HP-840-G10",
    "brand": "HP",
    "model": "EliteBook 840 G10",
    "categoryId": 1,
    "metricUnitId": 1,
    "condition": "new",
    "price": 1150.00,
    "pricePerMonth": 115,
    "stock": 10,
    "weight": 1.3,
    "imageUrl": "/assets/devices/hp-840.png",
    "barcode": "HP-840-G10-001",
    "specs": {
      "cpu": "i7-1355U",
      "ram": "16GB",
      "storage": "512GB SSD",
      "gpu": "Intel Iris Xe"
    }
  }'
```

## Listar Todos los Productos

```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## Obtener un Producto Específico

```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## Eliminar un Producto

```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## Nuevos Campos Agregados

| Campo | Tipo | Descripción | Obligatorio |
|-------|------|-------------|-------------|
| `deviceId` | String | ID único del dispositivo | No |
| `sku` | String | Código SKU (único) | No |
| `brand` | String | Marca del producto | No |
| `model` | String | Modelo del producto | No |
| `condition` | String | Condición: new, refurbished, used | No |
| `imageUrl` | String | URL de la imagen | No |
| `specs` | String (JSON) | Especificaciones técnicas en JSON | No |
| `pricePerMonth` | Double | Precio de alquiler mensual | No |

## Notas Importantes

1. **deviceId y SKU**: Ambos campos son únicos, no puedes tener dos productos con el mismo valor
2. **specs**: Es un objeto JSON dinámico. Puedes agregar cualquier propiedad que necesites. No necesita escapes de caracteres.
3. **condition**: Valores permitidos: `new`, `refurbished`, `used`
4. **pricePerMonth**: Se utiliza si el producto es alquilable
5. **imageBase64**: Continúa soportando imagen en base64 para guardar en la BD
6. **imageUrl**: URL externa a la imagen (CDN, servidor, etc.)

## Notas sobre specs

El campo `specs` ahora es un objeto JSON flexible. Puedes enviar cualquier propiedad:

```json
{
  "specs": {
    "cpu": "i7-1355U",
    "ram": "16GB",
    "storage": "512GB SSD",
    "gpu": "Intel Iris Xe",
    "display": "14 inch 4K",
    "battery": "80Wh",
    "weight": "1.3kg"
  }
}
```

Ya no necesitas escapes de caracteres ni formatear manualmente como string JSON.

## Validaciones

- El campo `deviceId` debe ser único (no puedes crear dos productos con el mismo deviceId)
- El campo `sku` debe ser único (no puedes crear dos productos con el mismo sku)
- El campo `specs` es flexible: puedes enviar un objeto JSON con cualquier propiedad
- Todos los campos son opcionales excepto `name`, `categoryId` y `metricUnitId`

