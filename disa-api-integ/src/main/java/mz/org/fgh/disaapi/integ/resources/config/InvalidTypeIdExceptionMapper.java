package mz.org.fgh.disaapi.integ.resources.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;

@Provider
public class InvalidTypeIdExceptionMapper implements ExceptionMapper<InvalidTypeIdException> {

    @Override
    public Response toResponse(InvalidTypeIdException exception) {
        // Retrieve the invalid type id if available
        String invalidTypeId = exception.getTypeId() != null 
                ? exception.getTypeId().toString() 
                : "unknown";
        
     // Extrai os tipos válidos do enum TypeOfResult
        String validTypes = Arrays.stream(TypeOfResult.values())
                                  .map(Enum::name)
                                  .collect(Collectors.joining(", ", "[", "]"));
        
        // Create a friendly error message
        String errorMessage = String.format("ID de tipo inválido '%s'. Os tipos válidos conhecidos são: %s", invalidTypeId, validTypes
        );

        // Prepare a JSON response body
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", errorMessage);

        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(errorResponse)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}

