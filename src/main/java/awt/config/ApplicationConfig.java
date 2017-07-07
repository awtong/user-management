package awt.config;

import java.util.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import awt.diagnostics.*;
import awt.error.handler.*;
import awt.user.UsersResource;
import io.swagger.jaxrs.listing.*;

@ApplicationPath("/")
public final class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
	final Set<Class<?>> classes = new HashSet<>();
	// endpoints
	classes.add(UsersResource.class);
	classes.add(StatusCheckResource.class);

	// exception mappers
	classes.add(ConstraintViolationExceptionMapper.class);
	classes.add(ThrowableMapper.class);

	// filter/features
	classes.add(DiagnosticsFeature.class);

	// swagger
	classes.add(ApiListingResource.class);
	classes.add(SwaggerSerializers.class);
	return classes;
    }

}
