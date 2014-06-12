package com.jaspreet.mymob;

import android.app.Application;
import android.util.Log;

import com.jaspreet.mymob.contact.LoadPhoneContacts;
import com.jaspreet.mymob.util.WorkQueue;

public class ApplicationCalss extends Application {

	private static String TAG;


	public void onCreate() {

		/**
		 * Work to be done when application installed first time
		 */

		TAG = this.getClass().getName();

		/**
		 * Get all phone contacts
		 */
		
		LoadPhoneContacts contacts=new LoadPhoneContacts(ApplicationCalss.this);
		WorkQueue queue=WorkQueue.getInstance();
		queue.execute(contacts);
		
		Log.v(TAG, "Load contact thread started");

	}
	
	
	



}
