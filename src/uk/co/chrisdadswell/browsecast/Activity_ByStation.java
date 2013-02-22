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
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Adapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ByStation extends ListActivity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener 
{
	
	private final static String APP_TAG = "browsecast";
	private final static String ACT_TAG = "ByStation: ";
	private final static String ARRAYLIST_STATE = "0";
	public static boolean ARRAYLIST_STATUS = false;
	public static ArrayList<HashMap<String,String>> stationNameArrayList = new ArrayList<HashMap<String,String>>();
	private CountPodcastsTask cpTask = null;
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(APP_TAG, ACT_TAG + "... OnSaveInstanceState ...");
        Log.d(APP_TAG, ACT_TAG + "(Activity closed or orientation change)");
        if(stationNameArrayList.size() > 0) {
        	outState.putInt(ARRAYLIST_STATE, 1);
        	Log.d(APP_TAG, ACT_TAG + "ONSAVEINSTANCESTATE: ArrayList has " + stationNameArrayList.size() + " entries");
        }else{
        	outState.putInt(ARRAYLIST_STATE, 0);
        	Log.d(APP_TAG, ACT_TAG + "ONSAVEINSTANCESTATE: ArrayList empty");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(APP_TAG, ACT_TAG + "... OnRestoreInstanceState ...");
        int arrayState = savedInstanceState.getInt(ARRAYLIST_STATE);
        if(arrayState == 1) {
        	Log.d(APP_TAG, ACT_TAG + "ONSAVEINSTANCESTATE: ArrayList has " + stationNameArrayList.size() + " entries");
        	ARRAYLIST_STATUS = true;        	
        }else{
        	Log.d(APP_TAG, ACT_TAG + "ONRESTOREINSTANCESTATE: Array is empty");
        	ARRAYLIST_STATUS = false;        	
        }
    }
	
	// LIFECYCLES
	public void onStart() {
		super.onStart();
		Log.d(APP_TAG, ACT_TAG + "... OnStart ...");
		Log.d(APP_TAG, ACT_TAG + "ONSTART: Checking for running Asynctask");
		if (cpTask==null) {
			Log.d(APP_TAG, ACT_TAG + "ONSTART: Asynctask is NULL");
       	 	cpTask = new CountPodcastsTask(this);
        }else{
       	 	Log.d(APP_TAG, ACT_TAG + "ONSTARTLog.d: Asynctask status: " + cpTask.getStatus());
        	Log.d(APP_TAG, ACT_TAG + "ONSTART: Asynctask already running");
            cpTask.attach(this);
        } 
	}
     
	@Override
	public void onCreate(Bundle icicle) {
		Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
		super.onCreate(icicle);
	  	this.setContentView(R.layout.stations);
	  	cpTask = (CountPodcastsTask) getLastNonConfigurationInstance();
	  	
//		 Set NAVIGATION up	 
		ActionBar actionBar = getActionBar();
 	 	actionBar.setDisplayHomeAsUpEnabled(true);
	  	
	  	// SETUP VIEW
	  	final ProgressBar wProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
	  	final TextView title = (TextView) findViewById(R.id.tv_Title);

		Log.d(APP_TAG, ACT_TAG + "ONCREATE: ARRAYLIST_STATUS: " + ARRAYLIST_STATUS);
		// CHECK ARRAYLIST STATUS
		if(ARRAYLIST_STATUS) {
			wProgressBar.setVisibility(View.GONE);
			Log.d(APP_TAG, ACT_TAG + "ONCREATE: Using existing ArrayList to populate ListAdapter");
		  	SimpleAdapter stations_adapter = new SimpleAdapter(this,stationNameArrayList,R.layout.station_rows, new String[] {"stationName","stationPodcount"},new int[] {R.id.txt_stationname, R.id.txt_podcastname});
		  	setListAdapter(stations_adapter);
			title.setText(getString(R.string.bystation_subtitle));
			title.invalidate();
		}else{
			Log.d(APP_TAG, ACT_TAG + "ONCREATE: No ArrayList, run Asynctask to generate one");
		  	new CountPodcastsTask(Activity_ByStation.this).execute();
		}
			  	
	  	getListView().setTextFilterEnabled(true);
	  	getListView().setSmoothScrollbarEnabled(true);
	  	getListView().setFadingEdgeLength(60);	 
	  	getListView().setFastScrollEnabled(true);
	  	getListView().setVerticalFadingEdgeEnabled(true); 

		//CREATE CONTEXT MENU
		getListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
			    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
				Adapter adapter = getListAdapter();
			    Object item = adapter.getItem(info.position);
				HashMap fullObject = (HashMap)item;
			    
			    String selectedStation = fullObject.get("stationName").toString();
			    String stationTextShortName = Xml_MainDataset.GetNetworkInfo(selectedStation);
			    Intent radioWeb_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.bbc.co.uk/" + stationTextShortName));
				startActivity(radioWeb_intent);
				overridePendingTransition(R.anim.fadeout,R.anim.push_down_out);
			}
		});

	}	

	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
	    super.onListItemClick(listView, view, position, id);
	    Intent byPodcast_intent = new Intent();
	    
	    Object pos = this.getListAdapter().getItem(position); 
	    HashMap fullObject = (HashMap)pos;
	    
	    String selectedStation = fullObject.get("stationName").toString();
	    Log.d(APP_TAG, ACT_TAG + "ONLISTITEMCLICK: Station " + selectedStation + " selected. Putting extras...");
	    
	    byPodcast_intent.setClass(view.getContext(), Activity_ByPodcast.class);
	    byPodcast_intent.putExtra("SelectedStation", selectedStation);
	    startActivity(byPodcast_intent);
	    overridePendingTransition(R.anim.fadeout,R.anim.push_down_out);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bystation, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		case android.R.id.home:
			Intent intent = new Intent(this, Activity_Dashboard.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fadeout,R.anim.push_down_out);
			return true;
			
		case R.id.menu_bbcradio_www:
        	Intent stationURL_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(Constants.urlBBCRadio));
			startActivity(stationURL_intent);
			overridePendingTransition(R.anim.fadeout,R.anim.push_down_out);
			return true;
		}
		return false;
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		cpTask.detach();
	    return(cpTask);
	}
	
	private class CountPodcastsTask extends AsyncTask<Void, Boolean, Void> {
		Activity_ByStation activity=null;
    	ProgressBar wPbar = (ProgressBar) findViewById(R.id.ProgressBar);
    	TextView wTitle = (TextView) findViewById(R.id.tv_Title);
    	
	    CountPodcastsTask(Activity_ByStation activity) {
	    	Log.d(APP_TAG, ACT_TAG + "AYSNCTASK: Attaching Activity");
	    	attach(activity);
	    }
	    
	    @Override
	    protected void onPreExecute() {
	    	wPbar.setVisibility(View.VISIBLE);
			
	    	wTitle.setText("Reticulating splines, please wait...");
	    	wTitle.invalidate();
	    }
	    
		@Override
		protected Void doInBackground(Void... params) {
			Log.d(APP_TAG, ACT_TAG + "ASYNCTASK: doInBackground");
			stationNameArrayList = Func_XMLCounter.getStationPodcastTotals();
			if(stationNameArrayList.size() >0) {
				ARRAYLIST_STATUS = true;
			}else{
				ARRAYLIST_STATUS = false;
			}
			publishProgress(ARRAYLIST_STATUS);
			return null;
		}

		@Override
	    public void onProgressUpdate(Boolean... ARRAYLIST_STATUS) {
			super.onProgressUpdate(ARRAYLIST_STATUS);
			Log.d(APP_TAG, ACT_TAG + "ASYNCTASK: OnProgressUpdate");
			String arrliststatus = ARRAYLIST_STATUS[0].toString();
			if (ARRAYLIST_STATUS[0]){
				Log.d(APP_TAG, ACT_TAG + "AYSNCTASK: ArrayList full ?: " + arrliststatus);
			}else{
				Log.d(APP_TAG, ACT_TAG + "AYSNCTASK: ArrayList full ?: " + arrliststatus);
			} 
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.d(APP_TAG, ACT_TAG + "ASYNCTASK: OnPostExecute");
			Log.d(APP_TAG, ACT_TAG + "ASYNCTASK: Setting up ListAdapter with generated ArrayList, displaying STATIONS");
			setListAdapter(new SimpleAdapter(Activity_ByStation.this, stationNameArrayList, R.layout.station_rows, new String[] {"stationName","stationPodcount"},new int[] {R.id.txt_stationname, R.id.txt_podcastname}));
			wPbar.setVisibility(View.GONE);
			wTitle.setText(getString(R.string.bystation_subtitle));
			wTitle.invalidate();
			Toast.makeText(Activity_ByStation.this, "\n\nLong press a station for its mobile website\n\n", Toast.LENGTH_SHORT).show();
		}
		
		void detach() {
			Log.d(APP_TAG, ACT_TAG + "AYSNCTASK: Detaching Activity");
			activity=null;
		}

		void attach(Activity_ByStation activity) {
			this.activity=activity;
		}
	}

	public boolean onClose() {
		return false;
	}

	public boolean onQueryTextChange(String newText) {
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		return false;
	}
	
}