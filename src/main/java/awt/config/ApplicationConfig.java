package awt.config;

import java.util.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import awt.diagnostics.*;
import awt.user.UsersResource;
import awt.util.ConstraintViolationExceptionMapper;
import io.swagger.jaxrs.listing.*;

@ApplicationPath("/")
public final class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
	final Set<Class<?>> classes = new HashSet<>();
	// endpoints
	classes.add(UsersResource.class);
	classes.add(StatusCheckResource.class);

	// providers
	classes.add(ConstraintViolationExceptionMapper.class);
	classes.add(DiagnosticsFeature.class);

	// swagger
	classes.add(ApiListingResource.class);
	classes.add(SwaggerSerializers.class);
	return classes;
    }

}
