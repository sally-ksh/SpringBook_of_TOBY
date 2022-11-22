package ch06.separateTransaction;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TransactionFactoryBean implements FactoryBean<Object> {
	private Object target;
	private PlatformTransactionManager transactionManager;
	private String pattern;
	private Class<?> serviceInterface;

	public TransactionFactoryBean(
		Object target,
		PlatformTransactionManager transactionManager,
		String pattern,
		Class<?> serviceInterface) {
		this.target = target;
		this.transactionManager = transactionManager;
		this.pattern = pattern;
		this.serviceInterface = serviceInterface;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	@Override
	public Object getObject() {
		TransactionHandler transactionHandler = new TransactionHandler(target, transactionManager, pattern);
		return Proxy.newProxyInstance(
			getClass().getClassLoader(),
			new Class[]{serviceInterface},
			transactionHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return false;  // getObject()에서 생성으로 다른 오브젝트 반환
	}
}
