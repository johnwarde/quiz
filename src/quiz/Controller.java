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

//import quiz.DBConnectionMgr;

/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String cookieValueSeparator = "$";
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
	    GamesManager gamesMgr = new GamesManager();;
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
			gamesMgr.setUsername(username);
			nvpairs = gamesMgr.getValuesForWelcome();
			view.render(out, nvpairs, "welcome", cssref);
			break;
		case "play":
		    cookies = request.getCookies();
		    for (int i = 0; i < cookies.length; i++) {
		    	if (cookieName.equalsIgnoreCase(cookies[i].getName())) {
		    		// TODO: test to make sure this happens!
		    		String cookieValue = cookies[i].getValue();
		    		String[] cookieParts = cookieValue.split(cookieValueSeparator);
		    		gamesMgr.setUsername(cookieParts[0]);
		    		//gamesMgr.setUsername(cookieValue.split(cookieValueSeparator)[0]);
					response.addCookie(cookies[i]);		    		
		    		break;
				}
			}
		    gamesMgr.setQuestionNo(request.getParameter("qno"));
		    gamesMgr.setQuestionId(request.getParameter("qid"));
		    gamesMgr.setUserAnswer(request.getParameter("answer"));
		    gamesMgr.setUserTotalScore(request.getParameter("total"));
			nvpairs = gamesMgr.getValuesForSequence();
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
