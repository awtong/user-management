package awt.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.ws.rs.core.Application;

import org.junit.Test;

import awt.user.*;

public class ApplicationConfigTest {

    @Test
    public void testClasses() {
	final Application app = new ApplicationConfig();
	final Set<Class<?>> classes = app.getClasses();

	assertThat(classes, is(notNullValue()));
	// assertThat(classes.size(), is(4));

	assertThat(classes.contains(UsersResource.class), is(true));
    }
}
