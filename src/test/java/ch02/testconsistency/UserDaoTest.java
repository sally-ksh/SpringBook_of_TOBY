package ch02.testconsistency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class UserDaoTest {

	@Test
	void addAndGet() throws SQLException, ClassNotFoundException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao dao = context.getBean("userDao", UserDao.class);
		User expectedA = new User("sally", "sallysh", "study");
		User expectedB = new User("sally2", "sallysh2", "study2");

		dao.deleteAll();
		assertThat(dao.getCount()).isZero();

		dao.add(expectedA);
		dao.add(expectedB);
		assertThat(dao.getCount()).isGreaterThan(1);

		User actualA = dao.get(expectedA.getId());
		assertThat(actualA.getName()).isEqualTo(actualA.getName());
		assertThat(actualA.getPassword()).isEqualTo(actualA.getPassword());

		User actualB = dao.get(expectedB.getId());
		assertThat(actualB.getName()).isEqualTo(actualB.getName());
		assertThat(actualB.getPassword()).isEqualTo(actualB.getPassword());
	}

	@Test
	void getUserFailure() throws SQLException, ClassNotFoundException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao dao = context.getBean("userDao", UserDao.class);
		User expected = new User("sally", "sallysh", "study");

		dao.deleteAll();
		assertThat(dao.getCount()).isZero();

		assertThatThrownBy(() -> dao.get("unknownId"))
			.isInstanceOf(SQLException.class);
	}
}
