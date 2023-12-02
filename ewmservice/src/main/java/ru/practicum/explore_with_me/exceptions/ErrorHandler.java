package ru.practicum.explore_with_me.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ConflictException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "CONFLICT",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.NOT_FOUND.toString(),
                "NOT FOUND",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBlankException(final BadRequestException e) {
        log.error(e.getLocalizedMessage(), e.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST.toString(),
                "BAD REQUEST",
                e.getMessage());
    }
}