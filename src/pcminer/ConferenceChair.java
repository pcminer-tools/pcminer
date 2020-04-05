package pcminer;

public class ConferenceChair extends CommitteeRole {
  public ConferenceChair(ConferenceInstance ci) {
    super(ci);
  }

  public String toString() {
    return "Conference Chair, " + super.toString();
  }

  public String getRoleJson() {
    return "\"Conference Chair\"";
  }
}
