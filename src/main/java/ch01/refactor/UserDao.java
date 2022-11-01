package ch01.refactor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch01.base.User;

public abstract class UserDao {
	public void add(ch01.base.User user) throws ClassNotFoundException, SQLException {
		Connection connection = getConnection();

		PreparedStatement prepareStatement = connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
		prepareStatement.setString(1, user.getId());
		prepareStatement.setString(2, user.getName());
		prepareStatement.setString(3, user.getPassword());

		prepareStatement.executeUpdate();

		prepareStatement.close();
		connection.close();
	}

	public ch01.base.User get(String id) throws ClassNotFoundException, SQLException {
		Connection connection = getConnection();

		PreparedStatement prepareStatement = connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
		prepareStatement.setString(1, id);

		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		ch01.base.User user = new User(
			resultSet.getString("id"),
			resultSet.getString("name"),
			resultSet.getString("password")
		);

		resultSet.close();
		prepareStatement.close();
		connection.close();

		return user;
	}

	protected abstract Connection getConnection() throws ClassNotFoundException, SQLException ;
}
