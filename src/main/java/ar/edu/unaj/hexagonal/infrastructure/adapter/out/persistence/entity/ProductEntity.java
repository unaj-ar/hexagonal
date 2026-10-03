package ar.edu.unaj.hexagonal.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA de Persistencia: ProductEntity.
 *
 * Esta clase vive en la capa de INFRAESTRUCTURA y es completamente
 * diferente a la entidad de dominio (Product).
 *
 * ¿Por qué tener dos clases para "lo mismo"?
 * - La entidad de dominio (Product) expresa el NEGOCIO: tiene comportamiento,
 *   invariantes, y no depende de ningún framework.
 * - La entidad JPA (ProductEntity) expresa la PERSISTENCIA: tiene anotaciones
 *   de JPA, puede tener columnas adicionales, relaciones de BD, etc.
 *
 * Esta separación permite:
 * 1. Cambiar el esquema de BD sin tocar el dominio
 * 2. Cambiar el ORM (JPA → MongoDB) sin tocar el dominio
 * 3. Tener diferentes representaciones para diferentes contextos
 *
 * El adaptador de persistencia (ProductPersistenceAdapter) se encarga
 * de mapear entre ambas representaciones.
 */
@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
