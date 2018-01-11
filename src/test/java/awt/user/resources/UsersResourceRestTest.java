package awt.user.resources;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.*;
import org.junit.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

import awt.config.ApplicationConfig;
import awt.jaxrs.diagnostics.error.ErrorMessages;
import awt.rule.DatabaseRule;
import awt.user.*;
import awt.user.database.UserDatabaseService;

public class UsersResourceRestTest extends JerseyTest {
    @ClassRule
    public static final DatabaseRule DATABASE = new DatabaseRule.Builder().host("localhost").port(8176).dbName("users")
    .username("root").password("password1").build();

    @BeforeClass
    public static void setUpClass() throws IOException {
	// Optionally remove existing handlers attached to j.u.l root logger
	SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

	// add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
	// the initialization phase of your application
	SLF4JBridgeHandler.install();
    }

    @Override
    protected Application configure() {
	this.forceEnable(TestProperties.LOG_TRAFFIC);
	this.forceEnable(TestProperties.DUMP_ENTITY);
	final Application config = new ApplicationConfig();
	return new ResourceConfig() {
	    {
		this.register(new UserServiceBinder());
		for (final Class<?> clazz : config.getClasses()) {
		    this.register(clazz);
		}
	    }
	};
    }

    @Test
    public void testCreateUser() {
	final User user = new User();
	user.setUsername("at");
	user.setFirstName("A");
	user.setLastName("T");
	user.setEmail("a@t.com");
	user.setPassword("foobar");
	final Response response = this.target("users").request().post(Entity.json(user));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

	final String location = response.getHeaderString("Location");
	assertThat(location, is(notNullValue()));
    }

    @Test
    public void testCreateUserFailedValidation() {
	final User user = new User();

	final Response response = this.target("users").request().post(Entity.json(user));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
	final ErrorMessages errors = response.readEntity(ErrorMessages.class);
	assertThat(errors, is(notNullValue()));
    }

    @Test
    public void testUpdateUser() {
	final UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");

	final User user = new User();
	user.setId(id);
	user.setUsername("at");
	user.setFirstName("A");
	user.setLastName("T");
	user.setEmail("a@t.com");
	user.setPassword("password1");

	final Response response = this.target("users").path(id.toString()).request().put(Entity.json(user));

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

	final User updated = response.readEntity(User.class);
	assertThat(updated, is(notNullValue()));
	assertThat(updated.getId(), is(id));
	assertThat(updated.getUsername(), is("at"));
	assertThat(updated.getFirstName(), is("A"));
	assertThat(updated.getLastName(), is("T"));
	assertThat(updated.getEmail(), is("a@t.com"));
	assertThat(updated.getPassword(), is(nullValue()));
    }

    @Test
    public void testUpdateUserFailedValidation() {
	final UUID id = UUID.randomUUID();
	final User user = new User();

	final Response response = this.target("users").path(id.toString()).request().put(Entity.json(user));

	assertThat(response, is(notNullValue()));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
	final ErrorMessages errors = response.readEntity(ErrorMessages.class);
	assertThat(errors, is(notNullValue()));
    }

    @Test
    public void testDeleteUser() {
	final UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
	final Response response = this.target("users").path(id.toString()).request().delete();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void testGetUsers() {
	final Response response = this.target("users").request().header("Authorization", "Basic QW5keTpUb25n").get();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

	final List<User> users = response.readEntity(new GenericType<List<User>>() {
	});
	assertThat(users, is(notNullValue()));
	assertThat(users.isEmpty(), is(Boolean.FALSE));
    }

    @Test
    public void testGetUser() {
	final UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
	final Response response = this.target("users").path(id.toString()).request().get();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testGetUserNotFound() {
	final UUID id = UUID.fromString("aaaaaaaa-1111-1111-1111-111111111111");
	final Response response = this.target("users").path(id.toString()).request().get();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    public static class UserServiceBinder extends AbstractBinder {

	@Override
	protected void configure() {
	    this.bind(UserDatabaseService.class).to(UserService.class);
	}

    }
}