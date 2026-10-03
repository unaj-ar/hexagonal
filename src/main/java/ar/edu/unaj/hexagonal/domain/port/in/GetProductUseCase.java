package ar.edu.unaj.hexagonal.domain.port.in;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;

import java.util.Optional;

/**
 * Puerto de Entrada: Obtener Producto por ID.
 *
 * Define el contrato para recuperar un producto específico.
 * Retorna Optional para expresar explícitamente que el producto
 * puede no existir — esto es una decisión de dominio, no de infraestructura.
 */
public interface GetProductUseCase {

    /**
     * Busca un producto por su identificador único.
     *
     * @param productId identificador del producto
     * @return el producto si existe, vacío si no se encuentra
     */
    Optional<Product> execute(ProductId productId);
}
