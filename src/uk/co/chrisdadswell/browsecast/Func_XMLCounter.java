package uk.co.chrisdadswell.browsecast;

import java.util.ArrayList;
import java.util.HashMap;
import uk.co.chrisdadswell.browsecast.R;

public class Func_XMLCounter {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "XML Counter: ";
	public static int totalStationPodCount = 0;
	public static int totalGenrePodCount = 0;
	public static int stationCount = 0;
	
	// STATION
	static ArrayList<HashMap<String,String>> stationPodCountArrayList = new ArrayList<HashMap<String,String>>();
	static HashMap<String, String> stationPodCountHashMap = new HashMap<String,String>();
	// GENRE
	static ArrayList<HashMap<String,String>> genrePodCountArrayList = new ArrayList<HashMap<String,String>>();
	static HashMap<String, String> genrePodCountHashMap = new HashMap<String,String>();
	
	public static ArrayList<HashMap<String, String>> getStationPodcastTotals() {
		String[] stations_list = getNameList(1);
		int stationPodCount = 0;
		stationCount = stations_list.length;
		
			for (String stationName : stations_list) {
				HashMap<String, String> stationPodCountHashMap = new HashMap<String,String>();
				stationPodCount = podCounter(stationName, 1);
				if(stationPodCount == 0) { stationCount -= stationCount; continue; }
				stationPodCountHashMap.put("stationName", stationName);
				stationPodCountHashMap.put("stationPodcount", "" + stationPodCount + " podcast(s)");
				stationPodCountArrayList.add(stationPodCountHashMap);
				totalStationPodCount += stationPodCount;
			}
			return stationPodCountArrayList;
	}

	public static ArrayList<HashMap<String, String>> getGenrePodcastTotals() {
		String[] genres_list = getNameList(2);
		int genrePodCount = 0;
		
			for (String genreName : genres_list) {
				HashMap<String, String> genrePodCountHashMap = new HashMap<String,String>();
				genrePodCount = podCounter(genreName, 2);
				genrePodCountHashMap.put("genreName", genreName);
				genrePodCountHashMap.put("genrePodcount", "" + genrePodCount + " podcast(s) available");
				genrePodCountArrayList.add(genrePodCountHashMap);
				totalGenrePodCount += genrePodCount;
			}
			return genrePodCountArrayList;
	}
	
	public static int podCounter(String counterName, int counterType) {
		int podCount = 0;
		String splitter = "#";

		if(counterType == 1) {
			Singleton.getInstance().setgSelStation(counterName);
		}else if(counterType == 2) {
			Singleton.getInstance().setgSelGenre(counterName);
		}
		
		String[] arr_podcasts = new String[]{Xml_MainParser.ByType(3)};	
		String temp_podcasts = Func_Strings.arrayToString(arr_podcasts, splitter);
		String[] final_podcasts = temp_podcasts.split(splitter);
		
		if(temp_podcasts.contains("No Podcasts Available")) {
			podCount = 0;
		}else{
			podCount = final_podcasts.length;
		}
		return podCount;
	}
	
	public static String[] getNameList(int NameType) {
		String splitter = null;

		switch(NameType) {
		case 1: //station
			splitter = "#";
			break;
		case 2: //genre
			splitter = "\\|";
			break;
		}
		
		String[] arr_names = new String[]{Xml_MainParser.ByType(NameType)};
		String temp_names = Func_Strings.arrayToString(arr_names, splitter);
		String[] names_list = temp_names.split(splitter);
		return names_list;
	}
}