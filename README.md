# Proyecto Leban Challenge

Aplicación Spring Boot para gestión de departamentos con API REST completa.

## Arquitectura del Proyecto

- **Framework**: Spring Boot 3.5.11
- **Base de datos**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Documentación API**: OpenAPI/Swagger
- **Testing**: JUnit 5, TestContainers, WebTestClient, MockMvc

## Prerrequisitos

- Java 21
- Maven 3.8+
- Docker y Docker Compose (para tests con TestContainers y entorno local)

## Configuración del Entorno

### Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto:

```bash
# Configuración de la Base de Datos
DB_NAME=lebanDB
DB_USER=postgres
DB_PASSWORD=123456
DB_HOST=postgres
DB_PORT=5432

# Configuración de la Aplicación
APP_NAME=leban
SERVER_PORT=8083
SPRING_PROFILES_ACTIVE=docker

# Configuración de JPA/Hibernate
JPA_HIBERNATE_DDL_AUTO=update
JPA_SHOW_SQL=true
JPA_FORMAT_SQL=true
```

### Base de Datos Local

Para ejecutar la aplicación localmente:

```bash
docker-compose up -d postgres
```

Esto iniciará PostgreSQL en el puerto 5432.

## Ejecución de la Aplicación

### Modo Desarrollo

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8083`

### Modo Docker

```bash
docker-compose up --build
```

## Suite de Tests

El proyecto incluye múltiples tipos de tests para diferentes niveles de integración:

### 1. Tests Unitarios

Ejecutar tests unitarios del servicio:

```bash
mvn test -Dtest=DepartamentoServiceImplTest
```

### 2. Tests de Integración

#### Tests con TestContainers (Base de datos real)

Estos tests usan PostgreSQL en contenedor para testing completo:

```bash
# Ejecutar solo tests con TestContainers
mvn test -Dtest=DepartamentoRESTControllerIntegrationTestContainersTest

# Ejecutar con perfil específico
mvn test -Dspring.profiles.active=integration -Dtest=DepartamentoRESTControllerIntegrationTestContainersTest
```

#### Tests con WebTestClient (Testing reactivo)

Tests enfocados en la capa web usando WebTestClient:

```bash
# Ejecutar solo tests con WebTestClient
mvn test -Dtest=DepartamentoRESTControllerWebTestClientTest
```

#### Tests con MockMvc (Testing MVC tradicional)

Tests usando MockMvc para simulación completa del contexto web:

```bash
# Ejecutar solo tests con MockMvc
mvn test -Dtest=DepartamentoRESTControllerIntegrationTest
```

### 3. Ejecutar Todo el Suite de Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar con reporte detallado
mvn test -Dmaven.surefire.debug=true

# Ejecutar tests en paralelo
mvn test -DforkCount=2 -DreuseForks=true
```

### 4. Tests con Cobertura

```bash
# Ejecutar tests con reporte de cobertura (requiere plugin jacoco)
mvn test jacoco:report
```

## Endpoints de la API

### GET /api/departamentos
Obtener lista de departamentos con filtros opcionales.

**Parámetros de consulta:**
- `disponible` (boolean): Filtrar por disponibilidad
- `precioMin` (string): Precio mínimo
- `precioMax` (string): Precio máximo

**Ejemplos:**
```bash
# Todos los departamentos
GET /api/departamentos

# Solo disponibles
GET /api/departamentos?disponible=true

# Rango de precios
GET /api/departamentos?precioMin=100000&precioMax=200000

# Combinación de filtros
GET /api/departamentos?disponible=true&precioMin=50000
```

### POST /api/departamentos
Crear un nuevo departamento.

**Cuerpo de la petición:**
```json
{
  "titulo": "Departamento Centro",
  "descripcion": "Hermoso departamento en el centro",
  "precio": 150000.00,
  "moneda": "ARS",
  "metros_cuadrados": 75.5,
  "direccion": "Calle Principal 123",
  "disponible": true
}
```

**Validaciones:**
- `titulo`: Obligatorio, máximo 150 caracteres
- `descripcion`: Obligatorio, máximo 500 caracteres
- `precio`: Obligatorio, mayor a 0
- `moneda`: Obligatorio (ARS/USD)
- `metros_cuadrados`: Obligatorio, mayor a 0
- `direccion`: Obligatorio, máximo 500 caracteres

### PUT /api/departamentos/{id}
Actualizar un departamento existente.

**Parámetros de ruta:**
- `id` (string): ID del departamento a actualizar

**Cuerpo de la petición:** Igual que POST

## Documentación de la API

Acceder a Swagger UI:
```
http://localhost:8083/swagger-ui.html
```

## Estructura de Tests

```
src/test/java/com/challenge/leban/
├── controller/
│   ├── DepartamentoRESTControllerIntegrationTest.java           # Tests con MockMvc
│   ├── DepartamentoRESTControllerIntegrationTestContainersTest.java # Tests con TestContainers
│   └── DepartamentoRESTControllerWebTestClientTest.java         # Tests con WebTestClient
└── service/
    └── DepartamentoServiceImplTest.java                          # Tests unitarios del servicio
```

## Configuración de Tests

### application-integration.yml

Configuración específica para tests de integración:

```yaml
spring:
  datasource:
    url: jdbc:tc:postgresql:15-alpine:///testdb
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  sql:
    init:
      mode: never
```

## Troubleshooting

### Tests con TestContainers no funcionan

1. Verificar que Docker esté ejecutándose
2. Verificar que no haya conflictos de puertos
3. Ejecutar con más verbosidad: `mvn test -Dtest=*TestContainers* -Dmaven.surefire.debug=true`

### Errores de conexión a base de datos

1. Verificar que PostgreSQL esté ejecutándose: `docker-compose ps`
2. Verificar las variables de entorno en `.env`
3. Revisar logs: `docker-compose logs postgres`

### Tests lentos

Los tests con TestContainers pueden ser lentos en la primera ejecución debido al pull de la imagen de PostgreSQL. Las ejecuciones siguientes serán más rápidas.

## Comandos Útiles

```bash
# Limpiar y compilar
mvn clean compile

# Ejecutar solo tests de integración
mvn test -Dgroups=integration

# Ejecutar tests y generar reporte HTML
mvn surefire-report:report

# Ver dependencias del proyecto
mvn dependency:tree

# Verificar código sin ejecutar tests
mvn compile test-compile

# Ejecutar aplicación en modo debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```