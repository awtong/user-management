package awt.user;

import java.util.*;

public interface UserService {
    Optional<User> getUser(final UUID id);

    Collection<User> getUsers();

    User create(final User user);

    User update(final User user);

    void delete(final UUID id);

    void delete(final Collection<UUID> ids);
}
