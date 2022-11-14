package ch04.jdbccontext.exceptiontranslation;

import java.util.List;

import ch03.exceptionz.User;

public interface UserDao {
	void add(User user);

	User get(String id);

	void deleteAll();

	int getCount();
}
