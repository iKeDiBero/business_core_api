@echo off
echo ====================================
echo Test API - Categories Endpoint
echo ====================================
echo.

echo 1. Registrando usuario de prueba...
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"Test123!\"}"
echo.
echo.

echo 2. Haciendo login...
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"password\":\"Test123!\"}" > token_response.json
echo.
echo.

echo 3. Token obtenido (ver token_response.json)
type token_response.json
echo.
echo.

echo 4. COPIA EL TOKEN DE token_response.json Y EJECUTA:
echo curl -X GET http://localhost:8080/api/categories -H "Authorization: Bearer TU_TOKEN_AQUI"
echo.

pause

