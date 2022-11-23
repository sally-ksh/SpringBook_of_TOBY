package ch06.springdynamicproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
	private final PlatformTransactionManager transactionManager;

	public TransactionAdvice(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			Object result = invocation.proceed();
			this.transactionManager.commit(status);
			return result;
		} catch (RuntimeException exception) {
			this.transactionManager.rollback(status);
			throw exception;
		}
	}
}
