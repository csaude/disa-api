package mz.org.fgh.disaapi.integ.resources.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

	@Override
	public Response toResponse(JsonMappingException exception) {
		// Verifica se a exceção é do tipo InvalidFormatException
		if(exception instanceof InvalidFormatException) {
			InvalidFormatException ife = (InvalidFormatException) exception;
			
			// Se o tipo alvo for um enum, constrói uma mensagem customizada
			if (ife.getTargetType().isEnum()) {
				String validValues = Arrays.stream(ife.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
				
				// Extrai o nome do campo, se disponível
				String fieldName = "erro"; 
                if (!ife.getPath().isEmpty()) {
                    String tempFieldName = ife.getPath().get(0).getFieldName();
                    if (tempFieldName != null && !tempFieldName.trim().isEmpty()) {
                        fieldName = tempFieldName;
                    }
                }
                
                String message = String.format("Valor '%s' inválido. Valores aceitos: [%s]",
                        ife.getValue(), fieldName, validValues);
                
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put(fieldName, message);
                
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
			}
		}
		
		// Caso não seja um InvalidFormatException ou não seja um enum, retorna a mensagem padrão
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
	}
}
