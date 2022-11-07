package ch03.exceptionz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

@SpringBootTest(classes = DaoFactory.class)
class UserDaoTest {
	@Autowired
	private UserDao dao;
	private User userA;  // fixture
	private User userB;

	@BeforeEach
	void beforeEach() {
		userA = new User("sally", "sallysh", "study");
		userB = new User("sally2", "sallysh2", "study2");
	}

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount()).isZero();

		dao.add(userA);
		dao.add(userB);
		assertThat(dao.getCount()).isGreaterThan(1);

		User actualA = dao.get(userB.getId());
		assertThat(actualA.getName()).isEqualTo(actualA.getName());
		assertThat(actualA.getPassword()).isEqualTo(actualA.getPassword());

		User actualB = dao.get(userB.getId());
		assertThat(actualB.getName()).isEqualTo(actualB.getName());
		assertThat(actualB.getPassword()).isEqualTo(actualB.getPassword());
	}

	@Test
	void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount()).isZero();

		assertThatThrownBy(() -> dao.get("unknownId"))
			.isInstanceOf(EmptyResultDataAccessException.class);
	}
}
