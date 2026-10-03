package ar.edu.unaj.hexagonal.infrastructure.config;

import ar.edu.unaj.hexagonal.application.usecase.CreateProductUseCaseImpl;
import ar.edu.unaj.hexagonal.application.usecase.GetProductUseCaseImpl;
import ar.edu.unaj.hexagonal.application.usecase.ListProductsUseCaseImpl;
import ar.edu.unaj.hexagonal.application.usecase.UpdateStockUseCaseImpl;
import ar.edu.unaj.hexagonal.domain.port.in.CreateProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.GetProductUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.ListProductsUseCase;
import ar.edu.unaj.hexagonal.domain.port.in.UpdateStockUseCase;
import ar.edu.unaj.hexagonal.domain.port.out.ProductRepositoryPort;
import ar.edu.unaj.hexagonal.domain.service.ProductDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Beans de Spring — el "pegamento" de la arquitectura.
 *
 * Esta clase es FUNDAMENTAL en la Arquitectura Hexagonal con Spring.
 * Aquí se conectan todas las piezas:
 * - Los casos de uso (Application) reciben los puertos de salida (Domain)
 * - Los puertos de salida son implementados por los adaptadores (Infrastructure)
 *
 * ¿Por qué no usar @Service en los casos de uso?
 * Si anotáramos CreateProductUseCaseImpl con @Service, Spring la detectaría
 * automáticamente, pero eso introduciría una dependencia de Spring en la
 * capa de aplicación. Al configurar los beans aquí (en infraestructura),
 * mantenemos la capa de aplicación libre de frameworks.
 *
 * Flujo de dependencias (Dependency Inversion Principle):
 *
 *   [REST Controller]
 *         ↓ usa
 *   [CreateProductUseCase] (interfaz del dominio)
 *         ↑ implementado por
 *   [CreateProductUseCaseImpl] (capa de aplicación)
 *         ↓ usa
 *   [ProductRepositoryPort] (interfaz del dominio)
 *         ↑ implementado por
 *   [ProductPersistenceAdapter] (capa de infraestructura)
 *         ↓ usa
 *   [ProductJpaRepository] (Spring Data JPA)
 *         ↓ usa
 *   [Base de datos H2/PostgreSQL]
 *
 * El dominio está en el centro y NO depende de nada externo.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Servicio de dominio — lógica de negocio transversal.
     */
    @Bean
    public ProductDomainService productDomainService(ProductRepositoryPort productRepository) {
        return new ProductDomainService(productRepository);
    }

    /**
     * Caso de uso: Crear Producto.
     * Recibe el puerto de salida (implementado por ProductPersistenceAdapter).
     */
    @Bean
    public CreateProductUseCase createProductUseCase(
            ProductRepositoryPort productRepository,
            ProductDomainService productDomainService) {
        return new CreateProductUseCaseImpl(productRepository, productDomainService);
    }

    /**
     * Caso de uso: Obtener Producto por ID.
     */
    @Bean
    public GetProductUseCase getProductUseCase(ProductRepositoryPort productRepository) {
        return new GetProductUseCaseImpl(productRepository);
    }

    /**
     * Caso de uso: Listar todos los Productos.
     */
    @Bean
    public ListProductsUseCase listProductsUseCase(ProductRepositoryPort productRepository) {
        return new ListProductsUseCaseImpl(productRepository);
    }

    /**
     * Caso de uso: Actualizar Stock.
     */
    @Bean
    public UpdateStockUseCase updateStockUseCase(ProductRepositoryPort productRepository) {
        return new UpdateStockUseCaseImpl(productRepository);
    }
}
