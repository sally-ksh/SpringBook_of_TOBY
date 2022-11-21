package ch06.dynamicproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeanTestConfig {
	public static final String TEXT_MESSAGE = "Factory Bean";
	@Bean
	public MessageFactoryBean message() {
		return new MessageFactoryBean(TEXT_MESSAGE);
	}
}
