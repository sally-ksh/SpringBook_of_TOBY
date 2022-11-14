package ch04.jdbccontext.exceptiontranslation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.sql.DataSource;

import ch03.exceptionz.User;

@SpringBootTest(classes = DaoFactory.class)
class UserDaoTest {
	@Autowired
	private UserDao userDao;
	@Autowired
	private DataSource dataSource;
	private User userA;
	private User userB;

	@BeforeEach
	void beforeEach() {
		userA = new User("sally", "sallysh", "study");
	}

	@Test
	void duplicateKey() {
		userDao.deleteAll();

		userDao.add(userA);

		assertThrows(DuplicateKeyException.class, () -> userDao.add(userA));
	}

	@Test
	void sqlExceptionTranslate() {
		userDao.deleteAll();

		try {
			userDao.add(userA);
			userDao.add(userA);
		} catch (DuplicateKeyException exception) {
			SQLException sqlEx = (SQLException)exception.getRootCause();
			SQLExceptionTranslator exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

			DataAccessException actual = exceptionTranslator.translate(null, null, sqlEx);
			assertThat(actual).isInstanceOf(DuplicateKeyException.class);
		}
	}
}
