package ch01.daoextend;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
	Connection makeConnection() throws ClassNotFoundException, SQLException;
}
