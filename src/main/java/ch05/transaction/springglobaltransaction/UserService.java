package ch05.transaction.springglobaltransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import ch05.transaction.Level;
import ch05.transaction.User;
import ch05.transaction.UserDao;
import ch05.transaction.UserLevelUpgradePolicy;

public class UserService {
	public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	private final UserDao userDao;
	private final UserLevelUpgradePolicy userLevelUpgradePolicy;


	public UserService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, DataSource dataSource, PlatformTransactionManager transactionManager) {
		this.userDao = userDao;
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
		this.dataSource = dataSource;
		this.transactionManager = transactionManager;
	}

	public void upgradeLevels() {
		TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			List<User> users = userDao.getAll();

			for (User user : users) {
				if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
					userLevelUpgradePolicy.upgradeLevel(user);
					userDao.update(user);
				}
			}
			transactionManager.commit(transactionStatus);
		} catch (RuntimeException exception) {
			transactionManager.rollback(transactionStatus);
			throw exception;
		}
	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
