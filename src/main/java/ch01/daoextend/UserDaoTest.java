package ch01.daoextend;

import java.sql.SQLException;

public class UserDaoTest {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		ConnectionMaker connectionMaker = new NConnectionMaker();

		UserDao userDao = new UserDao(connectionMaker);
		User user = new User("sally", "sallysh", "study");
		System.out.println("registered : " + user.getId());

		userDao.add(user);

		User selectedUser = userDao.get(user.getId());
		System.out.println("read : " + selectedUser.getId());
	}
}
