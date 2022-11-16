package ch05.transaction;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

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

	public void upgradeLevels() throws SQLException {
		TransactionSynchronizationManager.initSynchronization();
		Connection connection = DataSourceUtils.getConnection(dataSource);
		connection.setAutoCommit(false);
		try {
			List<User> users = userDao.getAll();

			for (User user : users) {
				if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
					userLevelUpgradePolicy.upgradeLevel(user);
					userDao.update(user);
				}
			}
			connection.commit();
		} catch (SQLException exception) {
			connection.rollback();
			throw exception;
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}

	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
