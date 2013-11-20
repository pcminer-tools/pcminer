package pcminer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Author implements Comparable<Author> {
	
	public static Author findOrCreate(String name){
		Author author = new Author(normalize(name));
		if (!authors.containsKey(author)){
			authors.put(author, author);
		}
		return authors.get(author);
		
	}
	
	public static boolean authorExists(String name){
		return authors.keySet().contains(new Author(name));
	}
	
	private Author(String name){
		this.name = name;
		this.firstName = (name.indexOf(" ") != -1) ? name.substring(0, name.lastIndexOf(" ")) : "";
		this.lastName = (name.indexOf(" ") != -1) ? name.substring(name.lastIndexOf(" ") + 1) : name;
		this.publications = new HashSet<Publication>();
		this.committees = new HashSet<CommitteeRole>();
	}
	
	public void addPublication(Publication pub){
		publications.add(pub);
	}
	
	public void addPCMembership(ConferenceInstance ci){
		committees.add(new PCMember(ci));
	}
	
	public void addGeneralChair(ConferenceInstance ci){
		committees.add(new GeneralChair(ci));
	}
	
	public void addProgramChair(ConferenceInstance ci){
		committees.add(new ProgramChair(ci));
	}

	public void addConferenceChair(ConferenceInstance ci){
		committees.add(new ConferenceChair(ci));
	}
	
	public void addOrganizingChair(ConferenceInstance ci){
		committees.add(new OrganizingChair(ci));
	}
	
	public void addERCMember(ConferenceInstance ci){
		committees.add(new ERCMember(ci));
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Set<Publication> getPublications(){
		return publications;
	}
	
	public Set<CommitteeRole> getCommittees(){
		return committees;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Author other = (Author) obj;
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Author o) { 
		if (!lastName.equals(o.lastName)){
			return this.lastName.compareTo(o.lastName);
		} else if (!firstName.equals(o.firstName)){
			return this.firstName.compareTo(o.firstName);
		} else {
			return 0;
		}
	}
	
	public String toJson(int indent) {
		return Util.repeat(" ", indent) + "  \"author\" : \"" + name + "\"";
	}
	
	private final String name;
	private final String lastName;
	private final String firstName; // note: includes any middle initials
	private final Set<Publication> publications;
	private final Set<CommitteeRole> committees;
	
	
	private static Map<String,String> nameMappings = new HashMap<String,String>();
	private static Map<Author,Author> authors = new HashMap<Author,Author>();

	public static void addNameMapping(String from, String to){
		nameMappings.put(from, to);
	}
	
	public static Set<Author> getAuthors(){
		return new HashSet<Author>(authors.keySet());
	}
	
	public static int nrAuthors(){
		int count = 0;
		for (Author a : authors.keySet()){
			if (a.publications.size() > 0) count++;
		}
		return count;
	}
	
	public Set<Author> allAuthors(){
		return authors.keySet();
	}
	
	public static int nrCommitteeMembers(){
		int count = 0;
		for (Author a : authors.keySet()){
			if (a.committees.size() > 0) count++;
		}
		return count;
	}
	
	/**
	 * Annoyingly, there are multiple ways to encode accented characters in HTML.
	 * For example, an a-with-umlaut may be encoded using either "&#228;" or "&auml;".
	 * This function normalizes them into a common form. 
	 * 
	 * A complete can be found here: http://www.ascii.cl/htmlcodes.htm
	 */
	private static String normalize(String authorName){
		String result = authorName;
		result = result.replace("&#193;","&Aacute;");
		result = result.replace("&#197;","&Aring;");
		result = result.replace("&#198;","&AElig;");
		result = result.replace("&#199;","&Ccedil;");
		result = result.replace("&#201;","&Eacute;");
		result = result.replace("&#211;","&Oacute;");
		result = result.replace("&#214;","&Ouml;");
		result = result.replace("&#216;","&Oslash;");
		result = result.replace("&#218;","&Uacute;");
		result = result.replace("&#223;","&szlig;");
		result = result.replace("&#224;","&agrave;");
		result = result.replace("&#225;","&aacute;");
		result = result.replace("&#226;","&acirc;");
		result = result.replace("&#227;","&atilde;");
		result = result.replace("&#228;","&auml;");
		result = result.replace("&#229;","&aring;");
		result = result.replace("&#230;","&aelig;");
		result = result.replace("&#231;", "&ccedil;");
		result = result.replace("&#232;", "&egrave;");
		result = result.replace("&#233;", "&eacute;");
		result = result.replace("&#234;", "&ecirc;");
		result = result.replace("&#235;", "&euml;");
		result = result.replace("&#237;", "&iacute;");
		result = result.replace("&#238;", "&icirc;");
		result = result.replace("&#239;", "&iuml;");
		result = result.replace("&#240;", "&eth;");
		result = result.replace("&#241;", "&ntilde;");
		result = result.replace("&#242;", "&ograve;");
		result = result.replace("&#243;", "&oacute;");
		result = result.replace("&#244;", "&ocirc;");
		result = result.replace("&#245;", "&otilde;");
		result = result.replace("&#246;", "&ouml;");
		result = result.replace("&#248;", "&oslash;");
		result = result.replace("&#250;", "&uacute;");
		result = result.replace("&#251;", "&ucirc;");
		result = result.replace("&#252;", "&uuml;");
		result = result.replace("&#253;", "&yacute;");
		
		result = result.replace("&#0193;","&Aacute;");
		result = result.replace("&#0197;","&Aring;");
		result = result.replace("&#0198;","&AElig;");
		result = result.replace("&#0199;","&Ccedil;");
		result = result.replace("&#0201;","&Eacute;");
		result = result.replace("&#0211;","&Oacute;");
		result = result.replace("&#0214;","&Ouml;");
		result = result.replace("&#0216;","&Oslash;");
		result = result.replace("&#0218;","&Uacute;");
		result = result.replace("&#0223;","&szlig;");
		result = result.replace("&#0224;","&agrave;");
		result = result.replace("&#0225;","&aacute;");
		result = result.replace("&#0226;","&acirc;");
		result = result.replace("&#0227;","&atilde;");
		result = result.replace("&#0228;","&auml;");
		result = result.replace("&#0229;","&aring;");
		result = result.replace("&#0230;","&aelig;");
		result = result.replace("&#0231;", "&ccedil;");
		result = result.replace("&#0232;", "&egrave;");
		result = result.replace("&#0233;", "&eacute;");
		result = result.replace("&#0234;", "&ecirc;");
		result = result.replace("&#0235;", "&euml;");
		result = result.replace("&#0237;", "&iacute;");
		result = result.replace("&#0238;", "&icirc;");
		result = result.replace("&#0239;", "&iuml;");
		result = result.replace("&#0240;", "&eth;");
		result = result.replace("&#0241;", "&ntilde;");
		result = result.replace("&#0242;", "&ograve;");
		result = result.replace("&#0243;", "&oacute;");
		result = result.replace("&#0244;", "&ocirc;");
		result = result.replace("&#0245;", "&otilde;");
		result = result.replace("&#0246;", "&ouml;");
		result = result.replace("&#0248;", "&oslash;");
		result = result.replace("&#0250;", "&uacute;");
		result = result.replace("&#0251;", "&ucirc;");
		result = result.replace("&#0252;", "&uuml;");
		result = result.replace("&#0253;", "&yacute;");
		
		if (result.contains("&#")){
			System.err.println("WARNING: unmapped character in author name: " + authorName);
		}
		
		if (nameMappings.containsKey(result)){
			result = nameMappings.get(result);
		}
		
		return result;
	}
}
