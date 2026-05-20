package com.backend.unab.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.unab.dto.ApiErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorDto> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		LOGGER.warn("Resource not found for path {}: {}", request.getRequestURI(), ex.getMessage());
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorDto> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
		LOGGER.warn("Invalid request for path {}: {}", request.getRequestURI(), ex.getMessage());
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorDto> handleUnexpected(Exception ex, HttpServletRequest request) {
		LOGGER.error("Unexpected error for path {}", request.getRequestURI(), ex);
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
	}

	private ResponseEntity<ApiErrorDto> buildResponse(HttpStatus status, String message, String path) {
		ApiErrorDto error = new ApiErrorDto(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
		return ResponseEntity.status(status).body(error);
	}
}
