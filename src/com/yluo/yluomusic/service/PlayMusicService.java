package com.yluo.yluomusic.service;

import java.util.List;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayMusicService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}
	
	public 
	
	
	public static class MusicBinder extends Binder implements SongManager{

		@Override
		public IBinder asBinder() {
			return this;
		}
		
		@Override
		public void startPlaySong(String songName) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void stopPlaySong() throws RemoteException {
			
		}

		@Override
		public void changePlaySongProgress(float progress)
				throws RemoteException {
			
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
