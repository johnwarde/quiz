package quiz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
//import java.sql.Connection;

public class GamesManager implements Renderable {
	private String username = "";

	public void setConnection(String playerUsername) {
		username = playerUsername;
	}
	
	public void setUsername(String playerUsername) {
		username = playerUsername;
	}

	
	public void awardGameScore(String parameter) {
		// TODO Auto-generated method stub
		// TODO Update database
	}
	
	public Hashtable<String, String> getPageNameValues() {
		Connection con = DBConnectionMgr.getInstance().getConnection();	
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();
		// TODO Next line was introduced to get rid of a warning, review.
		//nvpairs.put("username", username);
		//Connection con =  getDbConnection();

		try
		{
			Statement stmt = null;
			ResultSet rs = null;
			
			// Create a Statement object
			stmt = con.createStatement();
	
			// Execute a SQL query, get a ResultSet
			rs = stmt.executeQuery("SELECT id, name, last_score FROM `quiz`.`players` WHERE name = " + username + ";");
	
			// while there are more records in the table to be searched
			if (rs.next()) {
				System.out.print("id = [" + rs.getString("id") + "]; name = [" + rs.getString("name") + "]; last_score = [" + rs.getString("last_score") + "]");
				//nvpairs.put("lastscore", rs.getString("last_score"));
				nvpairs.put("welcomemsg-line1", "Welcome back " + username + "!");
				nvpairs.put("welcomemsg-line2", "Your last score was " + rs.getString("last_score") + "!");
			}
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		finally
		{
			// Always close the database connection
			DBConnectionMgr.getInstance().close();
		} // end finally
		return nvpairs;
	}
}
