package ch02.testconsistency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
	@Bean
	public UserDao userDao() {
		return new UserDao(dataSource());
	}

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		// dataSource.setUrl("jdbc:mysql://localhost/database-name");
		// dataSource.setUsername("id");
		// dataSource.setPassword("password");

		dataSource.setUrl("jdbc:mysql://localhost/logindb");
		dataSource.setUsername("sally");
		dataSource.setPassword("sally2022");
		return dataSource;
	}
}
