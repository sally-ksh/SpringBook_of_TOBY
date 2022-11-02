package ch01.ioc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class UserDaoTestTest {

	@Test
	void daoFactory_compareOfBean_identityAndEquality() {
		DaoFactory factory = new DaoFactory();
		UserDao daoA = factory.userDao();
		UserDao daoB = factory.userDao();

		assertThat(daoA).isNotEqualTo(daoB);
		assertThat(daoA == daoB).isFalse();
	}

	@Test
	void springContext_compareOfBean_identityAndEquality() {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

		UserDao daoA = context.getBean("userDao", UserDao.class);
		UserDao daoB = context.getBean("userDao", UserDao.class);

		assertThat(daoA).isEqualTo(daoB);
		assertThat(daoA == daoB).isTrue();
	}
}
