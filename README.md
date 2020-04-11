pcminer [![Build Status](https://travis-ci.org/pcminer-tools/pcminer.svg?branch=master)](https://travis-ci.org/pcminer-tools/pcminer)
=======

PC-miner is a tool that analyzes publications and program committee service in the top conferences 
in Programming Languages and Software Engineering. Currently, the tool supports the following 
conferences: ASE, ECOOP, ESOP, FSE, ICFP, ICSE, ISMM, ISSTA, OOPSLA, PLDI, POPL, and PPoPP and it covers the years 1995-2015. 
Publication data was obtained from [DBLP](http://www.informatik.uni-trier.de/~ley/db/), and program committee data was scraped from various 
conference web sites. 

I. Using PC-miner
------------------

To start using PC-miner, go into the "ui" subdirectory, and open the index.html file in your browser.
This should bring up a window that looks like the following
[screenshot](screenshot.jpg).

In the selector on the left is an alphabetical list of all authors. Clicking on
an author will display his publication and committee activity. It will also show
a word cloud that contains words that occur frequently in the titles of the author's papers.
You can search (awkwardly) by entering some text in the search box.
Entering "Jan" there will reduce the list to all authors whose name starts
with the characters "Jan". When you select Jan Vitek's name from the
resulting list, you'll see something that looks like the attached
 [screenshot](screenshot.jpg).
The file [screenshot2.jpg](screenshot2.jpg) shows Jan's record after restricting
the date range to 2009-2014 using the slider. 
    
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
consider using the slider. To copy the names currently displayed in the
selector, press the "Display selected names" button.

II. Adding conferences to PC-miner
----------------------------------
The PC-miner generator tool is a Java application that you can build and run using
Eclipse. To run PC-miner, right-click on the class pcminer.PCMiner, and select 
Run As->Java Application. No configuration should be necessary.  From the command
line, you can run `./gradlew run` from the `pcminer` root directory to run the
generator tool.

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
   acronym (e.g., "PLDI").  **Note:** Data for conferences corresponding to
   [PACMPL](https://dl.acm.org/journal/pacmpl) issues (currently POPL, ICFP, and
   OOPSLA) is now stored in the `data/PACMPL` directory (from the year of the
   PACMPL switch onward).
   
2. create a file "color" inside of this directory. This file should contain a single
   line that provides a name or hexcode of your preferred color (e.g, "#3BB9FF")
   See http://html-color-codes.info/color-names/ for a list of colors.    
   
3. inside of this new conference directory, create a subdirectory for each year for 
   which data is available (e.g., "2000", "2001", "2002", etc.)
   
4. inside each year subdirectory, you need to provide two files
     - a file "<ConfName><year>.html" providing the HTML source code 
       for that year of that conference as provided by DBLP. 
     - a file "<ConfName><year>-pc.txt" providing the list of PC members.

       If the PC member data is available on a conference site using [Researchr](http://conf.researchr.org/),
       you can run the `pc-from-researchr.py` script to generate the PC member list.
       The script requires Python 3; the `python3` executable should be on your path.
       Script usage is
       `./pc-from-researchr.sh CONF_NAME CONF_YEAR PC_URL [EXT_URL...]`.
       `CONF_NAME` is the conference name (e.g., PLDI) and `CONF_YEAR` is the
       year (e.g., 2019).  `PC_URL` is the URL of the Researchr page listing the
       program committee.  Optionally, you can provide URLs for any external
       review / program committees at the end.  Example:
       ```bash
       $ ./pc-from-researchr.py PLDI 2020 "https://pldi20.sigplan.org/committee/pldi-2020-papers-program-committee" "https://pldi20.sigplan.org/committee/pldi-2020-papers-external-program-committee" "https://pldi20.sigplan.org/committee/pldi-2020-papers-external-review-committee"
       Wrote PC data to data/PLDI/2020/pldi2020-pc.txt
       Please check for errors and to add information on chairs
       ```

       Important: *The name of each PC member must match the name and middle initials
       as they are used in DBLP* (otherwise, get duplicate entries will
       appear in the UI). Note that *special characters in people's names
       need to be encoded* (see [here](http://www.utexas.edu/learn/html/spchar.html)
       for a list of HTML character codes).  For example, you'll need to
       encode a lowercase 'a' with an umlaut as `&auml;` or `&#228;`.

       By default, including a person's name in the committee-file means
       that they are assumed to be a PC member. For other roles (e.g.,
       program chairs), names need to be prefixed as follows:
         - "P:<name>"  - program chair
         - "G:<name>"  - general chair
         - "E:<name>"  - ERC member
         - "C:<name>"  - conference chair
         - "B:<name>"  - program board member
         
5. Once your files are all in place, you can generate the UI by running PCMiner.java.
   (To compile and run, you need in your classpath the jar files in subdirectory lib.)
   This will generate an updated version of the file ui/data.js. Opening
   ui/index.html in your browser will bring up a version of PC-miner that
   contains the conference you just added.  Please contribute any data
   you added, so that others can benefit from your efforts.
   
6. Some authors (e.g., Alexander Aiken) have multiple DBLP records (there is one
   for "Alexander Aiken" and one for "Alex Aiken"). To merge such records in
   PC-miner, please edit the list of name mappings in ./data/NameMappings.txt    
 
III. Feedback, Suggestions, and Acknowledgements
-----------------------------------------------
 
I originally developed this as a tool for identifying PC member
candidates for the ISSTA'11 and PLDI'12 program committees. Since then,
it has been used by several other PC chairs for conferences such as
ISMM, ISSTA, ICFP, ICSE, FSE, PLDI, ASE, ECOOP, and OOPSLA.  Feedback, suggestions, and
improvements are most welcome, as is additional conference data.
Contributions from Steve Blackburn, Giuseppe Castagna, Matt Dwyer, Sebastian Erdweg, Cormac Flanagan, Jeremy Gibbons, Colin Gordon, David Grove,
Sam Guyer, Richard Jones, Alex Orso, Max Schaefer, Yannis Smaragdakis, Manu Sridharan, Eijiro Sumii, Sam Tobin-Hochstadt, Jan Vitek, 
and Andreas Zeller are gratefully acknowledged.

The data made available in PC-miner contains information from the
[DBLP Bibliography Server](http://www.informatik.uni-trier.de/~ley/db/index.html)
which is made available under the 
[ODC Attribution License](http://opendatacommons.org/licenses/by/summary/).
 
(c) 2010-2017 Frank Tip
