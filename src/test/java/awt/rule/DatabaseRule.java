package awt.rule;

import org.flywaydb.core.Flyway;
import org.junit.rules.ExternalResource;
import org.slf4j.*;

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

public class DatabaseRule extends ExternalResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRule.class);
    private static final EmbeddedPostgres POSTGRES = new EmbeddedPostgres();

    private final String host;
    private final int port;
    private final String dbName;
    private final String username;
    private final String password;

    private DatabaseRule(final DatabaseRule.Builder builder) {
	this.host = builder.host;
	this.port = builder.port;
	this.dbName = builder.dbName;
	this.username = builder.username;
	this.password = builder.password;
    }

    @Override
    protected void after() {
	LOGGER.info("Shutting down embedded PostgreSQL.");
	POSTGRES.stop();
	LOGGER.info("Embedded PostgreSQL successfully shut down.");
    }

    @Override
    protected void before() throws Throwable {
	LOGGER.info("Starting embedded PostgreSQL database on port '{}'", this.port);
	POSTGRES.start(this.host, this.port, this.dbName, this.username, this.password);
	LOGGER.info("Successfully started embedded PostgreSQL database on port '{}'", this.port);
	final String url = POSTGRES.getConnectionUrl().get();
	final Flyway flyway = new Flyway();

	// Point it to the database
	flyway.setDataSource(url, "root", "password1");

	LOGGER.info("Starting database setup.");
	// Start the migration
	flyway.migrate();
	LOGGER.info("Successfully set up.");
    }

    public static class Builder {
	private String host;
	private int port;
	private String dbName;
	private String username;
	private String password;

	public Builder host(final String host) {
	    this.host = host;
	    return this;
	}

	public Builder port(final int port) {
	    this.port = port;
	    return this;
	}

	public Builder dbName(final String dbName) {
	    this.dbName = dbName;
	    return this;
	}

	public Builder username(final String username) {
	    this.username = username;
	    return this;
	}

	public Builder password(final String password) {
	    this.password = password;
	    return this;
	}

	public DatabaseRule build() {
	    return new DatabaseRule(this);
	}
    }

}
