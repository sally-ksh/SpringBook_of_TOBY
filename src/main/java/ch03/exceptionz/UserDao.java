package ch03.exceptionz;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

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
			preparedStatement = connection.prepareStatement(
				"insert into users(id,name, password) values (?,?,?)"
			);
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
			preparedStatement = connection.prepareStatement(
				"select id, name, password from users where id = ?"
			);
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
			preparedStatement = connection.prepareStatement("delete from users");
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			throw exception;
		} finally {   // 예외 발생하거나 발생 안하더라도 실행
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// close() 시에도 SQLException 발생할 수 있다.
					// -> 특별히 실행할 것은 없지만 빠져나가지 않게 catch 에서 잡아줘야 connection.close(); 실행
					// 로그 등 남길 수 있기 때문에 남겨준다.
				}
			}
			// dataSource.getConnection(); 에서 예외 발생시 null 상태로 NPE 발생 할 땐 close() 호출하지 않게 한다.
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
			preparedStatement = connection.prepareStatement("select count(*) from users");
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
}
