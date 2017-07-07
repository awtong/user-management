package awt.diagnostics;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.*;

public class DiagnosticsFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String LOGGING_ID = "log-id";

    @Override
    public void filter(final ContainerRequestContext request) throws IOException {
	MDC.put(LOGGING_ID, UUID.randomUUID().toString());
	LOGGER.info("Entering: [{}] {}", request.getMethod(), request.getUriInfo().getAbsolutePath());
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
	    throws IOException {
	LOGGER.info("Exiting: [{}] {} with status code {}", request.getMethod(), request.getUriInfo().getAbsolutePath(),
		response.getStatus());
	LOGGER.debug("Request Headers: {}", this.formatHeaders(request.getHeaders()));
	LOGGER.debug("Response Headers: {}", this.formatHeaders(response.getHeaders()));
	MDC.remove(LOGGING_ID);
    }

    private String formatHeaders(final MultivaluedMap<String, ?> headers) {
	final StringJoiner joiner = new StringJoiner(", ");
	headers.forEach((key, values) -> {
	    joiner.add("[" + key + " = "
		    + values.stream().map(i -> i == null ? "" : i.toString()).collect(Collectors.joining(", "))
		    + "]");

	});

	return joiner.toString();
    }
}