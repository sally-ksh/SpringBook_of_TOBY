package ch03.strategypattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
	@Bean
	public UserDao userDaoAdd() {
		return new UserDaoAdd(dataSource());
	}

	@Bean
	public UserDao userDaoDeleteAll() {
		return new UserDaoDeleteAll(dataSource());
	}

	@Bean
	public UserDao userDaoGet() {
		return new UserDaoGet(dataSource());
	}

	@Bean
	public UserDao userDaoGetCount() {
		return new UserDaoGetCount(dataSource());
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
