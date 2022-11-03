package ch01.di;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserDao {
	private static volatile UserDao INSTANCE;
	private ConnectionMaker connectionMaker;

	private UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public static UserDao getInstance(ConnectionMaker connectionMaker) {
		if (Objects.isNull(INSTANCE)) {
			synchronized (UserDao.class) {
				INSTANCE = new UserDao(connectionMaker);
			}
		}
		return INSTANCE;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection connection = connectionMaker.makeConnection();

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
		Connection connection = connectionMaker.makeConnection();

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
