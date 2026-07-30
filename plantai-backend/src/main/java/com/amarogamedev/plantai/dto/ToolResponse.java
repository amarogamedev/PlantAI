package com.amarogamedev.plantai.dto;

public record ToolResponse<T>(
        boolean success,
        String errorCode,
        String message,
        T data
) {

    public static <T> ToolResponse<T> success(T data) {
        return new ToolResponse<>(true, null, null, data);
    }

    public static <T> ToolResponse<T> error(String code, String message) {
        return new ToolResponse<>(false, code, message, null);
    }

}