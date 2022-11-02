package ch01.ioc;

public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new NConnectionMaker();
		UserDao userDao = new UserDao(connectionMaker);
		return userDao;
	}
}
