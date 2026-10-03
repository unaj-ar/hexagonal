package ar.edu.unaj.hexagonal.infrastructure.config;

import ar.edu.unaj.hexagonal.domain.port.in.CreateProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Inicializador de datos de ejemplo.
 *
 * Carga datos de prueba al iniciar la aplicación.
 * Usa los casos de uso del dominio (no accede directamente a la BD),
 * lo que demuestra que la arquitectura hexagonal funciona de extremo a extremo.
 *
 * CommandLineRunner: se ejecuta después de que Spring Boot termina de inicializar.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner loadSampleData(CreateProductUseCase createProductUseCase) {
        return args -> {
            log.info("=== Cargando datos de ejemplo ===");

            createProductUseCase.execute(new CreateProductUseCase.Command(
                    "Laptop Dell XPS 15",
                    "Laptop de alto rendimiento con pantalla OLED 4K",
                    new BigDecimal("1299.99"),
                    10
            ));

            createProductUseCase.execute(new CreateProductUseCase.Command(
                    "Mouse Logitech MX Master 3",
                    "Mouse inalámbrico ergonómico para productividad",
                    new BigDecimal("89.99"),
                    50
            ));

            createProductUseCase.execute(new CreateProductUseCase.Command(
                    "Teclado Mecánico Keychron K2",
                    "Teclado mecánico compacto con switches Brown",
                    new BigDecimal("119.99"),
                    30
            ));

            createProductUseCase.execute(new CreateProductUseCase.Command(
                    "Monitor LG UltraWide 34\"",
                    "Monitor ultrawide 34 pulgadas, resolución 3440x1440",
                    new BigDecimal("549.99"),
                    5
            ));

            log.info("=== Datos de ejemplo cargados exitosamente ===");
            log.info("API disponible en: http://localhost:8080/api/products");
            log.info("Consola H2 en:     http://localhost:8080/h2-console");
        };
    }
}
