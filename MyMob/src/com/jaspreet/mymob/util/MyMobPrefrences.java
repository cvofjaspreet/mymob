package com.jaspreet.mymob.util;
/**
@author Jaspreet 
*/

import android.content.Context;
import android.content.SharedPreferences;



/**
 * 
 * 
 * It will save all app preferences
 * 
 * @author jaspreet
 *
 */
public class MyMobPrefrences {
	
	public static MyMobPrefrences mobPrefrences;
	public static SharedPreferences pref;
	
	
	//Preference constants
	public static String INSTALLED = "installed";
	
	public static MyMobPrefrences getInstance(Context context) {
		if (mobPrefrences == null) {
			mobPrefrences = new MyMobPrefrences(context);
			
		}
		return mobPrefrences;
	}
	
	private MyMobPrefrences(Context context) {
		pref = context.getSharedPreferences("maraPreferences",
				Context.MODE_PRIVATE);
	}
	
	

	public boolean isInstalled() {
		return pref.getBoolean(INSTALLED, false);
	}
	public void setInstalled(boolean installed) {
		
		pref.edit().putBoolean(INSTALLED,installed ).commit();

	}
	
	
}
