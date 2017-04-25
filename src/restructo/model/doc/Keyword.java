package restructo.model.doc;

import restructo.model.doc.nav.Location;

public class Keyword extends Member {
	
	private String[] body;
	
	public Keyword(String name, Location location, String[] body) {
		super(name, location);
		this.body = body;
	}
	
	public String getFullName(){
		String path = this.getLocation().getUri().getRawPath();
		String[] paths = path.split("/");
		String name = paths[paths.length-1];
		name = name.replace("\\.\\w+$", "");
		return name + "." + this.getName();
	}
	
	public String[] getBody(){
		return this.body;
	}

}
