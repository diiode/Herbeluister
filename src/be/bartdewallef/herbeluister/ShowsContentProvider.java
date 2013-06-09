package be.bartdewallef.herbeluister;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class ShowsContentProvider extends ContentProvider {
	private static String TAG = "ShowsContentProvider";
	
	private static final UriMatcher sUriMatcher;
	private static HashMap<String, String> showsProjectionMap;

	private static final int STUBRU_SHOWS = 0;
	private static final int STUBRU_ID = 1;
	private static final int MNM_SHOWS = 2;
	private static final int MNM_ID = 3;
	private static final int RADIO1_SHOWS = 4;
	private static final int RADIO1_ID = 5;
	private static final int RADIO2_SHOWS = 6;
	private static final int RADIO2_ID = 7;
	
	private DatabaseHelper mOpenHelper;
	
	private class DatabaseHelper extends SQLiteOpenHelper {
		private String TAG = "DatabaseHelper";
		
		private String table;

		private Context myContext;
		private SQLiteDatabase myDatabase;
		private String DATABASE_PATH = "";
		private static final String DATABASE_NAME = "radioshows.db";
		private static final int DATABASE_VERSION = 1;
		
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			//TODO: test for multiple API versions. Original: DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
			DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
			
			this.myContext = context;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + table);
			onCreate(db);
			*/
			
		}
		
		public void createDatabase() throws IOException{
	        boolean dbExist = checkDatabase();
	        
	        if(dbExist){
	            //do nothing - database already exist
	        }else{
	 
	            //By calling this method and empty database will be created into the default system path
	               //of your application so we are gonna be able to overwrite that database with our database.
	            this.getReadableDatabase();
	            
	 
	            try {
	 
	                copyDatabase();
	 
	            } catch (IOException e) {
	                throw new Error("Error copying database");
	 
	            }
	        }
		}
		
		private boolean checkDatabase() {
			SQLiteDatabase checkDB = null;
			 
			try{
				String myPath = DATABASE_PATH + DATABASE_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			 
			} catch(SQLiteException e){
				//database does't exist yet.
			}
			 
			if(checkDB != null){
				checkDB.close();		 
			}
			
			if(checkDB != null) {
	        	Log.d(TAG, "checkDatabase(): true");
			} else {
				Log.d(TAG, "checkDatabase(): false");
			}
			return checkDB != null ? true : false;
		}
		
		private void copyDatabase() throws IOException {
			 //Open your local db as the input stream
			String[] assetslist = myContext.getAssets().list("");
			Log.d(TAG, "Assets:");
			for (String asset : assetslist) {
				Log.d(TAG, asset);
			}
			InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
			
			// Path to the just created empty db
			String outFileName = DATABASE_PATH + DATABASE_NAME;
			Log.d(TAG, outFileName);
			//Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
			 
			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}
			Log.d(TAG, "Copy finished");
			 
			//Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();		
		}
		
		public void openDatabase() throws SQLException{
			 
			//Open the database
			String myPath = DATABASE_PATH + DATABASE_NAME;
			myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}	
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
        // Implement this to handle requests to delete one or more rows.
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    	int count;
    	switch(sUriMatcher.match(uri)) {
    	case MNM_SHOWS:
    		count = db.delete(ShowTable.MNM.BASE_PATH, where, whereArgs);
    		break;
    	
    	case MNM_ID:
    		String id = uri.getLastPathSegment();
            count = db.delete(ShowTable.MNM.BASE_PATH, BaseColumns._ID + "=" + id
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
    		break;
    	
    	case RADIO1_SHOWS:
    		count = db.delete(ShowTable.Radio1.BASE_PATH, where, whereArgs);
    		break;
    	
    	case RADIO1_ID:
    		String id1 = uri.getLastPathSegment();
            count = db.delete(ShowTable.Radio1.BASE_PATH, BaseColumns._ID + "=" + id1
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
    		break;
    			
    	case RADIO2_SHOWS:
    		count = db.delete(ShowTable.Radio2.BASE_PATH, where, whereArgs);
    		break;
    	
    	case RADIO2_ID:
    		String id2 = uri.getLastPathSegment();
            count = db.delete(ShowTable.Radio2.BASE_PATH, BaseColumns._ID + "=" + id2
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
    		break;
    	
    	case STUBRU_SHOWS:
    		count = db.delete(ShowTable.StuBru.BASE_PATH, where, whereArgs);
    		break;
    	
    	case STUBRU_ID:
    		String id3 = uri.getLastPathSegment();
            count = db.delete(ShowTable.StuBru.BASE_PATH, BaseColumns._ID + "=" + id3
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
    		break;
    			
		default:
	        throw new IllegalArgumentException("Unknown URI " + uri);
	    }
	
	    getContext().getContentResolver().notifyChange(uri, null);
    	return count;
	}

	@Override
	public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
        case MNM_SHOWS:
        	return ShowTable.MNM.CONTENT_TYPE_DIR;
        case MNM_ID:
        	return ShowTable.MNM.CONTENT_ITEM_TYPE;
        case RADIO1_SHOWS:
        	return ShowTable.Radio1.CONTENT_TYPE_DIR;
        case RADIO1_ID:
        	return ShowTable.Radio1.CONTENT_ITEM_TYPE;
        case RADIO2_SHOWS:
        	return ShowTable.Radio2.CONTENT_TYPE_DIR;
        case RADIO2_ID:
        	return ShowTable.Radio2.CONTENT_ITEM_TYPE;
        case STUBRU_SHOWS:
        	return ShowTable.StuBru.CONTENT_TYPE_DIR;
        case STUBRU_ID:
        	return ShowTable.StuBru.CONTENT_ITEM_TYPE;
        default:
        	throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
		 	values = new ContentValues();
		}
		String newUri;
		long id = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
        case MNM_SHOWS:
        	id = db.insert(ShowTable.MNM.BASE_PATH, null, values);
        	newUri = (ShowTable.MNM.BASE_PATH + "/" + id);
        	break;
        case RADIO1_SHOWS:
        	id = db.insert(ShowTable.Radio1.BASE_PATH, null, values);
        	newUri = (ShowTable.Radio1.BASE_PATH + "/" + id);
        	break;
        case RADIO2_SHOWS:
        	id = db.insert(ShowTable.Radio2.BASE_PATH, null, values);
        	newUri = (ShowTable.Radio2.BASE_PATH + "/" + id);
        	break;
        case STUBRU_SHOWS:
        	id = db.insert(ShowTable.StuBru.BASE_PATH, null, values);
        	newUri = (ShowTable.StuBru.BASE_PATH + "/" + id);
        	break;
        default:
        	throw new IllegalArgumentException("Unknown URI " + uri);
        }
		return Uri.parse(newUri);
	}

	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate()");
    	mOpenHelper = new DatabaseHelper(getContext());
    	try {			 
			mOpenHelper.createDatabase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database"); 
		}
			  
		try {
			mOpenHelper.openDatabase();
		}catch(SQLException sqle){
			throw sqle;
		}
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "Cursor query Uri: " + uri);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch(sUriMatcher.match(uri)) {
		case MNM_SHOWS:
        	qb.setTables(ShowTable.MNM.BASE_PATH);
        	break;
        case MNM_ID:
        	qb.setTables(ShowTable.MNM.BASE_PATH);
        	qb.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
        	break;
        case RADIO1_SHOWS:
        	qb.setTables(ShowTable.Radio1.BASE_PATH);
        	break;
        case RADIO1_ID:
        	qb.setTables(ShowTable.Radio1.BASE_PATH);
        	qb.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
        	break;
        case RADIO2_SHOWS:
        	qb.setTables(ShowTable.Radio2.BASE_PATH);
        	break;
        case RADIO2_ID:
        	qb.setTables(ShowTable.Radio2.BASE_PATH);
        	qb.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
        	break;
        case STUBRU_SHOWS:
        	qb.setTables(ShowTable.StuBru.BASE_PATH);
        	break;
        case STUBRU_ID:
        	qb.setTables(ShowTable.StuBru.BASE_PATH);
        	qb.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
        	break;
        default:
        	throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	//TODO implement method
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		return 0;
	}

	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.MNM.BASE_PATH, MNM_SHOWS);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.MNM.BASE_PATH + "/#", MNM_ID);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.Radio1.BASE_PATH, RADIO1_SHOWS);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.Radio1.BASE_PATH + "/#", RADIO1_ID);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.Radio2.BASE_PATH, RADIO2_SHOWS);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.Radio2.BASE_PATH + "/#", RADIO2_ID);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.StuBru.BASE_PATH, STUBRU_SHOWS);
		sUriMatcher.addURI(ShowTable.AUTHORITY, ShowTable.StuBru.BASE_PATH + "/#", STUBRU_ID);
		

		showsProjectionMap = new HashMap<String, String>();
		showsProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
		showsProjectionMap.put(ShowTable.SHOW_NAME, ShowTable.SHOW_NAME);
		showsProjectionMap.put(ShowTable.SHOW_DESCRIPTION, ShowTable.SHOW_DESCRIPTION);
		showsProjectionMap.put(ShowTable.SHOW_DESCRIPTION64, ShowTable.SHOW_DESCRIPTION64);
		showsProjectionMap.put(ShowTable.SHOW_CONTENTS, ShowTable.SHOW_CONTENTS);
		showsProjectionMap.put(ShowTable.SHOW_WEBSITEURL, ShowTable.SHOW_WEBSITEURL);
		showsProjectionMap.put(ShowTable.SHOW_PRESENTER, ShowTable.SHOW_PRESENTER);
		showsProjectionMap.put(ShowTable.SHOW_SCHEDULING, ShowTable.SHOW_SCHEDULING);
		showsProjectionMap.put(ShowTable.SHOW_STREAMURL, ShowTable.SHOW_STREAMURL);
		showsProjectionMap.put(ShowTable.SHOW_EMAIL, ShowTable.SHOW_EMAIL);
	}
}
