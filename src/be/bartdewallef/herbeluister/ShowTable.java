package be.bartdewallef.herbeluister;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShowTable implements BaseColumns {
	public static final String  AUTHORITY = "be.bartdewallef.herbeluister.provider";
	
	public static final String SHOW_NAME = "name";
	public static final String SHOW_DESCRIPTION = "description";
	public static final String SHOW_DESCRIPTION64 = "description64";
	public static final String SHOW_CONTENTS = "contents";
	public static final String SHOW_WEBSITEURL = "websiteUrl";
	public static final String SHOW_PRESENTER = "presenter";
	public static final String SHOW_SCHEDULING = "scheduling";
	public static final String SHOW_STREAMURL = "streamUrl";
	public static final String SHOW_EMAIL = "email";
	
	public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	
	/*
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Shows");
	//TODO: check MIME types
	public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	*/
	public static class MNM {
		public static final String BASE_PATH = "MNM";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
		//TODO: check MIME types
		public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	}
	
	public static class Radio1 {
		public static final String BASE_PATH = "Radio1";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
		//TODO: check MIME types
		public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	}
	
	public static class Radio2 {
		public static final String BASE_PATH = "Radio2";
		  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
		//TODO: check MIME types
		public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	}
	
	public static class StuBru {
		public static final String BASE_PATH = "StuBru";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
		//TODO: check MIME types
		public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.herbeluister.app";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.herbeluister.app";
	}
	
	
}
