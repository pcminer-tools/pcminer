package pcminer;

public class PCMember extends CommitteeRole {
  public PCMember(ConferenceInstance ci) {
    super(ci);
  }

  public String toString() {
    return "PC Member, " + super.toString();
  }

  public String getRoleJson() {
    return "\"PC Member\"";
  }
}
