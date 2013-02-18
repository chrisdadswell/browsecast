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
 
import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_PodcastView extends Activity {
	
	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "PodcastView: ";
	
	public static String podcastUrl = null;
	public static String pcastUrl = null;
	public static String podcastPage = null;
	public static String podcastName = null;
    public static String podcastImage =  null;
    public static String podcastDescription = null;
    public static String podcastFlavour = null;
    public static String podcastDuration  = null;
    public static String podcastGenre = null;
	
	@Override
    public void onStart() {
    	super.onStart();
    	Log.d(APP_TAG, ACT_TAG + "... OnStart...");
    	Log.d(APP_TAG, ACT_TAG + "ONSTART: Detecting for PocketCasts");
    	if(isPocketCastsInstalled()) {
    		
    	}else{
		
    	}
	}
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.podcastview);
        
        ImageView wPodcastImage = (ImageView) this.findViewById(R.id.imgview_podcast);
		TextView wPodcastName = (TextView) this.findViewById(R.id.tv_podcastName);
        TextView wPodcastDuration = (TextView) this.findViewById(R.id.tv_podcastDuration);
        TextView wPodcastDescription = (TextView) this.findViewById(R.id.tv_podcastDescription);

        wPodcastImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			Intent podcastWWW_intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(podcastPage));
    			startActivity(podcastWWW_intent);
            }
        });
      
//		 Set NAVIGATION up    	 
		ActionBar actionBar = getActionBar();
	 	actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle intent_extras = getIntent().getExtras(); 
		if(intent_extras !=null) {
			final String selPodcast = intent_extras.getString("SelectedPodcast");
			if(GetPodcastValues(selPodcast)) {
				podcastName = selPodcast;
				pcastUrl = podcastUrl.toString().replaceAll("http://", "pcast://");
			}else{
				Toast.makeText(Activity_PodcastView.this, "\n\nThere was a problem retrieving podcast information for this Podcast\n\n", Toast.LENGTH_LONG).show();
				finish();
			}
		}
        
        new DownloadImageTask((ImageView) findViewById(R.id.imgview_podcast))
        .execute(podcastImage);
        
//        GoogleAdView adView = (GoogleAdView) findViewById(R.id.adview);
//        adView.showAds(adSenseSpec);
        
        wPodcastName.setText(podcastName);
        wPodcastDuration.setText("Typical duration: " + podcastDuration + " mins");
        wPodcastDescription.setText(podcastDescription);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.podcastview, menu);
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
           
		case R.id.menu_sharetopodcastapp:
			Intent sharetoPodcastAppintent = new Intent(Intent.ACTION_VIEW); 
			sharetoPodcastAppintent .setData(Uri.parse(pcastUrl));
			startActivity(sharetoPodcastAppintent );
			return true;

		case R.id.menu_sharetoother:
			Intent sharetootherIntent = new Intent(Intent.ACTION_SEND);
			sharetootherIntent.setType("text/plain");
			sharetootherIntent.putExtra(Intent.EXTRA_TEXT, podcastUrl);
			startActivity(Intent.createChooser(sharetootherIntent, "Share Podcast Subscription"));				
			return true;
			
		case R.id.menu_copytoclip:
			ClipboardManager clipboard = (ClipboardManager)
	        getSystemService(Context.CLIPBOARD_SERVICE);
			// Creates a new text clip to put on the clipboard
			ClipData clip = ClipData.newPlainText("Podcast URL",podcastUrl);
			clipboard.setPrimaryClip(clip);
			Toast.makeText(Activity_PodcastView.this, "\n\nPodcast subscription information copied to clipboard\n\n", Toast.LENGTH_SHORT).show();
			return true;
			
/*
 * Uri myUri = Uri.parse("http://Yoururl.com/file.mp3");
    Intent intent = new Intent(android.content.Intent.ACTION_VIEW); 
    intent.setDataAndType(myUri, "audio/*"); 
    startActivity(intent);
 * */
			
			}
		return false;
	}
    
    public boolean GetPodcastValues(String podcastToGet){
    	Object[] oPInfo = null;
    	oPInfo = Xml_MainDataset.GetPodcastInfo(podcastToGet);
    	
    	if(oPInfo != null){
    		podcastUrl = oPInfo[0].toString();
    	    podcastImage = oPInfo[1].toString();
    	    podcastDescription = oPInfo[2].toString();
    	    podcastDuration = oPInfo[4].toString();
    	    podcastPage = oPInfo[5].toString();
    	    podcastGenre = oPInfo[6].toString();
    	    return true;
    	}else{
    		return false;
    	}
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

	private boolean isPocketCastsInstalled() {
        Func_FileIO.FileOrDirectoryExists(Constants.PocketCastsDir);
        if(Func_FileIO.FileOrDirectoryExists(Constants.PocketCastsDir)) {
        	return true;
        }else{
        	return false;
        }
	}

}
