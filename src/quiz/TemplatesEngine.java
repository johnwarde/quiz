package quiz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class TemplatesEngine {
	private static TemplatesEngine instance;
	private static int maxFileSize = 4096;	
	private static String templatesFolder;	
	private static Hashtable<String, String> templateCache = new Hashtable<String, String>();	
	
	public static TemplatesEngine getInstance() {
		if (null == instance) {
			instance = new TemplatesEngine();
		}
		return instance;
	}


	public void init(HttpServlet servlet) {
		templatesFolder = servlet.getServletContext().getRealPath("/WEB-INF/templates");
	}

	public void render(Hashtable<String, String> nvpairs, String pageName,
			PrintWriter out, HttpServletRequest request) {
		String templateConent;
		String deviceFolder = getTemplateConfigurationForRequest(request, nvpairs);
		String cacheKey = deviceFolder + "/" + pageName;
    	System.out.println("Rendering template = [" + cacheKey + "]"); 		
		if (templateCache.containsKey(cacheKey)) {
			templateConent = templateCache.get(cacheKey);
		} else {
			templateConent = getTemplateFromFile(templatesFolder + "/" + cacheKey + ".html");
			// Add to lazy cache
			templateCache.put(cacheKey, templateConent);
		}
		doRender(nvpairs, templateConent, out);
	}
		
	private TemplatesEngine() {			
	}
	


	private void doRender(Hashtable<String, String> nvpairs,
			String templateConent, PrintWriter out) {
		String rendered = templateConent;
		String replaceValue, placeHolder;
	    Enumeration<String> keys = nvpairs.keys();
	    while(keys.hasMoreElements()) {
	    	placeHolder = keys.nextElement();
	    	replaceValue = nvpairs.get(placeHolder);
	    	placeHolder = "@@" + placeHolder + "@@";
        	rendered = rendered.replaceAll(placeHolder, replaceValue);
        	System.out.println(placeHolder + " = [" + replaceValue + "]");        	
	    }
	    out.println(rendered);		
	}

	
	protected String getTemplateConfigurationForRequest(HttpServletRequest request, Hashtable<String, String> nvpairs) {
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
	    String cssref = request.getContextPath() + "/styles/" + templateFolderForBrowser + "/quiz.css";
    	nvpairs.put("css-file-ref", cssref);
    	return templateFolderForBrowser;
	}

	


	private String getTemplateFromFile(String filename) {
		String templateConent =  null;
		try {
			File file = new File(filename);
			if (file.canRead()) {
				templateConent = readEntireFile(filename);
			}			
		} catch (IOException e) {
			System.out.print(String.format("Error loading template (%s) : %s", filename, e.getMessage()));
			e.printStackTrace();
		}
		return templateConent;
	}


	// Taken from http://rosettacode.org/wiki/Read_entire_file#Java
    private static String readEntireFile(String filename) throws IOException {
        FileReader in = new FileReader(filename);
        StringBuilder contents = new StringBuilder();
        char[] buffer = new char[maxFileSize];
        int read = 0;
        do {
            contents.append(buffer, 0, read);
            read = in.read(buffer);
        } while (read >= 0);
        in.close();
        return contents.toString();
    }		
	
}
