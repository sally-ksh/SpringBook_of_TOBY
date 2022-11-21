package ch06.separateTransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {
	private Object target;
	private PlatformTransactionManager transactionManager;
	private String pattern;

	public TransactionHandler(Object target, PlatformTransactionManager transactionManager, String pattern) {
		this.target = target;
		this.transactionManager = transactionManager;
		this.pattern = pattern;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 적용 대상 메서드 선별
		if (method.getName().startsWith(pattern)) {
			return invokeTransaction(method, args);
		}
		return method.invoke(target, args);
	}

	private Object invokeTransaction(Method method, Object[] args) throws Throwable {
		TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			Object result = method.invoke(target, args);
			this.transactionManager.commit(transactionStatus);
			return result;
		} catch (InvocationTargetException exception) {
			this.transactionManager.rollback(transactionStatus);
			throw exception.getTargetException();
		}
	}
}
