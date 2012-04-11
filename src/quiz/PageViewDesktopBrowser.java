package quiz;

import java.io.PrintWriter;
import java.util.Hashtable;


public class PageViewDesktopBrowser extends PageView {

	@Override
	public void render(PrintWriter out,
			Hashtable<String, String> nvpairs, String pageName) {
		// TODO Auto-generated method stub
	    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
		  	      "Transitional//EN\">\n");
		    out.println("<HTML>\n" +
		                "<HEAD><TITLE>Hello</TITLE></HEAD>\n" +
		                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
		                "<H1>Hello World</H1>\n" +
		                "<P>Ready for a Quiz?</P>\n" +
		                "<P>Hello from the PageViewDesktopBrowser</P>\n" +
		                "</BODY></HTML>");					
	}
	
	
}
