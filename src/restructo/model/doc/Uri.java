package restructo.model.doc;

import java.io.File;

public class Uri {
	private String path;

	private Uri(String path){
		this.path = path;
	}
	
	public static Uri parseUri(String path){
		path = path.replaceAll("[\\\\/]", File.pathSeparator);
		return new Uri(path);
	}
	
	public String getPath() {
		return path;
	}
	
}
