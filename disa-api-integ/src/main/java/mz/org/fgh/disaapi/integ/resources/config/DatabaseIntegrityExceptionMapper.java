package mz.org.fgh.disaapi.integ.resources.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DatabaseIntegrityExceptionMapper implements ExceptionMapper<DataIntegrityViolationException> {

	@Override
	public Response toResponse(DataIntegrityViolationException exception) {
		String customMessage = "Erro de integridade de dados.";

		// Verifica se a causa contém a constraint específica
		Throwable cause = exception.getCause();
		if (cause != null && cause.getMessage() != null) {
			customMessage = "Já existe um lab result com este RequestID e Tipo de Resultado.";
		}

		// Cria uma resposta com status 409 (CONFLICT)
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("erro", customMessage);

		return Response.status(Response.Status.CONFLICT)
				.entity(errorResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
