package mz.org.fgh.disaapi.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> handleValidationException(ValidationException ex) {
		System.out.println("Are we here...");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); 
	}
}
