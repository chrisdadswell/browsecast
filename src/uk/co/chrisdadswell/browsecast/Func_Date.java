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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class Func_Date {

	final static String APP_TAG = "browsecast";
	final static String ACT_TAG = "Func_Date: ";
	
	public static String GetDayofTheDate(Date fileDate) {

		String day = null;
		DateFormat f = new SimpleDateFormat("EEEE");
		try {
			day = f.format(fileDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}
	
	public static String GetMonthofTheDate(Date fileDate) {

		String month = null;
		DateFormat f = new SimpleDateFormat("MM");
		try {
			month = f.format(fileDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return month;
	}
	
	public static int GetNumberofMonth() {
		int currentMonth = 0;
		Calendar month = Calendar.getInstance();
		currentMonth = (month.get(Calendar.MONTH) +1);
		return currentMonth;
	}
	
	public static int GetDayofMonth() {
		int tempDay = 0;
		Calendar calender = Calendar.getInstance();
		tempDay = calender.get(Calendar.DAY_OF_MONTH);
		return tempDay;
	}
	
	public static int GetYear() {
		Calendar calender = Calendar.getInstance();
		return calender.get(Calendar.YEAR);
	}

	public static int GetDaysinMonth() {
		Calendar cal = new GregorianCalendar(GetYear(), Calendar.MONTH, 1);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	public static int GetPreviousDay(int prevClicks) {
		int previousDay = 0;
		Calendar cal = Calendar.getInstance();
		previousDay = cal.get(Calendar.DAY_OF_MONTH)-prevClicks;
		Log.d(APP_TAG, ACT_TAG + "Previous day: " + prevClicks);
		return previousDay;
	}
	
	public static int GetNextDay(int nextClicks) {
		int nextDay = 0;
		Calendar cal = Calendar.getInstance();
		nextDay = cal.get(Calendar.DAY_OF_MONTH)+nextClicks;
		Log.d(APP_TAG, ACT_TAG + "Next day: " + nextDay);
		return nextDay;		
	}
	
	public static String GetMonth() {
		String[] months = {"January", "February",
	            "March", "April", "May", "June", "July",
	            "August", "September", "October", "November",
	            "December"};
	 
		Calendar cal = Calendar.getInstance(); 
		String month = months[cal.get(Calendar.MONTH)];
		return month;
	}
}
