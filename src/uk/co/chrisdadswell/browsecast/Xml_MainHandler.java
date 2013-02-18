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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class Xml_MainHandler extends DefaultHandler { 

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "XML Handler: ";

	private enum Tags {opml, head, title, body, dateCreated, dateModified, ownerName, ownerEmail, bbcstats, outline}
	
	private boolean in_head = false;
 	
	private String selectedStation = Singleton.getInstance().getgSelStation();
 	private String selectedGenre = Singleton.getInstance().getgSelGenre();
 	private String searchString = Singleton.getInstance().getgStrSearch();
 	
 	private String stationName = null;
 	private String networkId = null;
 	private String genreName = null;
 	private String podcastName = null;
 	public Xml_MainDataset myXmlDataset = new Xml_MainDataset(); 
	 
 	public Xml_MainDataset getParsedData() { 
 		//Log.d(APP_TAG, ACT_TAG + "Returning xmlDataset");
 		return this.myXmlDataset;
 	} 
     
 	@Override 
 	public void startDocument() throws SAXException {
 		//Log.d(APP_TAG, ACT_TAG + "START xmlDataset");
 		this.myXmlDataset = new Xml_MainDataset();
 	}
     
 	@Override 
 	public void endDocument() throws SAXException {
 		//Log.d(APP_TAG, ACT_TAG + "END xmlDataset");
 	}
 	
 	@Override 
 	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException { 
 		switch(Tags.valueOf(localName)) {
 		case head:
 			this.in_head = true;
 			//System.out.println("HANDLER: Start of element: " + localName);
 		case bbcstats:
 			if(this.in_head)
 				myXmlDataset.setGenre(atts.getValue("genres"));
 			break;
 		case body:
 			break;
 		case outline:
 			int numAtts = atts.getLength();
 			if(numAtts == 1) {
 			// FIRST OUTLINE
 			}else if(numAtts == 3) {
 				// OUTLINE STATIONS
 				myXmlDataset.setNetworkText(atts.getValue("text"));
 				myXmlDataset.setNetworkFullName(atts.getValue("fullname"));
 				myXmlDataset.setNetworkId(atts.getValue("networkId"));
 				// RETRIEVE STATIONNAME and NETWORKID ATTRIBUTE
 				stationName = atts.getValue("fullname");
 				networkId = atts.getValue("networkId");
 			}else if (numAtts == 15) {
 				if(selectedStation != null) {
    				boolean Presult = Func_Strings.compareStr(selectedStation, stationName);
    				if(Presult){
    					myXmlDataset.setPodText(atts.getValue("text"));
    					myXmlDataset.setPodImg(atts.getValue("imageHref"));
    					myXmlDataset.setPodXML(atts.getValue("xmlUrl"));
    					myXmlDataset.setPodDuration(atts.getValue("typicalDurationMins"));
    					myXmlDataset.setPodPage(atts.getValue("page"));
    					myXmlDataset.setPodFlavour(atts.getValue("flavour"));
    					myXmlDataset.setPodDescription(atts.getValue("description"));
    					myXmlDataset.setPodGenre(atts.getValue("bbcgenres"));
    				}else{
    					// NOT THE STATION PODCAST WERE LOOKING FOR
    				}
    			}else if(selectedGenre !=null) {
       			 	// RETRIEVE GENRE NAME ATTRIBUTE
       			 	genreName = atts.getValue("bbcgenres");
       			 	// CHECK FOR RETURN GENRES WITH MORE THAN ONE GENRE SET I.E. Factual|History
       			 	boolean Gresult = Func_Strings.FindWordInString(genreName, selectedGenre);
       			 	if(Gresult) {
       			 		genreName = selectedGenre; 
	       			 	boolean GPresult = Func_Strings.compareStr(selectedGenre, genreName);
	    				if(GPresult){
	    					myXmlDataset.setPodImg(atts.getValue("imageHref"));
	    					myXmlDataset.setPodXML(atts.getValue("xmlUrl"));
	    					myXmlDataset.setPodText(atts.getValue("text"));
	    					myXmlDataset.setPodDuration(atts.getValue("typicalDurationMins"));
	    					myXmlDataset.setPodPage(atts.getValue("page"));
	    					myXmlDataset.setPodFlavour(atts.getValue("flavour"));
	    					myXmlDataset.setPodDescription(atts.getValue("description"));
	    					myXmlDataset.setPodGenre(atts.getValue("bbcgenres"));
	    				}else{
	    					// NOT THE GENRE PODCAST WERE LOOKING FOR
	    				}
       			 	}
       			 	
    			}else if(searchString !=null) {
       			 	// RETRIEVE PODCAST NAME (TEXT) ATTRIBUTE
       			 	podcastName = atts.getValue("text");
       			 	// TRY TO FIND THE SEARCH TERM IN THE PODCAST NAME 
       			 	boolean Sresult = Func_Strings.FindWordInString(podcastName, searchString);
       			 	if(Sresult) {
       			 		podcastName = searchString; 
	       			 	boolean GSPresult = Func_Strings.compareStr(searchString, podcastName);
	    				if(GSPresult){
	    					myXmlDataset.setPodImg(atts.getValue("imageHref"));
	    					myXmlDataset.setPodXML(atts.getValue("xmlUrl"));
	    					myXmlDataset.setPodText(atts.getValue("text"));
	    					myXmlDataset.setPodDuration(atts.getValue("typicalDurationMins"));
	    					myXmlDataset.setPodPage(atts.getValue("page"));
	    					myXmlDataset.setPodFlavour(atts.getValue("flavour"));
	    					myXmlDataset.setPodDescription(atts.getValue("description"));
	    					myXmlDataset.setPodGenre(atts.getValue("bbcgenres"));
	    				}else{
	    					// NOT THE PODCASTS WE WERE SEARCHING FOR
	    				}
    			}
    			}else{}
    		 break;
     }}}
     
     @Override 
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException { 
    	 switch(Tags.valueOf(localName)) {
    	 case head:
    		 this.in_head = false;
    		 //System.out.println("HANDLER: End of element: " + localName);
    		 break;
    	 case body:
    		 break;
    	 case outline:
    		 break;
    	 case opml:
    	 }
     }
    	 
     @Override 
    public void characters(char ch[], int start, int length) { 
    	 /* ONLY USED FOR CHARS BETWEEN TAGS
    	  * if(this.node_outline_rss){
    	  * myXmlDataset.setExtractedString(new String(ch, start, length));
         */  
     } 
}