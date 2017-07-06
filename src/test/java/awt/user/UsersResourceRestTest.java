package awt.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import org.glassfish.jersey.test.*;
import org.junit.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

import awt.config.ApplicationConfig;
import awt.util.ErrorMessage;

public class UsersResourceRestTest extends JerseyTest {

    @BeforeClass
    public static void setUpClass() {
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
	return new ApplicationConfig();
    }

    @Test
    public void testCreateUser() {
	final User user = new User();
	user.setUid("at");
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
	final Collection<ErrorMessage> errors = response.readEntity(new GenericType<Collection<ErrorMessage>>() {
	});
	assertThat(errors, is(notNullValue()));
    }

    @Test
    public void testUpdateUser() {
	final UUID id = UUID.randomUUID();

	final User user = new User();
	user.setId(id);
	user.setUid("at");
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
	assertThat(updated.getUid(), is("at"));
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
	final Collection<ErrorMessage> errors = response.readEntity(new GenericType<Collection<ErrorMessage>>() {
	});
	assertThat(errors, is(notNullValue()));
    }

    @Test
    public void testDeleteUser() {
	final Response response = this.target("users").path(UUID.randomUUID().toString()).request().delete();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void testRetrieveUser() {
	final Response response = this.target("users").path(UUID.randomUUID().toString()).request().get();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}