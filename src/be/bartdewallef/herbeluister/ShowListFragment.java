package be.bartdewallef.herbeluister;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ShowListFragment extends SherlockListFragment implements LoaderCallbacks<Cursor>  {
	private final static String TAG = "ShowListFragment";
	
	private int stationId = -1;
	private String tableName;
	private String station;
	private String streamUrl;
	private Uri contentUri;
	private static final int LIST_LOADER = 0x01;
	private SimpleCursorAdapter adapter;
	private RadioStations radiostations;
	private Radio radio;
	
	//TODO verplaatsen naar newinstance
//	public ShowListFragment(int pos) {
//		stationId = pos;
//		switch(pos){
//		case 0:
//			station = "MNM";
//			streamUrl = "http://mp3.streampower.be/mnm-high.mp3";
//			tableName = ShowTable.MNM.BASE_PATH;
//			contentUri = ShowTable.MNM.CONTENT_URI;
//			break;
//		case 1:
//			station = "Radio 1";
//			streamUrl = "http://mp3.streampower.be/radio1-high.mp3";
//			tableName = ShowTable.Radio1.BASE_PATH;
//			contentUri = ShowTable.Radio1.CONTENT_URI;
//			break;
//		case 2:
//			station = "Radio 2";
//			streamUrl = "http://mp3.streampower.be/ra2lim-high.mp3";
//			tableName = ShowTable.Radio2.BASE_PATH;
//			contentUri = ShowTable.Radio2.CONTENT_URI;
//			break;
//		case 3:
//			station  = "Studio Brussel";
//			streamUrl = "http://mp3.streampower.be/stubru-high.mp3";
//			tableName = ShowTable.StuBru.BASE_PATH;
//			contentUri = ShowTable.StuBru.CONTENT_URI;
//			break;
//		default:
//			break; 
//		}
//	}

	
	public static ShowListFragment newInstance(RadioStations radiostations, int stationId){
		//ShowListFragment fragment = new ShowListFragment(stationId);
		ShowListFragment fragment = new ShowListFragment();
		fragment.setStationId(stationId);
		Bundle bundle = new Bundle();
		bundle.putSerializable("radiostations", radiostations);
		fragment.setArguments(bundle);
		return fragment;
	}

	
    private void setTitle() {
		((ShowOverviewActivity) getActivity()).setTitle(station);
		
	}


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		radiostations = (RadioStations) getArguments().getSerializable("radiostations");
				
		if (stationId == -1 && savedInstanceState != null) {
			stationId = savedInstanceState.getInt("stationId");
			tableName = savedInstanceState.getString("tableName");
			contentUri = savedInstanceState.getParcelable(ShowTable.CONTENT_ITEM_TYPE);
			station = savedInstanceState.getString("station");
			streamUrl = savedInstanceState.getString("streamUrl");
			radiostations = (RadioStations) savedInstanceState.getSerializable("radiostations");
			//radio = radiostations.radios.get(stationId);
			
		}
		setTitle();
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] projection = {BaseColumns._ID, ShowTable.SHOW_NAME};
		String[] uiBindFrom = {ShowTable.SHOW_NAME};
		int[] uiBindTo = {android.R.id.text1};
		
		getLoaderManager().initLoader(LIST_LOADER, null, this);
		
		 adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
				uiBindFrom, uiBindTo);
		setListAdapter(adapter);
		/*
		String[] stations = getResources().getStringArray(R.array.stations);
		int arrayId = getResources().getIdentifier(stations[stationId], "array", getSherlockActivity().getApplicationContext().getPackageName());
		String[] shows = getResources().getStringArray(arrayId);
		
		ArrayAdapter<String> showAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, shows);
		setListAdapter(showAdapter);
		*/
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("stationId", stationId);
		outState.putString("tableName", tableName);
		outState.putParcelable(ShowTable.CONTENT_ITEM_TYPE, contentUri);
		outState.putString("station", station);
		outState.putString("streamUrl", streamUrl);
		outState.putSerializable("radiostations", radiostations);
	}
	
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		Toast.makeText(getSherlockActivity(), "Selected item = " + tv.getText(), Toast.LENGTH_SHORT).show();
		
		Uri detailsUri = Uri.parse(contentUri + "/" + id);
		ShowOverviewActivity soa = (ShowOverviewActivity) getActivity();
		soa.onShowPressed(stationId, position, detailsUri);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		String[] projection = {BaseColumns._ID, ShowTable.SHOW_NAME};
		String[] uiBindFrom = {ShowTable.SHOW_NAME};
		int[] uiBindTo = {android.R.id.text1};
		
		CursorLoader cursorLoader = new CursorLoader(getActivity(),contentUri, projection, null, null, null);
		return cursorLoader;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
		
		
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
		
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_show_list, menu);
	}
	
	//TODO choose region with radio2
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.radio:
			if(isOnline()) {
				try {
					Intent in = new Intent(Intent.ACTION_VIEW);
					in.setDataAndType(Uri.parse(streamUrl), "audio/mpeg");
					startActivity(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity().getApplicationContext(), "Geen netwerk connectie", Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSherlockActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		else return false;
	}
	
	public void setStationId(int pos){
		stationId = pos;
		switch(pos){
		case 0:
			station = "MNM";
			streamUrl = "http://mp3.streampower.be/mnm-high.mp3";
			tableName = ShowTable.MNM.BASE_PATH;
			contentUri = ShowTable.MNM.CONTENT_URI;
			break;
		case 1:
			station = "Radio 1";
			streamUrl = "http://mp3.streampower.be/radio1-high.mp3";
			tableName = ShowTable.Radio1.BASE_PATH;
			contentUri = ShowTable.Radio1.CONTENT_URI;
			break;
		case 2:
			station = "Radio 2";
			streamUrl = "http://mp3.streampower.be/ra2lim-high.mp3";
			tableName = ShowTable.Radio2.BASE_PATH;
			contentUri = ShowTable.Radio2.CONTENT_URI;
			break;
		case 3:
			station  = "Studio Brussel";
			streamUrl = "http://mp3.streampower.be/stubru-high.mp3";
			tableName = ShowTable.StuBru.BASE_PATH;
			contentUri = ShowTable.StuBru.CONTENT_URI;
			break;
		default:
			break; 
		}
	}
}
