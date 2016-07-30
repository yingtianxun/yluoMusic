package com.yluo.yluomusic.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.utils.ParseSongLrc;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayMusicService extends Service {

	private Handler handler = new Handler();

	class SongInfo {
		List<WordLine> wordLines;
		float songDuration;
		String singer;
		String songName;
		String path;
		int resourceId;
	}

	private MediaPlayer mediaPlayer;

	private HashMap<String, SongInfo> songWordLines = new HashMap<String, SongInfo>();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 初始化数据的
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

	public int startPlaySong(String songName) {
		SongInfo songInfo = songWordLines.get(songName);

		if (songInfo == null) {
			// 找不到任何歌曲信息
			return 0;
		}
		if (mediaPlayer != null) {

			mediaPlayer.stop();

			mediaPlayer = MediaPlayer.create(this, R.raw.song);
		}
		try {
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		broadcastSongLrc(songInfo);
		
		return mediaPlayer.getDuration();
		// 解析歌词
	}

	private void broadcastSongLrc(SongInfo songInfo) {
		if(songInfo.wordLines == null) {
			handler.post(new ParseSongLrcRunnable(
					songInfo.songName,getResources(), 
					R.raw.wuzibei,
					mediaPlayer.getDuration()));
		} else {
			sendSongIntent(songInfo.wordLines);
		}
	}
	
	private void sendSongIntent(List<WordLine> wordLines) {
		Intent intent = new Intent();
		intent.setAction("com.yluo.yluomusic.songlrc");
		intent.putExtra("wordLines", wordLines.toArray());
		sendBroadcast(intent);
	}

	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	class ParseSongLrcRunnable implements Runnable {
		
		private String songName;
		
		private Resources resources;

		private int resourceId;

		private int songDuration;

		public ParseSongLrcRunnable(String songName ,Resources resources, int resourceId,
				int songDuration) {
			this.songName = songName;
			this.resources = resources;
			this.resourceId = resourceId;
			this.songDuration = songDuration;
		}

		@Override
		public void run() {
			List<WordLine> wordLines = ParseSongLrc.parseSongLrc(resources,
					resourceId, songDuration);
			// 这里没有解决安全问题
			songWordLines.get(songName).wordLines = wordLines;
			sendSongIntent(wordLines);
		}
	}

	  class MusicBinder extends Binder implements SongManager {

		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public int startPlaySong(String songName) throws RemoteException {
			return PlayMusicService.this.startPlaySong(songName);
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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<WordLine> getSongWordLine(String songName)
				throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
