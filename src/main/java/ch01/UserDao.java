package ch01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
	public void add(User user) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
			"jdbc:mysql://localhost/database-name", "id", "password"
		);

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
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
			"jdbc:mysql://localhost/database-name", "id", "password"
		);

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
