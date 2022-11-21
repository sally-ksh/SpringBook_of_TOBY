package ch06.separateTransaction;

public interface UserService {
	int MIN_LOG_COUNT_FOR_SILVER = 50;
	int MIN_RECOMMEND_FOR_GOLD = 30;

	void add(User user);

	void upgradeLevels();
}
