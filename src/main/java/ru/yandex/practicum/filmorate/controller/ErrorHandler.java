package ru.yandex.practicum.filmorate.controller;

import exception.ConditionsNotMetException;
import exception.DuplicateDataException;
import exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateDataException(final DuplicateDataException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleConditionsNotMetException(final ConditionsNotMetException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValidException(final MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getFieldErrors();
        StringBuffer sb = new StringBuffer();
        errors.forEach(fieldError -> {
            if (!sb.isEmpty()) {
                sb.append("; ");
            }

            sb.append(String.format("%s - %s", fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return new ErrorResponse(String.format("Ошибка(-и) валидации: %s", sb));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowableException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
