package ch04.jdbccontext.exception;

public class DuplicateUserIdException extends RuntimeException{
	public DuplicateUserIdException() {
	}

	public DuplicateUserIdException(Throwable cause) {
		super(cause);
	}
}
