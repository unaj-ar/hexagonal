package ar.edu.unaj.hexagonal.infrastructure.adapter.out.persistence;

import ar.edu.unaj.hexagonal.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositorio JPA de Spring Data.
 *
 * Esta interfaz es un detalle de implementación de la infraestructura.
 * Spring Data genera automáticamente la implementación en tiempo de ejecución.
 *
 * NOTA: Esta interfaz NO es el puerto de salida del dominio.
 * El puerto de salida es ProductRepositoryPort (en el dominio).
 * Esta interfaz es usada INTERNAMENTE por ProductPersistenceAdapter.
 *
 * La anotación @Repository aquí es opcional (Spring Data la detecta
 * automáticamente), pero la incluimos por claridad.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    /**
     * Spring Data genera automáticamente la query:
     * SELECT * FROM products WHERE name = ?
     */
    boolean existsByName(String name);

    /**
     * Busca por nombre exacto (útil para validaciones).
     */
    java.util.Optional<ProductEntity> findByName(String name);
}
