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

// NOTES

package uk.co.chrisdadswell.browsecast;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_iPlayer extends ListActivity {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "iPlayer: ";
	
	private String iPlayerBaseURL = "http://www.bbc.co.uk/mobile/iplayer/schedule/";
	private static String stationSelected = null;
	private static String iPlayerStationURL = null;
	private static String endURL = null;
	
	private static int dayNumber = 0;
	private static int monthNumber = 0;
	private static int yearNumber = 0;
	
	private static String dayString = null;
	private static String monthString = null;
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.fadeout, R.anim.push_right);   
	}
	
	// LIFECYCLES
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.iplayer);
	  Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
	  
//    Set NAVIGATION up	 
	  ActionBar actionBar = getActionBar();
	  actionBar.setDisplayHomeAsUpEnabled(true);

	  
	  String[] live_stations = {"Radio 1","1Xtra","Radio 2","Radio 3","Radio 4", "Radio 4ex", "5Live","6Music", "Asian Network"};
	  
	  yearNumber = Func_Date.GetYear();
	  monthNumber = Func_Date.GetNumberofMonth();
	  dayNumber = Func_Date.GetDayofMonth();
	  
	  if(monthNumber < 10) {
		  monthString = "0" + monthNumber;
	  }
	  
	  if(dayNumber < 10) {
		  dayString = "0" + dayNumber;
	  }
	  
	  // SETUP VIEW
	  TextView wTitle = (TextView) findViewById(R.id.tv_Title);
      wTitle.setText("Available iPlayer Stations");
      
      setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, live_stations));
	  getListView().setTextFilterEnabled(true);
	  getListView().setSmoothScrollbarEnabled(true);
	  getListView().setFadingEdgeLength(60);
	  getListView().setFastScrollEnabled(true);
	  getListView().setVerticalFadingEdgeEnabled(true);
	}	
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
	    super.onListItemClick(listView, view, position, id);

	    Object pos = this.getListAdapter().getItem(position); 
	    final String selectedStation = pos.toString();
		
		if(selectedStation == "Radio 1") {
			stationSelected = ("bbc_radio_one/");
		}else if(selectedStation == "1Xtra"){
			stationSelected = ("bbc_1xtra/");
		}else if(selectedStation == "Radio 2"){
			stationSelected = ("bbc_radio_two/");
		}else if(selectedStation == "Radio 3"){
			stationSelected = ("bbc_radio_three/");
		}else if(selectedStation == "Radio 4"){
			stationSelected = ("bbc_radio_four/");
		}else if(selectedStation == "Radio 4 Extra"){
			stationSelected = ("bbc_radio_four_extra/");
		}else if(selectedStation == "5Live"){
			stationSelected = ("bbc_radio_five_live/");
		}else if(selectedStation == "6Music"){
			stationSelected = ("bbc_6music/");
		}else if(selectedStation == "Asian Network"){
			stationSelected = ("bbc_asian_network/");
		}

		// SETUP URL TO LOAD
		endURL = stationSelected + yearNumber + "-" + monthString + "-" + dayString;
		iPlayerStationURL = iPlayerBaseURL + endURL;
		
		Intent browserInternet = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(iPlayerStationURL));
		startActivity(browserInternet);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.iplayer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		
		case android.R.id.home:
            Intent intent = new Intent(this, Activity_Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
		}
		return false;
    }
}

/* TO DO 
Latest music - http://www.bbc.co.uk/radio1/nowplaying/latest.mp
Tracklistings - http://www.bbc.co.uk/radio1/playlist.mp
*/