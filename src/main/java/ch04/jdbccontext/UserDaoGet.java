package ch04.jdbccontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoGet implements StatementStrategy {
	@Override
	public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
	}
}
