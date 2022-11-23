package ch06.springdynamicproxy;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import ch06.proxyz.HelloTarget;
import ch06.separateTransaction.TransactionFactoryBean;

@Configuration
public class DaoFactory {
	@Bean
	public TransactionAdvice transactionAdvice() {
		return new TransactionAdvice(transactionManager());
	}

	@Bean
	public NameMatchMethodPointcut transactionPointcut() {
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("upgrad*");
		return pointcut;
	}

	@Bean
	public DefaultPointcutAdvisor transactionAdvisor() {
		return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
	}

	@Bean
	public MailSender mailSender() {
		return new JavaMailSenderImpl();
	}

	@Bean
	public ProxyFactoryBean userService() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(userServiceImpl());
		proxyFactoryBean.setInterceptorNames("transactionAdvisor");
		return proxyFactoryBean;
	}

	@Bean
	public UserServiceImpl userServiceImpl() {
		return new UserServiceImpl(userDao(), userLevelUpgradePolicy(),mailSender());
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
