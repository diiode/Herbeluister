package be.bartdewallef.herbeluister;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Util {

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("https://github.com/diiode/Herbeluister");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		context.startActivity(launchBrowser);
	}
	
}
