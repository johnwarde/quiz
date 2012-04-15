package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


// TODO: Need throws exception to pass up errors??

public class DBConnectionMgr {
//	private static final DBConnectionMgr instance = new DBConnectionMgr();
//	private static Connection con;
	private static DBConnectionMgr instance;
	private Connection con;
	
	/**
	 * @return
	 */
	public static DBConnectionMgr getInstance() {
		if (null == instance) {
			instance = new DBConnectionMgr();
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
	
	private DBConnectionMgr() {
		try {
			// Load (and therefore register) the Database Driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// Get a connection to the database
			con = DriverManager.getConnection("jdbc:odbc:mysqlquiz","root","mysqlpass");
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
		finally {
			// Always close the database connection
			try {
				// if database connection is still open
				if (con != null) {
					con.close();
				}
			}
			catch(SQLException e) {
				System.out.print("SQL Exception caught close after exception : " + e.getMessage());
			}
		} // end finally
	}
	
}
