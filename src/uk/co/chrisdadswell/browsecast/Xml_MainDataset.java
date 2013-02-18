/*
Copyright 2012 Christian Dadswell
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.co.chrisdadswell.browsecast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.common.collect.LinkedHashMultimap;

public class Xml_MainDataset {  

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "XML Dataset: ";
	
	// CREATE LINKED HASHMAP TO HOLD PODCAST NAME AND URL
	public static LinkedHashMap <String,String> networkInfoMap = new LinkedHashMap <String, String>();
	public static LinkedHashMultimap lhmmPInfo = LinkedHashMultimap.create();
	
	// VARIABLES
	private String genre = null;
	private String networks = null;
	
	private String networktext = null;
	private String networkname = null;
	private String networkid = null;
	
	private String podtext = null;
	private String podimg = null;
	private String poddescription = null;
	private String podflavour = null;
	private String podduration = null;
	private String podpage = null;
	private String podxml = null;
	private String podgenre = null;

	public String intoGenre(){         
    	return getGenre();        
	}
    
    public String intoStations(){         
    	// CONVERT TEXT STRINGS INTO ARRAYS AND SEPERATE BY "#"
    	String[] arrSFName = Func_Strings.stringToArray(getNetworkFullName(), "#");
    	String[] arrSId = Func_Strings.stringToArray(getNetworkId(), "#");
    	
    	// PUT ARRAYS IN HASHMAP
    	PutNetworkInfo(arrSFName, arrSId);
    	// FOR THE ACTIVITY_BYSTATION LISTADAPTOR
    	return getNetworkFullName();  
    }
    
    public static void PutNetworkInfo(String[] stationName, String[] stationText) {
    	for(int i=0;i<stationName.length; i++)
    		networkInfoMap.put(stationName[i], stationText[i]);
    }
    
    public static String GetNetworkInfo(String stationNameToGet) {
    	String stationText = networkInfoMap.get(stationNameToGet);
		return stationText;
    }
    
    public String intoPodInfo(){
    	intoPodInfoArray();
		// FOR THE ACTIVITY_BYPODCAST LISTADAPTOR
		return getPodText();
    }
    
    public void intoPodInfoArray() {
    	ArrayList<String> alPInfo = new ArrayList<String>();
    	// CONVERT TEXT STRINGS INTO ARRAYS AND SEPERATE BY "#"
    	String[] arrPName = Func_Strings.stringToArray(getPodText(), "#");
    	// STARTS AT 0 HERE
    	String[] arrPXml = Func_Strings.stringToArray(getPodXML(), "#");
    	String[] arrPImage = Func_Strings.stringToArray(getPodImg(), "#");
    	String[] arrPDescription = Func_Strings.stringToArray(getPodDescription(), "#");
    	String[] arrPFlavour = Func_Strings.stringToArray(getPodFlavour(), "#");
    	String[] arrPDuration = Func_Strings.stringToArray(getPodDuration(), "#");
    	String[] arrPPage = Func_Strings.stringToArray(getPodPage(), "#");
    	String[] arrPGenre = Func_Strings.stringToArray(getPodGenre(), "#");
    	
    	// CREATE KEY ARRAYLIST
    	for (int i=0; i<arrPName.length; i++)
    		alPInfo.add(arrPName[i]);
    	
    	// ADD VALUES TO LINKEDHASHMULTIMAP
		for (int j=0; j<arrPXml.length; j++)
			lhmmPInfo.put(alPInfo.get(j), arrPXml[j]);
		for (int k=0; k<arrPImage.length; k++)
			lhmmPInfo.put(alPInfo.get(k), arrPImage[k]);
		for (int l=0; l<arrPDescription.length; l++)
			lhmmPInfo.put(alPInfo.get(l), arrPDescription[l]);
		for (int m=0; m<arrPFlavour.length; m++)
			lhmmPInfo.put(alPInfo.get(m), arrPFlavour[m]);
		for (int n=0; n<arrPDuration.length; n++)
			lhmmPInfo.put(alPInfo.get(n), arrPDuration[n]);
		for (int o=0; o<arrPPage.length; o++)
			lhmmPInfo.put(alPInfo.get(o), arrPPage[o]);
		for (int p=0; p<arrPGenre.length; p++)
			lhmmPInfo.put(alPInfo.get(p), arrPGenre[p]);
    }
    
    // GOOGLE MULTIMAP
    public static Object[] GetPodcastInfo(String podcastNameToGet) {
		
    	Object[] arrPInfo = null;
    	
    	Set keySet = lhmmPInfo.keySet( );
		Iterator keyIterator = keySet.iterator( );
		while( keyIterator.hasNext( ) ) {
		    Object key = keyIterator.next( );
		    String strKey = key.toString();
		    boolean result = Func_Strings.compareStr(podcastNameToGet, strKey);
		    if(result) {
		    	Collection values = lhmmPInfo.get(key);
		    	Iterator valuesIterator = values.iterator( );
		    	// SET COLLECTION OF VALUES TO AN ARRAY OBJECT
		    	arrPInfo = values.toArray();
		    	}else{
		    		
		    	}
    		}
    		return arrPInfo; //RETURNS THE ARRAY OBJECT OF VALUES FOR PODCAST PASSED
    	}
    
	// ---------------------- GET SETS -------------------------
	// STATIONS AND GENRE
    public String getGenre() {/*System.out.println("** attFound:" + genre);*/return genre;}
    public void setGenre(String Genre) {/*System.out.println("** attFound:" + genre); */this.genre = Genre;}
    
    public String getStations() { /*System.out.println("** attFound:" + networks); */ return networks;}
    public void setStations(String Networks){ /*System.out.println("** attFound:" + networks); */ this.networks = Networks;}
    
    public String getNetworkText() {return networktext;}
    public void setNetworkText(String NetworkText) {
    	if(this.networktext == null){
    		this.networktext = NetworkText;
    	}else{
    		this.networktext += "#" + NetworkText;
    	}
    	}

    // STATION NAME
    public String getNetworkFullName() { return networkname;} 
    public void setNetworkFullName(String NetworkName) { 
    	if(this.networkname == null){
    		this.networkname = NetworkName;
    	}else{
    	    this.networkname += "#" + NetworkName;
    	}
    }
    
    // NETWORK ID
    public String getNetworkId() { return networkid;} 
    public void setNetworkId(String NetworkId) { 
    	if(this.networkid == null){
    		this.networkid = NetworkId;
    	}else{
    	    this.networkid += "#" + NetworkId;
    	}
    }

    // NAME
    public String getPodText() { return podtext;} 
    public void setPodText(String PodText) {
    	//System.out.println("** attFound:" + podtext);
    	if(this.podtext == null){
    		this.podtext = PodText;
    	}else{
    		this.podtext += "#" + PodText;
    	}
    }
    
    // IMAGE
    public String getPodImg() { return podimg;} 
    public void setPodImg(String PodImg){
    	//System.out.println("** attFound:" + podimg); 
    	if(this.podimg == null){
    		this.podimg = PodImg;
    	}else{
    		this.podimg += "#" + PodImg;
    	}
    }
    
    // DESCRIPTION
    public String getPodDescription() { return poddescription;} 
    public void setPodDescription(String PodDescription){
    	//System.out.println("** attFound:" + poddescription); 
    	if(this.poddescription == null){
    		this.poddescription = PodDescription;
    	}else{
    		this.poddescription += "#" + PodDescription;
    	}
    }
    
    // TITLE e.g. Highlights show
    public String getPodFlavour() { return podflavour;} 
    public void setPodFlavour(String PodFlavour){
    	//System.out.println("** attFound:" + podflavour);  
    	if(this.podflavour == null){
    		this.podflavour = PodFlavour;
    	}else{
    		this.podflavour += "#" + PodFlavour;
    	}    	
    }
    
    // DURATION
    public String getPodDuration() {  return podduration;} 
    public void setPodDuration(String PodDuration){
    	//System.out.println("** attFound:" + podduration);
    	if(this.podduration == null){
    		this.podduration = PodDuration;
    	}else{
    		this.podduration += "#" + PodDuration;
    	}    	

    	}

    // WEBPAGE
    public String getPodPage() { return podpage;} 
    public void setPodPage(String PodPage){
    	//System.out.println("** attFound:" + podpage);
    	if(this.podpage == null){
    		this.podpage = PodPage;
    	}else{
    		this.podpage += "#" + PodPage;
    	}    	

    	}
    
    // XML URL
    public String getPodXML() { return podxml;} 
    public void setPodXML(String PodXML){
    	//System.out.println("** attFound:" + podxml); 
    	if(this.podxml == null){
    		this.podxml = PodXML;
    	}else{
    		this.podxml += "#" + PodXML;
    	}
    }
    
    // GENRE
    public String getPodGenre() { return podgenre;} 
    public void setPodGenre(String PodGenre){
    	/*System.out.println("** attFound:" + podgenre);*/
    	if(this.podgenre == null){
    		this.podgenre = PodGenre;
    	}else{
    		this.podgenre += "#" + PodGenre;
    	}
    }
    
} // END CLASS


/* BBC XML ELEMENTS AND ATTRIBUTES
<outline>
<bbcstats genres="Urban|Music|Sport|News &amp; Current Affairs|Factual|Comedy &amp; Quizzes|Entertainment|Religion &amp; Ethics|Arts &amp; Drama|Classical|World|Pop &amp; Chart|Rock &amp; Indie|Science|Childrens|History|Folk &amp; Country|Jazz|Classic Pop &amp; Rock|Music Documentaries|Blues Soul &amp; Reggae|Dance|Soap|Experimental" language="en-gb|cy|es|ar|zh|ga|gd|fa|pt"
networks="1xtra|6music|asiannetwork|bbc7|berkshire|birmingham|bristol|cambridgeshire|cornwall|coventry|cumbria|cymru|derby|devon|essex|fivelive|gloucestershire|guernsey|hampshire|humbe|jersey|kent|lancashire|leeds|leiceste|lincolnshire|liverpool|london|mancheste|norfolk|northamptonshire|northernireland|northyorkshire|nottingham|oxford|radio|radio1|radio2|radio3|radio4|scotland|shropshire|somerset|southerncounties|southyorkshire|stoke|suffolk|tees|threecounties|tyne|wales|wea|wiltshire|worldservice"

<outline>
text=radio1 (networktext)
fullname=BBC Radio 1
=======
}  

/* BBC XML ELEMENTS AND ATTRIBUTES
<outline>
<bbcstats genres="Urban|Music|Sport|News &amp; Current Affairs|Factual|Comedy &amp; Quizzes|Entertainment|Religion &amp; Ethics|Arts &amp; Drama|Classical|World|Pop &amp; Chart|Rock &amp; Indie|Science|Childrens|History|Folk &amp; Country|Jazz|Classic Pop &amp; Rock|Music Documentaries|Blues Soul &amp; Reggae|Dance|Soap|Experimental" language="en-gb|cy|es|ar|zh|ga|gd|fa|pt"
networks="1xtra|6music|asiannetwork|bbc7|berkshire|birmingham|bristol|cambridgeshire|cornwall|coventry|cumbria|cymru|derby|devon|essex|fivelive|gloucestershire|guernsey|hampshire|humbe|jersey|kent|lancashire|leeds|leiceste|lincolnshire|liverpool|london|mancheste|norfolk|northamptonshire|northernireland|northyorkshire|nottingham|oxford|radio|radio1|radio2|radio3|radio4|scotland|shropshire|somerset|southerncounties|southyorkshire|stoke|suffolk|tees|threecounties|tyne|wales|wea|wiltshire|worldservice"

<outline>
type="rss"
imageHref="http://www.bbc.co.uk/podcasts/assets/artwork/moyles.jpg"
xmlUrl="http://downloads.bbc.co.uk/podcasts/radio1/moyles/rss.xml"
imageHrefTVSafe=""
text="Best of Chris Moyles"
keyname="moyles"
active="true"
allow="all"
networkName=""
networkId=""
typicalDurationMins="42"
page="http://www.bbc.co.uk/radio1/chrismoyles/"
flavour="Programme Highlights"
rsstype=""
rssenc=""
language="en-gb"
description="Weekly highlights ... by Chris and team every morning from 6.30am to 10am."
bbcgenres="Entertainment"
<outline>
text=radio1 (networktext)
fullname=BBC Radio 1

<outline>
type="rss"
imageHref="http://www.bbc.co.uk/podcasts/assets/artwork/moyles.jpg"
xmlUrl="http://downloads.bbc.co.uk/podcasts/radio1/moyles/rss.xml"
imageHrefTVSafe=""
text="Best of Chris Moyles"
keyname="moyles"
active="true"
allow="all"
networkName=""
networkId=""
typicalDurationMins="42"
page="http://www.bbc.co.uk/radio1/chrismoyles/"
flavour="Programme Highlights"
rsstype=""
rssenc=""
language="en-gb"
description="Weekly highlights ... by Chris and team every morning from 6.30am to 10am."
bbcgenres="Entertainment"
*/

