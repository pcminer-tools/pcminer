package pcminer;

import java.util.ArrayList;
import java.util.List;

public final class Publication {

  public Publication(
      String title,
      ConferenceInstance conferenceInstance,
      String pages,
      List<Author> authors,
      String session) {
    this.title = title;
    this.conferenceInstance = conferenceInstance;
    this.pages = pages;
    this.authors = new ArrayList<Author>(authors);
    this.session = session;
  }

  @Override
  public String toString() {
    return "Publication ["
        + title
        + " "
        + authors
        + " "
        + conferenceInstance
        + " "
        + pages
        + ", session="
        + session
        + "]";
  }

  public String toJson(int indent) {
    String jsonTitle = title.replace("\\", "\\\\").replace("\"", "\\\"");
    return repeat(" ", indent)
        + "  {\n"
        + repeat(" ", indent + 2)
        + "\"title\" : "
        + "\""
        + jsonTitle
        + "\",\n"
        + repeat(" ", indent + 2)
        + "\"authors\" : "
        + toJson(authors)
        + ",\n"
        + repeat(" ", indent + 2)
        + "\"conference\" : "
        + conferenceInstance.toJson()
        + ",\n"
        + repeat(" ", indent + 2)
        + " \"pages\" : "
        + "\""
        + pages
        + "\",\n"
        + repeat(" ", indent + 2)
        + " \"session\" : "
        + "\""
        + session
        + "\""
        + "\n"
        + repeat(" ", indent)
        + " }";
  }

  private static String toJson(List<Author> authors) {
    String result = "[ ";
    for (Author author : authors) {
      result += ("\"" + author.getName() + "\"");
      result += ", ";
    }
    if (result.endsWith(", ")) { // remove last comma
      result = result.substring(0, result.length() - 2);
    }
    result += " ]";
    return result;
  }

  private static final String repeat(String s, int num) {
    String result = "";
    for (int i = 0; i < num; i++) {
      result += s;
    }
    return result;
  }

  public Object getConferenceName() {
    return conferenceInstance.getConferenceName();
  }

  private final String title;
  private final ConferenceInstance conferenceInstance;
  private final String pages;
  private final String session;
  private final List<Author> authors;
}
