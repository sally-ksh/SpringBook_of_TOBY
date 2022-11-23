package ch06.springdynamicproxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static ch06.springdynamicproxy.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static ch06.springdynamicproxy.UserService.MIN_RECOMMEND_FOR_GOLD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch05.transaction.Level;

@SpringBootTest(classes = MyDaoFactory.class)
public class UserServiceDynamicProxyTest {
	@Autowired
	UserDao userDao;

	@Autowired
	ApplicationContext context;

	// AnnotationConfigApplicationContext context;
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

	@DirtiesContext
	@Test
	void upgradeAllOrNothing() throws Exception {
		MockMailSender mockMailSender = new MockMailSender();
		UserServiceImpl testUserService = new UserServiceImpl(userDao, new TestUserLevelUpgrade(users.get(3).getId()), mockMailSender);

		ProxyFactoryBean transactionProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
		transactionProxyFactoryBean.setTarget(testUserService);
		UserService transactionUserService = (UserService)transactionProxyFactoryBean.getObject();

		userDao.deleteAll();
		users.values().forEach(userDao::add);

		assertThrows(TestUserServiceException.class, () -> transactionUserService.upgradeLevels());
		checkLevelUpgraded(users.get(1), false);  // 예외 발생선 레벨 변경 원복 확인
	}

	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<>();

		private List<String> getRequests() {
			return this.requests;
		}

		@Override
		public void send(SimpleMailMessage simpleMailMessage) throws
			ch06.separateTransaction.MailException {
			requests.add(simpleMailMessage.getTo());
		}

		@Override
		public void send(SimpleMailMessage[] simpleMailMessage) throws MailException {

		}
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
					return (user.getLogin() >= ch06.separateTransaction.UserService.MIN_LOG_COUNT_FOR_SILVER);
				case SILVER:
					return (user.getRecommend() >= ch06.separateTransaction.UserService.MIN_RECOMMEND_FOR_GOLD);
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
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdated = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isTrue();
		} else {
			assertThat(userUpdated.getLevel() == user.getLevel().nextLevel()).isFalse();
		}
	}
}
