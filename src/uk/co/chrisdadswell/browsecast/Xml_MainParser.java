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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

public class Xml_MainParser { 
	
	public static String ByType(int byType) {
		final String APP_TAG = "browsecast";
		final String ACT_TAG = "XML Parser: ";
		final String PARSER_RESULT = "No Podcasts Available";
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		InputStream fso = null;
		Xml_MainHandler myXmlHandler = new Xml_MainHandler(); 
		
		try {
			fso = new FileInputStream(Constants.mainxml);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		
        try { 
		   SAXParser sp = spf.newSAXParser(); 
		   XMLReader xr = sp.getXMLReader(); 
		   xr.setContentHandler(myXmlHandler); 
		   xr.parse(new InputSource(fso));
		
		   Xml_MainDataset parsedDataSet = myXmlHandler.getParsedData();
		   //Log.d(APP_TAG, ACT_TAG + "ByType = " + byType);
		   switch (byType) {
		   case 1:
			   //Xml_MainDataset parsedStationDataSet = myXmlHandler.getParsedData();
			   return parsedDataSet.intoStations();
		   case 2:
			   //Xml_MainDataset parsedGenreDataSet = myXmlHandler.getParsedData();
			   return parsedDataSet.intoGenre();
		   case 3:
			   //Xml_MainDataset parsedPodcastDataSet = myXmlHandler.getParsedData();
			   return parsedDataSet.intoPodInfo();
		   }
        } catch (Exception e) { 
        	Log.d(APP_TAG, ACT_TAG + e.getMessage(), e.getCause());
          	}	
        return PARSER_RESULT;
		}
	}