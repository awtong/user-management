package awt.user;

import org.junit.Test;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.*;

public class UserTest {
    // @Rule
    public SingleInstancePostgresRule database = EmbeddedPostgresRules.singleInstance();

    // @Rule
    public PreparedDbRule migrator = EmbeddedPostgresRules
	    .preparedDatabase(FlywayPreparer.forClasspathLocation("database-scripts"));

    @Test
    public void test() {

    }
}
