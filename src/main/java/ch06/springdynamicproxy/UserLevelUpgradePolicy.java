package ch06.springdynamicproxy;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);

	void upgradeLevel(User user);
}
