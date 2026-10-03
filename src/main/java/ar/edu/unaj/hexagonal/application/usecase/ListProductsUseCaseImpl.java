package ar.edu.unaj.hexagonal.application.usecase;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.port.in.ListProductsUseCase;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;

import java.util.List;

/**
 * Implementación del Caso de Uso: Listar Productos.
 *
 * Retorna todos los productos disponibles en el sistema.
 * En producción, se agregaría paginación y filtros.
 */
public class ListProductsUseCaseImpl implements ListProductsUseCase {

    private final ProductRepositoryPort productRepository;

    public ListProductsUseCaseImpl(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAll();
    }
}
