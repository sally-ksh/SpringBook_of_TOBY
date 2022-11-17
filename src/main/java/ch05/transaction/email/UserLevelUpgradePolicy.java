package ch05.transaction.email;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);

	void upgradeLevel(User user);
}
