package be.bartdewallef.herbeluister;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

public class RadioFragment extends SherlockListFragment {
	private static String TAG = "RadioFragment";
	
	DoubleListAdpater mAdapter;
	
	private RadioStations radiostations;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		radiostations = (RadioStations) getArguments().getSerializable("radiostations");
		Log.d(TAG, radiostations.radios.get(0).getStationdetail());	

		mAdapter = new DoubleListAdpater(getActivity(), radiostations.radios);
		setListAdapter(mAdapter);
		return inflater.inflate(R.layout.list, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	
	public static RadioFragment newInstance(RadioStations radiostations){
		RadioFragment fragment = new RadioFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("radiostations", radiostations);
		fragment.setArguments(bundle);
		
		return fragment;
	}

	public void setRadioStations(RadioStations radiostations) {
		this.radiostations = radiostations;
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id){
		//TODO delete (debug)
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		Toast.makeText(getSherlockActivity(), "Selected item = " + tv.getText(), Toast.LENGTH_SHORT).show();
		
		Radio radio = (Radio) mAdapter.getItem(position);
		if(isOnline()) {
			try {
				Intent in = new Intent(Intent.ACTION_VIEW);
				in.setDataAndType(Uri.parse(radio.getUrl()), "audio/mpeg");
				startActivity(in);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		} else {
			Toast.makeText(getSherlockActivity(), "Geen netwerk connectie", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		else return false;
	}
	
	private class DoubleListAdpater extends BaseAdapter {
		private Context context;
		private List<Radio> radios;
		
		public DoubleListAdpater(Context context, List<Radio> radios) {
			this.context = context;
			this.radios = radios;
		}
		
		@Override
		public int getCount() {
			return radios.size();
		}

		@Override
		public Object getItem(int position) {
			return radios.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inflater.inflate(
	                    android.R.layout.simple_list_item_2, null);
	        }
	        
	        Radio radio = radios.get(position);
	        
	        if (radio != null) {
	        	TextView title = (TextView) v.findViewById(android.R.id.text2);
	        	title.setText(radio.getStation());
	        	TextView details = (TextView) v.findViewById(android.R.id.text1);
	        	details.setText(radio.getStationdetail());
	        }
	        return v;
		}
	}
}
