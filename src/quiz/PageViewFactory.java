package quiz;

import javax.servlet.http.HttpServletRequest;



public class PageViewFactory {
	public static PageView createView(HttpServletRequest request, String templatesFolder) {
		// TODO: Determine appropriate PageView class from the contents 
		//        of request i.e. User Agent value etc.
		return new PageViewDesktopBrowser(templatesFolder);
//		assert false : "Unable to determine User Agent / Page View Type";
//		return null;
	}

}
