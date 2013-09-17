package pcminer;

public class ProgramChair extends CommitteeRole {
	public ProgramChair(ConferenceInstance ci){
		super(ci);
	}
	
	public String toString(){
		return "Program Chair, " + super.toString();
	}
	
	public String getRoleJson(){
		return "\"Program Chair\"";
	}
}
