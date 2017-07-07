package awt.error.handler;

import java.lang.invoke.MethodHandles;

import javax.validation.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import com.google.common.collect.Iterables;

import awt.error.*;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
	LOGGER.error("Validation Failed.", exception);
	final ErrorMessages errors = new ErrorMessages();
	exception.getConstraintViolations().forEach(violation -> {
	    final ErrorMessage error = new ErrorMessage();
	    error.setMessage(violation.getMessage());
	    final Path.Node field = Iterables.getLast(violation.getPropertyPath());
	    error.setField(field.getName());
	    errors.addError(error);
	});

	return Response.status(Status.BAD_REQUEST).entity(errors).build();
    }

}
