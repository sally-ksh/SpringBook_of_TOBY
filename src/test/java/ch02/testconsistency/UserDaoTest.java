package ch02.testconsistency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class UserDaoTest {
	@Test
	void userDao_read_dbConnectionAndSpringContainer() throws SQLException, ClassNotFoundException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao dao = context.getBean("userDao", UserDao.class);
		// User user = new User("sally", "sallysh", "study");
		// dao.add(user);
		User user = dao.get("sally");

		assertThat(user.getName()).isEqualTo("sallysh");

	}
}
