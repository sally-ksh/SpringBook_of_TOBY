package ch05.transaction;

import java.util.List;

public interface UserDao {
	void add(User user);

	User get(String id);

	void deleteAll();

	int getCount();

	void update(User user);

	List<User> getAll();
}

