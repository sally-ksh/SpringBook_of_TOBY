package ch03.templatemethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoGetCount extends UserDao{
	public UserDaoGetCount(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected PreparedStatement makeStatement(Connection connection) throws SQLException {
		return connection.prepareStatement("select count(*) from users");
	}
}
