package awt.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.time.*;

import org.junit.Test;

public class DateTimeUtilTest {

    @Test
    public void testFrom() {
	assertThat(DateTimeUtil.from(null), is(nullValue()));
	assertThat(DateTimeUtil.from(OffsetDateTime.now(ZoneOffset.UTC)), is(notNullValue()));
    }
}
