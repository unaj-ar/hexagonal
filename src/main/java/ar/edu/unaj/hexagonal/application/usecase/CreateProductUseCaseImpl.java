package ar.edu.unaj.hexagonal.application.usecase;

import ar.edu.unaj.hexagonal.domain.model.Product;
import ar.edu.unaj.hexagonal.domain.model.ProductId;
import ar.edu.unaj.hexagonal.domain.port.in.CreateProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;
import ar.edu.unaj.hexagonal.domain.service.ProductDomainService;

/**
 * Implementación del Caso de Uso: Crear Producto.
 *
 * Esta clase vive en la capa de APLICACIÓN y actúa como orquestador:
 * 1. Valida las reglas de negocio (delegando al dominio)
 * 2. Crea la entidad de dominio
 * 3. Persiste a través del puerto de salida
 * 4. Retorna el resultado
 *
 * IMPORTANTE: Esta clase NO tiene anotaciones de Spring (@Service, @Component).
 * La configuración de beans se hace en la capa de infraestructura (BeanConfiguration).
 * Esto mantiene la capa de aplicación libre de dependencias de frameworks.
 *
 * Dependencias:
 * - ProductRepositoryPort: puerto de salida (definido en el dominio)
 * - ProductDomainService: servicio de dominio para validaciones complejas
 */
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final ProductDomainService productDomainService;

    public CreateProductUseCaseImpl(
            ProductRepositoryPort productRepository,
            ProductDomainService productDomainService) {
        this.productRepository = productRepository;
        this.productDomainService = productDomainService;
    }

    @Override
    public Product execute(Command command) {
        // 1. Validar regla de negocio: nombre único
        productDomainService.validateProductNameUniqueness(command.name());

        // 2. Crear la entidad de dominio (el dominio valida sus invariantes)
        Product product = new Product(
                ProductId.generate(),
                command.name(),
                command.description(),
                command.price(),
                command.initialStock()
        );

        // 3. Persistir a través del puerto de salida
        return productRepository.save(product);
    }
}
