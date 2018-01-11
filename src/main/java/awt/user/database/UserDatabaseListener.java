package awt.user.database;

import java.time.*;
import java.util.UUID;

import awt.user.User;

public class UserDatabaseListener {
    protected void beforeInsert(final User user) {
	if (user.getId() == null) {
	    user.setId(UUID.randomUUID());
	}

	user.setCreationDate(OffsetDateTime.now(ZoneOffset.UTC));
	this.beforeUpdate(user);
    }

    protected void beforeUpdate(final User user) {
	user.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
