package ch01.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	@Bean
	public UserDao userDao() {
		return new UserDao(getConnectionMaker());
	}

	@Bean
	public ConnectionMaker getConnectionMaker() {
		return new NConnectionMaker();
	}
}
