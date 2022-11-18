package ch06.separateTransaction;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);

	void upgradeLevel(User user);
}
