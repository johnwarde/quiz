package quiz;

import java.util.Hashtable;

public class GamesManager implements Renderable {
	private String username = "";

	public void setUsername(String playerUsername) {
		username = playerUsername;
	}

	public void awardGameScore(String parameter) {
		// TODO Auto-generated method stub
		// TODO Update database
	}
	
	public Hashtable<String, String> getPageNameValues() {
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();
		// TODO Next line was introduced to get rid of a warning, review.
		nvpairs.put("username", username);
		return nvpairs;
	}
}
