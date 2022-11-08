package ch03.extractmethod;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

import ch03.exceptionz.User;

public class UserDao {
	private final DataSource dataSource;

	public UserDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = makeStatementAdd(connection);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();

		} catch (SQLException exception) {
			throw exception;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = makeStatementGet(connection);
			preparedStatement.setString(1, id);

			resultSet = preparedStatement.executeQuery();
			User user = null;
			if (resultSet.next()) {
				user = new User(
					resultSet.getString("id"),
					resultSet.getString("name"),
					resultSet.getString("password"));
			}
			if (Objects.isNull(user)) {
				throw new EmptyResultDataAccessException(1);
			}
			return user;
		} catch (SQLException exception) {
			throw exception;
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void deleteAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = makeStatementDeleteAll(connection);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			throw exception;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public int getCount() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = makeStatementCount(connection);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException exception) {
			throw exception;
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private PreparedStatement makeStatementDeleteAll(Connection connection) throws SQLException {
		return connection.prepareStatement("delete from users");
	}

	private PreparedStatement makeStatementCount(Connection connection) throws SQLException {
		return connection.prepareStatement("select count(*) from users");
	}

	private PreparedStatement makeStatementGet(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"select id, name, password from users where id = ?"
		);
	}

	private PreparedStatement makeStatementAdd(Connection connection) throws SQLException {
		return connection.prepareStatement(
			"insert into users(id,name, password) values (?,?,?)"
		);
	}
}
