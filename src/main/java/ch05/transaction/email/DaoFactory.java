package ch05.transaction.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
	@Bean
	public MailSender mailSender() {
		return new JavaMailSenderImpl();
	}

	@Bean
	public UserService userService() {
		return new UserService( dataSource(), transactionManager(), userDao(), userLevelUpgradePolicy(),mailSender());
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		// return new JtaTransactionManager();  // 글로벌 트랜잭션
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new BasicUserLevelUpgradePolicy();
	}

	@Bean
	public UserDao userDao() {
		return new UserDaoJdbc(dataSource());
	}

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/database-name");
		dataSource.setUsername("id");
		dataSource.setPassword("password");
		return dataSource;
	}
}
