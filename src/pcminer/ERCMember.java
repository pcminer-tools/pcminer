package pcminer;

public class ERCMember extends CommitteeRole {
  public ERCMember(ConferenceInstance ci) {
    super(ci);
  }

  public String toString() {
    return "ERC Member, " + super.toString();
  }

  public String getRoleJson() {
    return "\"ERC Member\"";
  }
}
