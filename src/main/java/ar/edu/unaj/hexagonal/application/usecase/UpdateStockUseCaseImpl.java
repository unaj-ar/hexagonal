package ar.edu.unaj.hexagonal.application.usecase;

import ar.edu.unaj.hexagonal.domain.exception.ProductNotFoundException;
import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.port.in.UpdateStockUseCase;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;

/**
 * Implementación del Caso de Uso: Actualizar Stock.
 *
 * Demuestra un flujo más complejo:
 * 1. Buscar el producto (puede no existir → excepción de dominio)
 * 2. Aplicar la operación de negocio en la entidad de dominio
 * 3. Persistir el estado actualizado
 *
 * La lógica de negocio (validar stock suficiente, etc.) vive
 * en la entidad Product, no aquí. Este caso de uso solo orquesta.
 */
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {

    private final ProductRepositoryPort productRepository;

    public UpdateStockUseCaseImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Command command) {
        // 1. Buscar el producto — lanza excepción de dominio si no existe
        Product product = productRepository
                .findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        // 2. Aplicar la operación de negocio (la entidad valida sus invariantes)
        switch (command.operation()) {
            case INCREASE -> product.increaseStock(command.quantity());
            case DECREASE -> product.decreaseStock(command.quantity());
        }

        // 3. Persistir el estado actualizado
        return productRepository.save(product);
    }
}
