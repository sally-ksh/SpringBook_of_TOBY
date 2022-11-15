package ch05.userlevel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
	@Bean
	public UserService userService() {
		return new UserService(userDao(), userLevelUpgradePolicy());
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
