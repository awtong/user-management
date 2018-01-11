package awt.config;

import javax.ws.rs.ext.*;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
	OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
		.setSerializationInclusion(Include.NON_NULL)
		.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
	return OBJECT_MAPPER;
    }
}
