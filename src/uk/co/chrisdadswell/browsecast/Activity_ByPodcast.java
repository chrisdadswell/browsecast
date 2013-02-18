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

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ByPodcast extends ListActivity {
	    
	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "ByPodcast: ";
	
	private static String podcastUrl = null;
	private static String podcastPage = null;
	private static String podcastName = null;
	private static String podcastImage =  null;
	private static String podcastDescription = null;
	private static String podcastFlavour = null;
	private static String podcastDuration  = null;
	private static String podcastGenre = null;
	private static String strSearch = null;
	private static String selStation = null;
	private static String selGenre = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
		setContentView(R.layout.lists);
	    
//		 Set NAVIGATION up	 
		ActionBar actionBar = getActionBar();
  	 	actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle intent_extras = getIntent().getExtras(); 
		if(intent_extras !=null) {
			selStation = intent_extras.getString("SelectedStation");
	    	Log.d(APP_TAG, ACT_TAG + "Bundled extras:\n strSearch = " + strSearch + 
	    			"\nselStation = " + selStation + "\nselGenre = " + selGenre);
		}
		
		// GET SEARCH INTENT
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      strSearch = intent.getStringExtra(SearchManager.QUERY);
	    }

		// INIT WIDGETS
		TextView wTitle = (TextView) findViewById(R.id.tv_Title);
		wTitle.setText(selStation);
		
// STATION		
		if(selStation !=null) {
			Singleton.getInstance().setgSelGenre("");
			Singleton.getInstance().setgSelStation(selStation);
			
			// SET FOOTER URL, GET NETWORK SHORTNAME
			String stationTextShortName = Xml_MainDataset.GetNetworkInfo(selStation);
			String stationText = ("http://www.bbc.co.uk/mobile/" + stationTextShortName);
			String stationTextUrl = ("<a href='" + stationText + "'" + "style='color: #ffffff'>" + selStation + " Mobile Website</a>");
//			wStationUrl.setText(Html.fromHtml(stationTextUrl));
//	        wStationUrl.setMovementMethod(LinkMovementMethod.getInstance());
			
			// RETRIEVE AND PREPARE ARRAY FOR LISTVIEW
			String[] arr_podcasts = new String[]{Xml_MainParser.ByType(3)};	
			String temp_podcasts = Func_Strings.arrayToString(arr_podcasts, "#");
			Log.d(APP_TAG, ACT_TAG + "temp_podcasts = " + temp_podcasts);
			String[] final_podcasts = temp_podcasts.split("#");
			int podCount = final_podcasts.length;
			
			if(temp_podcasts.contains("No Podcasts Available")) {
				podCount = 0;
				String[] no_podcasts = {""};
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, no_podcasts));
			}else{
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, final_podcasts));			
			}
			wTitle.setSelected(true);
			
// GENRE				
		}else if(selGenre !=null) {
			Singleton.getInstance().setgSelStation("");
			Singleton.getInstance().setgSelGenre(selGenre);
			
			// RETRIEVE AND PREPARE ARRAY FOR LISTVIEW
			String[] arr_podcasts = new String[]{Xml_MainParser.ByType(3)};	
			String temp_podcasts = Func_Strings.arrayToString(arr_podcasts, "#");
			String[] final_podcasts = temp_podcasts.split("#");
			int podGCount = final_podcasts.length;

			if(temp_podcasts.contains("No Podcasts Available")) {
				podGCount = 0;
				String[] no_podcasts = { "" };
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, no_podcasts));
			}else{
				wTitle.setText(selGenre);
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, final_podcasts));			
			}
							
// SEARCH			
		}else if(strSearch !=null) {
			Singleton.getInstance().setgSelStation("");
			Singleton.getInstance().setgSelGenre("");
			Singleton.getInstance().setgStrSearch(strSearch);
			
			String[] arr_podcasts = new String[]{Xml_MainParser.ByType(3)};	
			String temp_podcasts = Func_Strings.arrayToString(arr_podcasts, "#");
			String[] final_podcasts = temp_podcasts.split("#");
			int podSCount = final_podcasts.length;
			
			if(temp_podcasts.contains("No Podcasts Available")) {
				podSCount = 0;
				String[] no_podcasts = {""};
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, no_podcasts));
			}else{
				wTitle.setText("Your search for " + strSearch + " returned " + podSCount + " Podcast(s)");
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, final_podcasts));
			}
		} 
		
		// CREATE CONTEXT MENU
		getListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(selStation);
				menu.add(0, Constants.CONTEXT_PODCAST, 0, "Retrieve Podcast");
			}
		});
		
		getListView().setTextFilterEnabled(true);
		getListView().setSmoothScrollbarEnabled(true);
		getListView().setFadingEdgeLength(60);
		getListView().setFastScrollEnabled(true);
		getListView().setVerticalFadingEdgeEnabled(true);
	}
	
	// LISTVIEW ONCLICK
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
	    super.onListItemClick(listView, view, position, id);

	    // Get the item that was clicked 
	    Object pos = this.getListAdapter().getItem(position); 
	    podcastName = pos.toString();
	    GetPodcastValues(podcastName);
	    if(podcastUrl != null) {
	    	// LAUNCH PODCASTVIEW ACTIVITY
		    Intent byPodcastView_intent = new Intent();
		    byPodcastView_intent.setClass(view.getContext(), Activity_PodcastView.class);
		    byPodcastView_intent.putExtra("SelectedPodcast", podcastName);
		    startActivity(byPodcastView_intent);
	    }else{
	    	Toast.makeText(this, "No podcast(s) available at this time."  , Toast.LENGTH_SHORT).show();
	    }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bypodcast, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case android.R.id.home:
            Intent intent = new Intent(this, Activity_ByStation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
			}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		String podcastName = ((TextView) menuInfo.targetView).getText().toString();
		GetPodcastValues(podcastName);		
		switch (item.getItemId()) {
		case Constants.CONTEXT_PODCAST:
			if(podcastUrl != null) {
				Intent myIntent = null; 
			    myIntent = new Intent("android.intent.action.VIEW", Uri.parse(podcastUrl)); 
			    startActivity(myIntent);
			    }
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}
			
    public void GetPodcastValues(String podcastToGet){
    	Log.d(APP_TAG, ACT_TAG + "GETPODCASTVALUES: podcastToGet = " + podcastToGet);
    	Object[] oPInfo = Xml_MainDataset.GetPodcastInfo(podcastToGet);
    	if(oPInfo == null) {
    		Log.d(APP_TAG, ACT_TAG + "GETPODCASTVALUES: oPInfo, Null pointer error");
    	}else{
    	    podcastUrl = oPInfo[0].toString();
    	    podcastImage = oPInfo[1].toString();
    	    podcastDescription = oPInfo[2].toString();
    	    podcastFlavour = oPInfo[3].toString();
    	    podcastDuration = oPInfo[4].toString();
    	    podcastPage = oPInfo[5].toString();
    	    podcastGenre = oPInfo[6].toString();
    	    Log.d(APP_TAG, ACT_TAG + "GETPODCASTVALUES: Variables returned are: \npodcastUrl: " + podcastUrl + 
    	    		"\npodcastImage:" + podcastImage + "\npodcastDescription:" + podcastDescription +
    	    		"\npodcastFlavour:" + podcastFlavour + "\npodcastDuration:" + podcastDuration +
    	    		"\npodcastPage:" + podcastPage + "\npodcastGenre:" + podcastGenre);
    	}
    }
}