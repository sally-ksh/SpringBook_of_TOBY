package ch06.separateTransaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static ch06.separateTransaction.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static ch06.separateTransaction.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ch05.transaction.Level;
import ch05.transaction.UserService;

@SpringBootTest(classes = MyDaoFactory.class)
class UserServiceTest {
	@Autowired
	UserDao userDao;

	@Autowired
	DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	private Map<Integer, User> users;

	@BeforeEach
	void beforeEach() {
		users = Map.of(
			0, new User("1", "임화령", "1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "email1@email.com"),
			1, new User("2", "세자", "2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "email1@email.com"),
			2, new User("3", "성남대군", "3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "email1@email.com"),
			3, new User("4", "무안대군", "4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "email1@email.com"),
			4, new User("5", "계성대군", "5", Level.GOLD, 100, Integer.MAX_VALUE, "email1@email.com")
		);
	}

	@Test
	void upgradeAllOrNothing() {
		UserServiceTest.MockMailSender mockMailSender = new UserServiceTest.MockMailSender();
		UserServiceImpl userService = new UserServiceImpl(dataSource, transactionManager, userDao, new TestUserLevelUpgrade(users.get(3).getId()), mockMailSender);
		UserServiceTransaction userServiceTransaction = new UserServiceTransaction(userService, transactionManager);

		userDao.deleteAll();
		users.values().forEach(userDao::add);

		assertThrows(TestUserServiceException.class, () -> userServiceTransaction.upgradeLevels());
		checkLevelUpgraded(users.get(1), false);  // 예외 발생선 레벨 변경 원복 확인

	}

	static class TestUserLevelUpgrade implements UserLevelUpgradePolicy {
		private final String id;

		public TestUserLevelUpgrade(String id) {
			this.id = id;
		}

		@Override
		public boolean canUpgradeLevel(User user) {
			Level currentLevel = user.getLevel();
			switch (currentLevel) {
				case BASIC:
					return (user.getLogin() >= UserService.MIN_LOG_COUNT_FOR_SILVER);
				case SILVER:
					return (user.getRecommend() >= UserService.MIN_RECOMMEND_FOR_GOLD);
				case GOLD:
					return false;
				default:
					throw new IllegalArgumentException("Unknown Level: " + currentLevel);
			}
		}

		@Override
		public void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			user.upgradeLevel();
		}
	}

	static class TestUserServiceException extends RuntimeException {}

	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<>();

		private List<String> getRequests() {
			return this.requests;
		}

		@Override
		public void send(SimpleMailMessage simpleMailMessage) throws
			MailException {
			requests.add(simpleMailMessage.getTo());
		}

		@Override
		public void send(SimpleMailMessage[] simpleMailMessage) throws MailException {

		}
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdated = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isTrue();
		} else {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isFalse();
		}
	}
}