package pcminer;

public class GeneralChair extends CommitteeRole {
  public GeneralChair(ConferenceInstance ci) {
    super(ci);
  }

  public String toString() {
    return "General Chair, " + super.toString();
  }

  public String getRoleJson() {
    return "\"General Chair\"";
  }
}
