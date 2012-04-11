package quiz;

import java.util.Hashtable;

public class InPlayManager implements Renderable {
	private static final int maxQuestions = 10;
	private String username = "";
	private String questionNo = "";
	private String questionId = "";
	private String userAnswer = "";

	public void setUsername(String playerUsername) {
		username = playerUsername;
	}

	public void setQuestionNo(String qno) {
		questionNo = qno;
	}

	public void setQuestionId(String qid) {
		questionId = qid;
	}	

	public void setUserAnswer(String answer) {
		userAnswer = answer;
	}	
	
	@Override
	public Hashtable<String, String> getPageNameValues() {
		Hashtable<String, String> nvpairs = new Hashtable<String, String>();		
		// TODO Auto-generated method stub
		// TODO Next lines were introduced to get rid of warnings, review.
		nvpairs.put("username", username);
		nvpairs.put("qno", questionNo);
		nvpairs.put("qid", questionId);
		nvpairs.put("answer", userAnswer);
		nvpairs.put("maxQuestions", Integer.toString(maxQuestions));
		return nvpairs;
	}


	
}
