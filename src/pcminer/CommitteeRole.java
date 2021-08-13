package pcminer;

public abstract class CommitteeRole {
  public CommitteeRole(ConferenceInstance ci) {
    this.conferenceInstance = ci;
  }

  public ConferenceInstance getConferenceInstance() {
    return conferenceInstance;
  }

  public String toString() {
    return conferenceInstance.toString();
  }

  public String toJson(int indent) {
    return Util.repeat(" ", indent)
        + "{ "
        + "\"role\" : "
        + getRoleJson()
        + ", "
        + "\"conference\" : "
        + conferenceInstance.toJson()
        + " }";
  }

  public abstract String getRoleJson();

  private final ConferenceInstance conferenceInstance;
}
