package ch03.exceptionz;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

public class UserDao {
	private DataSource dataSource;

	public UserDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection connection = dataSource.getConnection();

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
		Connection connection = dataSource.getConnection();

		PreparedStatement prepareStatement = connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
		prepareStatement.setString(1, id);

		ResultSet resultSet = prepareStatement.executeQuery();

		User user = null;
		if (resultSet.next()) {
			user = new User(
				resultSet.getString("id"),
				resultSet.getString("name"),
				resultSet.getString("password"));
		}

		resultSet.close();
		prepareStatement.close();
		connection.close();

		if (Objects.isNull(user)) {
			throw new EmptyResultDataAccessException(1);
		}
		return user;
	}

	public void deleteAll() throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("delete from users");
		preparedStatement.executeUpdate();

		preparedStatement.close();
		connection.close();
	}

	public int getCount() throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from users");

		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		int count = resultSet.getInt(1);

		resultSet.close();
		preparedStatement.close();
		connection.close();

		return count;
	}
}
