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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Schedules extends ListActivity {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "Activity_Schedules: ";

	protected static final int DIALOG_REGION = 0;
	
	public static String selectedFrequency = null;
	public static String selectedRegion = null;
	public static String selectedPeriod = null;
	public static String selectedStation = null;
	public static String stationShortName = null;
	public static String scheduleURL = null;
	
	private static final String PREFS = "preferences"; //  Name of the file -.xml
	
	// LIFECYCLES
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.lists);
	  Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");

	  TextView listsTitle = (TextView) findViewById(R.id.tv_Title);
	  SharedPreferences settings = getSharedPreferences(PREFS, 0);
	  
	  // CHECK FOR CURRENT REGION IN PREFS
	  selectedRegion = settings.getString("prefs_region", null);
	  
	  if(selectedRegion == null) {
		  Log.d(APP_TAG, ACT_TAG + "INIT: No region set");
		  showDialog(DIALOG_REGION);
	  }else{
		  Log.d(APP_TAG, ACT_TAG + "INIT: Region already set");
		  listsTitle.setText(getString(R.string.schedules_current_region) + " " + selectedRegion);
		  listsTitle.invalidate();
	  }
	  
      String[] arr_stations = new String[]{Xml_MainParser.ByType(1)};
      String temp_stations = Func_Strings.arrayToString(arr_stations, "#");
      String[] STATIONS = temp_stations.split("#");
      
	  setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, STATIONS));
	  getListView().setTextFilterEnabled(true);
	  getListView().setSmoothScrollbarEnabled(true);
	  getListView().setFadingEdgeLength(60);
	  getListView().setFastScrollEnabled(true);
	  getListView().setVerticalFadingEdgeEnabled(true);
	}	
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
	    super.onListItemClick(listView, view, position, id);
		Intent intentWebView = new Intent();
	    Object pos = this.getListAdapter().getItem(position); 
	    selectedStation = pos.toString();

	    //GET THE SELECTED STATION SHORT NAME
        stationShortName = Xml_MainDataset.GetNetworkInfo(selectedStation);
        
		intentWebView.setClass(Activity_Schedules.this, Activity_SchedulesWebView.class);
		intentWebView.putExtra("SelectedRegion", selectedRegion);
		intentWebView.putExtra("SelectedStation", selectedStation);
		intentWebView.putExtra("StationShortName", stationShortName);
	    startActivity(intentWebView);
	}

	private void setSubtitle() {
		TextView listsTitle = (TextView) findViewById(R.id.tv_Title);
		listsTitle.setText(getString(R.string.schedules_current_region) + " " + selectedRegion);
		listsTitle.invalidate();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedules, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		TextView listsTitle = (TextView) findViewById(R.id.tv_Title);
		switch (item.getItemId()){
		case android.R.id.home:
            Intent intent = new Intent(this, Activity_Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
            
		case R.id.menu_setregion:
			showDialog(DIALOG_REGION);
			listsTitle.setText(getString(R.string.schedules_current_region) + " " + selectedRegion);
			listsTitle.invalidate();
			return true;
		}
		return false;
	}

	// REGION LIST DIALOG
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
        case DIALOG_REGION:
        	Log.d(APP_TAG, ACT_TAG + "DIALOG: Display Region list dialog");
        	final String[] REGION  = {"England", "N. Ireland", "Scotland", "Wales"};
        	AlertDialog.Builder region_dialog = new AlertDialog.Builder(this);
        	region_dialog.setTitle("Select Your Region");
        	region_dialog.setItems(REGION, new DialogInterface.OnClickListener() {
            	
        		public void onClick(DialogInterface dialog, int which) {
        			SharedPreferences settings = getSharedPreferences(PREFS, 0);
        			SharedPreferences.Editor editor = settings.edit();

                	selectedRegion = REGION[which].toLowerCase();
                	if(selectedRegion.contains("n. ireland")) selectedRegion = "northernireland";
                	Log.d(APP_TAG, ACT_TAG + "Region selected: " + selectedRegion);
                	editor.putString("prefs_region", selectedRegion);
                	editor.commit();
                	setSubtitle();
                	dialog.dismiss();
        			return;
        		}
            }                       
            );
            
        	region_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        		public void onCancel(DialogInterface dialog) {
        			Log.d(APP_TAG, ACT_TAG + "REGION dialog cancelled!");

        			Toast.makeText(Activity_Schedules.this, "\n\nA region must be selected\n\n", Toast.LENGTH_SHORT).show();
        			removeDialog(DIALOG_REGION);
        			showDialog(DIALOG_REGION);
        		}
			});
            
            // TYPE OF DIALOG
            AlertDialog region_alert = region_dialog.create();
            return region_alert;
            
        default:
    	}
    	return super.onCreateDialog(id);
    }
    
}