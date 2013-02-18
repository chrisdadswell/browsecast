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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import uk.co.chrisdadswell.browsecast.R;

import android.util.Log;

public class Func_Download {

	final static String APP_TAG = "browsecast";
	final static int size=1024;
	
	public static boolean DownloadFile(String fileAddress, String destinationDir)	{
		// Find the index of last occurrence of character ‘/’ and ‘.’.
		int lastIndexOfSlash =	fileAddress.lastIndexOf('/');
		int lastIndexOfPeriod =	fileAddress.lastIndexOf('.');
		
		// Find the name of file to be downloaded from the address.
		String fileName=fileAddress.substring(lastIndexOfSlash + 1);
		// Check whether path or file name is given correctly.
		if (lastIndexOfPeriod >=1 && lastIndexOfSlash >= 0 && lastIndexOfSlash < fileAddress.length()-1) {
			if(GetFile(fileAddress,fileName,destinationDir)) {
				// All good
				return true;
			}else{
				// Broked
				return false;
			}
		}else{
			System.err.println("Err: Specify correct path or file name.");
			return false;
		}
	}
	
	public static boolean GetFile(String fileAddress, String localFileName, String destinationDir) {
	OutputStream os = null;
	URLConnection URLConn = null;
	URL fileUrl;
	byte[] buf;
	
	// URLConnection class represents a communication link between the application and a URL.
	InputStream is = null;
	try {
		int ByteRead,ByteWritten=0;
		fileUrl= new URL(fileAddress);
		os = new BufferedOutputStream(new FileOutputStream(destinationDir + "/" + localFileName));
	
		// The URLConnection object is created by invoking the openConnection method on a URL.
		URLConn = fileUrl.openConnection();
		
		is = URLConn.getInputStream();
		buf = new byte[size];
		while ((ByteRead = is.read(buf)) != -1) {
			os.write(buf, 0, ByteRead);
			ByteWritten += ByteRead;
		}
		return true;
	}
		catch (Exception e) {
			if(is == null) {
				Log.d(APP_TAG, "RESPONSE: Null input stream");
				return false;
			}
			e.printStackTrace();
		}
		finally {
			if(is == null) {
				// Broked
				return false;
			}else{
				try {
					is.close();
					os.close();
				}	
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	return false;
	} 

	
}
