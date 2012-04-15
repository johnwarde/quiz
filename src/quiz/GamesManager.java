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
	PreparedStatement insertScoreStmt;
	PreparedStatement updateScoreStmt;
	
	public GamesManager(Connection dbConnection) {
		con = dbConnection;		
		try
		{
			lastScoreStmt = con.prepareStatement(
					"SELECT id, name, last_score FROM `quiz`.`players` WHERE name = ?");
			insertScoreStmt = con.prepareStatement(
					"INSERT INTO `quiz`.`players` (name, last_score) VALUES (?, ?)");			
			updateScoreStmt = con.prepareStatement(
					"UPDATE `quiz`.`players` SET last_score = ? WHERE name = ?");
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

	
	public void awardGameScore(String finalScore) {
		try
		{
			ResultSet rs = null;
			int score = Integer.parseInt(finalScore);
			lastScoreStmt.clearParameters(); //clear prev values
			lastScoreStmt.setString(1, username);
			System.out.println(lastScoreStmt.toString());
			rs = lastScoreStmt.executeQuery();
			
			if (rs.next()) {
				updateScoreStmt.clearParameters();
				updateScoreStmt.setInt(1, score);
				updateScoreStmt.setString(2, username);
				updateScoreStmt.executeUpdate();
			} else {
				insertScoreStmt.clearParameters();
				insertScoreStmt.setString(1, username);
				insertScoreStmt.setInt(2, score);
				insertScoreStmt.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		finally
		{
		} // end finally	
	}
	
	public Hashtable<String, String> getPageNameValues() {
		//Connection con = DBConnectionMgr.getInstance().getConnection();	
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();

		try
		{
			ResultSet rs = null;
			lastScoreStmt.clearParameters(); //clear prev values
			lastScoreStmt.setString(1, username);
			rs = lastScoreStmt.executeQuery();
			
			if (rs.next()) {
				nvpairs.put("welcome-msg", 
						String.format("You have already played the game %s - your previous score was %d / %d ", 
								rs.getString("name"), rs.getInt("last_score"), InPlayManager.getMaxquestions()));
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
