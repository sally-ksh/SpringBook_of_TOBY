package ch04.jdbccontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoAdd implements StatementStrategy{
	@Override
	public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
	}
}
