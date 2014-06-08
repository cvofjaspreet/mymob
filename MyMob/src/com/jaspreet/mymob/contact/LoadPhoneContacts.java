/**
@author Jaspreet 
*/

package com.jaspreet.mymob.contact;

import com.jaspreet.mymob.util.MyMobPrefrences;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class LoadPhoneContacts implements Runnable{
	
	private Context context;
	private MyMobPrefrences mobPrefrences;
	private static String TAG;
	public LoadPhoneContacts(Context context) {
		this.context=context;
	}

	@Override
	public void run() {
		TAG = this.getClass().getName();
		mobPrefrences = MyMobPrefrences.getInstance(context);
		if (!mobPrefrences.isInstalled())
			installTimeWork();
	}
	
	
	private void installTimeWork() {

		/**
		 * TODO
		 * 
		 * 1.Create app account
		 * 
		 */

		// Utils.showDialog(ApplicationCalss.this);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		// String[] projection = ContactsQuery.PROJECTION;

		Cursor people = context.getContentResolver().query(uri, null, null, null, null);

		people.moveToFirst();
		do {
			String phoneLabel = null;
			try {
				int type = people.getInt(people
						.getColumnIndexOrThrow(Phone.TYPE));

				String customLabel = people
						.getString(people
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
				phoneLabel = (String) ContactsContract.CommonDataKinds.Phone
						.getTypeLabel(context.getResources(), type, customLabel);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			int indexName = people.getColumnIndex(ContactsQuery.CONTACT_NAME);

			int indexNumber = people
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

			String name = people.getString(indexName);
			String number = people.getString(indexNumber);
			Log.i(TAG, name + " , " + number + " , " + phoneLabel);
			// Do work...
		} while (people.moveToNext());

		// Utils.closeDialog();
		mobPrefrences.setInstalled(true);

	}
	
	

}
