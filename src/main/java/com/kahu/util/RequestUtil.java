package com.kahu.util;

import com.kahu.exception.BusinessException;
import io.javalin.http.Context;

public final class RequestUtil {

    private RequestUtil() {}

    public static <T> T bodyAsValidated(Context ctx, Class<T> clazz) {
        T dto = ctx.bodyAsClass(clazz);
        ValidationUtil.validate(dto);
        return dto;
    }

    public static int pathParamAsInt(Context ctx, String name) {
        try {
            return Integer.parseInt(ctx.pathParam(name));
        } catch (NumberFormatException e) {
            throw new BusinessException("Parametro '" + name + "' debe ser un numero entero valido");
        }
    }

    public static String requireQueryParam(Context ctx, String name) {
        String value = ctx.queryParam(name);
        if (value == null || value.isBlank()) {
            throw new BusinessException("El parametro '" + name + "' es obligatorio");
        }
        return value;
    }
}
