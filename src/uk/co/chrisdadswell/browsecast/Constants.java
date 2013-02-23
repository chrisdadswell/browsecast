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

import android.os.Environment;
import android.view.Menu;
import uk.co.chrisdadswell.browsecast.R;

public final class Constants {

	private Constants() { }
	
	public static final int ByStation = 1;
	public static final int ByGenre = 2;
	public static final int ByPodcast = 3;
	
	// DIRECTORIES
	public static final String appdir = Environment.getExternalStorageDirectory() + "/browsecast";
	public static final String xmldir = "/browsecast/xml";
	public static final String imagesdir = "/browsecast/images";
	
	// FILES
	public static final String mainxml = Environment.getExternalStorageDirectory()+ "/browsecast/xml/podcasts.xml";
	
	// URLS
	public static final String urlRss = "http://www.google.com/reader/view/feed/http%3A//www.chrisdadswell.co.uk/feed/";
	public static final String urlTwitter = "http://twitter.com/#!/bbcbrowsecast";
	public static final String urlBlog = "http://www.chrisdadswell.co.uk";
	public static final String urlGooglePlus = "https://plus.google.com/103906208250029081417/posts";
	public static final String urlBBCRadio = "http://www.bbc.co.uk/mobile/radio";

	// CONTEXT AND POPUP MENUS
	public static final int MENU_PREFERENCES = Menu.FIRST +1; 
	public static final int MENU_DELETEXML = Menu.FIRST +2;
	
	public static final int CONTEXT_STATIONS = Menu.FIRST;
	public static final int CONTEXT_LISTENLIVE = Menu.FIRST +1;
	public static final int CONTEXT_GENRES = Menu.FIRST +2;
	public static final int CONTEXT_PODCAST = Menu.FIRST;
	public static final int CONTEXT_PODINFO = Menu.FIRST +1;
	
	
}
