package com.example.sales.shared.interfaces.rest.resources;

public record MessageResource(
    String timestamp,
    int status,
    String error,
    String code,
    String message,
    String path) {
}