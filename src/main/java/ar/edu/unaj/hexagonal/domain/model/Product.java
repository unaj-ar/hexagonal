package ar.edu.unaj.hexagonal.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de Dominio: Producto.
 *
 * Esta clase representa el NÚCLEO del negocio. Es la entidad más importante
 * de la capa de dominio y NO tiene ninguna dependencia de frameworks externos
 * (sin anotaciones de JPA, sin anotaciones de Spring, sin Lombok).
 *
 * Reglas de negocio encapsuladas aquí:
 * - El nombre no puede estar vacío
 * - El precio debe ser positivo
 * - El stock no puede ser negativo
 * - Solo se puede descontar stock si hay suficiente disponible
 *
 * Esta clase es PURA Java — puede ser testeada sin levantar ningún contexto.
 */
public class Product {

    private final ProductId id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor principal para crear un nuevo producto.
     * Aplica las reglas de negocio del dominio.
     */
    public Product(ProductId id, String name, String description, BigDecimal price, int stock) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        setName(name);
        setDescription(description);
        setPrice(price);
        setStock(stock);
    }

    /**
     * Constructor de reconstrucción (para cargar desde persistencia).
     * Permite restaurar el estado completo de la entidad.
     */
    public Product(ProductId id, String name, String description,
                   BigDecimal price, int stock,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        setName(name);
        setDescription(description);
        setPrice(price);
        setStock(stock);
    }

    // =========================================================
    // COMPORTAMIENTO DE DOMINIO (Reglas de Negocio)
    // =========================================================

    /**
     * Descuenta stock del producto.
     * Regla de negocio: no se puede descontar más de lo disponible.
     */
    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException(
                "Stock insuficiente. Disponible: %d, solicitado: %d".formatted(this.stock, quantity)
            );
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Agrega stock al producto.
     * Regla de negocio: la cantidad debe ser positiva.
     */
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser positiva");
        }
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Actualiza el precio del producto.
     * Regla de negocio: el precio debe ser positivo.
     */
    public void updatePrice(BigDecimal newPrice) {
        setPrice(newPrice);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si el producto tiene stock disponible.
     */
    public boolean isAvailable() {
        return this.stock > 0;
    }

    // =========================================================
    // SETTERS CON VALIDACIÓN (privados para proteger invariantes)
    // =========================================================

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres");
        }
        this.name = name.trim();
    }

    private void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }

    private void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        this.price = price;
    }

    private void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    // =========================================================
    // GETTERS (el dominio controla el acceso a su estado)
    // =========================================================

    public ProductId getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{id=%s, name='%s', price=%s, stock=%d}".formatted(id, name, price, stock);
    }
}
