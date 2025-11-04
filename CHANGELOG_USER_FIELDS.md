# Resumen de Cambios - Información Adicional del Usuario

## Archivos Actualizados

### 1. Entity: User.java
**Nuevos campos agregados:**
- `telefono` (String, max 20 caracteres)
- `direccion` (String, max 255 caracteres)
- `ciudad` (String, max 100 caracteres)
- `pais` (String, max 100 caracteres)
- `profilePhoto` (String, LONGTEXT - para guardar base64)

### 2. DTO: RegisterRequest.java
**Campos agregados para el registro:**
- `telefono`
- `direccion`
- `ciudad`
- `pais`
- `profilePhoto`

Todos estos campos son **opcionales** durante el registro.

### 3. DTO: AuthResponse.java
**Campos agregados en la respuesta:**
- `telefono`
- `direccion`
- `ciudad`
- `pais`
- `profilePhoto`

Estos campos se devuelven al hacer login, permitiendo al cliente obtener toda la información del usuario.

### 4. Security: UserDetailsImpl.java
**Campos agregados:**
- Incluye los 5 nuevos campos del usuario
- Constructor actualizado para recibir estos campos
- Método `build()` actualizado para mapear desde la entidad `User`
- Getters agregados para todos los campos nuevos

### 5. Service: AuthService.java
**Método `login()` actualizado:**
- Devuelve el `AuthResponse` con los nuevos campos del usuario

**Método `register()` actualizado:**
- Guarda los campos adicionales del `RegisterRequest` en la entidad `User`

### 6. Documentation: AUTH_README.md
**Actualizado con:**
- Ejemplos de request con los nuevos campos
- Ejemplos de response con los nuevos campos
- Nota indicando que los campos son opcionales
- Ejemplos de cURL actualizados

### 7. SQL Script: UPDATE_USER_SCHEMA.sql
**Creado nuevo archivo:**
- Script SQL para agregar las columnas a la tabla users existente
- Incluye todos los nuevos campos con tipos de datos apropiados

## Cómo Aplicar los Cambios

### Opción 1: Nueva Base de Datos
Si estás empezando desde cero, simplemente ejecuta la aplicación y Spring Boot creará automáticamente las tablas con todos los campos.

### Opción 2: Base de Datos Existente
Si ya tienes una base de datos con la tabla `users`, ejecuta el script SQL:

```bash
mysql -u root -p db_business_core_api < UPDATE_USER_SCHEMA.sql
```

O conéctate a MySQL y ejecuta:
```sql
source UPDATE_USER_SCHEMA.sql;
```

## Ejemplo de Uso Completo

### 1. Registrar un usuario con toda la información:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juanperez",
    "email": "juan@example.com",
    "password": "Password123!",
    "telefono": "+34123456789",
    "direccion": "Calle Mayor 25, 3B",
    "ciudad": "Madrid",
    "pais": "España",
    "profilePhoto": "data:image/png;base64,iVBORw0KGgo..."
  }'
```

### 2. Hacer login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juanperez",
    "password": "Password123!"
  }'
```

### 3. Respuesta esperada:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "juanperez",
  "email": "juan@example.com",
  "roles": ["ROLE_USER"],
  "telefono": "+34123456789",
  "direccion": "Calle Mayor 25, 3B",
  "ciudad": "Madrid",
  "pais": "España",
  "profilePhoto": "data:image/png;base64,iVBORw0KGgo..."
}
```

## Notas Importantes

1. **Foto de Perfil en Base64:**
   - Se recomienda que las imágenes no superen los 2-3 MB para evitar problemas de rendimiento
   - El formato debe ser: `data:image/png;base64,{base64_string}` o solo el string base64
   
2. **Campos Opcionales:**
   - Todos los nuevos campos son opcionales (pueden ser `null`)
   - Solo `username`, `email` y `password` son obligatorios para el registro

3. **Validaciones Pendientes:**
   - Considera agregar validaciones para el formato del teléfono
   - Considera agregar validaciones para la longitud del base64
   - Considera agregar un límite de tamaño para la foto de perfil

4. **Mejoras Futuras Recomendadas:**
   - Almacenar las fotos en un servicio de almacenamiento (S3, Azure Blob) en lugar de base64
   - Agregar un endpoint para actualizar el perfil del usuario
   - Agregar validaciones con anotaciones (@NotNull, @Size, @Email, etc.)

## Compatibilidad con Versión Anterior

Los usuarios registrados previamente (sin los nuevos campos) seguirán funcionando normalmente. 
Sus campos adicionales simplemente serán `null` hasta que los actualicen.

