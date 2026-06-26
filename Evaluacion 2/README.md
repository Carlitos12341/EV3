# Sistema de Gestión Taller Mecánico — Microservicios Spring Boot

Sistema backend basado en arquitectura de microservicios para la gestión integral de un taller mecánico. Desarrollado con Spring Boot 3.3.2 y Java 17.

---

## Microservicios

| Microservicio    | Puerto | Descripción                              | Swagger UI                                      |
|-----------------|--------|------------------------------------------|-------------------------------------------------|
| ms-cita         | 8080   | Gestión de citas del taller              | http://localhost:8080/doc/swagger-ui.html       |
| ms-cliente      | 8081   | Gestión de clientes                      | http://localhost:8081/doc/swagger-ui.html       |
| ms-factura      | 8082   | Facturación de reparaciones              | http://localhost:8082/doc/swagger-ui.html       |
| ms-inventario   | 8083   | Control de inventario de repuestos       | http://localhost:8083/doc/swagger-ui.html       |
| ms-mecanico     | 8084   | Gestión de mecánicos                     | http://localhost:8084/doc/swagger-ui.html       |
| ms-notificacion | 8085   | Envío de notificaciones a clientes       | http://localhost:8085/doc/swagger-ui.html       |
| ms-producto     | 8086   | Catálogo de repuestos y productos        | http://localhost:8086/doc/swagger-ui.html       |
| ms-proveedor    | 8087   | Gestión de proveedores                   | http://localhost:8087/doc/swagger-ui.html       |
| ms-reparacion   | 8088   | Registro de reparaciones realizadas      | http://localhost:8088/doc/swagger-ui.html       |
| ms-vehiculo     | 8089   | Gestión de vehículos de clientes         | http://localhost:8089/doc/swagger-ui.html       |
| **ms-gateway**  | **9090** | **API Gateway — punto de entrada único** | http://localhost:9090/doc/swagger-ui.html     |

---

## Rutas del API Gateway

Todas las peticiones pueden hacerse a través del gateway en `http://localhost:9090`:

| Ruta                          | Microservicio destino |
|------------------------------|-----------------------|
| `/api/citas/**`              | ms-cita :8080         |
| `/api/clientes/**`           | ms-cliente :8081      |
| `/api/facturas/**`           | ms-factura :8082      |
| `/api/inventarios/**`        | ms-inventario :8083   |
| `/api/mecanicos/**`          | ms-mecanico :8084     |
| `/api/notificaciones/**`     | ms-notificacion :8085 |
| `/api/productos/**`          | ms-producto :8086     |
| `/api/proveedores/**`        | ms-proveedor :8087    |
| `/api/reparaciones/**`       | ms-reparacion :8088   |
| `/api/vehiculos/**`          | ms-vehiculo :8089     |

---

## Tecnologías

- Java 17
- Spring Boot 3.3.2
- Spring Cloud Gateway 2023.0.3
- Spring Data JPA
- Spring WebFlux (WebClient)
- MySQL 8.0
- SpringDoc OpenAPI 2.6.0 (Swagger)
- JUnit 5 + Mockito (tests unitarios)
- Docker + Docker Compose

---

## Requisitos previos

- Java 17+
- Maven 3.8+
- MySQL 8.0 corriendo en `localhost:3306`
- Docker y Docker Compose (para ejecución con contenedores)

---

## Ejecución local (sin Docker)

### 1. Levantar la base de datos MySQL

Asegúrate de tener MySQL corriendo con usuario `root` y contraseña `root`. La base de datos `bd_taller` se crea automáticamente.

### 2. Compilar y ejecutar cada microservicio

```bash
# Ejemplo para ms-cliente
cd "Cliente taller/taller.cl.duoc"
mvn spring-boot:run
```

Repetir para cada microservicio. Orden recomendado:
1. ms-mecanico (8084)
2. ms-notificacion (8085)
3. ms-cita (8080) — depende de ms-mecanico
4. ms-reparacion (8088) — depende de ms-cita
5. Los demás en cualquier orden
6. ms-gateway (9090) — último

### 3. Ejecutar los tests

```bash
cd "Cliente taller/taller.cl.duoc"
mvn test
```

---

## Ejecución con Docker Compose

```bash
# Desde la raíz del proyecto, primero compilar cada servicio
cd "Cita taller/taller.cl.duoc" && mvn package -DskipTests && cd ../..
# ... repetir para cada servicio ...

# Luego levantar todo el stack
docker-compose up --build
```

Detener todos los servicios:
```bash
docker-compose down
```

---

## Comunicación entre microservicios

Los servicios se comunican entre sí usando **WebClient** (Spring WebFlux):

- **ms-cita** → **ms-mecanico**: verifica disponibilidad del mecánico antes de agendar
- **ms-reparacion** → **ms-cita**: verifica que la cita exista antes de registrar reparación

---

## Estructura del proyecto

```
Evaluacion 2/
├── Cita taller/
├── Cliente taller/
├── Facturacion taller/
├── Inventario taller/
├── Mecanico taller/
├── Notificacion taller/
├── Producto taller/
├── Proveedor taller/
├── Reparacion taller/
├── Vehiculo taller/
├── Gateway taller/         ← API Gateway
├── docker-compose.yml      ← Orquestación Docker
└── README.md
```

---

## Autor

Sistema desarrollado como proyecto universitario — Evaluación 2 Fullstack  
DUOC UC — Ingeniería en Informática
