package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_analytique?serverTimezone=UTC";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "2004";

	public static Connection getConnection() throws SQLException {
		try {
			// Charger le driver JDBC pour MySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("MySQL JDBC Driver not found.", e);
		}

		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
}
