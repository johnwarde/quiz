package quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public void init() {
		try
		{
			// Load (and therefore register) the Database Driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// Get a connection to the database
			con = DriverManager.getConnection("jdbc:odbc:mysqlquiz","root","mysqlpass");

			Statement stmt = null;
			ResultSet rs = null;
			
			// Create a Statement object
			stmt = con.createStatement();

			// Execute a SQL query, get a ResultSet
			rs = stmt.executeQuery("SELECT id, name, last_score FROM `quiz`.`players`;");

			// while there are more records in the table to be searched
			while(rs.next())
			{
				System.out.print("id = [" + rs.getString("id") + "]; name = [" + rs.getString("name") + "]; last_score = [" + rs.getString("last_score") + "]");
			}
		}
		catch(ClassNotFoundException e)
		{
//			out.println("Could not load Database Driver : " + e.getMessage());
			System.out.print("Could not load Database Driver : " + e.getMessage());
		}
		catch(SQLException e)
		{
//			out.println("SQL Exception caught : " + e.getMessage());
			System.out.print("SQL Exception caught : " + e.getMessage());
		}
		finally
		{
			// Always close the database connection
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

		} // end finally
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String requestURL = request.getRequestURL().toString();
	    String contextPath = request.getContextPath();
	    String pathInfo = request.getPathInfo();
	    String servletPath = request.getServletPath();

	    
	    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	  	      "Transitional//EN\">\n");
	    out.println("<HTML>\n" +
	                "<HEAD><TITLE>Hello</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                "<H1>Hello World</H1>\n" +
	                "<P>Ready for a Quiz?</P>\n" +
	                "<P>requestURL = [" + requestURL + "]</P>\n" +
	                "<P>contextPath = [" + contextPath + "]</P>\n" +
	                "<P>pathInfo = [" + pathInfo + "]</P>\n" +
	                "<P>servletPath = [" + servletPath + "]</P>\n" +
	                "</BODY></HTML>");		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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

}
