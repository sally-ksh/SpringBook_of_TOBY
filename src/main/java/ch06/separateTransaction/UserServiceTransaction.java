package ch06.separateTransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTransaction implements UserService {
	private final UserService userService;
	private final PlatformTransactionManager transactionManager;

	public UserServiceTransaction(UserService userService,
		PlatformTransactionManager transactionManager) {
		this.userService = userService;
		this.transactionManager = transactionManager;
	}

	@Override
	public void add(User user) {
		this.userService.add(user);
	}

	@Override
	public void upgradeLevels() {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			this.userService.upgradeLevels();

			this.transactionManager.commit(status);
		} catch (RuntimeException exception) {
			this.transactionManager.rollback(status);
			throw exception;
		}
	}
}
