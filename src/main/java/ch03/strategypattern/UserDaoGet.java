package ch03.strategypattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoGet extends UserDao {
	public UserDaoGet(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected PreparedStatement makeStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
	}
}
