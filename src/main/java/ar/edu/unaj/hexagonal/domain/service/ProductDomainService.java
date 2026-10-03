package ar.edu.unaj.hexagonal.domain.service;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;

/**
 * Servicio de Dominio: lógica de negocio que no pertenece a una sola entidad.
 *
 * Los Servicios de Dominio encapsulan reglas de negocio que involucran
 * múltiples entidades o que no encajan naturalmente en una sola entidad.
 *
 * DIFERENCIA con Application Service:
 * - Domain Service: contiene lógica de NEGOCIO pura (reglas del dominio)
 * - Application Service (Use Case): orquesta el flujo, coordina puertos
 *
 * Este servicio es parte del DOMINIO — puede usar puertos de salida
 * pero NO tiene dependencias de frameworks.
 */
public class ProductDomainService {

    private final ProductRepositoryPort productRepository;

    public ProductDomainService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Valida que el nombre del producto sea único en el sistema.
     * Esta es una regla de negocio que requiere consultar el repositorio.
     *
     * @param name nombre a validar
     * @throws IllegalArgumentException si ya existe un producto con ese nombre
     */
    public void validateProductNameUniqueness(String name) {
        if (productRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                "Ya existe un producto con el nombre: '%s'".formatted(name)
            );
        }
    }

    /**
     * Verifica si un producto puede ser vendido.
     * Combina múltiples condiciones de negocio.
     *
     * @param product el producto a verificar
     * @param requestedQuantity cantidad solicitada
     * @return true si el producto puede ser vendido en esa cantidad
     */
    public boolean canBeSold(Product product, int requestedQuantity) {
        return product.isAvailable() && product.getStock() >= requestedQuantity;
    }
}
