package com.yluo.yluomusic.provider;

import java.security.Provider;


import com.yluo.yluomusic.db.helper.SongInfoProviderSQLiteOpenHelper;
import com.yluo.yluomusic.db.helper.SongListSQLiteOpenHelper;

import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SongInfoProvider extends ContentProvider{

	private static final String TAG = "SongInfoProvider";
	private static final int DB_DIR = 1;
	private static final int DB_ITEM = 2;
	private static final String DBNAME = "SongListDb";
	public static final String TABLENAME= SongInfoProviderSQLiteOpenHelper.TABLENAME;
	public static final String AUTHORITY="com.yluo.yluomusic.provider.songinfoprovider";
	private SongInfoProviderSQLiteOpenHelper helper;
	private SQLiteDatabase db;
	private static UriMatcher uriMatcher;
	static
	{
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, TABLENAME, DB_DIR);
		uriMatcher.addURI(AUTHORITY, TABLENAME + "/#", DB_ITEM);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		helper = new SongInfoProviderSQLiteOpenHelper(getContext(),DBNAME, null, 3);
		db = helper.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, ""+ uri);
		Cursor cursor=null;
		switch (uriMatcher.match(uri)) {
		case DB_DIR:
			cursor=db.query(TABLENAME, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case DB_ITEM:
			//从uri中截取出当前需要查找表的id的行数据
			int _id=Integer.parseInt(uri.getPathSegments().get(1)) ;
			cursor=db.query(TABLENAME, projection, "_id = ?", new String[]{_id + ""}, null, null, sortOrder);
		default:
			break;
		}
		
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Cursor cursor=null;
		Uri uriReturn=null;
		switch (uriMatcher.match(uri)) {
		//insert数据的时候不管是传入表还是insert哪条item都是一样的添加
		case DB_DIR:
		case DB_ITEM:
			long newId = db.insert(TABLENAME, null, values);
			uriReturn = Uri.parse("content://"+AUTHORITY+"/"+TABLENAME+"/"+newId);
			break;
		default:
			break;
		}
		return uriReturn;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
