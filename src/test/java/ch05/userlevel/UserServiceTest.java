package ch05.userlevel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = DaoFactory.class)
class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;

	private List<User> users;

	@BeforeEach
	void beforeEach() {
		users = List.of(
			new User("Leem", "임화령", "1", Level.BASIC, 49, 0),
			new User("seja", "세자", "2", Level.BASIC, 50, 0),
			new User("sungnam", "성남대군", "3", Level.SILVER, 60, 29),
			new User("mooan", "무안대군", "4", Level.SILVER, 60, 30),
			new User("gyesung", "계성대군", "5", Level.GOLD, 100, 100)
			);
	}

	@Test
	void bean() {
		assertThat(this.userService).isNotNull();
	}

	@Test
	void updatedLevels() {
		userDao.deleteAll();
		users.forEach(userDao::add);

		userService.upgradeLevels();

		checkLevel(users.get(0), Level.BASIC);  // login 50미만 없그레이드 X
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER); // recommend 30 미만 업그레이드 X
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}

	@Test
	void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.upgrade(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User actualWithLevel = userDao.get(userWithLevel.getId());
		User actualWithoutLevel = userDao.get(userWithoutLevel.getId());

		assertThat(actualWithLevel.getLevel() == userWithLevel.getLevel()).isTrue();
		assertThat(actualWithoutLevel.getLevel() == Level.BASIC).isTrue();
	}

	private void checkLevel(User actual, Level expectedLevel) {
		User updatedUser = userDao.get(actual.getId());
		assertThat(updatedUser.getLevel()).isEqualTo(expectedLevel);
	}
}
