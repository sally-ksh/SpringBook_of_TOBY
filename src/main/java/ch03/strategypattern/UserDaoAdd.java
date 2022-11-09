package ch03.strategypattern;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoAdd extends UserDao {
	public UserDaoAdd(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected PreparedStatement makeStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
	}
}
