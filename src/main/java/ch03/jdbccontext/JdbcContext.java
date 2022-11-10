package ch03.jdbccontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {
	private final DataSource dataSource;

	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void workWithStatementStrategyByUpdate(StatementStrategy statementStrategy) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = this.dataSource.getConnection();

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
