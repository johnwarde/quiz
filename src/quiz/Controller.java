package quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.buf.Base64;


/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String cookieValueSeparator = "|";
	private static final String cookieName = "session";
	private static String templatesFolder;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
    }

    
    public void init() {
	    templatesFolder = getServletContext().getRealPath("/WEB-INF/templates");
    	DBConnectionMgr.getInstance(); // Will create the connection to the DB
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}
	
	
	/**
	 * @see HttpServlet#doRequest(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getServletPath().substring(1);
	    String cssref = request.getContextPath() + "/styles/browser-desktop/quiz.css";
	    GameManager gameMgr = new GameManager();;
	    PageView view = PageViewFactory.createView(request, templatesFolder);
	    Cookie[] cookies;
		Hashtable<String, String> nvpairs;
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    switch (action) {
		case "logon":
			// New Logon
		    String encodedAuth = request.getHeader("authorization").split(" ")[1];
			String username = Base64.base64Decode(encodedAuth).split(":")[0];
			Cookie newCookie = new Cookie(cookieName, username + cookieValueSeparator + generateSessionID());
			response.addCookie(newCookie);
			gameMgr.setUsername(username);
			nvpairs = gameMgr.getValuesForWelcome();
			view.render(out, nvpairs, "welcome", cssref);
			break;
		case "play":
		    cookies = request.getCookies();
		    for (int i = 0; i < cookies.length; i++) {
		    	if (cookieName.equalsIgnoreCase(cookies[i].getName())) {
		    		String cookieValue = cookies[i].getValue();
		    		gameMgr.setUsername(cookieValue.split("\\" + cookieValueSeparator)[0]);
					response.addCookie(cookies[i]);		    		
		    		break;
				}
			}
		    gameMgr.setQuestionNo(request.getParameter("qno"));
		    gameMgr.setQuestionId(request.getParameter("qid"));
		    gameMgr.setUserAnswer(request.getParameter("answer"));
		    gameMgr.setUserTotalScore(request.getParameter("total"));
			nvpairs = gameMgr.getValuesForSequence();
			view.render(out, nvpairs, nvpairs.get("use-template"), cssref);
			// TODO: use javascript/jQuery to validate that an answer was selected
			break;
		default:
			break;
		}	
	}
	
	
	public static String generateSessionID() {
		java.rmi.server.UID uid = new java.rmi.server.UID();
		String sessionID = uid.toString();
		return sessionID;
	}	


    public void destroy() {
    	DBConnectionMgr.getInstance().close();    	
    }

}
