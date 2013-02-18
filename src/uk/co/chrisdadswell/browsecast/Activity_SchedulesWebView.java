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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Activity_SchedulesWebView extends Activity {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "Activity_SchedulesWebView: ";

	private WebView scheduleWebView;

	private static int dayNumber = 0;
	private static int monthNumber = 0;
	private static int yearNumber = 0;
	public static int currentDay = 0;
	
	public static int nextClicks = 0;
	public static int prevClicks = 0;

	private static String selectedStation = null;
	private static String stationShortName = null;
	private static String selectedRegion = null;
	private static String scheduleURL = null;
	
	private static String baseURL = "http://www.bbc.co.uk/";
	private static String midURL = "/programmes/schedules";
	private static String endURL = ".mp";
	
	// LIFECYCLES
    @Override
	public void onStart() {
		super.onStart();
		Log.d(APP_TAG, ACT_TAG + "... OnStart ...");
		dayNumber = Func_Date.GetDayofMonth();
		monthNumber = Func_Date.GetNumberofMonth();
		yearNumber = Func_Date.GetYear();
		Log.d(APP_TAG, ACT_TAG + "Day no: " + dayNumber);
		Log.d(APP_TAG, ACT_TAG + "Month no: " + monthNumber);
		Log.d(APP_TAG, ACT_TAG + "Year: " + yearNumber);
	}    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.schedules);
	  Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
	    
//		 Set NAVIGATION up	 
		ActionBar actionBar = getActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(true);
	  
	  Bundle intent_extras = getIntent().getExtras(); 
	  if(intent_extras !=null) {
		  selectedRegion = intent_extras.getString("SelectedRegion");
		  selectedStation = intent_extras.getString("SelectedStation");
		  stationShortName = intent_extras.getString("StationShortName");
		  scheduleURL = intent_extras.getString("ScheduleURL");
	  }
	  
      if(stationShortName.contains("radio1")) {
      	scheduleURL = baseURL + stationShortName + midURL + "/" + selectedRegion + endURL;
      }else if(stationShortName.contains("radio4")) {
      	scheduleURL = baseURL + stationShortName + midURL + "/" + "fm" + endURL;
      }else{
      	scheduleURL = baseURL + stationShortName + midURL + endURL;
      }
	  
	  setupView();
	  scheduleWebView.clearCache(true);
	  scheduleWebView.loadUrl(scheduleURL);
	  scheduleWebView.setWebViewClient(new HelloWebViewClient());
	  
	  final Activity Activity_WebView = this;
	  scheduleWebView.setWebChromeClient(new WebChromeClient() {
		  public void onProgressChanged(WebView view, int progress)  
		  {
			  ProgressBar wPB = (ProgressBar) findViewById(R.id.pb_progressbar);
			  Activity_WebView.setProgress(progress * 100);
			  
			  if(progress == 100)
				  wPB.setVisibility(View.GONE);
		  }
	  });
	}	
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setupView() {
		scheduleWebView = (WebView) findViewById(R.id.webview);
		scheduleWebView.getSettings().setJavaScriptEnabled(true);
		TextView wTitle = (TextView) findViewById(R.id.tv_Title);
		wTitle.setText(selectedStation + " Schedule");
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedulesview, menu);
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
			
		case R.id.menu_viewstations:
			finish();
			return true;

		case R.id.menu_browsedates:
			if(stationShortName.contains("radio1")) {
				scheduleURL = baseURL + stationShortName + midURL + "/"
				+ selectedRegion + "/"
				+ yearNumber + "/" 
				+ monthNumber
				+ ".mp";					
			}else{
				scheduleURL = baseURL + stationShortName + midURL + "/"
				+ yearNumber + "/" 
				+ monthNumber
				+ ".mp";					
			}
			
			Log.d(APP_TAG, ACT_TAG + "Browse: " + scheduleURL);
			scheduleWebView.loadUrl(scheduleURL);
			return true;
		}
		return false;
	}

	private class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && scheduleWebView.canGoBack()) {
	        scheduleWebView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}