package ch05.userlevel;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);

	void upgradeLevel(User user);
}
