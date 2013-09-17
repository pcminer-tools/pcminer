package pcminer;

import java.util.HashMap;
import java.util.Map;

public class ConferenceInstance {
	
	private ConferenceInstance(Conference conference, String year) {
		this.conference = conference;
		this.year = year;
	}
	
	public static ConferenceInstance findOrCreate(Conference conference, String year) {
		ConferenceInstance ci = new ConferenceInstance(conference, year);
		if (!conferenceInstances.containsKey(ci)){
			conferenceInstances.put(ci, ci);
		}
		return conferenceInstances.get(ci);
	}	
		
	public static ConferenceInstance find(String conferenceName, String year){
		if (conferenceName.equals("SIGSOFT FSE")){
			conferenceName = "FSE";
		}
		Conference conference = Conference.find(conferenceName);
		return conferenceInstances.get(new ConferenceInstance(conference, year));
	}
	
	public String getConferenceName(){
		return conference.getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conference == null) ? 0 : conference.hashCode());
		result = prime * result
				+ ((year == null) ? 0 : year.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConferenceInstance other = (ConferenceInstance) obj;
		if (conference == null) {
			if (other.conference != null)
				return false;
		} else if (!conference.equals(other.conference))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return conference + " " + year;
	}
	
	public String toJson() {
		return 
		   "{ \"series\" : \"" + conference + "\", \"year\" : " + year + "}";
	}
	
	private static String getExternalConfName(String confName){
		if (confName.equals("SIGSOFT FSE")){
			return "FSE";
		} else {
			return confName;
		}
	}
	
	public static String toStringAll() {
		return conferenceInstances.keySet().toString();
	}
	
	public static int nrConferenceInstances() {
		return conferenceInstances.keySet().size();
	}

	private final static Map<ConferenceInstance,ConferenceInstance> conferenceInstances =
	  new HashMap<ConferenceInstance,ConferenceInstance>();
	
	private final Conference conference;
	private final String year;
}
