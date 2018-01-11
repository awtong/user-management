package awt.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import awt.user.resources.UsersResource;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest {

    @Mock
    private UserService service;

    @Mock
    private Request request;

    @Mock
    private UriInfo uriInfo;

    @Test
    public void testGetByUidNoUpdateDate() {
	final User user = new User();

	when(this.service.getUser(any(UUID.class))).thenReturn(Optional.of(user));

	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.getUserById(UUID.randomUUID(), this.request);

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test
    public void testGetByUidCaching() {
	final User user = new User();
	user.setUpdateDate(OffsetDateTime.now());

	when(this.service.getUser(any())).thenReturn(Optional.of(user));
	when(this.request.evaluatePreconditions(any(Date.class))).thenReturn(Response.notModified());

	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.getUserById(UUID.randomUUID(), this.request);

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.NOT_MODIFIED.getStatusCode()));
    }

    @Test
    public void testGetByUidNoCaching() {
	final User user = new User();
	user.setUpdateDate(OffsetDateTime.now());

	when(this.service.getUser(any())).thenReturn(Optional.of(user));

	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.getUserById(UUID.randomUUID(), this.request);

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test
    public void testGetUsers() {
	when(this.service.getUsers()).thenReturn(Collections.emptyList());

	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.getUsers();

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test
    public void testDeleteUser() {
	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.deleteUser(UUID.randomUUID());

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void testUpdateUserNotFound() {
	final User user = new User();

	final UsersResource resource = new UsersResource(this.service);
	try {
	    resource.updateUser(UUID.randomUUID(), user);
	    fail("Should throw NotFoundException");
	} catch (final NotFoundException expected) {
	}
    }

    @Test
    public void testCreateUser() {
	final User user = new User();
	final User created = new User();
	final UUID id = UUID.randomUUID();
	created.setId(id);
	created.setUpdateDate(OffsetDateTime.now());
	final UriBuilder builder = mock(UriBuilder.class);

	when(this.service.create(user)).thenReturn(created);
	when(this.uriInfo.getAbsolutePathBuilder()).thenReturn(builder);
	when(builder.path(any(String.class))).thenReturn(builder);
	when(builder.build(any())).thenReturn(mock(URI.class));

	final UsersResource resource = new UsersResource(this.service);
	final Response response = resource.createUser(user, this.uriInfo);

	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.CREATED.getStatusCode()));
	final URI location = response.getLocation();
	final Date lastModified = response.getLastModified();
	assertThat(location, is(notNullValue()));
	assertThat(lastModified, is(notNullValue()));

    }
}
