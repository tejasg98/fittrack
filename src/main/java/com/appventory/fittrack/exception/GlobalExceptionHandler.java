package com.appventory.fittrack.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR = "error";

	// Handle validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}

	// Handle resource not found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR, ex.getMessage()));
	}

	// Handle Access Denied Exception 
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

	// Handle duplicate resource
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateResource(DuplicateResourceException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ERROR, ex.getMessage()));
	}
	
	// Handle Invalid Credentials Exception 
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	// Catch-all for other exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(ERROR, ex.getMessage()));
	}

}
