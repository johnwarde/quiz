package quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class GamesManager implements Renderable {
	private String username = "";
	private Connection con;
	PreparedStatement lastScoreStmt;

	public GamesManager(Connection dbConnection) {
		con = dbConnection;		
		try
		{
			lastScoreStmt = con.prepareStatement(
					"SELECT id, name, last_score FROM `quiz`.`players` WHERE name = ?");
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
	}
	
	public void setConnection(Connection dbConnection) {
		con = dbConnection;
	}
	
	public void setUsername(String playerUsername) {
		username = playerUsername;
	}

	
	public void awardGameScore(String parameter) {
		// TODO Auto-generated method stub
		// TODO Update database
	}
	
	public Hashtable<String, String> getPageNameValues() {
		//Connection con = DBConnectionMgr.getInstance().getConnection();	
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();

		try
		{
			ResultSet rs = null;
			lastScoreStmt.clearParameters(); //clear prev values
			lastScoreStmt.setString(1, username);
			System.out.println(lastScoreStmt.toString());
			rs = lastScoreStmt.executeQuery();
			
			if (rs.next()) {
				System.out.println("id = [" + rs.getString("id") + "]; name = [" + rs.getString("name") + "]; last_score = [" + rs.getString("last_score") + "]");
				nvpairs.put("welcome-msg", String.format("You have already played the game %s - your previous score was %s / 10 ", rs.getString("name"), rs.getString("last_score")));
			} else {
				nvpairs.put("welcome-msg", String.format("Welcome %s. Let’s start the game.", username));
			}
			nvpairs.put("message", "Click Play to continue!");
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		finally
		{
		} // end finally
		return nvpairs;
	}
}
