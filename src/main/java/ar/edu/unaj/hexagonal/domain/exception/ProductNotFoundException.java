package ar.edu.unaj.hexagonal.domain.exception;

import ar.edu.unaj.hexagonal.domain.model.ProductId;

/**
 * Excepción de Dominio: Producto no encontrado.
 *
 * Las excepciones de dominio son parte del DOMINIO — expresan
 * situaciones excepcionales desde la perspectiva del negocio.
 *
 * NO son excepciones técnicas (como SQLException o IOException).
 * Son parte del lenguaje ubicuo del dominio.
 */
public class ProductNotFoundException extends RuntimeException {

    private final ProductId productId;

    public ProductNotFoundException(ProductId productId) {
        super("Producto no encontrado con ID: " + productId);
        this.productId = productId;
    }

    public ProductId getProductId() {
        return productId;
    }
}
