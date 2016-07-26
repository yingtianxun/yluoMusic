package com.yluo.yluomusic.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;
import android.util.Log;

import com.yluo.yluomusic.bean.LoveSongBean;
import com.yluo.yluomusic.db.helper.SongListSQLiteOpenHelper;

public class SongListDao {

	private SongListSQLiteOpenHelper songsHelper;
	private String songListDb;
	private SQLiteDatabase db;
	public SongListDao(Context context) {
		songsHelper = new SongListSQLiteOpenHelper(context, songListDb, null, 3);
		db = songsHelper.getWritableDatabase();
		Log.d("create database", "database success");
	}
	public void addLoveSong(LoveSongBean loveSongBean) {
		ContentValues values = new ContentValues();
		values.put(SongListSQLiteOpenHelper.SONGNAME, loveSongBean.getSongName());
		values.put(SongListSQLiteOpenHelper.SINGER, loveSongBean.getSingerName());
		values.put(SongListSQLiteOpenHelper.hasMv, loveSongBean.hasMv());
		values.put(SongListSQLiteOpenHelper.isLove, loveSongBean.isLove());
		db.insert(SongListSQLiteOpenHelper.TABLENAME, null, values);
	}
	public void cancelLoveSong(LoveSongBean loveSongBean) {
		db.delete(SongListSQLiteOpenHelper.TABLENAME, " _id=? ", new String[]{loveSongBean.getId()+""});
	}
	
	public List<LoveSongBean> getAllLoveSongName() {
		
		List<LoveSongBean> loveSongBeans = new ArrayList<LoveSongBean>();
		
		Cursor cursor =  db.query(SongListSQLiteOpenHelper.TABLENAME, new String[]{SongListSQLiteOpenHelper.ID,SongListSQLiteOpenHelper.SONGNAME}, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			LoveSongBean loveSongBean = new LoveSongBean();
			int id = cursor.getInt(cursor.getColumnIndex(SongListSQLiteOpenHelper.ID));
			loveSongBean.setId(id);
			String songName=cursor.getString(cursor.getColumnIndex(SongListSQLiteOpenHelper.SONGNAME));
			loveSongBean.setSongName(songName);
			loveSongBeans.add(loveSongBean);
		}
		
		
		cursor.close();
		
		return loveSongBeans;
	}
	
	public LoveSongBean queryLoveSongBySongName(){
		LoveSongBean loveSongBean = new LoveSongBean();
		
		Cursor cursor =  db.query(SongListSQLiteOpenHelper.TABLENAME, new String[]{SongListSQLiteOpenHelper.SONGNAME}, null, null, null, null, null);
		if(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex(SongListSQLiteOpenHelper.ID));
			String songName=cursor.getString(cursor.getColumnIndex(SongListSQLiteOpenHelper.SONGNAME));
			loveSongBean.setId(id);
			loveSongBean.setSongName(songName);
		}

		cursor.close();
		return loveSongBean;
	}
	
	public List<LoveSongBean> getAllLoveSong() {
		
		List<LoveSongBean> loveSongBeans = new ArrayList<LoveSongBean>();
		
		Cursor cursor =  db.query(SongListSQLiteOpenHelper.TABLENAME, new String[]{SongListSQLiteOpenHelper.ID,SongListSQLiteOpenHelper.SONGNAME}, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			LoveSongBean loveSongBean = new LoveSongBean();
			int id = cursor.getInt(cursor.getColumnIndex(SongListSQLiteOpenHelper.ID));
			loveSongBean.setId(id);
			String songName=cursor.getString(cursor.getColumnIndex(SongListSQLiteOpenHelper.SONGNAME));
			loveSongBean.setSongName(songName);
			loveSongBeans.add(loveSongBean);
		}
		
		
		cursor.close();
		
		return loveSongBeans;
	}
}
