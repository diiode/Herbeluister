package be.bartdewallef.herbeluister;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class MainMenuFragment extends SherlockListFragment{
	
	private CustomListAdapter mAdapter;
	
	private ArrayList<String> stations;
	private ArrayList<String> otherOptions;
	
	private RadioStations radiostations;
	
	public static MainMenuFragment newInstance(RadioStations radiostations){
		MainMenuFragment fragment = new MainMenuFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("radiostations", radiostations);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		radiostations = (RadioStations) getArguments().getSerializable("radiostations");
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//TODO setonclicklistener
		super.onActivityCreated(savedInstanceState);
		
		stations = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.station_names)));
		otherOptions = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.main_menu_other)));
		
		mAdapter = new CustomListAdapter();
		mAdapter.addSeparatorItem(getString(R.string.main_menu_shows_title));
		for(String item : stations) {
			mAdapter.addItem(item);
		}
		mAdapter.addSeparatorItem(getString(R.string.main_menu_other_title));
		for(String item : otherOptions) {
			mAdapter.addItem(item);
		}
		setListAdapter(mAdapter);
		
	}
	
	// TODO
	// Used for switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ShowOverviewActivity) {
			ShowOverviewActivity soa = (ShowOverviewActivity) getActivity();
			soa.switchContent(fragment);
		}
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		
		//check if item is item
		if(mAdapter.getItemViewType(position) == CustomListAdapter.TYPE_ITEM) {
			//TODO delete (debug)
			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			Toast.makeText(getSherlockActivity(), "Selected item = " + tv.getText(), Toast.LENGTH_SHORT).show();
			
			
			String item = (String) tv.getText();
			//check if item is a station
			Fragment newContent = null;
			if(stations.contains(item)) {
				newContent = ShowListFragment.newInstance(radiostations, stations.indexOf(item));
			} else {
				if(otherOptions.get(0) == item) {
					newContent = new DownloadsOverviewFragment();
				}
				else if(otherOptions.get(1) == item) {
					newContent = RadioFragment.newInstance(radiostations);
				}
			}
			if (newContent != null) {
				switchFragment(newContent);
			}
		}
	}
	
	private class CustomListAdapter extends BaseAdapter{
        public static final int TYPE_ITEM = 0;
        public static final int TYPE_HEADER = 1;
        private static final int TYPE_MAX_COUNT = TYPE_HEADER + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public CustomListAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            // save separator position
            mSeparatorsSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_HEADER : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        public int getCount() {
            return mData.size();
        }

        public String getItem(int position) {
            return mData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
	                case TYPE_ITEM:
	                    convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
	                    holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
	                    break;
	                case TYPE_HEADER:
	                    convertView = mInflater.inflate(R.layout.menu_row_header, null);
	                    holder.textView = (TextView)convertView.findViewById(R.id.text_menu_header);
	                    break;
	                /*
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.menu_row_item, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text_menu_item);
                        break;
                    case TYPE_HEADER:
                        convertView = mInflater.inflate(R.layout.menu_row_header, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text_menu_header);
                        break;
                	*/
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
    }
	
}