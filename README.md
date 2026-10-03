# Arquitectura Hexagonal con Java 21 y Spring Boot

> Proyecto de ejemplo para la cátedra de Programación de Sistemas — UNAJ  
> Implementa el patrón **Ports & Adapters** (Arquitectura Hexagonal) de Alistair Cockburn

---

## 📋 Tabla de Contenidos

1. [¿Qué es la Arquitectura Hexagonal?](#qué-es-la-arquitectura-hexagonal)
2. [Conceptos Clave](#conceptos-clave)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Diagrama de Capas](#diagrama-de-capas)
5. [Flujo de una Petición](#flujo-de-una-petición)
6. [Descripción de cada Capa](#descripción-de-cada-capa)
7. [Tecnologías Utilizadas](#tecnologías-utilizadas)
8. [Cómo Ejecutar el Proyecto](#cómo-ejecutar-el-proyecto)
9. [Endpoints de la API](#endpoints-de-la-api)
10. [Ejemplos de Uso (curl)](#ejemplos-de-uso-curl)
11. [Ventajas de esta Arquitectura](#ventajas-de-esta-arquitectura)
12. [Comparación con Arquitectura en Capas Tradicional](#comparación-con-arquitectura-en-capas-tradicional)
13. [Principios SOLID Aplicados](#principios-solid-aplicados)
14. [Preguntas Frecuentes](#preguntas-frecuentes)

---

## ¿Qué es la Arquitectura Hexagonal?

La **Arquitectura Hexagonal** (también llamada **Ports & Adapters**) fue propuesta por **Alistair Cockburn en 2005**. Su objetivo principal es:

> **Aislar el núcleo de la aplicación (lógica de negocio) de los detalles técnicos externos** (bases de datos, frameworks, APIs, interfaces de usuario).

La idea central es que una aplicación debería poder funcionar de la misma manera sin importar si la invoca:
- Un usuario a través de una interfaz web
- Un test automatizado
- Otro sistema a través de una API REST
- Un mensaje de una cola (RabbitMQ, Kafka)
- Un script de línea de comandos

El **hexágono** no tiene un significado especial en cuanto a los 6 lados — simplemente representa que la aplicación tiene múltiples "caras" por donde puede ser accedida o puede acceder a sistemas externos.

```
                    ┌─────────────────────────────────────┐
                    │                                     │
   [REST API]  ───► │                                     │ ───► [Base de Datos]
                    │                                     │
   [CLI]       ───► │      NÚCLEO DE LA APLICACIÓN        │ ───► [API Externa]
                    │      (Dominio + Casos de Uso)        │
   [Tests]     ───► │                                     │ ───► [Email Service]
                    │                                     │
   [Mensajería]───► │                                     │ ───► [Cache]
                    │                                     │
                    └─────────────────────────────────────┘
                         Adaptadores de Entrada (IN)
                         Adaptadores de Salida (OUT)
```

---

## Conceptos Clave

### 🔷 Dominio (Domain)
El **corazón** de la aplicación. Contiene:
- **Entidades**: objetos con identidad y comportamiento (ej: `Product`)
- **Value Objects**: objetos inmutables definidos por su valor (ej: `ProductId`)
- **Servicios de Dominio**: lógica de negocio que no pertenece a una sola entidad
- **Excepciones de Dominio**: situaciones excepcionales del negocio

**Regla de oro**: El dominio **NO** depende de ningún framework, librería externa, ni base de datos.

### 🔷 Puerto (Port)
Una **interfaz** (contrato) que define cómo el dominio se comunica con el exterior.

- **Puerto de Entrada (Driving Port / Primary Port)**: define los casos de uso que el dominio expone. Los adaptadores externos los invocan.
- **Puerto de Salida (Driven Port / Secondary Port)**: define lo que el dominio necesita del exterior (ej: persistencia). El dominio los define, la infraestructura los implementa.

### 🔷 Adaptador (Adapter)
Una **implementación concreta** de un puerto. Traduce entre el lenguaje del dominio y el lenguaje del mundo exterior.

- **Adaptador de Entrada (Driving Adapter)**: recibe peticiones del exterior y las traduce al dominio (ej: `ProductController`)
- **Adaptador de Salida (Driven Adapter)**: implementa los puertos de salida del dominio (ej: `ProductPersistenceAdapter`)

### 🔷 Caso de Uso (Use Case)
Orquesta el flujo de una operación de negocio. Vive en la capa de **Aplicación** y:
1. Recibe un comando (datos de entrada)
2. Aplica lógica de negocio (delegando al dominio)
3. Usa puertos de salida para persistir o comunicarse con externos
4. Retorna el resultado

---

## Estructura del Proyecto

```
src/main/java/com/unaj/hexagonal/
│
├── HexagonalApplication.java              ← Punto de entrada Spring Boot
│
├── domain/                                ← 🔴 NÚCLEO: sin dependencias externas
│   ├── model/
│   │   ├── Product.java                   ← Entidad de dominio (con comportamiento)
│   │   └── ProductId.java                 ← Value Object (inmutable)
│   ├── port/
│   │   ├── in/                            ← Puertos de ENTRADA (casos de uso)
│   │   │   ├── CreateProductUseCase.java
│   │   │   ├── GetProductUseCase.java
│   │   │   ├── ListProductsUseCase.java
│   │   │   └── UpdateStockUseCase.java
│   │   └── out/                           ← Puertos de SALIDA (repositorios, etc.)
│   │       └── ProductRepositoryPort.java
│   ├── service/
│   │   └── ProductDomainService.java      ← Lógica de negocio transversal
│   └── exception/
│       └── ProductNotFoundException.java  ← Excepción de dominio
│
├── application/                           ← 🟡 APLICACIÓN: orquestación
│   └── usecase/
│       ├── CreateProductUseCaseImpl.java
│       ├── GetProductUseCaseImpl.java
│       ├── ListProductsUseCaseImpl.java
│       └── UpdateStockUseCaseImpl.java
│
└── infrastructure/                        ← 🟢 INFRAESTRUCTURA: detalles técnicos
    ├── adapter/
    │   ├── in/
    │   │   └── rest/                      ← Adaptador REST (entrada)
    │   │       ├── ProductController.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── dto/
    │   │           ├── CreateProductRequest.java
    │   │           ├── UpdateStockRequest.java
    │   │           └── ProductResponse.java
    │   └── out/
    │       └── persistence/               ← Adaptador JPA (salida)
    │           ├── ProductPersistenceAdapter.java
    │           ├── ProductJpaRepository.java
    │           └── entity/
    │               └── ProductEntity.java
    └── config/
        ├── BeanConfiguration.java         ← Inyección de dependencias
        └── DataInitializer.java           ← Datos de ejemplo
```

---

## Diagrama de Capas

```
╔══════════════════════════════════════════════════════════════════╗
║                    INFRAESTRUCTURA (Verde)                       ║
║                                                                  ║
║  ┌─────────────────┐              ┌──────────────────────────┐  ║
║  │  ProductController│            │ ProductPersistenceAdapter │  ║
║  │  (REST Adapter)  │            │   (JPA Adapter)           │  ║
║  └────────┬─────────┘            └───────────────┬──────────┘  ║
║           │ implementa/usa                        │ implementa   ║
╠═══════════╪═══════════════════════════════════════╪════════════╣
║           │         APLICACIÓN (Amarillo)         │            ║
║           │                                       │            ║
║  ┌────────▼──────────────────────────────────────▼──────────┐  ║
║  │              CreateProductUseCaseImpl                     │  ║
║  │              GetProductUseCaseImpl                        │  ║
║  │              ListProductsUseCaseImpl                      │  ║
║  │              UpdateStockUseCaseImpl                       │  ║
║  └────────┬──────────────────────────────────────┬──────────┘  ║
║           │ implementa                            │ usa         ║
╠═══════════╪═══════════════════════════════════════╪════════════╣
║           │           DOMINIO (Rojo)              │            ║
║           │                                       │            ║
║  ┌────────▼──────────┐              ┌─────────────▼──────────┐  ║
║  │  CreateProductUseCase│           │  ProductRepositoryPort  │  ║
║  │  GetProductUseCase   │           │  (Puerto de Salida)     │  ║
║  │  (Puertos Entrada)   │           └────────────────────────┘  ║
║  └───────────────────┘                                          ║
║                                                                  ║
║  ┌──────────────────────────────────────────────────────────┐   ║
║  │  Product  │  ProductId  │  ProductDomainService          │   ║
║  └──────────────────────────────────────────────────────────┘   ║
╚══════════════════════════════════════════════════════════════════╝

Dirección de las dependencias:
  Infraestructura → Aplicación → Dominio
  (las capas externas dependen de las internas, NUNCA al revés)
```

---

## Flujo de una Petición

Ejemplo: `POST /api/products` para crear un producto.

```
1. Cliente HTTP
   │
   │  POST /api/products
   │  { "name": "Laptop", "price": 999.99, "initialStock": 10 }
   ▼
2. ProductController (Adaptador de Entrada)
   │  - Recibe la petición HTTP
   │  - Valida el DTO con Jakarta Validation
   │  - Convierte CreateProductRequest → CreateProductUseCase.Command
   │  - Invoca createProductUseCase.execute(command)
   ▼
3. CreateProductUseCaseImpl (Caso de Uso - Aplicación)
   │  - Valida unicidad del nombre (via ProductDomainService)
   │  - Crea la entidad Product (el dominio valida sus invariantes)
   │  - Llama a productRepository.save(product)
   ▼
4. ProductPersistenceAdapter (Adaptador de Salida)
   │  - Convierte Product (dominio) → ProductEntity (JPA)
   │  - Llama a jpaRepository.save(entity)
   │  - Convierte ProductEntity (JPA) → Product (dominio)
   │  - Retorna el Product guardado
   ▼
5. Base de Datos H2
   │  INSERT INTO products (id, name, price, stock, ...) VALUES (...)
   │
   ▲ (respuesta sube por el mismo camino)
   │
6. ProductController
   │  - Convierte Product (dominio) → ProductResponse (DTO)
   │  - Retorna HTTP 201 Created con el DTO
   ▼
7. Cliente HTTP
   HTTP 201 Created
   { "id": "uuid", "name": "Laptop", "price": 999.99, ... }
```

---

## Descripción de cada Capa

### 🔴 Capa de Dominio (`domain/`)

**Es el núcleo de la aplicación. No depende de nada externo.**

| Componente | Descripción |
|-----------|-------------|
| `Product` | Entidad principal. Tiene comportamiento (decreaseStock, increaseStock) y valida sus propias invariantes. |
| `ProductId` | Value Object. Inmutable, representa el ID del producto. Usa `record` de Java 21. |
| `CreateProductUseCase` | Puerto de entrada. Define el contrato del caso de uso con un `Command` record interno. |
| `ProductRepositoryPort` | Puerto de salida. Define qué operaciones de persistencia necesita el dominio. |
| `ProductDomainService` | Lógica de negocio que involucra múltiples entidades o requiere acceso al repositorio. |
| `ProductNotFoundException` | Excepción de dominio. Expresa una situación excepcional del negocio. |

### 🟡 Capa de Aplicación (`application/`)

**Orquesta los casos de uso. Coordina el dominio con los puertos.**

| Componente | Descripción |
|-----------|-------------|
| `CreateProductUseCaseImpl` | Implementa `CreateProductUseCase`. Valida, crea la entidad y persiste. |
| `GetProductUseCaseImpl` | Implementa `GetProductUseCase`. Busca un producto por ID. |
| `ListProductsUseCaseImpl` | Implementa `ListProductsUseCase`. Retorna todos los productos. |
| `UpdateStockUseCaseImpl` | Implementa `UpdateStockUseCase`. Busca el producto y aplica la operación de stock. |

**Nota**: Estas clases NO tienen anotaciones de Spring (`@Service`). Son POJOs puros configurados en `BeanConfiguration`.

### 🟢 Capa de Infraestructura (`infrastructure/`)

**Contiene todos los detalles técnicos. Depende del dominio, nunca al revés.**

#### Adaptadores de Entrada (REST)
| Componente | Descripción |
|-----------|-------------|
| `ProductController` | Controlador REST. Traduce HTTP ↔ Dominio. |
| `GlobalExceptionHandler` | Traduce excepciones de dominio a respuestas HTTP (404, 400, 422). |
| `CreateProductRequest` | DTO de entrada con validaciones Jakarta. |
| `ProductResponse` | DTO de salida. Controla qué datos se exponen. |

#### Adaptadores de Salida (Persistencia)
| Componente | Descripción |
|-----------|-------------|
| `ProductPersistenceAdapter` | Implementa `ProductRepositoryPort`. Mapea Dominio ↔ JPA. |
| `ProductJpaRepository` | Interfaz Spring Data JPA. Detalle de implementación interno. |
| `ProductEntity` | Entidad JPA. Tiene anotaciones de Hibernate. Separada de la entidad de dominio. |

#### Configuración
| Componente | Descripción |
|-----------|-------------|
| `BeanConfiguration` | Conecta todas las piezas. Crea los beans de los casos de uso inyectando los adaptadores. |
| `DataInitializer` | Carga datos de ejemplo al iniciar. |

---

## Tecnologías Utilizadas

| Tecnología | Versión | Rol |
|-----------|---------|-----|
| Java | 21 | Lenguaje principal. Usa records, pattern matching, text blocks. |
| Spring Boot | 3.3.2 | Framework de aplicación. Solo en la capa de infraestructura. |
| Spring Web (MVC) | 3.3.2 | Adaptador REST de entrada. |
| Spring Data JPA | 3.3.2 | Adaptador de persistencia de salida. |
| Hibernate | 6.x | ORM (implementación de JPA). |
| H2 Database | 2.x | Base de datos en memoria para desarrollo/demo. |
| Lombok | 1.18.x | Reduce boilerplate (getters, builders, constructores). |
| Jakarta Validation | 3.x | Validación de DTOs en la capa REST. |
| JUnit 5 | 5.x | Framework de testing. |
| Maven | 3.x | Gestión de dependencias y build. |

---

## Cómo Ejecutar el Proyecto

### Prerrequisitos
- Java 21 instalado (`java -version`)
- Maven 3.8+ instalado (`mvn -version`)

### Pasos

```bash
# 1. Clonar o navegar al directorio del proyecto
cd /Users/alejandro.v/IdeaProjects/unaj/hexagonal

# 2. Compilar el proyecto
mvn clean compile

# 3. Ejecutar los tests
mvn test

# 4. Iniciar la aplicación
mvn spring-boot:run
```

### Verificar que funciona

```bash
# La aplicación estará disponible en:
http://localhost:8080/api/products

# Consola web de H2 (base de datos en memoria):
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:hexagonaldb
# Usuario: sa
# Contraseña: (vacía)
```

---

## Endpoints de la API

| Método | URL | Descripción | Código de éxito |
|--------|-----|-------------|-----------------|
| `GET` | `/api/products` | Lista todos los productos | 200 OK |
| `GET` | `/api/products/{id}` | Obtiene un producto por ID | 200 OK |
| `POST` | `/api/products` | Crea un nuevo producto | 201 Created |
| `PATCH` | `/api/products/{id}/stock` | Actualiza el stock | 200 OK |

### Códigos de Error

| Código | Situación |
|--------|-----------|
| 400 Bad Request | Datos de entrada inválidos (nombre vacío, precio negativo, etc.) |
| 404 Not Found | Producto no encontrado |
| 422 Unprocessable Entity | Operación de negocio inválida (ej: stock insuficiente) |

---

## Ejemplos de Uso (curl)

### Listar todos los productos (cargados al inicio)
```bash
curl -X GET http://localhost:8080/api/products | python3 -m json.tool
```

### Crear un nuevo producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Auriculares Sony WH-1000XM5",
    "description": "Auriculares inalámbricos con cancelación de ruido",
    "price": 349.99,
    "initialStock": 20
  }'
```

### Obtener un producto por ID
```bash
# Reemplazar {id} con el UUID del producto
curl -X GET http://localhost:8080/api/products/{id}
```

### Agregar stock a un producto
```bash
curl -X PATCH http://localhost:8080/api/products/{id}/stock \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 10,
    "operation": "INCREASE"
  }'
```

### Descontar stock de un producto
```bash
curl -X PATCH http://localhost:8080/api/products/{id}/stock \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 3,
    "operation": "DECREASE"
  }'
```

### Intentar descontar más stock del disponible (error de negocio)
```bash
curl -X PATCH http://localhost:8080/api/products/{id}/stock \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 9999,
    "operation": "DECREASE"
  }'
# Respuesta: 422 Unprocessable Entity
# { "title": "Operación no permitida", "detail": "Stock insuficiente..." }
```

---

## Ventajas de esta Arquitectura

### ✅ Testabilidad
El dominio es **100% testeable sin Spring**. Los tests unitarios son rápidos y no necesitan levantar contexto.

```java
// Test puro de dominio — sin Spring, sin BD, sin mocks de framework
@Test
void shouldDecreaseStockCorrectly() {
    Product product = new Product(ProductId.generate(), "Laptop", "desc", new BigDecimal("999"), 10);
    product.decreaseStock(3);
    assertEquals(7, product.getStock());
}
```

### ✅ Intercambiabilidad de Adaptadores
¿Querés cambiar de H2 a PostgreSQL? Solo cambiás el adaptador de persistencia. El dominio no se toca.

```
ProductRepositoryPort (dominio, no cambia)
    ↑ implementado por
ProductPersistenceAdapter (JPA con H2)  →  ProductMongoPersistenceAdapter (MongoDB)
```

### ✅ Múltiples Interfaces de Entrada
El mismo dominio puede ser invocado desde REST, CLI, mensajería, etc.

```
REST Controller  ──┐
CLI Adapter      ──┤──► CreateProductUseCase ──► Dominio
Kafka Consumer   ──┘
```

### ✅ Independencia del Framework
Si Spring Boot cambia su API o querés migrar a Quarkus, el dominio y la capa de aplicación no se modifican.

### ✅ Claridad de Responsabilidades
Cada clase tiene una responsabilidad clara y bien definida. Es fácil saber dónde agregar nueva funcionalidad.

---

## Comparación con Arquitectura en Capas Tradicional

### Arquitectura en Capas Tradicional (N-Tier)
```
┌─────────────────────────────┐
│    Presentation Layer       │  ← Controller
├─────────────────────────────┤
│    Business Logic Layer     │  ← Service
├─────────────────────────────┤
│    Data Access Layer        │  ← Repository
├─────────────────────────────┤
│    Database                 │
└─────────────────────────────┘

Problema: las capas superiores dependen de las inferiores.
Si cambia la BD, puede afectar hasta la capa de negocio.
```

### Arquitectura Hexagonal
```
                ┌─────────────────────┐
                │      DOMINIO        │  ← No depende de nada
                │  (reglas de negocio)│
                └──────────┬──────────┘
                           │ define contratos (puertos)
              ┌────────────┴────────────┐
              │       APLICACIÓN        │  ← Solo depende del dominio
              │    (casos de uso)       │
              └────────────┬────────────┘
                           │ implementa puertos
    ┌──────────────────────┴──────────────────────┐
    │              INFRAESTRUCTURA                 │  ← Depende de todo
    │  REST │ JPA │ Kafka │ Email │ Cache │ ...   │
    └─────────────────────────────────────────────┘

Ventaja: el dominio está protegido. Los cambios técnicos
no afectan la lógica de negocio.
```

---

## Principios SOLID Aplicados

### S — Single Responsibility Principle
Cada clase tiene una única responsabilidad:
- `Product`: modela el negocio de un producto
- `ProductController`: maneja peticiones HTTP
- `ProductPersistenceAdapter`: maneja la persistencia

### O — Open/Closed Principle
Para agregar un nuevo adaptador (ej: GraphQL), no modificamos el dominio — solo agregamos un nuevo adaptador.

### L — Liskov Substitution Principle
`ProductPersistenceAdapter` puede ser reemplazado por cualquier otra implementación de `ProductRepositoryPort` sin afectar el comportamiento.

### I — Interface Segregation Principle
Los puertos de entrada están separados por caso de uso (`CreateProductUseCase`, `GetProductUseCase`, etc.) en lugar de tener una única interfaz gigante.

### D — Dependency Inversion Principle
El dominio define `ProductRepositoryPort` (abstracción).  
La infraestructura implementa `ProductPersistenceAdapter` (detalle).  
El dominio NO depende de JPA — JPA depende del dominio.

---

## Preguntas Frecuentes

### ¿Por qué hay dos clases para "Producto" (Product y ProductEntity)?

`Product` (dominio) y `ProductEntity` (JPA) tienen propósitos diferentes:
- `Product` expresa el **negocio**: tiene comportamiento, invariantes, y es independiente de la BD.
- `ProductEntity` expresa la **persistencia**: tiene anotaciones JPA, puede tener columnas adicionales.

Esta separación permite cambiar el esquema de BD sin tocar el dominio.

### ¿Por qué los casos de uso no tienen @Service?

Para mantener la capa de aplicación libre de dependencias de Spring. Los beans se configuran en `BeanConfiguration` (infraestructura). Esto hace que los casos de uso sean POJOs puros, más fáciles de testear.

### ¿Cuándo usar un Domain Service vs un Use Case?

- **Domain Service**: lógica de negocio pura que involucra múltiples entidades o requiere acceso al repositorio para validar reglas de negocio (ej: validar unicidad de nombre).
- **Use Case (Application Service)**: orquesta el flujo completo de una operación (recibe input, coordina dominio y puertos, retorna output).

### ¿Es obligatorio tener DTOs separados de las entidades de dominio?

No es obligatorio, pero es una buena práctica porque:
1. Controla qué datos se exponen en la API
2. Permite versionar la API sin cambiar el dominio
3. Evita exponer datos internos del dominio

### ¿Cómo agrego un nuevo caso de uso?

1. Crear la interfaz en `domain/port/in/` (ej: `DeleteProductUseCase.java`)
2. Crear la implementación en `application/usecase/` (ej: `DeleteProductUseCaseImpl.java`)
3. Registrar el bean en `BeanConfiguration.java`
4. Agregar el endpoint en `ProductController.java` (si aplica)

### ¿Cómo cambio de H2 a PostgreSQL?

1. Cambiar la dependencia en `pom.xml` (H2 → PostgreSQL driver)
2. Actualizar `application.yml` con la URL de PostgreSQL
3. El dominio y los casos de uso **no se modifican**

---

## Recursos Adicionales

- 📖 [Artículo original de Alistair Cockburn (2005)](https://alistair.cockburn.us/hexagonal-architecture/)
- 📖 [Clean Architecture — Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- 📖 [Domain-Driven Design — Eric Evans](https://domainlanguage.com/ddd/)
- 📖 [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

*Proyecto desarrollado para la cátedra de Programación de Sistemas — UNAJ*
