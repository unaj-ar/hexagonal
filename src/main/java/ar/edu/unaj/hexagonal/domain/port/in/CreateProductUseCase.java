package ar.edu.unaj.hexagonal.domain.port.in;

import ar.edu.unaj.hexagonal.domain.model.Product;

import java.math.BigDecimal;

/**
 * Puerto de Entrada (Driving Port / Primary Port): Crear Producto.
 *
 * En la Arquitectura Hexagonal, los PUERTOS DE ENTRADA definen
 * los casos de uso que el dominio expone al mundo exterior.
 *
 * Esta interfaz es parte del DOMINIO — no del framework.
 * Los adaptadores de entrada (REST, CLI, mensajería) la implementan
 * o la invocan para interactuar con el núcleo de negocio.
 *
 * Principio: el dominio define el contrato, el exterior lo cumple.
 */
public interface CreateProductUseCase {

    /**
     * Comando que encapsula los datos necesarios para crear un producto.
     *
     * Usar un record como Command es una práctica moderna en Java 21.
     * Evita pasar múltiples parámetros sueltos y hace el código más legible.
     */
    record Command(
            String name,
            String description,
            BigDecimal price,
            int initialStock
    ) {}

    /**
     * Ejecuta el caso de uso de creación de producto.
     *
     * @param command datos del producto a crear
     * @return el producto creado con su ID asignado
     */
    Product execute(Command command);
}
