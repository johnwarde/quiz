package quiz;

import java.io.PrintWriter;
import java.util.Hashtable;




public class PageViewDesktopBrowser extends PageView {
//	private String templateHeader = 
//			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" +
//			"<HTML>\n" +
//            "<HEAD><TITLE>Quiz</TITLE></HEAD>\n" +
//            "<BODY BGCOLOR=\"#FDF5E6\">\n";
//	private String templateFooter = 
//            "</BODY>" + 
//            "</HTML>";

	
	public PageViewDesktopBrowser(String templatesFolder) {
		super();
		LoadTemplates(templatesFolder + "/browser-desktop");
	}
	
	@Override
	public void render(PrintWriter out,
			Hashtable<String, String> nvpairs, String pageName, String cssref) {
		defaultRender(out, nvpairs, pageName, cssref);
	}
	
    
	
}
