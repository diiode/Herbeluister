package be.bartdewallef.herbeluister;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("NewApi")
public class ShowDetailsActivity extends SherlockActivity{
	private final static String TAG = "ShowDetailsActivity";
	
	private static int mPos = 0;
	private static int mShowPos = 0;
	private Uri mUri;
	
	private TextView mNameText;
	private TextView mDescriptionText;
	private TextView mSchedulingText;
	private TextView mContentsText;
	private TextView mPresenterText;
	private TextView mEmailText;
	private TextView mWeblinkText;
	private String mName;
	private String mDescription;
	private String mScheduling;
	private String mContents;
	private String mPresenter;
	private String mEmail;
	private String mWeblink;
	
	private Handler mHandler;
	
	private String mDownloadUrl;
	
	public static Intent newInstance(Activity activity, int pos, int showpos, Uri uri) {
		Intent intent = new Intent(activity, ShowDetailsActivity.class);
		intent.putExtra("pos", pos);
		intent.putExtra("showpos", showpos);
		intent.putExtra(ShowTable.CONTENT_ITEM_TYPE, uri);
		return intent;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_details);

		//String[] stations = getResources().getStringArray(R.array.stations);
		//int arrayId = getResources().getIdentifier(stations[mPos], "array", getPackageName());
		//String[] shows = getResources().getStringArray(arrayId);
		

		
		mNameText = (TextView) findViewById(R.id.text_name);
        mDescriptionText = (TextView) findViewById(R.id.text_description_content);
        mSchedulingText = (TextView) findViewById(R.id.text_scheduling_content);
        mContentsText = (TextView) findViewById(R.id.text_contents_content);
        mPresenterText = (TextView) findViewById(R.id.text_presenter_content);
        mEmailText = (TextView) findViewById(R.id.text_email_content);
        mWeblinkText = (TextView) findViewById(R.id.text_weblink_content);
        
		// Check from savedInstanceState
		if (savedInstanceState != null) {
			mPos = savedInstanceState.getInt("pos");
			mShowPos = savedInstanceState.getInt("showpos");
			mUri = savedInstanceState.getParcelable(ShowTable.CONTENT_ITEM_TYPE);
		}
		// Check from other activity
		if (getIntent().getExtras() != null) {
			mPos = getIntent().getExtras().getInt("pos");
			mShowPos = getIntent().getExtras().getInt("showpos");
			mUri = getIntent().getExtras().getParcelable(ShowTable.CONTENT_ITEM_TYPE);
			
			fillData();
		}
		

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
	}
	
	private void fillData() {
		String[] projection = { ShowTable.SHOW_NAME,
				ShowTable.SHOW_DESCRIPTION,
				ShowTable.SHOW_DESCRIPTION64,
				ShowTable.SHOW_CONTENTS,
				ShowTable.SHOW_WEBSITEURL,
				ShowTable.SHOW_PRESENTER,
				ShowTable.SHOW_SCHEDULING,
				ShowTable.SHOW_STREAMURL,
				ShowTable.SHOW_EMAIL };
		Cursor cursor = getContentResolver().query(mUri, projection, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			
			mName = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_NAME));
			mNameText.setText(mName);
			setTitle(mName);
			
			mDescription = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_DESCRIPTION));
			if(mDescription != null && !mDescription.isEmpty()) {
				mDescriptionText.setText(mDescription);
			} else {
				mDescriptionText.setVisibility(View.GONE);
				findViewById(R.id.text_description_title).setVisibility(View.GONE);
			}
			mScheduling = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_SCHEDULING));
			if(mScheduling!=null && !mScheduling.isEmpty()) {
				mSchedulingText.setText(mScheduling);
				Log.d(TAG, "Wel scheduling info");
			} else {
				mSchedulingText.setVisibility(View.GONE);
				findViewById(R.id.text_scheduling_title).setVisibility(View.GONE);
				Log.d(TAG, "Geen scheduling info");
			}
			mContents = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_CONTENTS));
			if(mContents != null && !mContents.isEmpty()) {
				mContentsText.setText(mContents);
			} else {
				mContentsText.setVisibility(View.GONE);
				findViewById(R.id.text_contents_title).setVisibility(View.GONE);
			}
			mPresenter = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_PRESENTER));
			if(mPresenter != null && !mPresenter.isEmpty()) {
				mPresenterText.setText(mPresenter);
			} else {
				mPresenterText.setVisibility(View.GONE);
				findViewById(R.id.text_presenter_title).setVisibility(View.GONE);
			}
			mEmail = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_EMAIL));
			if(mEmail != null && !mEmail.isEmpty()) {
				mEmailText.setText(mEmail);
				//Linkify
				Linkify.addLinks(mEmailText, Linkify.EMAIL_ADDRESSES);
			} else {
				mEmailText.setVisibility(View.GONE);
				findViewById(R.id.text_email_title).setVisibility(View.GONE);
			}
			mWeblink = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_WEBSITEURL));
			if(mWeblink != null) {
				mWeblinkText.setText(mWeblink);
				//Linkify
				Linkify.addLinks(mWeblinkText, Linkify.WEB_URLS);
			} else {
				mWeblinkText.setVisibility(View.GONE);
				findViewById(R.id.text_weblink_title).setVisibility(View.GONE);
			}
			mDownloadUrl = cursor.getString(cursor.getColumnIndexOrThrow(ShowTable.SHOW_STREAMURL));
			
			cursor.close();
			
			

		}
		
	}


	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_show_details, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.github:
			Util.goToGitHub(this);
			return true;
		case R.id.stream:
			if(isOnline()) {
				try {
					Intent in = new Intent(Intent.ACTION_VIEW);
					in.setDataAndType(Uri.parse(mDownloadUrl), "audio/mpeg");
					startActivity(in);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			} else {
				Toast.makeText(getApplicationContext(), "Geen netwerk connectie", Toast.LENGTH_SHORT).show();
			}

			return true;
		case R.id.download:
			if (isOnline()) {
				downloadFile();
			} else {
				Toast.makeText(getApplicationContext(), "Geen netwerk connectie", Toast.LENGTH_SHORT).show();				
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	private void downloadFile() {
		//TODO check if file exists.
		Log.d(TAG, "In fuction downloadFile()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    	Calendar cal = Calendar.getInstance();
    	String programmaNameUnderscore = mName.replace(" ", "_");
    	programmaNameUnderscore = programmaNameUnderscore.replace(",","");
    	String fileName = programmaNameUnderscore + "_" + dateFormat.format(cal.getTime())+".mp3";
    	
    	
		Request request = new Request(Uri.parse(mDownloadUrl));
		request.setDescription(mName);
    	// in order for this if to run, you must use the android 3.2 to compile your app
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    	    request.allowScanningByMediaScanner();
    	    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    	}
    	request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,  getString(R.string.download_subdir_test) + File.separator + fileName);
	}


	//TODO verder afmaken voor alle textdingen
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pos", mPos);
		outState.putInt("showpos", mShowPos);
		outState.putParcelable(ShowTable.CONTENT_ITEM_TYPE, mUri);
	}
	
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		else return false;
	}
	
}
