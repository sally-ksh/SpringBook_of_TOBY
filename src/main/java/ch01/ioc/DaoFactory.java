package ch01.ioc;

public class DaoFactory {
	public UserDao userDao() {
		return new UserDao(getConnectionMaker());
	}

	private ConnectionMaker getConnectionMaker() {
		return new NConnectionMaker();
	}
}
