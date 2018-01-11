package awt.config;

import java.util.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import awt.jaxrs.deprecated.DeprecatedFeature;
import awt.jaxrs.diagnostics.exception.mapper.*;
import awt.jaxrs.diagnostics.headers.HeaderPropagationFeature;
import awt.jaxrs.diagnostics.logging.TrafficLoggingFeature;
import awt.jaxrs.diagnostics.mdc.MDCFeature;
import awt.jaxrs.healthcheck.HealthCheckResource;
import awt.user.resources.UsersResource;
import io.swagger.jaxrs.listing.*;

@ApplicationPath("/")
public final class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
	final Set<Class<?>> classes = new HashSet<>();
	// endpoints
	classes.add(UsersResource.class);
	classes.add(HealthCheckResource.class);

	// exception mappers
	classes.add(ConstraintViolationExceptionMapper.class);
	classes.add(ThrowableMapper.class);

	// filter/features
	classes.add(MDCFeature.class);
	classes.add(HeaderPropagationFeature.class);
	classes.add(TrafficLoggingFeature.class);
	classes.add(DeprecatedFeature.class);

	classes.add(ObjectMapperProvider.class);

	// swagger
	classes.add(ApiListingResource.class);
	classes.add(SwaggerSerializers.class);
	return classes;
    }
}
