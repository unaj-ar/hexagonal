package ar.edu.unaj.hexagonal.domain.port.out;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida (Driven Port / Secondary Port): Repositorio de Productos.
 *
 * En la Arquitectura Hexagonal, los PUERTOS DE SALIDA son interfaces
 * que el DOMINIO define para comunicarse con el mundo exterior
 * (base de datos, APIs externas, sistemas de archivos, etc.).
 *
 * IMPORTANTE: Esta interfaz vive en el DOMINIO, no en la infraestructura.
 * El dominio define QUÉ necesita; la infraestructura decide CÓMO lo provee.
 *
 * Esto es la inversión de dependencias (DIP) en acción:
 * - El dominio NO depende de JPA, Hibernate, ni ningún framework.
 * - La infraestructura implementa esta interfaz con JPA, MongoDB, etc.
 * - Si mañana cambiamos de H2 a PostgreSQL, el dominio no se toca.
 */
public interface ProductRepositoryPort {

    /**
     * Persiste un producto (creación o actualización).
     *
     * @param product el producto a guardar
     * @return el producto guardado (puede tener datos actualizados por la BD)
     */
    Product save(Product product);

    /**
     * Busca un producto por su ID.
     *
     * @param productId el identificador del producto
     * @return el producto si existe
     */
    Optional<Product> findById(ProductId productId);

    /**
     * Retorna todos los productos almacenados.
     *
     * @return lista de todos los productos
     */
    List<Product> findAll();

    /**
     * Verifica si existe un producto con el nombre dado.
     * Útil para validar unicidad en el dominio.
     *
     * @param name nombre a verificar
     * @return true si ya existe un producto con ese nombre
     */
    boolean existsByName(String name);

    /**
     * Elimina un producto por su ID.
     *
     * @param productId el identificador del producto a eliminar
     */
    void deleteById(ProductId productId);
}
