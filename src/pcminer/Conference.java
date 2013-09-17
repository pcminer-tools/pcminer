package pcminer;

import java.util.HashMap;
import java.util.Map;

public class Conference {
	private Conference(String name){
		this.name = name; 
	}
	
	public static Conference findOrCreate(String name){
		if (name.equals("SIGSOFT FSE")){
			name = "FSE";
		}
		Conference c = new Conference(name);
		if (!conferences.containsKey(c)){
			conferences.put(c,c);
		}
		return conferences.get(c);
	}
	
	public static Conference find(String name){
		return conferences.get(new Conference(name));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Conference other = (Conference) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (name.equals("SIGSOFT FSE")){
			return "FSE";
		} else {
			return name;
		}
	}
	
	public String getName() { 
		return name;
	}

	private static Map<Conference,Conference> conferences = new HashMap<Conference,Conference>();
	
	private final String name; 
}
