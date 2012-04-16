package quiz;

import javax.servlet.http.HttpServletRequest;



public class PageViewFactory {
	public static PageView createView(HttpServletRequest request, String templatesFolder) {
	    String userAgent = request.getHeader("User-Agent");
    	System.out.println("userAgent = [" + userAgent + "]");
    	// Assume default first
    	String templateFolderForBrowser = "browser-desktop";
    	// Then check for other browser types
    	// TODO: untested
    	if (userAgent.matches("iphone")) {
    		templateFolderForBrowser = "browser-mobile";
		}
    	System.out.println("templateFolderForBrowser = [" + templateFolderForBrowser + "]");
    	// Mozilla/5.0 (Windows NT 6.1; WOW64; rv:10.0.2) Gecko/20100101 Firefox/10.0.2
		return new PageViewDesktopBrowser(templatesFolder);
	}

}
