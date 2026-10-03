package ar.edu.unaj.hexagonal.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Entrada: Actualizar Stock.
 */
public record UpdateStockRequest(

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int quantity,

        @NotNull(message = "La operación es obligatoria")
        String operation  // "INCREASE" o "DECREASE"
) {}
