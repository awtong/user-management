package awt.diagnostics;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.ws.rs.container.*;

import org.slf4j.*;

public class DiagnosticsFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void filter(final ContainerRequestContext request) throws IOException {
	LOGGER.debug("Entering: [{}] {}", request.getMethod(), request.getUriInfo().getAbsolutePath());
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
	    throws IOException {
	LOGGER.debug("Exiting: [{}] {}", request.getMethod(), request.getUriInfo().getAbsolutePath());
    }
}