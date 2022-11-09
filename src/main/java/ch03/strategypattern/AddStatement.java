package ch03.strategypattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ch03.exceptionz.User;

public class AddStatement implements StatementStrategy {
	private final User user;

	public AddStatement(User user) {
		this.user = user;
	}

	@Override
	public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
		preparedStatement.setString(1, user.getId());
		preparedStatement.setString(2, user.getName());
		preparedStatement.setString(3, user.getPassword());
		return preparedStatement;
	}
}
