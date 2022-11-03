package ch01.datasource;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class UserDaoTestTest {

	@Test
	void datasource_springApplication_bean() throws SQLException, ClassNotFoundException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao dao = context.getBean("userDao", UserDao.class);
		// User user = new User("sally", "sallysh", "study");
		// dao.add(user);
		User user = dao.get("sally");

		assertThat(user.getName()).isEqualTo("sallysh");

	}
}
