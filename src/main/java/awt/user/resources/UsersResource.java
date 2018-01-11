package awt.user.resources;

import static java.net.HttpURLConnection.*;
import static javax.ws.rs.core.MediaType.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;

import javax.inject.Inject;
import javax.validation.*;
import javax.validation.groups.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

import awt.user.*;
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
    private static final String SWAGGER_MESSAGE_200 = "Retrieved successfully.";
    private static final String SWAGGER_MESSAGE_400 = "Invalid user object.";
    private static final String SWAGGER_MESSAGE_401 = "Invalid credentials or no credentials provided.";
    private static final String SWAGGER_MESSAGE_403 = "Credentials unauthorized to perform action.";

    private final UserService service;

    @Inject
    public UsersResource(final UserService service) {
	this.service = service;
    }

    /**
     * Retrieves a list of users.
     *
     * @return HTTP response object containing 200 status code with a list of users
     *         in the payload if successful; otherwise see Swagger documentation for
     *         status code when error occurs.
     */
    @ApiOperation(value = "Retrieves a list of users")
    @ApiResponses(value = {
	    @ApiResponse(code = HTTP_OK, message = SWAGGER_MESSAGE_200, response = User.class, responseContainer = "Set"),
	    @ApiResponse(code = HTTP_UNAUTHORIZED, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = HTTP_FORBIDDEN, message = SWAGGER_MESSAGE_403) })
    @GET
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response getUsers() {
	final Collection<User> users = this.service.getUsers();
	return Response.ok(new GenericEntity<Collection<User>>(users) {
	}).build();
    }

    /**
     * Retrieves a user based on the passed-in id.
     *
     * @param id
     *            unique user identifier.
     * @param request
     *            request object.
     * @return HTTP response object containing 200 status code with user in the
     *         payload if successful; otherwise see Swagger documentation for status
     *         code when error occurs.
     */
    @ApiOperation(value = "Retrieves a single user")
    @ApiResponses(value = { @ApiResponse(code = HTTP_OK, message = SWAGGER_MESSAGE_200, response = User.class),
	    @ApiResponse(code = HTTP_UNAUTHORIZED, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = HTTP_FORBIDDEN, message = SWAGGER_MESSAGE_403),
	    @ApiResponse(code = HTTP_NOT_FOUND, message = "User not found.") })
    @GET
    @Path("{id}")
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response getUserById(@PathParam("id") final UUID id, @Context final Request request) {
	final User user = this.service.getUser(id).orElseThrow(NotFoundException::new);
	final OffsetDateTime updateDate = user.getUpdateDate();
	if (updateDate == null) {
	    return Response.ok(user).build();
	}

	final Date lastModified = DateTimeUtil.from(updateDate);
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
    @ApiOperation(value = "Creates a user")
    @ApiResponses(value = {
	    @ApiResponse(code = HTTP_CREATED, message = "User created successfully.", responseHeaders = {
		    @ResponseHeader(name = "Location", description = "URL to access newly created user") }),
	    @ApiResponse(code = HTTP_BAD_REQUEST, message = SWAGGER_MESSAGE_400),
	    @ApiResponse(code = HTTP_UNAUTHORIZED, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = HTTP_FORBIDDEN, message = SWAGGER_MESSAGE_403) })
    @POST
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    public Response createUser(
	    @Valid @ConvertGroup(from = Default.class, to = ValidationGroup.Create.class) final User user,
	    @Context final UriInfo uriInfo) {
	final User created = this.service.create(user);

	final URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(created.getId());
	return Response.created(uri).lastModified(DateTimeUtil.from(created.getUpdateDate())).build();
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
    @ApiOperation(value = "Updates a user")
    @ApiResponses(value = { @ApiResponse(code = HTTP_OK, message = "User updated successfully.", response = User.class),
	    @ApiResponse(code = HTTP_BAD_REQUEST, message = SWAGGER_MESSAGE_400),
	    @ApiResponse(code = HTTP_UNAUTHORIZED, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = HTTP_FORBIDDEN, message = SWAGGER_MESSAGE_403) })
    @PUT
    @Path("{id}")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response updateUser(@PathParam("id") final UUID id,
	    @Valid @ConvertGroup(from = Default.class, to = ValidationGroup.Update.class) final User user) {
	this.service.getUser(id).orElseThrow(() -> new NotFoundException("Not Found"));
	user.setId(id);
	final User updated = this.service.update(user);

	return Response.ok(updated).lastModified(DateTimeUtil.from(updated.getUpdateDate())).build();
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
    @ApiResponses(value = { @ApiResponse(code = HTTP_NO_CONTENT, message = "User deleted successfully."),
	    @ApiResponse(code = HTTP_UNAUTHORIZED, message = SWAGGER_MESSAGE_401),
	    @ApiResponse(code = HTTP_FORBIDDEN, message = SWAGGER_MESSAGE_403) })
    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") final UUID id) {
	this.service.delete(id);

	return Response.noContent().build();
    }
}
