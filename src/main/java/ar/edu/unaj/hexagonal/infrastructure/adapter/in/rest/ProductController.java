package ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;
import ar.edu.unaj.hexagonal.domain.port.in.CreateProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.GetProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.ListProductsUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.UpdateStockUseCase;
import ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto.CreateProductRequest;
import ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto.ProductResponse;
import ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto.UpdateStockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de Entrada (Driving Adapter / Primary Adapter): REST Controller.
 *
 * Este controlador es un ADAPTADOR — traduce las peticiones HTTP
 * al lenguaje del dominio (Commands) y las respuestas del dominio
 * al lenguaje HTTP (DTOs + códigos de estado).
 *
 * Responsabilidades:
 * 1. Recibir peticiones HTTP
 * 2. Validar el formato de entrada (Jakarta Validation)
 * 3. Convertir DTOs a Commands del dominio
 * 4. Invocar el caso de uso correspondiente (puerto de entrada)
 * 5. Convertir la respuesta del dominio a DTO de salida
 * 6. Retornar la respuesta HTTP apropiada
 *
 * Lo que NO hace este controlador:
 * - Lógica de negocio (eso es del dominio)
 * - Acceso a la base de datos (eso es del adaptador de persistencia)
 * - Conocer cómo se implementan los casos de uso
 *
 * Depende de los PUERTOS DE ENTRADA (interfaces del dominio),
 * NO de las implementaciones concretas.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    // Depende de las INTERFACES (puertos), no de las implementaciones
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateStockUseCase updateStockUseCase;

    /**
     * POST /api/products
     * Crea un nuevo producto.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        // Convertir DTO → Command del dominio
        CreateProductUseCase.Command command = new CreateProductUseCase.Command(
                request.name(),
                request.description(),
                request.price(),
                request.initialStock()
        );

        // Invocar el caso de uso
        Product product = createProductUseCase.execute(command);

        // Convertir entidad de dominio → DTO de respuesta
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResponse.from(product));
    }

    /**
     * GET /api/products
     * Lista todos los productos.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<ProductResponse> products = listProductsUseCase.execute()
                .stream()
                .map(ProductResponse::from)
                .toList();

        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id}
     * Obtiene un producto por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        return getProductUseCase.execute(ProductId.of(id))
                .map(ProductResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/products/{id}/stock
     * Actualiza el stock de un producto.
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable String id,
            @Valid @RequestBody UpdateStockRequest request) {

        UpdateStockUseCase.Command command = new UpdateStockUseCase.Command(
                ProductId.of(id),
                request.quantity(),
                UpdateStockUseCase.StockOperation.valueOf(request.operation().toUpperCase())
        );

        Product product = updateStockUseCase.execute(command);
        return ResponseEntity.ok(ProductResponse.from(product));
    }
}
