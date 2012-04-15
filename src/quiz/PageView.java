package quiz;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

abstract public class PageView {
	protected Hashtable<String, String> templates = new Hashtable<String, String>();
	
	public abstract void render(PrintWriter out, Hashtable<String, String> nvpairs, String pageName, String cssRef);
	
	protected void LoadTemplates(String templatesForDevice) {
		File dir = new File(templatesForDevice);
		File[] files = dir.listFiles();
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.toString().endsWith(".html");
		    }
		};
		files = dir.listFiles(fileFilter);
		String key;
		String templateContents;
		try {
			for (File file : files) {
				key = file.getName();
				key = key.substring(0, key.length() - 5);
				templateContents = readEntireFile(file.toString());
				templates.put(key, templateContents);		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void defaultRender(PrintWriter out,
			Hashtable<String, String> nvpairs, String pageName, String cssref) {
		String rendered = templates.get(pageName);
		String replaceValue, placeHolder;
	    Enumeration<String> keys = nvpairs.keys();
	    while(keys.hasMoreElements()) {
	    	placeHolder = keys.nextElement();
	    	replaceValue = nvpairs.get(placeHolder);
	    	placeHolder = "@@" + placeHolder + "@@";
        	System.out.println(placeHolder);
        	rendered = rendered.replaceAll(placeHolder, replaceValue);
	    }
    	rendered = rendered.replaceAll("@@css-file-ref@@", cssref);
	    out.println(rendered);
	}	
	
	// Taken from http://rosettacode.org/wiki/Read_entire_file#Java
    private static String readEntireFile(String filename) throws IOException {
        FileReader in = new FileReader(filename);
        StringBuilder contents = new StringBuilder();
        char[] buffer = new char[4096];
        int read = 0;
        do {
            contents.append(buffer, 0, read);
            read = in.read(buffer);
        } while (read >= 0);
        in.close();
        return contents.toString();
    }	
}
