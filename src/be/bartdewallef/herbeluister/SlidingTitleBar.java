package be.bartdewallef.herbeluister;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SlidingTitleBar extends BaseActivity {

	public SlidingTitleBar() {
		super(R.string.title_bar_slide);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new ShowListFragment(0))
		.commit();
		
		setSlidingActionBarEnabled(true);
	}
	
}