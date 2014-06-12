package com.jaspreet.mymob;
/**
@author Jaspreet 
*/


import com.jaspreet.mymob.register.RegisterComman;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class Splash extends ActionBarActivity{

	private static String TAG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getName();
		setContentView(R.layout.splashscreen);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.header));
		actionBar.setLogo(getResources().getDrawable(R.drawable.ic_launcher_small));
	}

	
	CountDownTimer cdt = new CountDownTimer(3000, 1000) {

        public void onTick(long millisUntilFinished) {
            Log.i(TAG,"clocks ticks");
        }

        public void onFinish() {
        	
        	Intent intent=new Intent(Splash.this,RegisterComman.class);
        	startActivity(intent);
        	
        	finish();
        }
    }.start(); 
	
	

	
}
