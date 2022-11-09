package ch03.strategypattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoAdd implements StatementStrategy {
	@Override
	public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
	}
}
