package quiz;

//import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class GameManager implements Renderable {
	private static final int maxQuestions = 10;
	private String username = "";
	private int questionNo = 0;
	private String questionId = "";
	private String userAnswer;
	private int totalScore = 0;

	PreparedStatement lastScoreStmt;
	PreparedStatement insertScoreStmt;
	PreparedStatement updateScoreStmt;
	PreparedStatement getQuestionStmt;
	
	public GameManager() {
		QuizDatabaseManager dbMgr = QuizDatabaseManager.getInstance();
		lastScoreStmt   = dbMgr.getPreparedStatement("get-last-score");
		insertScoreStmt = dbMgr.getPreparedStatement("insert-new-score");
		updateScoreStmt = dbMgr.getPreparedStatement("update-score");
		getQuestionStmt = dbMgr.getPreparedStatement("get-question");
	}
	
	public void setUsername(String playerUsername) {
		username = playerUsername;
	}

	
	public void setQuestionNo(String qno) {
		if (null == qno) {
			questionNo = 0;
		} else {
			questionNo = Integer.parseInt(qno);			
		}
	}
	

	public void setQuestionId(String qid) {
		questionId = qid;
	}	

	public void setUserAnswer(String answer) {
		userAnswer = answer;
	}	
	
	public void setUserTotalScore(String score) {
		if (null == score) {
			totalScore = 0;
		} else {
			totalScore = Integer.parseInt(score);			
		}		
	}
	
	
	public Hashtable<String, String> getValuesForSequence() {
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();
		ResultSet rs = null;
		nvpairs.put("message", "&nbsp;");
		nvpairs.put("use-template", "game-sequence"); // default / regular template
		if (null != questionId && "" != questionId) {
			Boolean answer;
			if (null == userAnswer) {
				// User did not select an answer
				nvpairs.put("message", "Please choose an option above!");
			} else {
				answer = Boolean.parseBoolean(userAnswer);
				// User has just answered a question, need to check answer against database
				try
				{
					getQuestionStmt.clearParameters();
					getQuestionStmt.setString(1, questionId);
					rs = getQuestionStmt.executeQuery();
					if (rs.next()) {
						if (answer == rs.getBoolean("correct_answer")) {
							totalScore++;
							nvpairs.put("message", "Correct answer! Now for the next one.");
						} else {
							nvpairs.put("message", "Incorrect! Better luck with this one.");
						}
					} else {
						nvpairs.put("message", "An application error has occurred!");
						System.out.println("Error getting the current answer");
					}
					questionNo++;
					// questionId will be used when supporting random 
					// questions from many questions in the database 
					questionId = String.format("%d", questionNo);
				}
				catch(SQLException e)
				{
					System.out.print("SQL Exception caught : " + e.getMessage());
				}
			}
		} else {
			// First question, set defaults
			questionNo = 1;
			questionId = String.format("%d", questionNo);
		}
		if (questionNo > maxQuestions) {
			// The user has just answered the last question
			nvpairs.put("use-template", "game-over");  // This will cause the controller to behave differently
			awardGameScore(totalScore);
			nvpairs.put("summary", String.format("You got %s questions out of %d", totalScore, maxQuestions));	
			nvpairs.put("message", "Game Over!");
			// We have all we need at this point, no need to get the next question this time.
			return nvpairs;
		}
		// Get the next question content
		try
		{
			getQuestionStmt.clearParameters();
			getQuestionStmt.setString(1, questionId);
			rs = getQuestionStmt.executeQuery();
			if (rs.next()) {
				nvpairs.put("question-content", rs.getString("question"));
			} else {
				nvpairs.put("question-content", "Error");
				nvpairs.put("message", "An application error has occurred!");
				System.out.println("Error getting the next question content");
			}
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}		
		nvpairs.put("qno", String.format("%d", questionNo));
		nvpairs.put("qid", questionId);
		nvpairs.put("total", Integer.toString(totalScore));
		return nvpairs;
	}
	

	
	
	public void awardGameScore(int finalScore) {
		try
		{
			ResultSet rs = null;
			lastScoreStmt.clearParameters(); //clear prev values
			lastScoreStmt.setString(1, username);
			System.out.println(lastScoreStmt.toString());
			rs = lastScoreStmt.executeQuery();
			
			if (rs.next()) {
				updateScoreStmt.clearParameters();
				updateScoreStmt.setInt(1, finalScore);
				updateScoreStmt.setString(2, username);
				updateScoreStmt.executeUpdate();
			} else {
				insertScoreStmt.clearParameters();
				insertScoreStmt.setString(1, username);
				insertScoreStmt.setInt(2, finalScore);
				insertScoreStmt.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
	}
	
	public Hashtable<String, String> getValuesForWelcome() {
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
								rs.getString("name"), rs.getInt("last_score"), maxQuestions));
			} else {
				nvpairs.put("welcome-msg", String.format("Welcome %s. Let’s start the game.", username));
			}
			nvpairs.put("message", "Click Play to continue!");
		}
		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		return nvpairs;
	}
}
