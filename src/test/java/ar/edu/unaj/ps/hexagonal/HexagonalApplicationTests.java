package ar.edu.unaj.ps.hexagonal;

import ar.edu.unaj.hexagonal.HexagonalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test de integración: verifica que el contexto de Spring Boot
 * levanta correctamente con toda la configuración de la arquitectura hexagonal.
 */
@SpringBootTest(classes = HexagonalApplication.class)
class HexagonalApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto levanta sin errores, la arquitectura está bien conectada
    }
}
