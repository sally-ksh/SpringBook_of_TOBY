package ch05.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest(classes = MyDaoFactory.class)
class UserDaoTest {
	@Autowired
	private UserDao userDao;
	private User userA;  // fixture
	private User userB;
	private User userC;

	@BeforeEach
	void beforeEach() {
		userA = new User("sally", "sallysh", "study", Level.BASIC, 1, 0);
		userB = new User("ho", "이름2", "springno2", Level.SILVER, 55, 10);
		userC = new User("toby", "tobyVol1", "springno1", Level.GOLD, 100, 40);
	}

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		userDao.deleteAll();
		assertThat(userDao.getCount()).isZero();

		userDao.add(userA);
		userDao.add(userB);
		assertThat(userDao.getCount()).isGreaterThan(1);

		User actualA = userDao.get(userA.getId());
		checkSameUser(actualA, userA);

		User actualB = userDao.get(userB.getId());
		checkSameUser(actualB, userB);
	}

	@Test
	void update() {
		userDao.deleteAll();

		userDao.add(userA); // 수정 O
		userDao.add(userB); // 수정 X

		userA.update("레모나", "spring06", Level.GOLD, 1000, 999);

		userDao.update(userA);

		User actual = userDao.get(userA.getId());
		checkSameUser(actual, userA);
		User actualB = userDao.get(userB.getId());
		checkSameUser(actualB, userB);
	}

	private void checkSameUser(User actual, User expected) {
		assertThat(actual.getName()).isEqualTo(expected.getName());
		assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
		assertThat(actual.getLevel()).isEqualTo(expected.getLevel());
		assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
		assertThat(actual.getRecommend()).isEqualTo(expected.getRecommend());
	}

}
