package be.bartdewallef.herbeluister;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Radio implements Serializable{
	
	@Attribute
	private String station;
	
	@Element
	private String stationdetail;
	
	@Element
	private String url;
	
	public String getStation() {
		return station;
	}
	
	public void setStation(String station) {
		this.station = station;
	}
	
	public String getStationdetail() {
		return stationdetail;
	}
	
	public void setStationdetail(String stationdetail) {
		this.stationdetail = stationdetail;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
