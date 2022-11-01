package ch01.daoextend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch01.base.User;

public class UserDao {
	private SimpleConnectionMaker simpleConnectionMaker;

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection connection = simpleConnectionMaker.makeNewConnection();

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

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection connection = simpleConnectionMaker.makeNewConnection();

		PreparedStatement prepareStatement = connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
		prepareStatement.setString(1, id);

		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		User user = new User(
			resultSet.getString("id"),
			resultSet.getString("name"),
			resultSet.getString("password")
		);

		resultSet.close();
		prepareStatement.close();
		connection.close();

		return user;
	}
}
