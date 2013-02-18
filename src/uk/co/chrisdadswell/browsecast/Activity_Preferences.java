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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;
import uk.co.chrisdadswell.browsecast.R;
 
public class Activity_Preferences extends PreferenceActivity {
	
    public static final String _APP_TAG = "Activity_Preferences"; 
	public Intent intent = new Intent(Intent.ACTION_VIEW); 
	SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final Context act_prefs = this;
        
        Preference prefGettingStarted = (Preference) findPreference("prefGettingStarted");
        prefGettingStarted.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
		    	Intent intentGetStarted = new Intent(Activity_Preferences.this, Activity_Help.class);
		    	Activity_Preferences.this.startActivity(intentGetStarted);
        		return true;
        	}
        });
        
        Preference prefGetListen = (Preference) findPreference("prefGetListen");
        prefGetListen.setOnPreferenceClickListener(new OnPreferenceClickListener() { 
        	public boolean onPreferenceClick(Preference preference) {
        		intent.setData(Uri.parse("market://details?id=com.google.android.apps.listen"));
        		Toast.makeText(act_prefs, "Loading the Marketplace...", Toast.LENGTH_SHORT).show();
        		startActivity(intent);
        		return true;
        	}
        });
        
        Preference prefAboutListen = (Preference) findPreference("prefAboutListen");
        prefAboutListen.setOnPreferenceClickListener(new OnPreferenceClickListener() { 
        	public boolean onPreferenceClick(Preference preference) {
				intent.setData(Uri.parse("http://www.chrisdadswell.co.uk/how-google-listen-interacts-with-google-reader/"));
				startActivity(intent);
        		return true;
        	}
        });
        
        Preference prefGetSupport = (Preference) findPreference("prefGetSupport");
        prefGetSupport.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	public boolean onPreferenceClick(Preference preference) {
				intent.setData(Uri.parse("http://www.chrisdadswell.co.uk/bbcbrowsecast/support"));
				startActivity(intent);
        		return true;
        	}
        });
          
        Preference prefAbout = (Preference) findPreference("prefAbout");
        prefAbout.setOnPreferenceClickListener(new OnPreferenceClickListener() { 
        	public boolean onPreferenceClick(Preference preference) {
		    	Intent intentAbout = new Intent(Activity_Preferences.this, Activity_About.class);
		    	Activity_Preferences.this.startActivity(intentAbout);
        		return true;
        	}
        });
        
        
        
	} // END OF ACTIVITY
	
	public void onStart() {
		super.onStart();
//		//Toast.makeText(this, ".. onStart ..", Toast.LENGTH_SHORT).show();	
//		if(Func_FileIO.FileOrDirectoryExists(Constants.listenDir)) {
//			Preference prefGetListen = (Preference) findPreference("prefGetListen");
//			prefGetListen.setSummary("Google Listen already installed");
//	 		prefGetListen.setEnabled(false);
//		}else{
//	 		 
//		}
	}
}