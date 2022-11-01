package ch01.base;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		UserDao userDao = new UserDao();

		User user = new User("sally", "sallysh", "study");
		System.out.println("registered : " + user.getId());

		userDao.add(user);

		User selectedUser = userDao.get(user.getId());
		System.out.println("read : " + selectedUser.getId());
	}
}
