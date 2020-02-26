var pcminer = function(){

	/**
	 * The list of supported conferences. Maps each conference to a boolean
	 * value indicating if the corresponding checkbox is enabled. Populated
	 * dynamically.
	 */
	var conferences = { };
	
	/**
	 * Arrays for storing the range of years for which is data is available,
	 * for each conference.
	 */
	var minYear = [];
	var maxYear = [];
	
	/**
	 * The list of colors to be used for conferences. Maps each conference to a string
	 * value indicating the color or hex code. Populated dynamically.
	 */
	var confColors = { };
	
	/**
	 *  start year, extracted from the slider
	 */
	var startYear = undefined;
	
	/**
	 *  start year, extracted from the slider
	 */
	var endYear = undefined;  
	
	/** 
	 * current author (will be displayed at top of page if defined)
	 */
	var currentAuthor = undefined;
	
	/**
	 * filter for narrowing down the set of people; only names starting with
	 * these characters are displayed
	 */
	var nameFilter = "";
	
	/**
	 * filter for restricting the set of people to those meeting certain criteria
	 * e.g., 'committees("PLDI")>2 && publications("PLDI")>2'
	 */
	var conditionFilter = "true";
	
	/**
	 * used for clipboard feature to allow copy & paste of a set of names
	 */ 
	var clip = undefined;
	
	/**
	 * clears the name filter
	 */
	function clearNameFilter(){ 
		nameFilter = "";
		document.getElementById("formField").value = ""; 
	};
	
	/**
	 * add a conference. The isEnabled flag indicates whether the corresponding
	 * radio-button should be enabled.
	 */
	function addConference(confName, isEnabled){
		conferences[confName] = isEnabled;
	}
	
	/**
	 * add a year to a conference. Used for keeping track of the range of
	 * years for which data is available.
	 */
    function addConfYear(confName, year){
        if (!minYear[confName]){
          minYear[confName] = year;
        } else if (minYear[confName] > year){
          minYear[confName] = year;
        }
        if (!maxYear[confName]){
          maxYear[confName] = year;
        } else if (maxYear[confName] < year){
          maxYear[confName] = year;
        }
    }
	
	/**
	 * add a conference color.  
	 */
	function addConfColor(confName, color){
		confColors[confName] = color;
	 
		var elHead = document.getElementsByTagName('head')[0];
		var elStyle = document.createElement('style');
		elStyle.type= 'text/css';
		elHead.appendChild( elStyle );
		
		var styleText =  "." + confName + " { color : " + color + "} ";
		elStyle.innerHTML = styleText;
	}
	
	/**
	 * clears the condition filter
	 */
	function clearConditionFilter(){ 
		conditionFilter = true;
		document.getElementById("filterField").value = ""; 
	};
	
	/**
	 * computes how many committees an author has served:
	 *   committees(x) : committees for author x (any conference, any role)
	 *   committees(x, c) : committees for author x at conference c (any role) 
	 */
	function committees(x, conf){
		if (conf == undefined){
			return x.committees.length;
		} else {
			var count = 0;
			for (var i=0; i < x.committees.length; i++){
				var committee = x.committees[i];
				if (committee.conference.series == conf && withinRange(committee)){
					count++;
				}
			}
			return count;
		}
	};
	
	/**
	 * computes on how many committees an author has served:
	 *   committees(x) : committees for author x (any conference, PC member role)
	 *   committees(x, c) : committees for author x at conference c (PC member role) 
	 */
	function pc(x, conf){
		if (conf == undefined){
			return x.committees.length;
		} else {
			var count = 0;
			for (var i=0; i < x.committees.length; i++){
				var committee = x.committees[i];
				if (committee.conference.series == conf && committee.role.indexOf("PC Member") != -1 && withinRange(committee)){
					count++; 
				}  
			}
			return count;
		}
	};
	
	/**
	 * Determines if this author has been chair of the specified conference
	 */
	function chair(x, conf){
		for (var i=0; i < x.committees.length; i++){
			var committee = x.committees[i];
			if (!withinRange(committee)) continue;
			if (committee.conference.series == conf && x.committees[i].role.indexOf("Chair") != -1){
				return true;
			}
		}
		return false;
	};
	
	/**
	 * returns true if the author has published a paper whose title contains any of the supplied keywords
	 */
	function keywords(x){
		if (arguments.length < 2){
			return false; 
		}  
		else {
			for (var i=0; i < x.publications.length; i++){
				var pub = x.publications[i];
				for (s in arguments){
					var arg = arguments[s];
					if (typeof arg == "string"){
						if (pub.title.indexOf(arg) != -1){
							return true;
						}
					}
				}
			}
			return false; 
		}
	}
	
	/**
	 * computes how many publications an author has at a given venue:
	 *   publications(x) : number of publications for author x (any conference)
	 *   publications(x, c) : number of publications for author x at conference c 
	 */
	function publications(x, conf){ 
		if (conf == undefined){
			return x.publications.length; 
		} else {
			var count = 0;
			for (var i=0; i < x.publications.length; i++){
				var pub = x.publications[i];
				if (pub.conference.series == conf && withinRange(pub)){
					count++;
				}
			}
			return count;
		}
	};
	
	/**
	 * computes author profile
	 */
	function authorProfile(x){ 
	  var pubs = {}; 
	  var committees = {}; 
	  var hasPubs = false;
	  var hasCommittees = false;
	  
	  for (var i=0; i < x.publications.length; i++){
		  var pub = x.publications[i];
		  if (withinRange(pub)){	  
			  var confName = pub.conference.series;
			  hasPubs = true;
			  if (pubs[confName]){
			    pubs[confName]++;
			  } else {
			    pubs[confName] = 1;
			  }
		  }
	  }
	  for (var j=0; j < x.committees.length; j++){
		  var committee = x.committees[j];
		  if (withinRange(committee)){
		      var confName = committee.conference.series;
		       hasCommittees = true;
		      if (committees[confName]){
			    committees[confName]++;
			  } else {
			    committees[confName] = 1;
			  }
		  }
	  }
	  
	  var result = "";
	  
	  if (hasPubs){
	    result += "publications:\n";
	    var pubsArray = [];
	    for (confName in pubs){
	       pubsArray.push("  " + confName + ": " + pubs[confName] + "\n");
	    }
	    pubsArray.sort();
	    for (var p in pubsArray){
	    	result += pubsArray[p];
	    }
	  }
	  if (hasCommittees){
	    result += "committees:\n";
	    var commsArray = [];
	    for (confName in committees){
	       commsArray.push("  " + confName + ": " + committees[confName] + "\n");
	    }
	    commsArray.sort();
	    for (var c in commsArray){
	    	result += commsArray[c];
	    }
	  }
	  return result;
	};
		
	/**
	 * invoked when the dateFilter has changed (using the slider)
	 */
	function dateFilterChanged(sY, eY) {
		startYear = sY;
		endYear = eY;
		updateNrEntries(); 
		selectionChanged(currentAuthor);
		resetSelector();  
	};
	
	/**
	 * invoked when the nameFilter has changed.
	 */
	function nameFilterChanged(val){
		if (val != nameFilter){
			nameFilter = val; 
			if (currentAuthor != undefined && currentAuthor.indexOf(nameFilter) != 0){
			    currentAuthor = undefined;
			    document.getElementById("title").innerHTML = "";
			    document.getElementById("authorinfo").innerHTML = "";
		  	}
		}	
		resetSelector();
	};
	
	/**
	 * invoked when the conditionFilter has changed.
	 */
	function conditionFilterChanged(newExp){ 
		if (newExp.length == 0){
			conditionFilter = "true";
		} else {
			conditionFilter = newExp.replace(/\(/gi, "(x,"); // use regular expression to replace all occurrences of "(" with "(x,"
		}
		resetSelector();
	};
	
	/**
	 * invoked when a user selects/unselects a conference using the checkboxes
	 */
	function confSelected(conf){
		var selected = document.conferences[conf].checked;
		conferences[conf] = selected; 
		if (currentAuthor != undefined){
			authorClicked(currentAuthor);
		} else {
			resetSelector();
		}
	};
	
	/**
	 * invoked when the mouse is moved over an author; will start underlining the text.
	 */
	function mouseOverAuthor(arg){
		arg.style.textDecoration = 'underline';
		var profile = authorProfile(getAuthor(arg.textContent));
		if (profile != ""){
			arg.title = profile;
		} else {
			arg.title = "no publications/committees in selected timespan";
		}
	};
	
	/**
	 * invoked when the mouse is moved away from an author; will stop underlining the text.
	 */
	function mouseOutAuthor(arg){
		arg.style.textDecoration = 'none';
	};
	
	/**
	 * invoked when the user clicks on an author; will navigate to that author.
	 * clears the name filter & condition filter.
	 */
	function authorClicked(s){ 
		document.getElementById("title").innerHTML = s; // update title
		var selector = $("#selector"); // programmatically change selection
		selector.val(s);
		selectionChanged(s);
		
		clearNameFilter();
		clearConditionFilter(); 
		resetSelector();
	};
	
	/**
	 * retrieves the color associated with a specified conference
	 */
	function getConfColor(s){
		return confColors[s];
	};
	
	/**
	 * updates the number of entries (authors)
	 */
	function updateNrEntries(){
		var nrP = 0;
		var nrC = 0;
		 for (var i=0; i < list.length; i++){
			 nrP += nrPublications(list[i]);
			 nrC += nrCommittees(list[i]);
		 }
		 document.getElementById("nrpubs").innerHTML = "<span float=\"left\">" + nrP + " publication records selected</span>";
		 document.getElementById("nrcommittees").innerHTML = "<span float=\"left\">" + nrC + " committee records selected</span>";
	};
	
	/**
	 * resets the selector to reflect the current filter settings
	 */
	function resetSelector(){	 
	  var selector = $("#selector");
	  selector.empty();
	  clip = $("#basic-modal-content"); //REJ
	  clip.empty();
	  clip.append("<p>");
	  for (var i=0; i < list.length; i++){
		  var x = list[i];
		  if (nameFilter == ""  || x.author.indexOf(nameFilter) == 0){
			  if (nrPublicationsAndCommittees(x) > 0 && eval(conditionFilter)){ // evil call to eval()
				  selector.append("<option>" + x.author + "</option>");
				  clip.append(x.author+","); //REJ
                  for(var j=0; j<x.committees.length; j++){
                  	clip.append(x.committees[j].conference.series+" "+x.committees[j].conference.year+" ");
				  }
                  clip.append("<br />");


			  }
		  }  
	  }    
	  clip.append("</p>");
	  selector.attr("size", Math.min(10,list.length));
	  updateNrEntries(); 
	};
	
	/**
	 * show current list of authors on clipboard so that user can copy&paste it
	 */
	function copyToClipboard() { //REJ
		$('#basic-modal-content').modal();
	};
	
	/**
	 * counts the number of publications and committees for an author in the current date range
	 */
	function nrPublicationsAndCommittees(x){
		 return nrPublications(x) + nrCommittees(x);
	};
	
	
	/**
	 * counts the number of publications for an author in the current date range
	 */
	function nrPublications(x){
		var count=0;
		for (var j=0; j < x.publications.length; j++){
			 var pub = x.publications[j];
			 if (conferences[pub.conference.series] && withinRange(pub)){
				 count++;
			 }
		}
		return count;
	};
	
	/**
	 * count words that occur in titles of papers by a given authors and
	 * create array structure needed to generate a word cloud
	 */
	function gatherWords(author){
		var result = [];
		var confMap = {};
		
		// count the number of occurrences of each word 
		for (var j=0; j < author.publications.length; j++){
			var pub = author.publications[j];
			if (conferences[pub.conference.series] && withinRange(pub)){
				 var titleWords = pub.title.split(" ");
				 for (var k=0; k < titleWords.length; k++){
					 var word = titleWords[k].toLowerCase();
					 if (word.length > 3){ 
						 if (result[word] === undefined){
							 result[word] = 1;
							 confMap[word] = pub.conference.series;
						 } else {
							 result[word]++;
						 }
					 }
				 }
			}
		}
		
		// create the array structure required for jQcloud
		var nresult = [ ];
		for (var i in result){
			var obj = {};
			obj.text = i;
			obj.weight = result[i];
			obj.html = { "class" : confMap[i] }; 
			nresult.push(obj);
		}
		
		return nresult;
	}
	
	/**
	 * counts the number of committees for an author in the current date range
	 */
	function nrCommittees(x){
		var count=0;
		for (var j=0; j < x.committees.length; j++){
			 var committee = x.committees[j];
			 if (conferences[committee.conference.series] && withinRange(committee)){
				 count++;
			 }
		 }
		 return count;
	};
	
	
	/**
	 * determines if a publication conforms to the date filter
	 */
	function withinRange(pub){
		return pub.conference.year >= startYear && pub.conference.year <= endYear;
	};
	
	//hack to deal with some nasty font issue
	function mapCharacters(s){
		if (s.indexOf('&') == -1){ 
			return s; 
		} else {
			return $('<div />').html(s).text();
		}
	};
	
	/**
	 * find author by name
	 */
	function getAuthor(authorName){
		for (var i=0; i < list.length; i++){
		    if (mapCharacters(list[i].author) == authorName){ 
		      return list[i];
			}
		}
		return undefined;
	}
	
	
	/**
	 * update the displayed publications and committees to reflect the
	 * specified authors and current filter settings
	 */
	function selectionChanged(authorName){ 
	  if (authorName != undefined){	 
		  currentAuthor = authorName; 
		  for (var i=0; i < list.length; i++){
		    if (mapCharacters(list[i].author) == authorName){ 
		      document.getElementById("title").innerHTML = list[i].author;
		      var words = gatherWords(list[i]); 
		     
		      document.getElementById("wordCloud").innerHTML=""; // clear old word cloud
		      $("#wordCloud").jQCloud(words);
		      
		      var text = "";
		      
		      var arr = new Array();
		      for (var j=0; j < list[i].publications.length; j++){
		    	  arr[j] = list[i].publications[j]; 
		      }
		      arr.sort(function(a,b){ return a.conference.year - b.conference.year; });
		      
		      if (arr.length > 0){
		    	  var pubPrinted = false;  
			      for (var j=0; j < arr.length; j++){
			    	  var pub = arr[j]; 
			    	  if (conferences[pub.conference.series] && withinRange(pub)){
			    		  if (!pubPrinted){
			    			  text += "<br><b>publications</b><br>";
			    			  pubPrinted = true;
			    		  }
			    		  text = text + "&nbsp;&nbsp;&nbsp;&nbsp;" + getPublication(arr[j],authorName) + "<br>";
			    	  }
			      }
		      }
		      
		      arr = new Array();
		      for (var j=0; j < list[i].committees.length; j++){
		    	  arr[j] = list[i].committees[j]; 
		      }
		      arr.sort(function(a,b){ return a.conference.year - b.conference.year; });
		      
		      if (list[i].committees.length > 0){
		    	  var committeePrinted = false;
			      for (var j=0; j < arr.length; j++){
			    	  var committee = arr[j];
			    	  if (conferences[committee.conference.series] && withinRange(committee)){
			    		  if (!committeePrinted){
			    			  text += "<br><b>committees</b><br>";
			    			  committeePrinted = true;
			    		  }
			    		  text = text + "&nbsp;&nbsp;&nbsp;&nbsp;" + getCommittee(arr[j]) + "<br>";
			    	  }
			      }
		      }
		      
		      document.getElementById("authorinfo").innerHTML = text;
		      break;
		    }  
		  }
	  }
	};
	
	/**
	 * produce HTML for publication in the appropriate color
	 */
	function getPublication(p, authorName){
		var color = confColors[p.conference.series];
		return "<font color=\"" + color + "\">" + getAuthors(p.authors, authorName) + ": " + p.title + ", " + 
		       getConference(p.conference) + " (session = " + p.session + "),  pp." + p.pages + "</font>";
	};
	
	/**
	 * produce HTML for committee service in the appropriate color
	 */
	function getCommittee(c){
		var color = confColors[c.conference.series];
		return "<font color=\"" + color + "\">" + c.role + ", " + c.conference.series + " " + c.conference.year + "</font>";
	};
	
	/**
	 * produce HTML for conference and year
	 */
	function getConference(c){
		return c.series + " " + c.year; 
	};
	
	/**
	 * produce HTML for list of authors
	 */
	function getAuthors(as, authorName){
		var text = "";
		for (var i=0; i < as.length; i++){
			if (mapCharacters(as[i]) == authorName){
				text = text + "<b><font onMouseOver='pcminer.mouseOverAuthor(this)' " +
						"               onMouseOut='pcminer.mouseOutAuthor(this)' " +
						"               onclick='pcminer.authorClicked(\"" + mapCharacters(authorName) + "\")'>" + mapCharacters(authorName) + "</font></b>";
			} else {
				text = text + "<font onMouseOver='pcminer.mouseOverAuthor(this)' " +
						"            onMouseOut='pcminer.mouseOutAuthor(this)' " +
						"            onclick='pcminer.authorClicked(\"" + mapCharacters(as[i]) + "\")'>" + mapCharacters(as[i]) + "</font>";
	
			}
			if (i < (as.length-1)){
				text = text + ", ";
			}
		}
		return text;
	};

    /**
     * return a label like "ICSE (1999-2015)" indicating the range of covered years
     * for a given conference
     */
    
    function getConfRange(confName){
      return confName + " (" + minYear[confName] + "-" + maxYear[confName] + ")";
    };

	
	/**
	 * installs the date range slider on the page
	 */
	function installSlider(){
	    var currentYear = new Date().getFullYear();
		 $("#slider").rangeSlider({
		    	bounds:{
		    		min: 1995,
		    		max: currentYear
		    		},
		    	defaultValues:{
		    		min: 1995,
		    		max: currentYear
		    		},
		    	step: 1,	 
				scales : [
					// primary scale
					{
						first : function(val) {
							return val;
						},
						next : function(val) {
							return val + 3;
						},
						stop : function(val) {
							return false;
						},
						label : function(val) {
							return val;
						},
						format : function(tickContainer, tickStart, tickEnd) {
							tickContainer.addClass("myCustomClass");
						}
					},
					// secondary scale
					{
						first: function(val){ return val; },
						next: function(val){
						if (val % 3 === 2){
						return val + 2;
						}
						return val + 1;
						},
						stop: function(val){ return false; },
						label: function(){ return ""; }
					 }
				  ]
				});
		    
		       var initialSliderBounds = $("#slider").rangeSlider("bounds");
		       dateFilterChanged(initialSliderBounds.min, initialSliderBounds.max);
		    
		       $("#slider").bind("valuesChanged", function(e, data){
		    	   dateFilterChanged(data.values.min, data.values.max);
		        });
	}
	
	/**
	 * install the checkboxes and color them according to the colors specified in confColor.js
	 */
	function installCheckboxes(){
		var form = "<form name=conferences>\n";
		for (confName in conferences){
			form += "<label><input type=\"checkbox\" ";
			form += "id=\"" + confName + "\" ";
			form += "checked onclick='pcminer.confSelected(\"" + confName + "\")'> ";
			form += "<font id=\"" + confName + "color\" ";
			form += "class=\"conf\">" + confName + "</font>"; 
			form += "</label>\n";
		}
		form += "</form>\n";
		document.getElementById("checkboxes").innerHTML = form;
		for (conf in confColors){
			document.getElementById(conf + "color").style.color = confColors[conf];
			document.getElementById(conf + "color").title = getConfRange(conf);
		}
		
	}
	
	/**
	 * initializes the page
	 */
	function main(){
		  installCheckboxes();
		  resetSelector();
		  installSlider();
	};
	
	return {
		addConference : addConference,
		addConfColor : addConfColor,
		addConfYear : addConfYear,
		getConfRange : getConfRange,
		mouseOverAuthor : mouseOverAuthor,
		mouseOutAuthor : mouseOutAuthor,
		authorClicked : authorClicked,
		selectionChanged : selectionChanged,
		nameFilterChanged : nameFilterChanged,
		dateFilterChanged : dateFilterChanged,
		conditionFilterChanged : conditionFilterChanged,
		confSelected : confSelected,
		copyToClipboard : copyToClipboard,
		main : main
	};
}();

$(pcminer.main);
