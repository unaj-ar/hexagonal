package ar.edu.unaj.hexagonal.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios de la entidad de dominio Product.
 *
 * Estos tests son PUROS — no necesitan Spring, ni base de datos,
 * ni ningún framework. Solo Java puro.
 *
 * Esto es una de las grandes ventajas de la Arquitectura Hexagonal:
 * el dominio es completamente testeable de forma aislada.
 *
 * Ejecutar: mvn test -Dtest=ProductTest
 */
@DisplayName("Product - Entidad de Dominio")
class ProductTest {

    private Product createValidProduct() {
        return new Product(
                ProductId.generate(),
                "Laptop Test",
                "Descripción de prueba",
                new BigDecimal("999.99"),
                10
        );
    }

    @Test
    @DisplayName("Debe crear un producto válido con todos sus atributos")
    void shouldCreateValidProduct() {
        Product product = createValidProduct();

        assertNotNull(product.getId());
        assertEquals("Laptop Test", product.getName());
        assertEquals(new BigDecimal("999.99"), product.getPrice());
        assertEquals(10, product.getStock());
        assertTrue(product.isAvailable());
        assertNotNull(product.getCreatedAt());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre está vacío")
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(ProductId.generate(), "", "desc", new BigDecimal("10"), 5)
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción si el precio es negativo")
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(ProductId.generate(), "Producto", "desc", new BigDecimal("-1"), 5)
        );
    }

    @Test
    @DisplayName("Debe descontar stock correctamente")
    void shouldDecreaseStockCorrectly() {
        Product product = createValidProduct(); // stock = 10

        product.decreaseStock(3);

        assertEquals(7, product.getStock());
    }

    @Test
    @DisplayName("Debe lanzar excepción al descontar más stock del disponible")
    void shouldThrowExceptionWhenInsufficientStock() {
        Product product = createValidProduct(); // stock = 10

        assertThrows(IllegalStateException.class, () ->
                product.decreaseStock(15)
        );
    }

    @Test
    @DisplayName("Debe agregar stock correctamente")
    void shouldIncreaseStockCorrectly() {
        Product product = createValidProduct(); // stock = 10

        product.increaseStock(5);

        assertEquals(15, product.getStock());
    }

    @Test
    @DisplayName("Producto sin stock no debe estar disponible")
    void shouldNotBeAvailableWhenStockIsZero() {
        Product product = new Product(
                ProductId.generate(), "Sin Stock", "desc", new BigDecimal("10"), 0
        );

        assertFalse(product.isAvailable());
    }

    @Test
    @DisplayName("Dos productos con el mismo ID deben ser iguales")
    void shouldBeEqualWhenSameId() {
        ProductId sharedId = ProductId.generate();
        Product product1 = new Product(sharedId, "Producto A", "desc", new BigDecimal("10"), 5);
        Product product2 = new Product(sharedId, "Producto B", "desc", new BigDecimal("20"), 3);

        assertEquals(product1, product2);
    }
}
