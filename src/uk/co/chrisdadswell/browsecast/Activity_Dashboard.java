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
import java.util.Date;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Dashboard extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener { 
	
	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "Dashboard: ";
	final static Date todayDate = new Date();
	static String todayDay = null;
	static String fileDay = null;
	private MenuItem refresh;
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.fadeout, R.anim.push_down);   
	}
	
	@Override
    public void onStart() {
    	super.onStart();
    	Log.d(APP_TAG, ACT_TAG + "... OnStart ...");
    }
	
	@Override
    public void onPause() {
    	super.onPause();
    	Log.d(APP_TAG, ACT_TAG + "... OnPause ...");
    }

    @Override
    public void onDestroy() {    	
    	super.onDestroy();
    	Log.d(APP_TAG, ACT_TAG + "... OnDestroy ...");
    	clearDashList();
    	Log.d(APP_TAG, ACT_TAG + "ONDESTROY: Clearing Dashlist");
    }
	
	// CREATE
    @Override 
    public void onCreate(Bundle icicle) { 
    	 super.onCreate(icicle); 
    	 setContentView(R.layout.dashboard);
    	 Context dashboard = this;

    	 createAppDirectory();
    	 Init();
    	 populateDashList();

         Log.d(APP_TAG, ACT_TAG + "ONCREATE: Showing Dashboard");
    	 SimpleAdapter dashAdapter = new SimpleAdapter(dashboard,dash_list,R.layout.dash_rows, new String[] {"option", "desc"},new int[] {R.id.text1, R.id.text2});
    	 setListAdapter(dashAdapter);
     } 
    // END OF ONCREATE

    // MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        // Set NAVIGATION up	 
		ActionBar actionBar = getActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(false);
	 	//actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.));
	 	actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_underline)));


        refresh = menu.findItem(R.id.menu_refresh);
        
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
            return true;
            
		case R.id.menu_about:
			Intent aboutActivity = new Intent(Activity_Dashboard.this, Activity_About.class);
			startActivity(aboutActivity);
			return true;

		case R.id.menu_refresh:
			downloadNewListingsDialog(this.getResources().getString(R.string.download_listings_dialog_title), this.getResources().getString(R.string.download_listings_dialog_body));
			refresh.setActionView(R.layout.actionbar_indeterminate_progress);
			return true;
			
		case R.id.menu_blog:
			 Intent blogURL_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(Constants.urlBlog));
			 startActivity(blogURL_intent);
			return true;

		case R.id.menu_gplus:
			 Intent gplusURL_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(Constants.urlGooglePlus));
			 startActivity(gplusURL_intent);
			return true;
			
		case R.id.menu_rss:
			 Intent rssURL_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(Constants.urlRss));
			 startActivity(rssURL_intent);
			return true;
			
		case R.id.menu_twitter:
			Intent twitter_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(Constants.urlTwitter));
			startActivity(twitter_intent);
			return true;
		}
		return false;
	}
    
    private void BrowseCastToast(String toast_text) {
    	LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.browsecast_toast,(ViewGroup) findViewById(R.id.custom_toast_layout_id));
	
    	// set a message
    	ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
    	image.setImageResource(R.drawable.browsecast_toast);
    	TextView text = (TextView) layout.findViewById(R.id.toast_text);
    	text.setText(toast_text);
	 
    	// 	Toast...
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.BOTTOM, 0, 50);
    	toast.setDuration(Toast.LENGTH_LONG);
    	toast.setView(layout);
    	toast.show();
    }
    
    // INITIALISATION
    public boolean Init() {
		Log.d(APP_TAG, ACT_TAG + "METHOD: Init()");

		// CHECK IF XML HAS BEEN ALREADY DOWNLOADED
        if(!Func_FileIO.FileOrDirectoryExists(Constants.mainxml)) {
        	Log.d(APP_TAG, ACT_TAG + "INIT: XML doesn't exist");
        	if(isInternetOn()) {
        		Log.d(APP_TAG, ACT_TAG + "INIT: Internet available, downloading file");
        		DownloadListings();
        		return true;
        	}else{
        		Log.d(APP_TAG, ACT_TAG + "INIT: No internet, denied!");
        		noInternetNoQuitDialog("No Internet Connection", "Unable to download Podcast listings.\n\nThere appears to be no internet connection.\n\nPlease connect to the internet\nand relaunch BrowseCast.");
        		return false;
        	}
        }else{ 
        // XML EXISTS
        	Log.d(APP_TAG, ACT_TAG + "INIT: XML exists, checking date stamp");
        	if (getFileDay().equals(getTodayDay())){
        		Log.d(APP_TAG, ACT_TAG + "INIT: XML has already been downloaded today");
        		if(getFileSize() != 0) { // DOWNLOADED TODAY ALREADY AND NOT 0 BYTES START ACTIVITY_BYSTATION
        			Log.d(APP_TAG, ACT_TAG + "INIT: XML downloaded and not 0 bytes, display dashboard");
        			return true;
        		}else{ // FILE AVAILABLE. BUT IS 0 BYTES. NEED TO CHECK INTERNET AND DOWNLOAD
        			Log.d(APP_TAG, ACT_TAG + "INIT: XML exists, but is 0 bytes, attempt re-download if internet available");
        			if(isInternetOn()) {
        				Log.d(APP_TAG, ACT_TAG + "INIT: Internet available, downloading ...");
        				DownloadListings();
        				return true;
        			}else{ // NO INTERNET, RAISE AN ALERT AND FORCE USER TO QUIT
        				Log.d(APP_TAG, ACT_TAG + "INIT: No internet, display no internet dialog and quit");
        				noInternetDialog("No internet connection", "A new Podcast listings file needs to be downloaded, but there appears to be no internet connection.\n\nPlease connect to the internet\nand relaunch BrowseCast.");
        			}
        		}
        	}else{ // OUT OF DATE XML, ADVISE USER AN UPDATE IS AVAILABLE
        		Log.d(APP_TAG, ACT_TAG + "INIT: XML is a day old, show the toast");
        		BrowseCastToast(this.getResources().getString(R.string.toast_listings_outofdate));
        		return true;
        	}
        }
        return false;
	}

    private void DownloadListings() {
    	String url = "http://www.bbc.co.uk/podcasts.xml";

    	DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    	request.setDescription("BBC Podcast listings");
    	request.setTitle("BBC Podcasts.xml");
    	// in order for this if to run, you must use the android 3.2 to compile your app
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    	    request.allowScanningByMediaScanner();
    	    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
    	}
    	request.setDestinationInExternalPublicDir(Constants.xmldir, "podcasts.xml");
    	
    	// get download service and enqueue file
    	DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    	manager.enqueue(request);
    	Log.d(APP_TAG, ACT_TAG + "RUN: Starting download of Podcasts.xml file ...");
    	BrowseCastToast(this.getResources().getString(R.string.toast_download_listings));
    	refresh.setActionView(null);
    }

    ArrayList<HashMap<String,String>> dash_list = new ArrayList<HashMap<String,String>>();
	private void populateDashList() {
		 Log.d(APP_TAG, ACT_TAG + "DASHBOARD: Adding options to dashboard");
		 
		 HashMap<String,String> temp = new HashMap<String,String>();
		 temp.put("option", getString(R.string.dashboard_menu_bystation));
		 temp.put("desc", getString(R.string.dashboard_menu_bystation_subtitle));
		 dash_list.add(temp);
		 
		 HashMap<String,String> temp1 = new HashMap<String,String>();
		 temp1.put("option","5Live Podcasts");
		 temp1.put("desc", "5Live Podcasts Website");
		 dash_list.add(temp1);
		 
		 HashMap<String,String> temp3 = new HashMap<String,String>();
		 temp3.put("option","Radio Schedules");
		 temp3.put("desc", "View BBC Radio scheduling information");
		 dash_list.add(temp3);
		 
		 HashMap<String,String> temp4 = new HashMap<String,String>();
		 temp4.put("option","Radio iPlayer by Station");
		 temp4.put("desc", "View iPlayer content by Station (BBC Media Player and BBC iPlayer required)");
		 dash_list.add(temp4);
		 
//		 HashMap<String,String> temp5 = new HashMap<String,String>();
//		 temp5.put("option","Podcast Help and Assistance");
//		 temp5.put("desc", "What are Podcasts? What apps are good for Podcasting?");
//		 dash_list.add(temp5);
	}                                                      
    
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		    super.onListItemClick(listView, view, position, id);
		    if(id == 0) {
		    	Intent intentStation = new Intent(Activity_Dashboard.this, Activity_ByStation.class);
		    	Activity_Dashboard.this.startActivity(intentStation);
				overridePendingTransition(R.anim.fadeout,R.anim.push_left);
		    }else if(id == 1){
		    	Intent fivelive_intent = new Intent(Intent.ACTION_VIEW); 
				fivelive_intent.setData(Uri.parse("http://www.bbc.co.uk/podcasts/5live.mp"));
				startActivity(fivelive_intent);
				overridePendingTransition(R.anim.fadeout,R.anim.push_left);
		    }else if(id == 2){
		    	Intent intentSchedules = new Intent(Activity_Dashboard.this, Activity_Schedules.class);
		    	Activity_Dashboard.this.startActivity(intentSchedules);
		    	overridePendingTransition(R.anim.fadeout,R.anim.push_left);
		    }else if(id == 3){
		    	Intent intentiPlayer = new Intent(Activity_Dashboard.this, Activity_iPlayer.class);
		    	Activity_Dashboard.this.startActivity(intentiPlayer);
		    	overridePendingTransition(R.anim.fadeout,R.anim.push_left);
		    }else if(id == 4){
		    	Intent intentHelp = new Intent(Activity_Dashboard.this, Activity_Help.class);
		    	Activity_Dashboard.this.startActivity(intentHelp);
		    	overridePendingTransition(R.anim.fadeout,R.anim.push_left);
		    }
	}
	
     // CHECK INTERNET METHOD
     public final boolean isInternetOn() {
    	 Log.d(APP_TAG, ACT_TAG + "ISINTERNETON: Checking connectivity ...");
    	 ConnectivityManager connec = null;
         connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
         if ( connec != null){
        	 NetworkInfo result = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
             if (result != null && result.isConnectedOrConnecting());
             return true;
         }
         return false;
     }
        	 
     // DIALOGS
     protected void noInternetDialog(String title, String message) { 
    	 Log.d(APP_TAG, ACT_TAG + "NOINTERNETDIALOG: No internet");
    	 AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	 alertbox.setMessage(message) 
    	 .setTitle(title) 
    	 .setCancelable(true) 
     	 .setIcon(R.drawable.ic_alert_thumbsdown)
    	 .setNeutralButton("Quit", new DialogInterface.OnClickListener() { 
    		 public void onClick(DialogInterface dialog, int whichButton){
    			 finish();
    		 }
    	 });
    	 alertbox.show(); 
     }

     protected void noInternetNoQuitDialog(String title, String message) { 
    	 Log.d(APP_TAG, ACT_TAG + "NOINTERNETNOQUITDIALOG: No InternetNoQuit");
    	 AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	 alertbox.setMessage(message) 
    	 .setTitle(title) 
    	 .setCancelable(true) 
     	 .setIcon(R.drawable.ic_alert_thumbsdown)
     	 .setPositiveButton("Dashboard", new DialogInterface.OnClickListener() { 
    		 public void onClick(DialogInterface dialog, int whichButton){
    			 Log.d(APP_TAG, ACT_TAG + "NOINTERNETNOQUITDIALOG: Using existing XML file");
    			 dialog.dismiss();
    		 }
     	 })
    		 
     	 .setNeutralButton("Quit", new DialogInterface.OnClickListener() { 
    		 public void onClick(DialogInterface dialog, int whichButton){
    			 Log.d(APP_TAG, ACT_TAG + "DIALOG: Quitting BrowseCast");
    			 finish();
    		 }
    	 });
    	 alertbox.show(); 
     }

     protected void downloadNewListingsDialog(String title, String message) { 
    	 Log.d(APP_TAG, ACT_TAG + "DOWNLOADNEWLISTINGSDIALOG: Download new file or not");
    	 AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	 alertbox.setMessage(message) 
    	 .setTitle(title) 
    	 .setCancelable(true) 
     	 .setIcon(R.drawable.ic_action_about)
     	 .setPositiveButton("Download new listings", new DialogInterface.OnClickListener() { 
    		 public void onClick(DialogInterface dialog, int whichButton){
    			 Log.d(APP_TAG, ACT_TAG + "DOWNLOADNEWLISTINGSDIALOG: Download new file");
    			 if(isInternetOn()) {
    				 Log.d(APP_TAG, ACT_TAG + "INIT: We have internet, go get it!");
    				 // delete current podcasts.xml
    				 Func_FileIO.DeleteFile(Constants.mainxml);
    				 // clear stationnamearraylist
    				 resetData();
    				 // download new listings
    				 DownloadListings();
    			 }else{
         			Log.d(APP_TAG, ACT_TAG + "INIT: No internet, display no internet dialog and quit");
         			noInternetDialog("No internet connection", "A new Podcast listings file needs to be downloaded, but there appears to be no internet connection.\n\nPlease connect to the internet\nand relaunch BrowseCast.");
    			 }
    		 }
     	 })
    		 
     	 .setNeutralButton("Use current listings", new DialogInterface.OnClickListener() { 
    		 public void onClick(DialogInterface dialog, int whichButton){
    			 Log.d(APP_TAG, ACT_TAG + "DOWNLOADNEWLISTINGSDIALOG: Resuming with current file");
    			 dialog.dismiss();
    		 }
    	 });
    	 alertbox.show(); 
     }

     
     private void createAppDirectory() {
    	 Log.d(APP_TAG, ACT_TAG + "CREATEAPPDIRECTORY: Create application directories");
    	 Func_FileIO.CreateDirectory(Constants.appdir);
         Func_FileIO.CreateDirectory(Constants.xmldir);
         Func_FileIO.CreateDirectory(Constants.imagesdir);
     }
     
     private long getFileSize() {
    	 long fileSize = Func_FileIO.FileSize(Constants.mainxml);
    	 return fileSize;
     }

     private String getFileDay() {
 		Date fileDate = Func_FileIO.FileLastModified(Constants.mainxml); // GET DAY OF THE WEEK FOR LAST FILE DOWNLOAD
  		fileDay = Func_Date.GetDayofTheDate(fileDate);
  		return fileDay;
     }
     
     private String getTodayDay() {
    	 String todayDay = Func_Date.GetDayofTheDate(todayDate);
    	 return todayDay;
     }
     
     private void clearDashList() {
    	 dash_list.clear();
     }

 	private final void resetData() {
		Log.d(APP_TAG, ACT_TAG + "RESETDATA: Clearing ArrayLists");
		Activity_ByStation.ARRAYLIST_STATUS = false;
		Activity_ByStation.stationNameArrayList.clear();
	}

	public boolean onClose() {
		return false;
	}

	public boolean onQueryTextChange(String arg0) {
		return false;
	}

	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
    
} // END OF ACTIVITY CLASS