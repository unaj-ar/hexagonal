package ar.edu.unaj.hexagonal.infrastructure.adapter.out.persistence;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;
import ar.edu.unaj.hexagonal.infrastructure.adapter.out.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de Salida (Driven Adapter / Secondary Adapter): Persistencia JPA.
 *
 * Esta clase es el PUENTE entre el dominio y la base de datos.
 * Implementa el puerto de salida (ProductRepositoryPort) definido en el dominio,
 * usando Spring Data JPA como mecanismo de persistencia.
 *
 * Responsabilidades:
 * 1. Implementar el contrato del puerto de salida
 * 2. Mapear entre entidades de dominio (Product) y entidades JPA (ProductEntity)
 * 3. Delegar las operaciones de BD al repositorio JPA
 *
 * Si mañana queremos usar MongoDB, solo creamos un nuevo adaptador
 * (ProductMongoAdapter) que implemente ProductRepositoryPort.
 * El dominio y los casos de uso NO se modifican.
 *
 * @Component: Spring gestiona este bean, pero el dominio no lo sabe.
 */
@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpaRepository;

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return jpaRepository.findById(productId.value())
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(ProductId productId) {
        jpaRepository.deleteById(productId.value());
    }

    // =========================================================
    // MAPPERS: Dominio ↔ Infraestructura
    // =========================================================

    /**
     * Convierte una entidad de dominio a entidad JPA.
     * El dominio NO conoce esta transformación.
     */
    private ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId().value())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Convierte una entidad JPA a entidad de dominio.
     * Reconstruye el objeto de dominio con todos sus datos.
     */
    private Product toDomain(ProductEntity entity) {
        return new Product(
                ProductId.of(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
