package ch05.transaction;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import ch05.transaction.jdbc.StatementStrategy;
import ch05.transaction.jdbc.UserDaoGet;
import ch05.transaction.jdbc.UserDaoGetAll;
import ch05.transaction.jdbc.UserDaoGetCount;

public class UserDaoJdbc implements UserDao{
	private final DataSource dataSource;

	public UserDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// 클래스 -> 로컬클래스 -> 익명 클래스 -> 람다
	public void add(final User user) {
		jdbcContextWithStatementStrategy((Connection connection) -> {
			 {
				PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into users(id,name, password, level, login, recommend) values (?,?,?,?,?,?)"
				);
				preparedStatement.setString(1, user.getId());
				preparedStatement.setString(2, user.getName());
				preparedStatement.setString(3, user.getPassword());
				preparedStatement.setInt(4, user.getLevel().intValue());
				preparedStatement.setInt(5, user.getLogin());
				preparedStatement.setInt(6, user.getRecommend());
				return preparedStatement;
			}
		});
	}

	public User get(String id) {
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
					resultSet.getString("password"),
					Level.valueOf(resultSet.getInt("level")),
					resultSet.getInt("login"),
					resultSet.getInt("recommend"));
			}
			if (Objects.isNull(user)) {
				throw new EmptyResultDataAccessException(1);
			}
			return user;
		} catch (SQLException exception) {
			throw new DataAccessResourceFailureException("userDao get()", exception);
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

	public void deleteAll() {
		jdbcContextWithStatementStrategy((Connection connection) -> connection.prepareStatement("delete from users"));
	}

	public int getCount() throws DataAccessException {
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
			throw new IncorrectResultSizeDataAccessException("userDao getCount", -1, exception);
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

	@Override
	public void update(User user) {
		jdbcContextWithStatementStrategy((Connection connection) -> {
			{
				PreparedStatement preparedStatement = connection.prepareStatement(
					"update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ? "
				);
				preparedStatement.setString(1, user.getName());
				preparedStatement.setString(2, user.getPassword());
				preparedStatement.setInt(3, user.getLevel().intValue());
				preparedStatement.setInt(4, user.getLogin());
				preparedStatement.setInt(5, user.getRecommend());
				preparedStatement.setString(6, user.getId());
				return preparedStatement;
			}
		});
	}

	@Override
	public List<User> getAll() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			StatementStrategy statementStrategy = new UserDaoGetAll();
			preparedStatement = statementStrategy.makePreparedStatement(connection);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<>();
			while (resultSet.next()) {
				users.add(new User(
					resultSet.getString("id"),
					resultSet.getString("name"),
					resultSet.getString("password"),
					Level.valueOf(resultSet.getInt("level")),
					resultSet.getInt("login"),
					resultSet.getInt("recommend")));
			}
			if (Objects.isNull(users)) {
				throw new EmptyResultDataAccessException(1);
			}
			return users;
		} catch (SQLException exception) {
			throw new DataAccessResourceFailureException("userDao get()", exception);
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

	public void jdbcContextWithStatementStrategy(StatementStrategy statementStrategy) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			throw new IncorrectUpdateSemanticsDataAccessException("fail update userDao", exception);
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
