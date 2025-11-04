# API de Autenticación - Documentación

## Endpoints de Autenticación

### 1. Registro de Usuario
**POST** `/api/auth/register`

**Request Body:**
```json
{
  "username": "usuario123",
  "email": "usuario@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "message": "Usuario registrado exitosamente"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "El nombre de usuario ya está en uso"
}
```

---

### 2. Login (Iniciar Sesión)
**POST** `/api/auth/login`

**Request Body:**
```json
{
  "username": "usuario123",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "usuario123",
  "email": "usuario@example.com",
  "roles": ["ROLE_USER"],
  "telefono": "123456789",
  "direccion": "Calle Falsa 123",
  "ciudad": "Madrid",
  "pais": "España",
  "profilePhoto": "iVBORw0KGgoAAAANSUhEUgAA..."
}
```

**Nota:** Los campos adicionales (`telefono`, `direccion`, `ciudad`, `pais`, `profilePhoto`) pueden ser `null` si no fueron proporcionados durante el registro.

**Response (400 Bad Request):**
```json
{
  "error": "Credenciales inválidas"
}
```

---

### 3. Logout (Cerrar Sesión)
**POST** `/api/auth/logout`

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "message": "Sesión cerrada exitosamente"
}
```

---

## Cómo usar el token JWT

Una vez que obtienes el token al hacer login, debes incluirlo en el header `Authorization` de todas las peticiones protegidas:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Endpoints Públicos (No requieren autenticación)

- `GET /version` - Información de la versión de la API
- `GET /db-connection-status` - Estado de la conexión a la base de datos
- `POST /api/auth/register` - Registro de nuevos usuarios
- `POST /api/auth/login` - Login de usuarios

---

## Configuración Inicial

### 1. Asegúrate de tener MySQL corriendo con la base de datos creada:
```sql
CREATE DATABASE db_business_core_api;
```

### 2. La aplicación creará automáticamente las tablas necesarias:
- `users` - Tabla de usuarios
- `roles` - Tabla de roles
- `user_roles` - Relación muchos a muchos entre usuarios y roles
- `db_connection_test` - Tabla de prueba para validar conexión

### 3. Los roles se crean automáticamente al iniciar la aplicación:
- `ROLE_USER` - Usuario estándar
- `ROLE_ADMIN` - Administrador del sistema
- `ROLE_MODERATOR` - Moderador del sistema

### 4. Variables de entorno opcionales:
```
DB_URL=jdbc:mysql://127.0.0.1:3306/db_business_core_api?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASS=admin
JWT_SECRET=mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890
### Registro (con campos básicos):
```

---

## Ejemplo de uso con cURL

### Registro (con todos los campos):
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario123",
    "email": "usuario@example.com",
    "password": "password123",
    "telefono": "123456789",
    "direccion": "Calle Falsa 123",
    "ciudad": "Madrid",
    "pais": "España",
    "profilePhoto": "iVBORw0KGgoAAAANSUhEUgAA..."
  }'
```

### Registro:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario123","email":"usuario@example.com","password":"password123"}'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario123","password":"password123"}'
```

### Acceder a endpoint protegido:
```bash
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Estructura del Proyecto

```
src/main/java/com/kedibero/business_core_api/
├── config/
│   └── SecurityConfig.java          # Configuración de seguridad Spring
├── controller/
│   ├── AuthController.java          # Endpoints de autenticación
│   ├── DbStatusController.java      # Estado de BD
│   └── VersionController.java       # Versión de API
├── dto/
│   ├── AuthResponse.java            # DTO de respuesta de login
│   ├── LoginRequest.java            # DTO de petición de login
│   └── RegisterRequest.java         # DTO de petición de registro
├── entity/
│   ├── DbConnectionTest.java        # Entidad de prueba
│   ├── Role.java                    # Entidad de rol
│   └── User.java                    # Entidad de usuario
├── repository/
│   ├── DbConnectionTestRepository.java
│   ├── RoleRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java # Filtro JWT para peticiones
│   ├── JwtTokenProvider.java        # Generador y validador de tokens
│   └── UserDetailsImpl.java         # Implementación de UserDetails
└── service/
    ├── AuthService.java              # Lógica de autenticación
    └── UserDetailsServiceImpl.java   # Carga de usuarios para Spring Security
```

