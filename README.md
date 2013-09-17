pcminer
=======

PC-Miner is a tool that analyzes publications and program committee service in the top conferences 
in Programming Languages and Software Engineering. Currently, the tool supports the following 
conferences: ASE, ECOOP, FSE, ICSE, ISSTA, OOPSLA, PLDI, and POPL and it covers the years 1995-2014. 
Publication data was obtained from [DBLP](http://www.informatik.uni-trier.de/~ley/db/), and program committee data was scraped from various 
conference web sites. 

I. Using PC-Miner
------------------

To start using PC-miner, go into the "ui" subdirectory, and open the index.html file in your browser.
This should bring up a window that looks like the following
[screenshot](screenshot.jpg).

In the selector on the left is an alphabetical list of all authors. Clicking on
an author will display his publication and committee activity.  You can
search (awkwardly) by entering some text in the search box.
Entering "Jan" there will reduce the list to all authors whose name starts
with the characters "Jan". When you click on Jan Vitek's name, you will see 
his impressive record:

publications
 - Jan Vitek, R. Nigel Horspool, Andreas Krall: Efficient Type Inclusion Tests, OOPSLA 1997 (session = Language Implementation), pp. 142-157
 - James Noble, Jan Vitek, John Potter: Flexible Alias Protection, ECOOP 1998 (session = Language Problems and Solutions), pp. 158-185
 - Jan Vitek, Boris Bokowski: Confined Types, OOPSLA 1999 (session = Panel), pp. 82-96
 - Christian Grothoff, Jens Palsberg, Jan Vitek: Encapsulating Objects with Confined Types, OOPSLA 2001 (session = Modules), pp. 241-253
 - Krzysztof Palacz, Jan Vitek, Grzegorz Czajkowski, Laurent Daynès: Incommunicado: efficient communication for isolates, OOPSLA 2002 (session = Scalability), pp. 262-274
 - Tian Zhao, Jens Palsberg, Jan Vitek: Lightweight confinement for featherweight Java, OOPSLA 2003 (session = Generics), pp. 135-148
 - Krzysztof Palacz, Jan Vitek: Java Subtype Tests in Real-Time, ECOOP 2003 (session = Algorithms, Optimization and Runtimes), pp. 378-404
 - Antonio Cunei, Jan Vitek: PolyD: a flexible dispatching framework, OOPSLA 2005 (session = Exceptional exceptions), pp. 487-503
 - Chris Andreae, Yvonne Coady, Celina Gibbs, James Noble, Jan Vitek, Tian Zhao: Scoped Types and Aspects for Real-Time Java, ECOOP 2006 (session = Ownership and Concurrency), pp. 124-147
 - Jesper Honig Spring, Jean Privat, Rachid Guerraoui, Jan Vitek: Streamflex: high-throughput stream programming in java, OOPSLA 2007 (session = Language design), pp. 211-228
 - Jan Vitek: Introduction to: The Myths of Object-Orientation, ECOOP 2009 (session = ECOOP '08 Banquet Speech), pp. 618
 - Tobias Wrigstad, Filip Pizlo, Fadi Meawad, Lei Zhao, Jan Vitek: Loci: Simple Thread-Locality for Java, ECOOP 2009 (session = Concurrency, Exceptions and Initialization), pp. 445-469
 - Bard Bloom, John Field, Nathaniel Nystrom, Johan Östlund, Gregor Richards, Rok Strnisa, Jan Vitek, Tobias Wrigstad: Thorn: robust, concurrent, extensible scripting on the JVM, OOPSLA 2009 (session = Concurrency), pp. 117-136
 - Filip Pizlo, Lukasz Ziarek, Petr Maj, Antony L. Hosking, Ethan Blanton, Jan Vitek: Schism: fragmentation-tolerant real-time garbage collection, PLDI 2010 (session = Heap management), pp. 146-159
 - Mandana Vaziri, Frank Tip, Julian Dolby, Christian Hammer, Jan Vitek: A Type System for Data-Centric Synchronization, ECOOP 2010 (session = Type Systems), pp. 304-328
 - Tobias Wrigstad, Francesco Zappa Nardelli, Sylvain Lebresne, Johan Östlund, Jan Vitek: Integrating typed and untyped code in a scripting language, POPL 2010 (session = Relating and integrating static and dynamic checks), pp. 377-388
 - Gregor Richards, Sylvain Lebresne, Brian Burg, Jan Vitek: An analysis of the dynamic behavior of JavaScript programs, PLDI 2010 (session = Dynamic analysis), pp. 1-12
 - Gregor Richards, Andreas Gal, Brendan Eich, Jan Vitek: Automated construction of JavaScript benchmarks, OOPSLA 2011 (session = Empirical results), pp. 677-694
 - Gregor Richards, Christian Hammer, Brian Burg, Jan Vitek: The Eval That Men Do - A Large-Scale Study of the Use of Eval in JavaScript Applications, ECOOP 2011 (session = Empirical Studies), pp. 52-78
 - Fadi Meawad, Gregor Richards, Floréal Morandat, Jan Vitek: Eval begone!: semi-automated removal of eval from javascript programs, OOPSLA 2012 (session = Dynamic languages), pp. 607-620
 - Floréal Morandat, Brandon Hill, Leo Osvald, Jan Vitek: Evaluating the Design of the R Language - Objects and Functions for Data Analysis, ECOOP 2012 (session = Language Evaluation), pp. 104-131
 - Tomas Kalibera, Matthew Mole, Richard E. Jones, Jan Vitek: A black-box approach to understanding concurrency in DaCapo, OOPSLA 2012 (session = Concurrency II), pp. 335-354
 - Daniel Marino, Christian Hammer, Julian Dolby, Mandana Vaziri, Frank Tip, Jan Vitek: Detecting deadlock in programs with data-centric synchronization, ICSE 2013 (session = Code Analysis), pp. 322-331
 - Zachary DeVito, James Hegarty, Alex Aiken, Pat Hanrahan, Jan Vitek: Terra: a multi-stage language for high-performance computing, PLDI 2013 (session = High performance computing), pp. 105-116
 - Delphine Demange, Vincent Laporte, Lei Zhao, Suresh Jagannathan, David Pichardie, Jan Vitek: Plan B: a buffered memory model for Java, POPL 2013 (session = Concurrency), pp. 329-342

committees
 - PC Member, ECOOP 1998
 - PC Member, OOPSLA 2000
 - PC Member, ECOOP 2000
 - PC Member, ECOOP 2001
 - PC Member, POPL 2001
 - PC Member, PLDI 2002
 - PC Member, ECOOP 2003
 - PC Member, OOPSLA 2004
 - PC Member, OOPSLA 2007
 - PC Member, ECOOP 2007
 - PC Member, OOPSLA 2008
 - PC Member, POPL 2008
 - Program Chair, ECOOP 2008
 - PC Member, ECOOP 2009
 - PC Member, PLDI 2010
 - PC Member, ECOOP 2010
 - PC Member, POPL 2011
 - General Chair, PLDI 2012
 - PC Member, ECOOP 2013
 - PC Member, PLDI 2013

The file [screenshot2.jpg](screenshot2.jpg) shows Jan's record after changing the date range to 2009-2013. 
    
Note that clicking on a co-author's name will result in navigating to the record
for that co-author.    
    
The textbox on the right can be used to search for people who meet
certain criteria. For example, you might be interested in finding all people
who served on both the FSE and ICSE committees by entering:

committees("FSE")>0 && committees("ICSE")>0

which produces a long list of familiar names.

Or you can search for all people who published at least 5 papers at PLDI
but who haven't served on the PLDI PC yet, using:

publications("PLDI")>4 && committees("PLDI") == 0
 
Or you could, e.g., search for people who published at least 6 papers in
{ OOPSLA, ECOOP }, but haven't served on the ECOOP committee yet:

publications("OOPSLA")+publications("ECOOP")>5 && committees("ECOOP") == 0

There are some other bells and whistles. You can uncheck the checkboxes at the
top of the tool to filter out conferences, and limit the period you want to
consider using the slider. 

II. Adding conferences to PC-Miner
----------------------------------
The PC-Miner generator tool is a Java application that you can build and run using
Eclipse. To run PC-Miner, right-click on the class pcminer.PCMiner, and select 
Run As->Java Application. No configuration should be necessary.  

The input to the generator tool is provided in the data subdirectory, which
contains one subdirectory for each conference (e.g., "PLDI"). Each
conference-subdirectory, in turn, contains one subdirectory for each year
(e.g., "1999", "2000", etc.) as well as a file "color" that specifies the color
to be used for the conference in the generated UI 
(see [here](http://www.computerhope.com/htmcolor.htm) for a list of HTML color
codes). Each year-subdirectory contains two files: a file that was copied
from [DBLP](http://www.informatik.uni-trier.de/~ley/db/) containing the DBLP info for a specific year of a specific conference
(e.g., here is a link to the file for
 [PLDI 2000](http://www.informatik.uni-trier.de/~ley/db/conf/pldi/pldi2000.html))
and a file specifying the program committee for that year (e.g., 
[here](https://github.com/franktip/pcminer/blob/master/data/PLDI/2000/pldi2000-pc.txt) is the
file that was created for PLDI 2000).

Adding a conference involves the following steps:

1. create a new subdirectory in the "data" directory with a name matching the conference
   acronym (e.g., "PLDI")
   
2. create a file "color" inside of this directory. This file should contain a single
   line that provides a name or hexcode of your preferred color (e.g, "#3BB9FF")
   See http://html-color-codes.info/color-names/ for a list of colors.    
   
3. inside of this new conference directory, create a subdirectory for each year for 
   which data is available (e.g., "2000", "2001", "2002", etc.)
   
4. inside each year subdirectory, you need to provide two files
     - a file "<ConfName><year>.html" providing the HTML source code 
       for that year of that conference as provided by DBLP. 
     - a file "<ConfName><year>-pc.txt" providing the list of PC members.
       The name of each PC member must match the name and middle initials
       as they are used in DBLP (otherwise, get duplicate entries will
       appear in the UI). Note that special characters in people's names
       need to be encoded (see (here)[http://www.w3schools.com/tags/ref_entities.asp]
       for a list of HTML character codes).  For example, you'll need to
       encode a lowercase 'a' with an umlaut as '&auml' or '&#228'.

       By default, including a person's name in the committee-file means
       that they are assumed to be a PC member. For other roles (e.g.,
       program chairs), names need to be prefixed as follows:
         "P:<name>"  - program chair
         "G:<name>"  - general chair
         "E:<name>"  - ERC member
         "C:<name>"  - conference chair
         
5. Once your files are all in place, you can generate the UI by running PCMiner.java.
   This will generate an updated version of the file ui/data.js. Opening
   ui/index.html in your browser will bring up a version of PC-Miner that
   contains the conference you just added. 
 
III. Feedback, Suggestions, and Acknowledgements
-----------------------------------------------
 
PC-miner was developed by Frank Tip as a tool to help identify PC member
candidates for the ISSTA'11 and PLDI'12 program committees. Since then,
it has been used by several other PC chairs for conferences such as
FSE, PLDI, ASE, ECOOP, and OOPSLA.  Feedback, suggestions, and improvements 
are most welcome, as is additional conference data.
Feedback, contributions and suggestions from Matt Dwyer, Cormac Flanagan, 
Richard Jones, Max Schaefer, Alex Orso, Jan Vitek, and Andreas Zeller are 
gratefully acknowledged.

The data made available in PC-Miner contains information from the
[DBLP Bibliography Server](http://www.informatik.uni-trier.de/~ley/db/index.html)
which is made available under the 
[ODC Attribution License](http://opendatacommons.org/licenses/by/summary/).
 
(c) 2010-2013 Frank Tip
