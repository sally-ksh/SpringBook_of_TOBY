package ch01.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class CountingConnectionMakerTest {

	@Test
	void countingConnectionMaker_callUserDao_countingConnectionMaker() throws SQLException, ClassNotFoundException {
		int expected = 3;

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao userDao = context.getBean("userDao", UserDao.class);

		callUserDao(userDao, expected);
		CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
		int actual = connectionMaker.getCounter();

		assertThat(expected).isEqualTo(actual);
	}

	private void callUserDao(UserDao userDao, int times) throws ClassNotFoundException, SQLException {
		int callCount = 1;
		User user = new User("sally", "sallysh", "study");

		while (times-- > 0) {
			User selectedUser = userDao.get(user.getId());
			System.out.println("read : " + callCount++ + ", " + selectedUser.getId());
		}
	}

}
