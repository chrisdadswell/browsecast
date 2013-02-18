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
import android.app.ListActivity;
import android.content.DialogInterface;
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

public class Activity_ListenLive extends ListActivity {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "ListenLive: ";
	private String stationListenLiveURLIP = null;
	
	// LIFECYCLES
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.listenlive);
	 
	  Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
	  String[] live_stations = {"Radio 1","1Xtra","Radio 2","Radio 3","Radio 4 FM","5Live","5Live Sports Extra","6Music","Radio 7","Asian Network"};
	  
	  // SETUP VIEW
	  TextView wTitle = (TextView) findViewById(R.id.tv_Title);
      wTitle.setText("Available Listen Live Stations");
      
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
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/radio1_3gp_live.sdp");
		}else if(selectedStation == "1Xtra"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/bbc1xtra_3gp_live.sdp");
		}else if(selectedStation == "Radio 2"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/radio2_3gp_live.sdp");
		}else if(selectedStation == "Radio 3"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/radio3_3gp_live.sdp");
		}else if(selectedStation == "Radio 4 FM"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/radio4_3gp_live.sdp");
		}else if(selectedStation == "5Live"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/5live_3gp_live.sdp");
		}else if(selectedStation == "5Live Sports Extra"){
			stationListenLiveURLIP = ("http://www.bbc.co.uk/mobile/live/sdp/bbcradio5sx.sdp?");
		}else if(selectedStation == "6Music"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/6music_3gp_live.sdp");
		}else if(selectedStation == "Radio 7"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/bbc7_3gp_live.sdp");
		}else if(selectedStation == "Asian Network"){
			stationListenLiveURLIP = ("rtsp://3gplive-acl.bbc.co.uk:554/bbc-rbs/rmlive-3gp/rtpencoder/bbcasiannet_3gp_live.sdp");
		}

	    // DISPLAY DIALOG PRESENTING WHETHER TO USE INTERNAL 3GGP OR REALPLAYER
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setIcon(R.drawable.browsecast);
		builder.setTitle("About Listen Live");
		builder.setInverseBackgroundForced(true);
		builder.setMessage("Only newer handsets have built in " +
				"codecs to play BBC 3GGP streams. So your handsets mileage may vary.\n" +
				"\nIf you are unable to get a connection it usually means that the stream is busy or down.\n" +
				"\nIt is NOT the fault of this application.");
		
		builder.setPositiveButton("Listen Live", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent listenlive_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(stationListenLiveURLIP));
				startActivity(listenlive_intent);
			}
		});			
		builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
		
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listenlive, menu);
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