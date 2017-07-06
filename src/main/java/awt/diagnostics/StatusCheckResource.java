package awt.diagnostics;

import static javax.ws.rs.core.MediaType.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

/**
 * Collection of internal health monitoring endpoints.
 *
 * @author awt
 */
@Api(value = "status")
@Path("status/")
public class StatusCheckResource {

    /**
     * Performs a status check to verify server is up.
     *
     * @return HTTP response object containing 200 status code with no payload.
     */
    @ApiOperation(value = "Checks to make sure server can be reached")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Server reached successfully.") })
    @GET
    public Response performSimpleHealthCheck() {
	return Response.ok().build();
    }

    /**
     * Performs a status check on all internal services (ex: database) are up.
     *
     * @return HTTP response object containing 200 status code with detailed health
     *         check status.
     */
    @ApiOperation(value = "Checks to make sure application services can be reached")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "All services reached successfully."),
	    @ApiResponse(code = 400, message = "Not all services reached successfully.") })
    @GET
    @Path("detailed/")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    public Response performDetailedHealthCheck() {
	return Response.ok().build();
    }
}
