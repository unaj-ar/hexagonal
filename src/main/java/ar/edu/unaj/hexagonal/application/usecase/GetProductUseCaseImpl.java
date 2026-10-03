package ar.edu.unaj.hexagonal.application.usecase;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;
import ar.edu.unaj.hexagonal.domain.port.in.GetProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;

import java.util.Optional;

/**
 * Implementación del Caso de Uso: Obtener Producto por ID.
 *
 * Caso de uso simple que delega directamente al repositorio.
 * Demuestra que no toda lógica de aplicación es compleja —
 * a veces el caso de uso es simplemente una consulta directa.
 *
 * En sistemas más complejos, aquí podría haber:
 * - Verificación de permisos del usuario
 * - Logging de auditoría
 * - Caché de resultados
 */
public class GetProductUseCaseImpl implements GetProductUseCase {

    private final ProductRepositoryPort productRepository;

    public GetProductUseCaseImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> execute(ProductId productId) {
        return productRepository.findById(productId);
    }
}
