# Troubleshooting - Error 401 en endpoints protegidos

## Problema
Al intentar acceder a `http://localhost:8080/api/categories` con un token válido, se recibe un error 401 Unauthorized.

## Pasos para diagnosticar

### 1. Verificar que el servidor esté ejecutándose
```bash
# Verifica que la aplicación esté corriendo
curl http://localhost:8080/version
```

### 2. Obtener un token válido
```bash
# Primero, registra un usuario (si no existe)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123!"
  }'

# Luego, haz login para obtener el token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123!"
  }'
```

**Copia el token de la respuesta (campo "token")**

### 3. Verificar el formato del token
El token debe tener el siguiente formato en el header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**IMPORTANTE:** 
- Debe comenzar con "Bearer " (con espacio después)
- No debe tener comillas adicionales
- No debe tener espacios extras

### 4. Probar el endpoint con el token correcto

#### Formato CORRECTO:
```bash
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

#### Formatos INCORRECTOS (estos darán 401):
```bash
# Sin "Bearer"
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: TU_TOKEN_AQUI"

# Con comillas extras
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer \"TU_TOKEN_AQUI\""

# Sin el header Authorization
curl -X GET http://localhost:8080/api/categories
```

### 5. Verificar los logs de la aplicación

He agregado logs de depuración al sistema. Cuando ejecutes tu request, verás en la consola algo como:

```
Processing request to: /api/categories
JWT Token present: true
JWT Token valid: true
Username from token: testuser
UserDetails loaded: true
Authentication set in SecurityContext
```

Si ves algo diferente, eso te dará pistas del problema:

- **JWT Token present: false** → No se está enviando el token o el header está mal formado
- **JWT Token valid: false** → El token es inválido, ha expirado o la clave secreta no coincide
- **UserDetails loaded: false** → El usuario no existe en la base de datos

### 6. Errores comunes y soluciones

#### Error: "Token JWT expirado"
**Solución:** Haz login nuevamente para obtener un token nuevo.

#### Error: "Token JWT inválido"
**Solución:** Verifica que:
1. Estés copiando el token completo (sin espacios extra)
2. El token no tenga comillas adicionales
3. No estés usando un token de una sesión anterior con diferente configuración

#### Error: "JWT Token present: false"
**Solución:** Verifica el formato del header:
```bash
# CORRECTO
-H "Authorization: Bearer TU_TOKEN"

# INCORRECTO
-H "Authorization:Bearer TU_TOKEN"  # Sin espacio después de ":"
-H "Authorization: TU_TOKEN"        # Sin "Bearer "
```

### 7. Prueba completa paso a paso

```bash
# Paso 1: Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"Test123!"}' \
  | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# Paso 2: Verificar que el token se obtuvo
echo "Token: $TOKEN"

# Paso 3: Usar el token
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer $TOKEN" \
  -v
```

El flag `-v` (verbose) te mostrará los headers enviados y recibidos.

### 8. Si el problema persiste

Revisa los logs de la aplicación en la consola donde ejecutaste `mvn spring-boot:run` o donde esté corriendo el servidor.

Busca mensajes como:
- "No se pudo establecer la autenticación del usuario"
- "Token JWT inválido"
- "Token JWT expirado"
- Cualquier stack trace o excepción

### 9. Verificar la configuración de seguridad

Los siguientes endpoints NO requieren autenticación:
- `/api/auth/register`
- `/api/auth/login`
- `/version`
- `/db-connection-status`

Los siguientes endpoints SÍ requieren autenticación:
- `/api/categories`
- `/api/metric-units`
- `/api/products`
- `/api/user/profile`

### 10. Ejemplo funcional completo

```bash
#!/bin/bash

# 1. Registrar usuario
echo "1. Registrando usuario..."
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "email": "demo@example.com",
    "password": "Demo123!"
  }'

echo -e "\n"

# 2. Login y obtener token
echo "2. Haciendo login..."
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo",
    "password": "Demo123!"
  }')

echo "$RESPONSE"
echo -e "\n"

# 3. Extraer token
TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token obtenido: ${TOKEN:0:50}..."
echo -e "\n"

# 4. Crear categoría
echo "3. Creando categoría..."
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Category",
    "description": "Category created for testing"
  }'

echo -e "\n"

# 5. Listar categorías
echo "4. Listando categorías..."
curl -X GET http://localhost:8080/api/categories \
  -H "Authorization: Bearer $TOKEN"

echo -e "\n"
```

Guarda esto como `test_api.sh` y ejecútalo:
```bash
chmod +x test_api.sh
./test_api.sh
```

## Checklist rápido

- [ ] El servidor está corriendo
- [ ] Hice login y obtuve un token
- [ ] Copié el token completo (sin espacios extra)
- [ ] Uso el formato: `Authorization: Bearer TOKEN`
- [ ] El header tiene "Bearer " con espacio después
- [ ] No hay comillas extras alrededor del token
- [ ] El token no ha expirado (24 horas desde el login)
- [ ] Revisé los logs de la aplicación

## Contacto de soporte

Si después de seguir todos estos pasos el problema persiste, proporciona:
1. El comando cURL exacto que estás usando
2. La respuesta completa que recibes
3. Los logs de la aplicación
4. El token que estás usando (primeros y últimos 10 caracteres)

