package ar.edu.unaj.hexagonal.domain.port.in;

import ar.edu.unaj.hexagonal.domain.model.Product;

import java.util.List;

/**
 * Puerto de Entrada: Listar todos los Productos.
 *
 * Define el contrato para obtener el catálogo completo de productos.
 * En un sistema real, aquí se agregarían parámetros de paginación,
 * filtros y ordenamiento como parte del contrato del dominio.
 */
public interface ListProductsUseCase {

    /**
     * Retorna todos los productos disponibles en el sistema.
     *
     * @return lista de productos (puede estar vacía, nunca null)
     */
    List<Product> execute();
}
