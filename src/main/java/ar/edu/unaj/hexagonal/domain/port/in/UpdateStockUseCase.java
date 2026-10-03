package ar.edu.unaj.hexagonal.domain.port.in;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;

/**
 * Puerto de Entrada: Actualizar Stock de un Producto.
 *
 * Caso de uso que encapsula la lógica de modificación de stock.
 * Demuestra cómo los casos de uso pueden tener operaciones
 * con semántica de negocio clara (aumentar/disminuir stock).
 */
public interface UpdateStockUseCase {

    /**
     * Comando para actualizar el stock de un producto.
     *
     * @param productId  ID del producto a actualizar
     * @param quantity   cantidad a modificar (positivo = agregar, negativo = descontar)
     * @param operation  tipo de operación a realizar
     */
    record Command(
            ProductId productId,
            int quantity,
            StockOperation operation
    ) {}

    /**
     * Tipo de operación de stock — expresa la intención del negocio.
     */
    enum StockOperation {
        INCREASE,   // Agregar stock (reposición, devolución)
        DECREASE    // Descontar stock (venta, pérdida)
    }

    /**
     * Ejecuta la actualización de stock.
     *
     * @param command datos de la operación
     * @return el producto con el stock actualizado
     */
    Product execute(Command command);
}
