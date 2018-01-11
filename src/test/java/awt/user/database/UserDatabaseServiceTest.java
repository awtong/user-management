package awt.user.database;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.*;

import awt.rule.DatabaseRule;
import awt.user.*;

public class UserDatabaseServiceTest {
    private final UserService service = new UserDatabaseService();

    @ClassRule
    public static final DatabaseRule DATABASE = new DatabaseRule.Builder().host("localhost").port(8176).dbName("users")
    .username("root").password("password1").build();

    @Test
    public void testGetUser() {
	final UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
	final Optional<User> optional = this.service.getUser(id);
	assertThat(optional.isPresent(), is(Boolean.TRUE));
	optional.ifPresent(user -> {
	    assertThat(user, is(notNullValue()));
	    assertThat(user.getId(), is(id));
	    assertThat(user.getFirstName(), is("Andrew1"));
	    assertThat(user.getLastName(), is("Tong1"));
	    assertThat(user.getUsername(), is("awt1"));
	    assertThat(user.getEmail(), is("andrew1.tong1@gmail.com"));
	    assertThat(user.getCreationDate(), is(notNullValue()));
	    assertThat(user.getUpdateDate(), is(notNullValue()));
	});
    }

    @Test
    public void testCreateUser() {
	final UUID id = UUID.randomUUID();
	final User user = new User();
	user.setId(id);
	user.setFirstName("Andrew4");
	user.setLastName("Tong4");
	user.setUsername("awt4");
	user.setEmail("andrew4.tong4@gmail.com");
	this.service.create(user);

	final Optional<User> optional = this.service.getUser(id);
	assertThat(optional.isPresent(), is(Boolean.TRUE));
	optional.ifPresent(created -> {
	    assertThat(created.getId(), is(id));
	    assertThat(created.getFirstName(), is("Andrew4"));
	    assertThat(created.getLastName(), is("Tong4"));
	    assertThat(created.getUsername(), is("awt4"));
	    assertThat(created.getEmail(), is("andrew4.tong4@gmail.com"));
	    assertThat(created.getCreationDate(), is(notNullValue()));
	    assertThat(created.getUpdateDate(), is(notNullValue()));
	});
    }

    @Test
    public void testUpdateUser() {
	final UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");
	this.service.getUser(id).ifPresent(user -> {
	    user.setUsername("awt-new");
	    this.service.update(user);
	});

	final Optional<User> optional = this.service.getUser(id);
	assertThat(optional.isPresent(), is(Boolean.TRUE));
	optional.ifPresent(updated -> {
	    assertThat(updated, is(notNullValue()));
	    assertThat(updated.getId(), is(id));
	    assertThat(updated.getFirstName(), is("Andrew2"));
	    assertThat(updated.getLastName(), is("Tong2"));
	    assertThat(updated.getUsername(), is("awt-new"));
	    assertThat(updated.getEmail(), is("andrew2.tong2@gmail.com"));
	    assertThat(updated.getCreationDate(), is(notNullValue()));
	    assertThat(updated.getUpdateDate(), is(notNullValue()));
	});

    }

    @Test
    public void testDeleteUser() {
	final UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");
	final Optional<User> optional = this.service.getUser(id);
	assertThat(optional.isPresent(), is(Boolean.TRUE));
	optional.ifPresent(user -> {
	    this.service.delete(user.getId());
	});

	assertThat(this.service.getUser(id).isPresent(), is(Boolean.FALSE));
    }
}
