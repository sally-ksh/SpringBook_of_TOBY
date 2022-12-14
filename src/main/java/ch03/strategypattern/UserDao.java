package ch03.strategypattern;

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

	// 클래스 -> 로컬클래스 -> 익명 클래스 -> 람다
	public void add(final User user) throws ClassNotFoundException, SQLException {
		jdbcContextWithStatementStrategy((Connection connection) -> {
			 {
				PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into users(id,name, password) values (?,?,?)"
				);
				preparedStatement.setString(1, user.getId());
				preparedStatement.setString(2, user.getName());
				preparedStatement.setString(3, user.getPassword());
				return preparedStatement;
			}
		});
	}

	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			StatementStrategy statementStrategy = new UserDaoGet();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
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
		jdbcContextWithStatementStrategy((Connection connection) -> connection.prepareStatement("delete from users"));
	}

	public int getCount() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			StatementStrategy statementStrategy = new UserDaoGetCount();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
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

	public void jdbcContextWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
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
}
