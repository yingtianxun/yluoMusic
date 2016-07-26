package com.yluo.yluomusic.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongListSQLiteOpenHelper extends SQLiteOpenHelper{
	
//	private  String tableName = "songList";
	
	public static final String TABLENAME = "songList";
	public static final String ID = "_id";
	public static final String SONGNAME = "songName";
	public static final String SINGER = "singer";
	public static final String hasMv = "hasMv";
	public static final String isLove = "isLove";
	private static final String CREATE_TABLE = "create table " + TABLENAME + "(" + ID + " Integer primary key autoincrement," +
			SONGNAME + " text," + SINGER + " text," + hasMv +" Integer,"+isLove + " Integer)";
	public SongListSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);	
		Log.d("create table", CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

//	public String getTableName() {
//		return tableName;
//	}
	
}
