package awt.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@Converter(autoApply = true)
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime>
implements AttributeConverter<OffsetDateTime, Date> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public Date convertToDatabaseColumn(final OffsetDateTime temporal) {
	final Instant instant = Instant.from(temporal);
	return Date.from(instant);
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(final Date temporal) {
	final Instant instant = temporal.toInstant();
	return OffsetDateTime.from(instant);
    }

    @Override
    public String marshal(final OffsetDateTime temporal) throws Exception {
	return temporal == null ? null : FORMATTER.format(temporal);
    }

    @Override
    public OffsetDateTime unmarshal(final String value) throws Exception {
	return value == null ? null : FORMATTER.parse(value, OffsetDateTime::from);
    }
}