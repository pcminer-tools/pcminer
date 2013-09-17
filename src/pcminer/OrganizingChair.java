package pcminer;

public class OrganizingChair extends CommitteeRole {
	public OrganizingChair(ConferenceInstance ci){
		super(ci);
	}
	
	public String toString(){
		return "Organizing Chair, " + super.toString();
	}
	
	public String getRoleJson(){
		return "\"Organizing Chair\"";
	}
}
