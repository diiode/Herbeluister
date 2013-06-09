package be.bartdewallef.herbeluister;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ShowOverviewActivity extends SlidingFragmentActivity{
	
	private static final String TAG = "ShowOverViewActivity";
	protected SherlockListFragment mContent;
	protected SherlockListFragment mFrag;
	private RadioStations radiostations;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		InputStream xmlradiolinks = null;
		try {
			Serializer serializer = new Persister();
			xmlradiolinks = getAssets().open("radiolinks.xml");
			
			radiostations = serializer.read(RadioStations.class, xmlradiolinks);
			/*
			Log.d(TAG, radiostations.radios.get(0).getStationdetail());
			Log.d(TAG, radiostations.radios.get(1).getStationdetail());
			*/
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(xmlradiolinks != null)
				try {
					xmlradiolinks.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		setTitle(R.string.title_show_overview);
		
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		// set contentframe
		setContentView(R.layout.content_frame);
		if (savedInstanceState != null)
			mContent = (SherlockListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = ShowListFragment.newInstance(radiostations, 0);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, mContent)
			.commit();
		
		// set the Behind View Fragment
		setBehindContentView(R.layout.menu_frame);
		mFrag = MainMenuFragment.newInstance(radiostations);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.menu_frame, mFrag)
			.commit();
		
		// customizing slidingmenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.d("ShowOverviewActivity", "onCreate()");
		


		//TODO check with ResponsiveUIActivity for other methods
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.github:
			Util.goToGitHub(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(final Fragment fragment) {
		mContent = (SherlockListFragment) fragment;
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, mContent)
			.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
	
	
	//TODO ?
	public class BasePagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> mFragments = new ArrayList<Fragment>();
		private ViewPager mPager;

		public BasePagerAdapter(FragmentManager fm, ViewPager vp) {
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
			for (int i = 0; i < 3; i++) {
				addTab(new SampleListFragment());
			}
		}

		public void addTab(Fragment frag) {
			mFragments.add(frag);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
	}


	public void onShowPressed(int position, int showposition, Uri uri) {
		Intent intent = ShowDetailsActivity.newInstance(this, position, showposition, uri);
		startActivity(intent);
		
	}
	
	
}
