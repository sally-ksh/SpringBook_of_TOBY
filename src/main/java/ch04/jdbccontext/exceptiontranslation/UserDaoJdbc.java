package ch04.jdbccontext.exceptiontranslation;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import ch03.exceptionz.User;
import ch04.jdbccontext.StatementStrategy;
import ch04.jdbccontext.UserDaoAdd;
import ch04.jdbccontext.UserDaoDeleteAll;
import ch04.jdbccontext.UserDaoGet;
import ch04.jdbccontext.UserDaoGetCount;

public class UserDaoJdbc implements UserDao{
	private final DataSource dataSource;

	public UserDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws DuplicateKeyException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			StatementStrategy statementStrategy = new UserDaoAdd();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();

		} catch (SQLException exception) {
			throw new DuplicateKeyException("userDao", exception);
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
					resultSet.getString("password"));
			}
			if (Objects.isNull(user)) {
				throw new EmptyResultDataAccessException(1);
			}
			return user;
		} catch (SQLException exception) {
			throw new DataAccessException("userDao", exception) {
				@Override
				public Throwable getRootCause() {
					return super.getRootCause();
				}
			};
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
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			StatementStrategy statementStrategy = new UserDaoDeleteAll();
			preparedStatement = statementStrategy.makePreparedStatement(connection);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			throw new DataAccessException("userDao", exception) {
				@Override
				public Throwable getRootCause() {
					return super.getRootCause();
				}
			};
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

	public int getCount() {
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
			throw new DataAccessException("userDao", exception) {
				@Override
				public Throwable getRootCause() {
					return super.getRootCause();
				}
			};
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
}
