package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;


public class QuizDatabaseManager {
	private static QuizDatabaseManager instance;
	private static Connection con;
	private static Hashtable<String, PreparedStatement> preparedStatements = new Hashtable<String, PreparedStatement>();	
	
	/**
	 * @return
	 */
	public static QuizDatabaseManager getInstance() {
		if (null == instance) {
			instance = new QuizDatabaseManager();
		}
		return instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.print("SQL Exception caught on close : " + e.getMessage());
		}
	}	
	
	public PreparedStatement getPreparedStatement(String statementName) {
		return preparedStatements.get(statementName);
	}
	
	private QuizDatabaseManager() {		
		try {
			// Load (and therefore register) the Database Driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// Get a connection to the database
			con = DriverManager.getConnection("jdbc:odbc:mysqlquiz","root","mysqlpass");
			// Create Prepared Statement Objects so that they can be re-used.
			String[][] statementsToPrepare = {
					{"get-last-score", 		"SELECT id, name, last_score FROM `quiz`.`players` WHERE name = ?"},
					{"insert-new-score", 	"INSERT INTO `quiz`.`players` (name, last_score) VALUES (?, ?)"},
					{"update-score", 		"UPDATE `quiz`.`players` SET last_score = ? WHERE name = ?"},
					{"get-question", 		"SELECT question, correct_answer FROM `quiz`.`questions` WHERE id = ?"}
			};
			String name, sqlStatement;
			PreparedStatement builder;
			for (String[] entry : statementsToPrepare) {
				name = entry[0];
				sqlStatement = entry[1];
				builder = con.prepareStatement(sqlStatement);
				preparedStatements.put(name, builder);
			}
		}
		catch(ClassNotFoundException e) {
			System.out.print("Could not load Database Driver : " + e.getMessage());
		}
		catch(SQLException e) {
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		catch(Exception e) {
			System.out.print("Exception caught : " + e.getMessage());
		}
	}
	
}
