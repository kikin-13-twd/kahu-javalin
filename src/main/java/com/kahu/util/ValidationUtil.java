package com.kahu.util;

import com.kahu.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.stream.Collectors;

public final class ValidationUtil {

    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtil() {}

    public static <T> void validate(T dto) {
        var violations = VALIDATOR.validate(dto);
        if (!violations.isEmpty()) {
            String mensaje = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new BusinessException(mensaje);
        }
    }
}
