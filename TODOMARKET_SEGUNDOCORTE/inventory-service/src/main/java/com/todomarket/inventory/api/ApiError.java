package com.todomarket.inventory.api;

import java.time.LocalDateTime;

public record ApiError(String message, LocalDateTime timestamp) { }
