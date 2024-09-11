package mz.org.fgh.disaapi.integ.resources.config;

import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessages(exception))
                .build();
    }

    private MultivaluedMap<String, String> prepareMessages(ConstraintViolationException exception) {
        MultivaluedMap<String, String> errors = new MultivaluedHashMap<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            List<Node> nodes = toList(cv.getPropertyPath().iterator());
            errors.add(nodes.get(1).toString(), cv.getMessage());
        }
        return errors;
    }

    private List<Node> toList(Iterator<Node> iterator) {
        return StreamSupport.stream(((Iterable<Node>) () -> iterator).spliterator(), false).toList();
    }
}
