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
import java.util.HashMap;
import uk.co.chrisdadswell.browsecast.R;

public class Singleton
{
  // methods and attributes for Singleton pattern
  private Singleton() {}

  static private Singleton _instance;

  static public Singleton getInstance() {
    if (_instance == null) 
      _instance = new Singleton();
    return _instance;
  }

  // keep global data private
  private String gSelStation = null;
  private String gSelGenre = null;
  private String gStrSearch = null;
  private String gStationAttrib = null;
  private String gScreenSize = null;
  
  public String getgSelStation() {
    return gSelStation; 
  }
  
  public void setgSelStation(String selStation) {
	  if(selStation == ""){
		  gSelStation = null;
	  }else{
		  //System.out.println("BBCBC:" + selStation + " set");
		  gSelStation = selStation;
	  }
  }
  
  public String getgSelGenre() {
	    return gSelGenre; 
  }
	  
  public void setgSelGenre(String selGenre) {
	  if(selGenre == ""){
		  gSelGenre = null;
	  }else{
		  //System.out.println("BBCBC:" + selGenre + " set");
		  gSelGenre = selGenre;
	  }
  }
  
  public String getgStrSearch() {
	    return gStrSearch; 
}
	  
public void setgStrSearch(String strSearch) {
	  if(strSearch == ""){
		  gStrSearch = null;
	  }else{
		  //System.out.println("BBCBC:" + strSearch + " set");
	      gStrSearch = strSearch; 
	  }

}
  
  public String getgScreenSize() {
	    return gScreenSize; 
  }
	  
  public void setgScreenSize(String ScreenSize) {
	  //System.out.println("BBCBC:" + ScreenSize + " set");
	  gScreenSize = ScreenSize;
  }
  
  public String getgStationAttrib() { 
	  return gStationAttrib; 
  }
	  
  public void setgStationAttrib(String attStation) {
	  //System.out.println("BBCBC: Element attribute: " + attStation + " set");
	  gStationAttrib = attStation;
  }
  
}
