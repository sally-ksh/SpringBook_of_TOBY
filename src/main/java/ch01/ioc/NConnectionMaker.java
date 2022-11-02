package ch01.ioc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {
	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
			"jdbc:mysql://localhost/database-name", "id", "password"
		);
		return connection;
	}
}
