package awt.util;

import java.util.*;

import javax.validation.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.*;

import com.google.common.collect.Iterables;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
	final Collection<ErrorMessage> errors = new HashSet<>();
	exception.getConstraintViolations().forEach(violation -> {
	    final ErrorMessage error = new ErrorMessage();
	    error.setMessage(violation.getMessage());
	    final Path.Node field = Iterables.getLast(violation.getPropertyPath());
	    error.setField(field.getName());
	    errors.add(error);
	});

	return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<Collection<ErrorMessage>>(errors) {
	}).build();
    }

}
