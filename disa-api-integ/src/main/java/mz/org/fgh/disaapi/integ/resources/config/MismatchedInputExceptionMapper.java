package mz.org.fgh.disaapi.integ.resources.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MismatchedInputExceptionMapper implements ExceptionMapper<MismatchedInputException> {

	@Override
	public Response toResponse(MismatchedInputException exception) {
		
		// Extract the field name causing the issue
		String fieldName = "unknown field";
		if (!exception.getPath().isEmpty()) {
            fieldName = exception.getPath().get(exception.getPath().size() - 1).getFieldName();
        }
		
		// Custom error message
        String errorMessage = String.format(
                "Valor inválido para o campo '%s'. O campo não pode estar vazio e deve conter um valor válido.", 
                fieldName
        );
        
        // Prepare JSON response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", errorMessage);
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
	}
}
