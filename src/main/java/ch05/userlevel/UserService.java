package ch05.userlevel;

import java.util.List;
import java.util.Objects;

public class UserService {
	public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	private final UserDao userDao;
	private final UserLevelUpgradePolicy userLevelUpgradePolicy;

	public UserService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userDao = userDao;
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}

	public void upgradeLevels() {
		List<User> users = userDao.getAll();

		for (User user : users) {
			if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
				userLevelUpgradePolicy.upgradeLevel(user);
				userDao.update(user);
			}
		}
	}

	public void add(User user) {
		if (Objects.isNull(user.getLevel())) {
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
