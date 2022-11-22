package ch06.separateTransaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static ch06.separateTransaction.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static ch06.separateTransaction.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import ch05.transaction.Level;

@SpringBootTest(classes = MyDaoFactory.class)
// @ContextConfiguration(classes = MyDaoFactory.class)
class UserServiceTest {
	@Autowired
	UserLevelUpgradePolicy userLevelUpgradePolicy;

	@Autowired
	UserDao userDao;

	@Autowired
	DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	// @Autowired
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

	@Test
	void upgradeAllOrNothing() {
		UserServiceTest.MockMailSender mockMailSender = new UserServiceTest.MockMailSender();
		UserServiceImpl userService = new UserServiceImpl(userDao, new TestUserLevelUpgrade(users.get(3).getId()), mockMailSender);
		UserServiceTransaction userServiceTransaction = new UserServiceTransaction(userService, transactionManager);

		userDao.deleteAll();
		users.values().forEach(userDao::add);

		assertThrows(TestUserServiceException.class, () -> userServiceTransaction.upgradeLevels());
		checkLevelUpgraded(users.get(1), false);  // 예외 발생선 레벨 변경 원복 확인
	}

	@Test
	void upgradeLevels() {
		MockMailSender mockMailSender = new MockMailSender();
		UserServiceImpl userService = new UserServiceImpl(new MockUserDao(mapToUserList()), userLevelUpgradePolicy, mockMailSender);

		userDao.deleteAll();
		users.values().forEach(userDao::add);

		userService.upgradeLevels();

		List<String> requests = mockMailSender.getRequests();
		assertThat(requests.size()).isEqualTo(2);
		assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
		assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());

	}

	@Test
	void mockUpgradeLevels() {
		UserDao mockUserDao = mock(UserDao.class);
		MailSender mockMailSender = mock(MailSender.class);
		when(mockUserDao.getAll()).thenReturn(mapToUserList());
		UserServiceImpl userService = new UserServiceImpl(mockUserDao, userLevelUpgradePolicy, mockMailSender);

		userService.upgradeLevels();

		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel() == Level.SILVER).isTrue();
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel() == Level.GOLD).isTrue();

		ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArgumentCaptor.capture());

		List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
		assertThat(mailMessages.get(0).getTo()).isEqualTo(users.get(1).getEmail());
		assertThat(mailMessages.get(1).getTo()).isEqualTo(users.get(3).getEmail());
	}

	private List<User> mapToUserList() {
		return IntStream.range(0, 5)
			.mapToObj(key -> users.get(key))
			.collect(Collectors.toList());
	}

	static class MockUserDao implements UserDao {
		private final List<User> users;
		private List<User> updated = new ArrayList<>();

		public MockUserDao(List<User> users) {
			this.users = users;
		}

		public List<User> getUpdated() {
			return this.updated;
		}

		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void update(User user) {
			updated.add(user);
		}

		@Override
		public List<User> getAll() {
			return this.users;
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
