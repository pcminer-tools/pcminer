package pcminer;

public class ProgramBoardMember extends CommitteeRole {
  public ProgramBoardMember(ConferenceInstance ci) {
    super(ci);
  }

  public String toString() {
    return "Program Board Member, " + super.toString();
  }

  public String getRoleJson() {
    return "\"Program Board Member\"";
  }
}
