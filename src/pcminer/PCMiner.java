package pcminer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;

/**
 * This program is a simple utility for doing some analysis on publication and program committee data. 
 * It generates a JSON file "data.js" that is stored in the "ui/js" subdirectory of this project.
 * 
 * (c) 2009-2013 Frank Tip
 *
 */
public class PCMiner {
	
	private List<String> conferences = new ArrayList<String>();
	private Map<String,String> confColors = new HashMap<String,String>();
	private String theConference = null;
	private String theYear = null;
	private String theSession = null;
	private String theTitle = null;
	private String thePageNums = null;
	private List<Author> theAuthors = new ArrayList<Author>();
	 
	private Set<Publication> publications = new HashSet<Publication>();
	
	public static void main(String[] args) throws IOException {
		if (args.length != 0){
			System.err.println("usage: PCMiner");
			System.exit(1);
		} 
		(new PCMiner()).process();
	}
	
	void process()  throws IOException {
		String inputDirectory = "." + File.separator + "data";
		String outputDirectory = "." + File.separator + "ui" + File.separator + "js"; 
		
		scan(inputDirectory);
		
		String jsonFileName = outputDirectory + File.separator + "data.js";
		FileOutputStream jsonFos = new FileOutputStream(jsonFileName);
		PrintStream jsonStream = new PrintStream(jsonFos);
		printJson(jsonStream);
	}
	
	/**
	 * scans the input directory to find conference data. It is assumed that the input
	 * directory contains a subdirectory for each conference (e.g., "PLDI"), which
	 * has subdirectories for each year (e.g., "1999", "2000", etc.). The main
	 * conference directory is also assumed to contain a file "color", which contains
	 * the name or hex-code to be used for that conference (e.g., the text "blue"
	 * without surrounding quotes). Each year-subdirectory under each conference 
	 * directory is assumed to contain two files: an HTML file that was copied from
	 * DBLP (e.g., "pldi2010.html") and a text file "pldi2010-pc.txt" containing the
	 * list of committee members for that year. Names of committee members must
	 * match the names used for authors in DBLP, or duplicate entries will start
	 * appearing in the UI.
	 */
	private void scan(String dirName) throws IOException {
		File directory = new File(dirName);
		if (directory.exists() && directory.isDirectory()){
			int count = 1;
			File[] files = directory.listFiles();
			for (int i=0; i < files.length; i++){
				File subDir = files[i];
				if (subDir.isDirectory()){ // one subdirectory for each conference
					theConference = subDir.getName();
					conferences.add(theConference);
					File[] subDirFiles = subDir.listFiles();
					for (int j=0; j < subDirFiles.length; j++){
						File file = subDirFiles[j];
						if (file.isDirectory()){ // one subdirectory for each year
							theYear = file.getName();
							File[] subsubDirFiles = file.listFiles();
							for (int k=0; k < subsubDirFiles.length; k++){
								File file2 = subsubDirFiles[k];
								String fileName = file2.getName();
								if (fileName.endsWith(".html")){ // the file from DBLP
									System.err.print(theConference + theYear + " ");
									if (count++ % 10 == 0) System.err.println();
									scanConference(file2);
								} else if (fileName.endsWith("-pc.txt")){ // the file containing PC members
									Conference conf = Conference.findOrCreate(theConference);
									ConferenceInstance ci = ConferenceInstance.findOrCreate(conf, theYear);
									scanCommitteeFile(file2, ci); 								
								}
							}
						} else if (file.getName().equals("color")){ // the file containing the color code
							FileReader fr = new FileReader(file);
							BufferedReader br = null;
							try {
								br = new BufferedReader(fr);
								if (br.ready()){ // only read first line
									String colorName = br.readLine().trim(); 
									confColors.put(theConference, colorName);
								}
							} finally {
								br.close();
							}
						}
					}
				}
			}
			System.err.println();
		}
	}
	
	/**
	 * helper function to scan the HTML files from DBLP.
	 */
	private void scanConference(File file){
		Parser parser;
		try {
			parser = new Parser (file.getAbsolutePath());
			NodeList list = parser.parse (null);
			for (SimpleNodeIterator it = list.elements(); it.hasMoreNodes(); ){
				Node node = it.nextNode();
				node.accept(new NodeVisitor() {
					 
					@Override
					public void visitTag(Tag tag) {
						if (tag instanceof BulletList){
							BulletList bl = (BulletList)tag; 
							for (int i=0; i < bl.getChildren().size(); i++){
								Node child = bl.childAt(i);
								if (child instanceof TagNode){
									String classAtt = ((TagNode)child).getAttribute("class");
									if (classAtt != null && classAtt.equals("entry inproceedings")){ 
										theAuthors = new ArrayList<Author>();
										findTitle(child);
										findAuthors(child);
										findPageNums(child);
									}
								}
							}
						} else if (tag instanceof HeadingTag){
							HeadingTag ht = (HeadingTag)tag; 
							if (ish2(ht)){
								for (SimpleNodeIterator it = ht.elements(); it.hasMoreNodes(); ){
									Node node = it.nextNode();
									if (node instanceof TextNode){
										
										TextNode tn = (TextNode)node;
										String sessionName = tn.getText();
										sessionName = sessionName.replace("\n", ""); // apparently, some session names contain line breaks
										theSession = sessionName;
									}
								}
							}
						}
						super.visitTag(tag);
					}

					private boolean ish2(HeadingTag ht) {
						return ht.getRawTagName().equals("h2");
					}
				});	
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * helper function to find the title of a paper.
	 */
	private void findTitle(Node node){
		node.accept(new NodeVisitor() {
			 
			@Override
			public void visitTag(Tag tag) {
				if (tag.getAttribute("class") != null && tag.getAttribute("class").equals("title")){ 
					String title = tag.getChildren().elementAt(0).getText();
					theTitle = title.substring(0, title.length()-1); 
				}
				super.visitTag(tag);
			}
		});
	}
		
	/**
	 * helper function to find the authors of a paper.
	 */
	private void findAuthors(Node node){
		node.accept(new NodeVisitor() {
			 
			@Override
			public void visitTag(Tag tag) {
				String tagName = tag.getTagName(); 
				if (tagName.equals("A")){
					Node child = tag.getChildren().elementAt(0);
					if (child instanceof Text){
						String authorName = ((Text)child).getText(); 
						Author author = Author.findOrCreate(authorName);
						theAuthors.add(author);
					}
				}
				super.visitTag(tag);
			}
		});
	}
	
	/**
	 * helper function to find the page numbers of a paper.
	 */
	private void findPageNums(Node node){
		node.accept(new NodeVisitor() {
			 
			@Override
			public void visitTag(Tag tag) {
				String tagName = tag.getTagName(); 
				 
				if (tagName.equals("DIV")){
					if (tag.getAttribute("class") != null && tag.getAttribute("class").equals("data")){
						NodeList nodeList = tag.getChildren();
						thePageNums = nodeList.elementAt(nodeList.size()-1).getText(); 
						Conference conf = Conference.findOrCreate(theConference);
						ConferenceInstance ci = ConferenceInstance.findOrCreate(conf, theYear);
						Publication pub = new Publication(theTitle, ci, thePageNums, theAuthors, theSession);
						for (Author author : theAuthors){ 						
							author.addPublication(pub);
						}
						publications.add(pub);
					}
				}
				super.visitTag(tag);
			}
		});
	}
			
	/**
	 * Reads in program committee information. This needs to be entered
	 * manually in a text file, one name per line. Note that the name needs 
	 * to the match name as it occurs in DBLP pages. Names can be prefixed 
	 * with:
	 * <dl> 
	 *   <li>"P:" - program chair</li>
	 *   <li>"G:" - general chair</li>
	 *   <li>"O:" - organizing chair</li>
	 *   <li>"E:" - ERC member</li>
	 *   <li>(default is PC member)</li>
	 * </dl>
	 */
	private void scanCommitteeFile(File file, ConferenceInstance ci) throws IOException {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			while (br.ready()){
				String authorName = br.readLine().trim();
				if (authorName.equals("")) continue; // skip blank lines
				if (authorName.startsWith("G:")){
					authorName = authorName.substring(2);
					Author author = Author.findOrCreate(authorName); 
					author.addGeneralChair(ci);
				} else if (authorName.startsWith("P:")){
					authorName = authorName.substring(2);
					Author author = Author.findOrCreate(authorName); 
					author.addProgramChair(ci);
				} else if (authorName.startsWith("C:")){
					authorName = authorName.substring(2);
					Author author = Author.findOrCreate(authorName); 
					author.addConferenceChair(ci);
				} else if (authorName.startsWith("O:")){
					authorName = authorName.substring(2);
					Author author = Author.findOrCreate(authorName); 
					author.addOrganizingChair(ci);
				} else if (authorName.startsWith("E:")){
					authorName = authorName.substring(2);
					Author author = Author.findOrCreate(authorName); 
					author.addERCMember(ci);
				} else {
					Author author = Author.findOrCreate(authorName); 
					author.addPCMembership(ci);
				}
			}
		}  finally {
			br.close();
		}
	}
	
	/**
	 * generate data.js file 
	 */
	private void printJson(PrintStream stream){
		dumpStatistics(stream);
		initializeConferences(stream);
		dumpAuthorsJson(stream, 0); 
	}
	
	/**
	 * generate author data in JSON format 
	 */
	private void dumpAuthorsJson(PrintStream stream, int indent) {
		
		stream.println("list = [\n");
		SortedSet<Author> sortedAuthors = new TreeSet<Author>(Author.getAuthors());
		int count = 0;
		for (Author author : sortedAuthors){
			count++;
			if (!author.getPublications().isEmpty() || !author.getCommittees().isEmpty()){
				stream.println("{\n" + 
				author.toJson(indent) + ",");
				stream.println(Util.repeat(" ", indent+2) + "\"publications\" : ["); 
				for (Publication pub : author.getPublications()){
					stream.println(pub.toJson(indent+4));
				}
				stream.println(Util.repeat(" ", indent+2) + "],");
				stream.println(Util.repeat(" ", indent+2) + "\"committees\" : ["); 
				for (CommitteeRole c : author.getCommittees()){
					stream.println(c.toJson(indent+4));
				}
				stream.println(Util.repeat(" ", indent+2) + "]");
				if (count < sortedAuthors.size()){
					stream.println("},");
				} else {
					stream.println("}");
				}
			}
		}
		stream.println("]\n");
	}
	
	/**
	 * generate comments in data.js that gives some statistics on the analyzed conferences
	 */
	private void dumpStatistics(PrintStream stream) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(); 
		stream.println("/* file data.js generated on " + dateFormat.format(date));
		int n = ConferenceInstance.nrConferenceInstances();
		stream.println("  " + n + " Conferences analyzed: " + ConferenceInstance.toStringAll());
		stream.println("  " + Author.nrAuthors() + " distinct authors");
		stream.println("  " + Author.nrCommitteeMembers() + " distinct PC Members");
		stream.println("  " + publications.size() + " publications");
		stream.println("*/");
	}
	
	/**
	 * generate code in data.js that adds the supported conferences to the user-interface 
	 * @param stream
	 */
	private void initializeConferences(PrintStream stream) {
		stream.println("");
		stream.println("/* INITIALIZE SUPPORTED CONFERENCES: */");
		
		// initialize conferences
		for (String confName : conferences){
			stream.println("pcminer.addConference(\"" + confName + "\", true);");
		}
		// initialize colors separately (not every conference may have a specified color)
		for (String confName : confColors.keySet()){
			String color = confColors.get(confName);
			stream.println("pcminer.addConfColor(\"" + confName + "\", \"" + color + "\");");
		}
		stream.println("");
	}
}