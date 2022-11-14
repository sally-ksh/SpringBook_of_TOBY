package ch04.jdbccontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll implements StatementStrategy{
	@Override
	public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
		return connection.prepareStatement("delete from users");
	}
}
