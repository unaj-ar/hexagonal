package ar.edu.unaj.hexagonal.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un Producto.
 *
 * En la Arquitectura Hexagonal, los Value Objects son parte del DOMINIO.
 * Son inmutables y su identidad está definida por su valor, no por referencia.
 *
 * Al usar un tipo propio (en lugar de UUID o Long directamente), el dominio
 * expresa su intención con claridad y evita confusiones entre distintos IDs.
 */
public record ProductId(UUID value) {

    /**
     * Constructor compacto con validación de dominio.
     * El dominio impone sus propias reglas de negocio.
     */
    public ProductId {
        Objects.requireNonNull(value, "El ID del producto no puede ser nulo");
    }

    /**
     * Factory method para generar un nuevo ID único.
     */
    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    /**
     * Factory method para reconstruir un ID desde un String (ej: desde la BD).
     */
    public static ProductId of(String value) {
        return new ProductId(UUID.fromString(value));
    }

    /**
     * Factory method para reconstruir un ID desde un UUID.
     */
    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
