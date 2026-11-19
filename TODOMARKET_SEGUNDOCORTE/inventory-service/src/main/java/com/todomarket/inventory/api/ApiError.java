package com.todomarket.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ApiError", description = "Formato de error devuelto por la API.")
public record ApiError(
        @Schema(description = "Detalle del error", example = "Producto no encontrado.") String message,
        @Schema(description = "Fecha y hora del error", example = "2026-11-19T10:15:30") LocalDateTime timestamp) { }
