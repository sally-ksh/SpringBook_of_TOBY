package ch05.transaction.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ch05.transaction.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static ch05.transaction.UserService.MIN_RECOMMEND_FOR_GOLD;

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

@SpringBootTest(classes = MyDaoFactory.class)
class UserServiceTest {
	@Autowired
	UserDao userDao;

	@Autowired
	DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	UserLevelUpgradePolicy userLevelUpgradePolicy;

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
		MockMailSender mockMailSender = new MockMailSender();
		UserService userService = new UserService( dataSource, transactionManager, this.userDao, userLevelUpgradePolicy,
			mockMailSender);
		userDao.deleteAll();
		users.values().forEach(userDao::add);

		userService.upgradeLevels();

		checkLevelUpgraded(users.get(0), false); // login 50미만 없그레이드 X
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false); // recommend 30 미만 업그레이드 X
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);

		List<String> requests = mockMailSender.getRequests();
		assertThat(requests.size()).isEqualTo(2);
		assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
		assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());

	}

	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<>();

		private List<String> getRequests() {
			return this.requests;
		}

		@Override
		public void send(SimpleMailMessage simpleMailMessage) throws MailException {
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
