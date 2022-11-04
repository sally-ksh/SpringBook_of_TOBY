package ch02.testconsistency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class UserDaoTest {

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao dao = context.getBean("userDao", UserDao.class);

		dao.deleteAll();
		assertThat(dao.getCount()).isZero();

		User expected = new User("sally", "sallysh", "study");
		dao.add(expected);

		assertThat(dao.getCount()).isOne();

		User actual = dao.get(expected.getId());

		assertThat(actual.getName()).isEqualTo(expected.getName());
		assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
	}
}
