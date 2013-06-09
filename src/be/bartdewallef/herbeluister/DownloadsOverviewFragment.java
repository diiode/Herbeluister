package be.bartdewallef.herbeluister;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class DownloadsOverviewFragment extends SherlockListFragment {
	private String TAG = "DownloadsOverviewFragment";
	private String dir;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
        dir = Environment.getExternalStoragePublicDirectory(Environment
    			.DIRECTORY_MUSIC) + File.separator + getString(R.string.download_subdir_test);
        Log.d(TAG, dir);
        File file = new File(dir);
        
        ArrayList <String> fileNames = new ArrayList<String>();
        if (file.exists()) {
        	Log.d(TAG, "file exists");
  	        fileNames = new ArrayList<String>(Arrays.asList(file.list()));
	        if(fileNames.size() < 1) {
	        	Log.d(TAG, "Geen fileNames");
	        	//Show empty Activity
				Toast.makeText(getSherlockActivity().getApplicationContext(), "Geen bestanden", Toast.LENGTH_SHORT)
				.show();
	        } else {
	        	Log.d(TAG, "size: " + fileNames.size());
	        	for(int i = 0; i < fileNames.size(); i++){
	        		Log.d(TAG, "Filename(" + i + ")" + fileNames.get(i));
	        	}
	        	Log.d(TAG, "test");
	        	/*
		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
		        setListAdapter(adapter);
	        */
	        }
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, fileNames);
	        setListAdapter(adapter);
        } else {
        	Log.d(TAG, "file doesn't exists.");
        }   
	}
	
	@Override
	//TODO use a service
	public void onListItemClick(ListView lv, View v, int position, long id) {
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		Toast.makeText(getSherlockActivity(), "Selected item = " + tv.getText(), Toast.LENGTH_SHORT).show();
		
		String item = (String) getListAdapter().getItem(position);
		File musicFile = new File(dir + File.separator + item);
		Uri musicUri = Uri.fromFile(musicFile);
		Log.d(TAG, "URI: " + musicUri);
		
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(musicUri, "audio/*");
		try{
			startActivity(Intent.createChooser(intent, "Open with"));
		} catch (ActivityNotFoundException e) {
			 Log.e(TAG,"Activity not found: " + musicUri, e);
		}

	}	
}
