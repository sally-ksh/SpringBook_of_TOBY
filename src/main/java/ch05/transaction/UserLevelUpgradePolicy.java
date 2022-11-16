package ch05.transaction;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);

	void upgradeLevel(User user);
}
