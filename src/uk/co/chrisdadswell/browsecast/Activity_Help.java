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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Help extends ListActivity  {
	
	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "Help: ";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
//		 Set NAVIGATION up	 
		ActionBar actionBar = getActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(true);
        
        Log.d(APP_TAG, ACT_TAG + "... OnCreate ...");
  	  	String[] support_options = {"Using BrowseCast","About Podcasts","Available Podcast Applications","Further Information and Support"};
  	  
  	  	// SETUP VIEW
  	  	TextView wTitle = (TextView) findViewById(R.id.tv_Title);
        wTitle.setText("Select a support option below");

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, support_options));
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
	    final String selectedOption = pos.toString();
		
		if(selectedOption == "Using BrowseCast") {
			
		}else if(selectedOption == "About Podcasts"){
			
		}else if(selectedOption == "Available Podcast Applications"){
			
		}else if(selectedOption == "Further Information and Support"){
			
		}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help, menu);
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
