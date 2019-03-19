package net.xj.nutz.ext;

import org.nutz.filepool.NutFilePool;
import org.nutz.mvc.Mvcs;

public class WebFilePool extends NutFilePool{

	public WebFilePool(String homePath,long size) {
		super(webinfPath(homePath),size);
	}
	
	private static String webinfPath(String str) {
		return Mvcs.getServletContext().getRealPath("/WEB-INF")+str;
	}
}
