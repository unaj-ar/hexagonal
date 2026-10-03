package ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de Entrada (Request): Crear Producto.
 *
 * Los DTOs (Data Transfer Objects) son objetos de la capa de INFRAESTRUCTURA.
 * Representan el contrato de la API REST — lo que el cliente envía.
 *
 * Diferencias con el Command del dominio:
 * - El DTO tiene anotaciones de validación de Jakarta (@NotBlank, @Min)
 * - El DTO puede tener campos adicionales específicos de la API
 * - El adaptador REST convierte el DTO al Command del dominio
 *
 * Usar records de Java 21 para DTOs es una práctica moderna y concisa.
 */
public record CreateProductRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
        BigDecimal price,

        @Min(value = 0, message = "El stock inicial no puede ser negativo")
        int initialStock
) {}
