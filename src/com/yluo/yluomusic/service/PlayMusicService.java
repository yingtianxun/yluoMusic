package com.yluo.yluomusic.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayMusicService extends Service{
	
	class SongInfo {
		 List<WordLine> wordLines;
		 float songDuration;
		 String singer;
		 String songName;
		 String path;
		 int resourceId;
	}
	
	private MediaPlayer mediaPlayer;
	
	private HashMap<String, SongInfo> songWordLines 
		= new HashMap<String,SongInfo>();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		SongInfo songInfo = new SongInfo();
		songInfo.resourceId = R.raw.song;
		songInfo.singer = "张靓颖";
		songInfo.songName = "无字碑";
		
		songWordLines.put(songInfo.songName, songInfo);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}
	public void startPlaySong(String songName) {
		SongInfo songInfo = songWordLines.get(songName);

		if(songInfo == null) {
			// 找不到任何歌曲信息
			return;
		}
		if(mediaPlayer != null) {
			
			mediaPlayer.stop();
			
			 mediaPlayer = MediaPlayer.create(this, R.raw.song);
		} 
		try {
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void stopMusic() {
		if(mediaPlayer != null) { 
			mediaPlayer.stop();
		}
	}
	
	public static class MusicBinder extends Binder implements SongManager{

		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public void startPlaySong(String songName) throws RemoteException {
//			PlayMusicService.this.startPlaySong(songName);
		}

		@Override
		public void stopPlaySong() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changePlaySongProgress(float progress)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public float getSongDuration(String songName) throws RemoteException {
			return 0;
		}

		@Override
		public List<WordLine> getSongWordLine(String songName)
				throws RemoteException {
			return null;
		}



	
		
	}
	

}
