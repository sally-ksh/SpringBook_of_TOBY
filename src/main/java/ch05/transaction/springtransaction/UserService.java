package ch05.transaction.springtransaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
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

	private final UserDao userDao;
	private final UserLevelUpgradePolicy userLevelUpgradePolicy;
	private final DataSource dataSource;


	public UserService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, DataSource dataSource) {
		this.userDao = userDao;
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
		this.dataSource = dataSource;
	}

	public void upgradeLevels() {
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); // 트랜잭션 추상 오브젝트 생성
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
