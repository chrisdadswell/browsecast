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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

public class Func_FileIO {

    // =========================================================== 
    // Fields 
    // =========================================================== 
	static Date dateObj = new Date();
	
	// CHECK FOR SDCARD
	public static boolean IsSDPresent() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	// CHECK FOR WORKING DIRECTORIES, CREATE IF NECESSARY
	public static boolean CreateDirectory(String forf){
		File file=new File(forf);
		if(IsSDPresent()){
			boolean exists = file.exists(); {
				if (!exists) {
					// It returns false if File or directory does not exist and creates directory
					try {
						File sddir = new File(forf);
							if(sddir.mkdir()) {
								System.out.println("MAIN: Directory " + forf + " created successfully");
							}else{
							}
					}
					finally{}
				    }else{
				    	System.out.println("MAIN: Directory " + forf + " already exists");
				    }
			}
			return exists;
		}else{
			System.out.println("MAIN: /sdcard not mounted");
		}
		return true;
		}
	
	@SuppressLint("WorldReadableFiles")
	public static boolean CreateFile(String filename, String value, Context ctx) {
		FileOutputStream fos;
		try {
            fos = ctx.openFileOutput(filename, Context.MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(value.getBytes());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch(IOException e){
            e.printStackTrace();
        }
		return true;
	}
	
	public static String ReadFile(String filename, Context ctx) throws FileNotFoundException {
		FileInputStream fis;
		fis = ctx.openFileInput(filename);
		StringBuffer content = new StringBuffer("");
	    try {
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = fis.read(buffer)) != -1) {
	    	    content.append(new String(buffer));
	    	}
	    	
//	    	fis = ctx.openFileInput(filename);
//	    	byte[] input = new byte[fis.available()];
//	    	while (fis.read(input) != -1) {}
//	    	content = new String(input);
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace(); 
	    }
		return content.toString();
	}
	
	public static boolean DeleteFile(String fileToDelete) {
	    File file=new File(fileToDelete);
	    boolean exists = file.exists();
	    if(!exists) {
	      // It returns false if File does not exist
	      System.out.println("MAIN: File: " + fileToDelete + " does NOT exist");
	    }else{
	      // It returns true if File exists
	      System.out.println("MAIN: File: " + fileToDelete + " exists");
	      file.delete();
	      return exists;
	    }
		return false;
	}
	
	public static boolean FileOrDirectoryExists(String ForDToLookFor) {
		    File file=new File(ForDToLookFor);
		    boolean exists = file.exists();
		    if(!exists) {
		      // It returns false if File or directory does not exist
		      System.out.println("MAIN: File or Directory: " + ForDToLookFor + " does NOT exist");
		    }else{
		      // It returns true if File or directory exists
		      System.out.println("MAIN: File or Directory: " + ForDToLookFor + " exists");
		      return exists;
		    }
			return false;
		  }

	public static Date FileLastModified(String fileToLookFor) {
		File file=new File(fileToLookFor);
		long tmp = file.lastModified();  
        Date fileDate = new Date(tmp);  
		return fileDate;
	}
	
	public static int IsFileOld(Date fileDate) {
		int result = fileDate.compareTo(dateObj);
		return result;
	}

	public static long FileSize(String fileName) {
		File file = new File(fileName);
		long fileSize = file.length();
		return fileSize;	
	}
	
}