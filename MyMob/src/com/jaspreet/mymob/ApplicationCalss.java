package com.jaspreet.mymob;

import com.jaspreet.mymob.util.MyMobPrefrences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;

public class ApplicationCalss extends Application{

	
	
	private static final String ACCOUNT_NAME = "mymob";
	private static final String ACCOUNT_TYPE = "contacts";
	private static final String PROVIDER = "sdj";

	private MyMobPrefrences mobPrefrences;
	public void onCreate() {
		
		/**
		 * Work to be done when application installed first time
		 */
		
		mobPrefrences=MyMobPrefrences.getInstance(ApplicationCalss.this);
		if(!mobPrefrences.isInstalled())
		installTimeWork();
		
	}
	
	
	
	
	
	
	
	
	
	
private void installTimeWork() {
		
		/**
		 * TODO 
		 * 
		 * 1.Create app account 
		 * 2.Get phone constants
		 * 
		 */
		/*
		Account appAccount = new Account(ACCOUNT_NAME,ACCOUNT_TYPE);
		AccountManager accountManager = AccountManager.get(getApplicationContext());
		if (accountManager.addAccountExplicitly(appAccount, null, null)) {
		   ContentResolver.setIsSyncable(appAccount, PROVIDER, 1);
		   ContentResolver.setMasterSyncAutomatically(true);
		   ContentResolver.setSyncAutomatically(appAccount, PROVIDER, true);
		}*/

 /*   Account account = new Account("jaspreet", "dfkj");
    AccountManager am = AccountManager.get(this);
    boolean accountCreated = am.addAccountExplicitly(account, null, null);
     
    Bundle extras = getIntent.getExtras();
    if (extras != null) {
     if (accountCreated) {  //Pass the new account back to the account manager
      AccountAuthenticatorResponse response = extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
      Bundle result = new Bundle();
      result.putString(AccountManager.KEY_ACCOUNT_NAME, username);
      result.putString(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.ACCOUNT_TYPE));
      response.onResult(result);
     }
     finish();
    }*/

		
		mobPrefrences.setInstalled(true);
	}

	
}
