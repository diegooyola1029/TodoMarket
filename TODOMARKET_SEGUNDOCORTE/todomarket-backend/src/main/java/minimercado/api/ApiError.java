package minimercado.api;

import java.time.LocalDateTime;

public record ApiError(String message, LocalDateTime timestamp) { }
