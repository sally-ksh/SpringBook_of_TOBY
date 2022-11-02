package ch01.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
		// UserDao userDao = new DaoFactory().userDao();
		User user = new User("sally", "sallysh", "study");
		System.out.println("registered : " + user.getId());

		userDao.add(user);

		User selectedUser = userDao.get(user.getId());
		System.out.println("read : " + selectedUser.getId());
	}
}
