# BCI - API REST

Implementa el ejercicio del PDF.

## Requisitos
- Java 17+
- Maven

## Cómo ejecutar la API localmente
```bash
mvn spring-boot:run
```

## Cómo probar la creacion de registro
```bash
curl -X POST http://localhost:8080/api/users/register \  -H "Content-Type: application/json" \  -d '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Hunter22",
    "phones": [{"number":"1234567","citycode":"1","countrycode":"57"}]
  }'
```

### Respuesta exitosa (201)
Devuelve el usuario con:
- id (UUID)
- created
- modified
- last_login
- token (JWT)
- isactive
- phones

### Errores (formato)
```json
{"mensaje":"<detalle>"}
```

### Validaciones
- Email único, con regex configurable (`app.regex.email`).
- Password con regex configurable (`app.regex.password`).

### Cómo probar Conulta de usuarios
```bash
curl -X GET "http://localhost:8080/api/users/getAll" \
  -H "Accept: application/json"
```

## Cómo ejecutar prueba unitaria
```bash
mvn test
```

## URL para acceso a Swagger
- Documentación: `http://localhost:8080/docs`

## Diagrama de secuencia y componentes en la ruta
- `docs/sequence.png`
- `docs/components.png`
