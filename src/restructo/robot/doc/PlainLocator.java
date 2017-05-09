package restructo.robot.doc;

import java.util.LinkedList;
import java.util.List;

import restructo.robot.doc.nav.Location;

public class PlainLocator {
	private String value;
	private List<Location> location = new LinkedList<>();
	
	public PlainLocator(String value, Location location){
		this.value = value;
		this.location.add(location);
	}
	
	public String getValue() {
		return value;
	}

	public Location[] getLocation() {
		return location.toArray(new Location[location.size()]);
	}

	public void addLocation(Location location){
		this.location.add(location);
	}
	
}
