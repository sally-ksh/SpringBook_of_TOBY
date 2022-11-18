package ch06.separateTransaction;

public interface UserService {
	void add(User user);

	void upgradeLevels();
}
