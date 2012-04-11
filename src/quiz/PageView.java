package quiz;

import java.io.PrintWriter;
import java.util.Hashtable;

abstract public class PageView {
	public abstract void render(PrintWriter out, Hashtable<String, String> nvpairs, String pageName);
}
