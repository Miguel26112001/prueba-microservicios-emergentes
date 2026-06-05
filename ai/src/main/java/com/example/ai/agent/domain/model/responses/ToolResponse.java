package com.example.ai.agent.domain.model.responses;

public record ToolResponse<T>(
    boolean success,
    String code,
    String message,
    T data
) {}