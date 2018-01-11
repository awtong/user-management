package awt.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public String marshal(final OffsetDateTime temporal) throws Exception {
	return temporal == null ? null : FORMATTER.format(temporal);
    }

    @Override
    public OffsetDateTime unmarshal(final String value) throws Exception {
	return value == null ? null : FORMATTER.parse(value, OffsetDateTime::from);
    }
}