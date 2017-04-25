package restructo.robot.doc;

import restructo.robot.doc.nav.Location;

public class KeywordCaller extends Caller {

	public KeywordCaller(Keyword origin, Location callerLocation) {
		super(origin, callerLocation);
	}
	
	public String getName(){
		return this.getOrigin().getName();
	}

}
