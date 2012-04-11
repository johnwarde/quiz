package quiz;

import javax.servlet.http.HttpServletRequest;



public class PageViewFactory {
	public static PageView createView(HttpServletRequest request) {
		// TODO: Determine appropiate PageView class from the contents of request i.e. User Agent value etc.
		return new PageViewDesktopBrowser();
//		assert false : "Unable to determine User Agent / Page View Type";
//		return null;
	}

}
