package awt.user.database;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.slf4j.*;

import awt.user.*;

public class UserDatabaseService implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDatabaseService.class);

    /**
     * Retrieves a single user.
     *
     * @param id
     *            user's unique id.
     * @return user
     */
    @Override
    public Optional<User> getUser(final UUID id) {
	LOGGER.info("Attempting to retrieve user '{}'", id);

	try (final CloseableEntityManager mgr = EMF.createEntityManager()) {
	    final User user = mgr.find(User.class, id);
	    if (user == null) {
		LOGGER.info("User '{}' not found.", id);
	    } else {
		LOGGER.info("User '{}' successfully retrieved with the following information '{}'", id, user);
	    }

	    return Optional.ofNullable(user);
	}
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of users. This should never return <code>null</code> because
     *         if no users are found, an empty list is returned.
     */
    @Override
    public Collection<User> getUsers() {
	LOGGER.info("Attempting to retrieve users");
	try (final CloseableEntityManager mgr = EMF.createEntityManager()) {
	    final TypedQuery<User> query = mgr.createNamedQuery("User.findAll", User.class);
	    final List<User> users = query.getResultList();
	    return users == null ? Collections.emptyList() : users;
	}
    }

    /**
     * Creates a user.
     */
    @Override
    public User create(final User user) {
	Objects.requireNonNull(user);

	LOGGER.info("Attempting to create user with the following information '{}'", user);

	try (final CloseableEntityManager mgr = EMF.createEntityManager()) {
	    final EntityTransaction transaction = mgr.getTransaction();
	    transaction.begin();
	    try {
		mgr.persist(user);
		transaction.commit();
		LOGGER.info("User '{}' successfully created", user.getId());
	    } catch (final Exception exception) {
		LOGGER.error("Error creating user", exception);
		transaction.rollback();
	    }
	}

	return user;
    }

    /**
     * Updates a user.
     */
    @Override
    public User update(final User user) {
	Objects.requireNonNull(user);
	final UUID id = user.getId();
	Objects.requireNonNull(id);

	LOGGER.info("Attempting to update user '{}' with the following information '{}'", id, user);

	User updated = null;
	try (final CloseableEntityManager mgr = EMF.createEntityManager()) {
	    final EntityTransaction transaction = mgr.getTransaction();
	    transaction.begin();
	    try {
		updated = mgr.merge(user);
		transaction.commit();
		LOGGER.info("User '{}' successfully updated", id);
	    } catch (final Exception exception) {
		LOGGER.error("Error updating user '{}'", id, exception);
		transaction.rollback();
	    }
	}

	return updated;
    }

    @Override
    public void delete(final UUID id) {
	if (id == null) {
	    LOGGER.info("No id provided. Skipping deletion attempt.");
	    return;
	}

	final Collection<UUID> ids = new ArrayList<>();
	ids.add(id);
	this.delete(ids);
    }

    /**
     * Bulk deletes users.
     */
    @Override
    public void delete(final Collection<UUID> ids) {
	if ((ids == null) || ids.isEmpty()) {
	    LOGGER.info("No ids provided. Skipping deletion attempt.");
	    return;
	}

	try (final CloseableEntityManager mgr = EMF.createEntityManager()) {
	    LOGGER.info("Attempting to delete users '{}'", ids);
	    final EntityTransaction transaction = mgr.getTransaction();
	    transaction.begin();
	    try {
		final CriteriaBuilder cb = mgr.getCriteriaBuilder();
		final CriteriaDelete<User> query = cb.createCriteriaDelete(User.class);
		final Root<User> from = query.from(User.class);

		query.where(from.get("id").in(ids));
		mgr.createQuery(query).executeUpdate();
		transaction.commit();
		LOGGER.info("Users '{}' successfully deleted", ids);
	    } catch (final Exception exception) {
		LOGGER.error("Error deleting users", exception);
		transaction.rollback();
	    }
	}
    }
}
