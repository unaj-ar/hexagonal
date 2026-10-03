package ar.edu.unaj.hexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 *
 * Esta clase vive en el paquete raíz para que Spring Boot
 * pueda escanear todos los componentes de la aplicación.
 *
 * Estructura de paquetes:
 * com.unaj.hexagonal
 * ├── domain/          → Núcleo del negocio (sin dependencias externas)
 * │   ├── model/       → Entidades y Value Objects
 * │   ├── port/        → Interfaces (contratos)
 * │   │   ├── in/      → Puertos de entrada (casos de uso)
 * │   │   └── out/     → Puertos de salida (repositorios, servicios externos)
 * │   ├── service/     → Servicios de dominio
 * │   └── exception/   → Excepciones de dominio
 * ├── application/     → Casos de uso (orquestación)
 * │   └── usecase/     → Implementaciones de los puertos de entrada
 * └── infrastructure/  → Adaptadores y configuración de Spring
 *     ├── adapter/
 *     │   ├── in/      → Adaptadores de entrada (REST, CLI, mensajería)
 *     │   └── out/     → Adaptadores de salida (JPA, HTTP clients, etc.)
 *     └── config/      → Configuración de beans de Spring
 */
@SpringBootApplication
public class HexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalApplication.class, args);
    }
}
