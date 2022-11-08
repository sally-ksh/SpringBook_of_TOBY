package ch03.templatemethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import ch03.exceptionz.User;

@SpringBootTest(classes = DaoFactory.class)
class UserDaoTest {
	// @Autowired  // userDao 필드명 -> 타입이 2개 이상이라 자동주입 X
	@Autowired
	private UserDao userDaoDeleteAll;
	@Autowired
	private UserDao userDaoGet;
	@Autowired
	private UserDao userDaoAdd;
	@Autowired
	private UserDao userDaoGetCount;

	private User userA;  // fixture
	private User userB;

	@BeforeEach
	void beforeEach() {
		userA = new User("sally", "sallysh", "study");
		userB = new User("sally2", "sallysh2", "study2");
	}

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		userDaoDeleteAll.deleteAll();
		assertThat(userDaoGetCount.getCount()).isZero();

		userDaoAdd.add(userA);
		userDaoAdd.add(userB);
		assertThat(userDaoGetCount.getCount()).isGreaterThan(1);

		User actualA = userDaoGet.get(userB.getId());
		assertThat(actualA.getName()).isEqualTo(actualA.getName());
		assertThat(actualA.getPassword()).isEqualTo(actualA.getPassword());

		User actualB = userDaoGet.get(userB.getId());
		assertThat(actualB.getName()).isEqualTo(actualB.getName());
		assertThat(actualB.getPassword()).isEqualTo(actualB.getPassword());
	}

	@Test
	void getUserFailure() throws SQLException {
		userDaoDeleteAll.deleteAll();
		assertThat(userDaoGetCount.getCount()).isZero();

		assertThatThrownBy(() -> userDaoGet.get("unknownId"))
			.isInstanceOf(EmptyResultDataAccessException.class);
	}
}
