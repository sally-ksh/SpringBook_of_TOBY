package ch03.jdbccontext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;

import ch03.exceptionz.User;

@SpringBootTest(classes = DaoFactory.class)
class UserDaoTest {
	@Autowired
	private UserDao userDao;
	private User userA;  // fixture
	private User userB;

	@BeforeEach
	void beforeEach() {
		userA = new User("sally", "sallysh", "study");
		userB = new User("sally2", "sallysh2", "study2");
	}

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		assertThat(userDao.getCount()).isZero();

		userDao.add(userA);
		userDao.add(userB);
		assertThat(userDao.getCount()).isGreaterThan(1);

		User actualA = userDao.get(userB.getId());
		assertThat(actualA.getName()).isEqualTo(actualA.getName());
		assertThat(actualA.getPassword()).isEqualTo(actualA.getPassword());

		User actualB = userDao.get(userB.getId());
		assertThat(actualB.getName()).isEqualTo(actualB.getName());
		assertThat(actualB.getPassword()).isEqualTo(actualB.getPassword());
	}

	@Test
	void getUserFailure() throws SQLException {
		userDao.deleteAll();
		assertThat(userDao.getCount()).isZero();

		assertThatThrownBy(() -> userDao.get("unknownId"))
			.isInstanceOf(EmptyResultDataAccessException.class);
	}

	@Test
	void getAll() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		assertThat(userDao.getCount()).isZero();

		userDao.add(userA);
		userDao.add(userB);

		List<User> users = userDao.getAll();

		assertThat(users).contains(userA, userB);
	}
}
