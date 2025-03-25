package mz.org.fgh.disaapi.integ.resources.config;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		System.out.println("Estamos a entrar aqui?");
		
		Map<String, String> errors = new HashMap<>();
		
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			//String propertyPath = extractFieldName(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.put("erro", message);
		}
		return Response.status(Status.BAD_REQUEST)
				.entity(errors)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
