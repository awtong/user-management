package awt.user;

import static javax.ws.rs.core.MediaType.*;

import java.net.URI;
import java.time.*;
import java.util.*;

import javax.validation.*;
import javax.validation.groups.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;

import awt.util.*;
import io.swagger.annotations.*;

/**
 * Collection of user-related endpoints.
 *
 * @author awt
 */
@Api(value = "users")
@Path("users/")
public class UsersResource {
    private static final String SWAGGER_MESSAGE_401 = "Invalid credentials or no credentials provided.";
    private static final String SWAGGER_MESSAGE_403 = "Credentials unauthorized to perform action.";

    /**
     * Retrieves a list of users.
     *
     * @return HTTP response object containing 200 status code with a list of users
     *         in the payload if successful; otherwise see Swagger documentation for
     *         status code when error occurs.
     */
    @ApiOperation(value = "Retrieves a list of users", response = User.class, responseContainer = "Set")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Users retrieved successfully."),
	    @ApiResponse(code = 401, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = 403, message = SWAGGER_MESSAGE_403) })
    @GET
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    public Response getUsers() {
	final User user1 = new User();
	user1.setFirstName("A1");
	user1.setLastName("T1");
	user1.setCreationDate(OffsetDateTime.now(ZoneOffset.UTC));

	final User user2 = new User();
	user2.setFirstName("A2");
	user2.setLastName("T2");

	return Response.ok(new User[] { user1, user2 }).build();
    }

    /**
     * Retrieves a user based on the passed-in id.
     *
     * @param id
     *            unique user identifier.
     * @return HTTP response object containing 200 status code with user in the
     *         payload if successful; otherwise see Swagger documentation for status
     *         code when error occurs.
     */
    @ApiOperation(value = "Retrieves a single user", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User retrieved successfully."),
	    @ApiResponse(code = 401, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = 403, message = SWAGGER_MESSAGE_403),
	    @ApiResponse(code = 404, message = "User not found.") })
    @GET
    @Path("{id}")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    public Response getUserById(@PathParam("id") final UUID id, @Context final Request request) {
	final User user = User.getUser(id);
	if (user == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}

	final Date lastModified = DateTimeUtil.from(user.getUpdateDate());
	if (lastModified == null) {
	    return Response.ok(user).build();
	}

	final ResponseBuilder temp = request.evaluatePreconditions(lastModified);
	final ResponseBuilder builder = temp == null ? Response.ok(user) : temp;
	return builder.lastModified(lastModified).build();
    }

    /**
     * Creates a user.
     *
     * @param user
     *            user to be created
     * @param uriInfo
     *            injected object containing uri information.
     * @return HTTP response object containing 201 status code with the Location
     *         response header set if successful; otherwise see Swagger
     *         documentation for status code when error occurs.
     * @throws ConstraintViolationException
     *             if passed-in user object is invalid.
     */
    @ApiOperation(value = "Creates a user", responseHeaders = {
	    @ResponseHeader(name = "Location", description = "URL from where the newly created user information can be retrieved.") })
    @ApiResponses(value = { @ApiResponse(code = 201, message = "User created successfully."),
	    @ApiResponse(code = 400, message = "Invalid user object."),
	    @ApiResponse(code = 401, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = 403, message = SWAGGER_MESSAGE_403) })
    @POST
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response createUser(
	    @Valid @ConvertGroup(from = Default.class, to = ValidationGroup.Create.class) final User user,
	    @Context final UriInfo uriInfo) {
	user.create();

	final URI created = uriInfo.getAbsolutePathBuilder().path("{id}").build(user.getId());
	return Response.created(created).lastModified(DateTimeUtil.from(user.getUpdateDate())).build();
    }

    /**
     * Updates a user.
     *
     * @param id
     *            unique user identifier
     * @param user
     *            user to be created
     * @return HTTP response object containing 200 status code with user in the
     *         payload if successful; otherwise see Swagger documentation for status
     *         code when error occurs.
     * @throws ConstraintViolationException
     *             if passed-in user object is invalid.
     */
    @ApiOperation(value = "Updates a user", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User updated successfully."),
	    @ApiResponse(code = 400, message = "Invalid user object."),
	    @ApiResponse(code = 401, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = 403, message = SWAGGER_MESSAGE_403) })
    @PUT
    @Path("{id}")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response updateUser(@PathParam("id") final UUID id,
	    @Valid @ConvertGroup(from = Default.class, to = ValidationGroup.Update.class) final User user) {
	user.setId(id);
	user.update();

	return Response.ok(user).lastModified(DateTimeUtil.from(user.getUpdateDate())).build();
    }

    /**
     * Deletes a user
     *
     * @param id
     *            unique user identifier
     * @return HTTP response object containing 204 status code if successful;
     *         otherwise see Swagger documentation for status code when error
     *         occurs.
     */
    @ApiOperation(value = "Deletes a user")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "User deleted successfully."),
	    @ApiResponse(code = 401, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = 403, message = SWAGGER_MESSAGE_403) })
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") final UUID id) {
	final User user = new User();
	user.setId(id);
	user.delete();

	return Response.noContent().build();
    }
}
