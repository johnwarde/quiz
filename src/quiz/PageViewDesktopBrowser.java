package quiz;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;



public class PageViewDesktopBrowser extends PageView {

	@Override
	public void render(PrintWriter out,
			Hashtable<String, String> nvpairs, String pageName) {

		String templateWelcome = 
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" +
				"<HTML>\n" +
	                "<HEAD><TITLE>Hello</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                	"<H1>Hello World</H1>\n" +
	                	"<P>@@welcomemsg-line1@@</P>\n" +
	                	"<P>@@welcomemsg-line2@@</P>\n" +
	                "</BODY>" + 
	            "</HTML>";
		String rendered = templateWelcome;
		String replaceValue;
	    Enumeration<String> keys = nvpairs.keys();
	    while(keys.hasMoreElements()) {
	    	replaceValue = nvpairs.get(keys.nextElement());
        	System.out.println(replaceValue);
        	rendered = rendered.replaceAll(keys.toString(), replaceValue);
	    }
	    out.println(rendered);
	}
	
	
}
