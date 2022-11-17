package ch05.transaction.email;

import static ch05.userlevel.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static ch05.userlevel.UserService.MIN_RECOMMEND_FOR_GOLD;

import ch05.transaction.Level;

public class BasicUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
	@Override
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
			case BASIC:
				return (user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER);
			case SILVER:
				return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD:
				return false;
			default:
				throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
	}
}
