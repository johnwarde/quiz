package quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
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
	private static final String cookieValueSeparator = "|";
	private static final String cookieName = "session";
	private static Connection con;
	private static String templatesFolder;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
    }

    
    public void init() {
	    templatesFolder = getServletContext().getRealPath("/WEB-INF/templates");
	    
//    	Connection con = null;
		try
		{
//	    	con = DBConnectionMgr.getInstance().getConnection();			

			// Load (and therefore register) the Database Driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// Get a connection to the database
			con = DriverManager.getConnection("jdbc:odbc:mysqlquiz","root","mysqlpass");
		}

		catch(ClassNotFoundException e)
		{
			System.out.print("Could not load Database Driver : " + e.getMessage());
		}

		catch(SQLException e)
		{
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		finally
		{

		} // end finally
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
	    GamesManager gamesMgr;
	    InPlayManager playMgr;
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
			gamesMgr = new GamesManager(con);
			gamesMgr.setUsername(username);
			nvpairs = gamesMgr.getPageNameValues();
			view.render(out, nvpairs, "welcome", cssref);
			break;
		case "play":
			playMgr = new InPlayManager(con);
		    cookies = request.getCookies();
		    for (int i = 0; i < cookies.length; i++) {
		    	if (cookieName == cookies[i].getName()) {
					response.addCookie(cookies[i]);		    		
		    		break;
				}
			}
		    playMgr.setQuestionNo(request.getParameter("qno"));
		    playMgr.setQuestionId(request.getParameter("qid"));
		    playMgr.setUserAnswer(request.getParameter("answer"));
		    playMgr.setUserTotalScore(request.getParameter("total"));
			nvpairs = playMgr.getPageNameValues();
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
		try
		{
			// if database connection is still open
			if (con != null)
			{
				con.close();
			}
		}
		catch(SQLException ignored)
		{
		}
    }
    
    
//	public Connection getDbConnection() {
//		return con;
//	}		
	
}
