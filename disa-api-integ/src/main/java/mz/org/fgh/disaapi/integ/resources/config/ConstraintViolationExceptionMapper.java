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

	/*@Override
	public Response toResponse(ConstraintViolationException exception) {
        List<ApiError.FieldError> fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String field = extractField(violation.getPropertyPath());
                    return new ApiError.FieldError(field, violation.getMessage());
                })
                .collect(Collectors.toList());

            ApiError apiError = new ApiError("Erro de validação nos dados fornecidos",fieldErrors);

            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(apiError)
                .type(MediaType.APPLICATION_JSON)
                .build();
	}
	
	private String extractField(Path path) {
        // Pega o último elemento do path que representa o nome do campo
        Path.Node last = null;
        for (Path.Node node : path) {
            last = node;
        }
        return last != null ? last.toString() : "";
    }*/

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
	
	/*private String extractFieldName(String propertyPath) {
        if (propertyPath.contains(".")) {
            return propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
        }
        return propertyPath;
    }*/
}
