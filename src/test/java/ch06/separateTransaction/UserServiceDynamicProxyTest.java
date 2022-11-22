package ch06.separateTransaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static ch06.separateTransaction.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static ch06.separateTransaction.UserService.MIN_RECOMMEND_FOR_GOLD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import ch05.transaction.Level;

@SpringBootTest(classes = DaoFactory.class)
public class UserServiceDynamicProxyTest {
	@Autowired
	UserDao userDao;

	@Autowired
	ApplicationContext context;

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
	void upgradeAllOrNothingByDynamicProxy() throws Exception {
		UserServiceTest.MockMailSender mockMailSender = new UserServiceTest.MockMailSender();
		UserServiceImpl testUserService = new UserServiceImpl(userDao, new UserServiceTest.TestUserLevelUpgrade(users.get(3).getId()), mockMailSender);
	/*
		TransactionHandler transactionHandler = new TransactionHandler(userService, transactionManager, "upgradeLevels");
		UserService proxyInstance = (UserService)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {
			UserService.class}, transactionHandler);*/

		TransactionFactoryBean transactionFactoryBean = context.getBean("&userService", TransactionFactoryBean.class);
		transactionFactoryBean.setTarget(testUserService);
		UserService userServiceTx = (UserService)transactionFactoryBean.getObject();

		userDao.deleteAll();
		users.values().forEach(userDao::add);

		assertThrows(UserServiceTest.TestUserServiceException.class, () -> userServiceTx.upgradeLevels());
		checkLevelUpgraded(users.get(1), false);  // 예외 발생선 레벨 변경 원복 확인
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
