package awt.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.mockito.Mockito;

public class UsersResourceTest {

    @Test
    public void testGetByUidNoCaching() {
	final UsersResource resource = new UsersResource();
	final Request request = mock(Request.class);
	when(request.evaluatePreconditions(Mockito.any(Date.class))).thenReturn(null);
	final Response response = resource.getUserById(UUID.randomUUID(), request);
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test
    public void testGetByUidCaching() {
	final UsersResource resource = new UsersResource();
	final Request request = mock(Request.class);
	when(request.evaluatePreconditions(Mockito.any(Date.class))).thenReturn(Response.notModified());
	final Response response = resource.getUserById(UUID.randomUUID(), request);
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Status.NOT_MODIFIED.getStatusCode()));
    }
}
