package ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto;

import ar.edu.unaj.hexagonal.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de Salida (Response): Producto.
 *
 * Representa la respuesta que la API REST devuelve al cliente.
 * El adaptador REST convierte la entidad de dominio (Product) a este DTO.
 *
 * Ventajas de tener un DTO de respuesta separado:
 * - Controla exactamente qué datos se exponen (no exponer datos internos)
 * - Puede tener un formato diferente al del dominio (ej: IDs como String)
 * - Permite versionar la API sin cambiar el dominio
 * - Evita exponer accidentalmente datos sensibles
 */
public record ProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * Factory method: convierte una entidad de dominio a DTO de respuesta.
     * El mapeo vive en el DTO para mantener el controlador limpio.
     */
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId().toString(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.isAvailable(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
