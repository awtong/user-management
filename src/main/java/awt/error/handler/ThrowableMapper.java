package awt.error.handler;

import java.lang.invoke.MethodHandles;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import awt.error.ErrorMessages;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(final Throwable exception) {
	LOGGER.error("Unexpected as it is handled by the catch-all exception mapper.", exception);

	final ErrorMessages errors = new ErrorMessages();
	return Response.status(Status.BAD_REQUEST).entity(errors).build();
    }
}
