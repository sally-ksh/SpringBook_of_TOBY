package ch05.userlevel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static ch05.userlevel.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static ch05.userlevel.UserService.MIN_RECOMMEND_FOR_GOLD;

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
			new User("Leem", "임화령", "1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0),
			new User("seja", "세자", "2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
			new User("sungnam", "성남대군", "3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
			new User("mooan", "무안대군", "4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("gyesung", "계성대군", "5", Level.GOLD, 100, Integer.MAX_VALUE)
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

		checkLevelUpgraded(users.get(0), false);  // login 50미만 없그레이드 X
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false); // recommend 30 미만 업그레이드 X
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdated = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isTrue();
		} else {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isFalse();
		}
	}

	@Test
	void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User actualWithLevel = userDao.get(userWithLevel.getId());
		User actualWithoutLevel = userDao.get(userWithoutLevel.getId());

		assertThat(actualWithLevel.getLevel() == userWithLevel.getLevel()).isTrue();
		assertThat(actualWithoutLevel.getLevel() == Level.BASIC).isTrue();
	}

	// private void checkLevel(User actual, Level expectedLevel) {
	// 	User updatedUser = userDao.get(actual.getId());
	// 	assertThat(updatedUser.getLevel()).isEqualTo(expectedLevel);
	// }
}
