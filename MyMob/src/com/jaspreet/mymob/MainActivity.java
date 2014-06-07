package com.jaspreet.mymob;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	private static final int VIDEO_GALLERY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		

Account account = new Account("dnkf", "dkf");
AccountManager am = AccountManager.get(this);
boolean accountCreated = am.addAccountExplicitly(account, null, null);
 
Bundle extras = getIntent().getExtras();
if (extras != null) {
 if (accountCreated) {  //Pass the new account back to the account manager
  AccountAuthenticatorResponse response = extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
  Bundle result = new Bundle();
  result.putString(AccountManager.KEY_ACCOUNT_NAME, "dnkf");
  result.putString(AccountManager.KEY_ACCOUNT_TYPE, "dkf");
  response.onResult(result);
 }

}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		public void onButtonClick(View view) {
			Intent intent = new Intent();

			intent.setType("video/*");

			intent.setAction(Intent.ACTION_GET_CONTENT);
			// intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT,
			// 10485760L);
			startActivityForResult(intent, VIDEO_GALLERY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			try {
				Uri mUri = data.getData();
				String path = convertMediaUriToPath(mUri);
				File file = new File(path);
				long fileVideo = file.length();
				Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
						MediaStore.Images.Thumbnails.MICRO_KIND);
				String thumb_path = PhotoUtil.getThumbUri(MainActivity.this)
						.getPath();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				thumb.compress(CompressFormat.PNG, 100, baos);

				thumb.compress(CompressFormat.PNG, 100, new FileOutputStream(
						thumb_path));

				SendVideoFile file2 = new SendVideoFile(file);

				file2.execute();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	protected String convertMediaUriToPath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();

		return path;
	}
	
	
	
	public void onButtonClick(View view) {
		Intent intent = new Intent();

		intent.setType("video/*");

		intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT,
		// 10485760L);
		startActivityForResult(intent, VIDEO_GALLERY);
	}

}