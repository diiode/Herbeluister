package be.bartdewallef.herbeluister;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class RadioStations implements Serializable {

	@ElementList
	public List<Radio> radios = new ArrayList<Radio>();
}
