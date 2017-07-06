package awt.util;

import java.time.OffsetDateTime;
import java.util.Date;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    public static Date from(final OffsetDateTime datetime) {
	return datetime == null ? null : Date.from(datetime.toInstant());
    }
}
