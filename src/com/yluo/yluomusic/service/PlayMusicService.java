package com.yluo.yluomusic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.utils.ParseSongLrc;
import com.yluo.yluomusic.utils.ToastUtil;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;

public class PlayMusicService extends Service {
	private static final String TAG = "PlayMusicService";
	private Handler handler = new Handler();

	class SongInfo {
		List<WordLine> wordLines;
		int songDuration;
		String singer;
		String songName;
		String path;
		int resourceId;
	}

	private MediaPlayer mediaPlayer;

	private HashMap<String, SongInfo> songWordLines = new HashMap<String, SongInfo>();
	
	
	private ListenSongPlayProgressRunable listenRunable 
		=  new ListenSongPlayProgressRunable();
	
	private boolean isPlaying = false;
	
	private Intent songPlayProgressIntent = new Intent();
	
	private boolean mIsPause = false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 初始化数据的
		SongInfo songInfo = new SongInfo();
		songInfo.resourceId = R.raw.song;
		songInfo.singer = "张靓颖";
		songInfo.songName = "无字碑";

		songWordLines.put(songInfo.songName, songInfo);
		
		songPlayProgressIntent.setAction("com.yluo.yluomusic.songprogress");
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}

	public void startPlaySong(String songName) {
		SongInfo songInfo = songWordLines.get(songName);
		if (songInfo == null) {
			// 找不到任何歌曲信息
			return ;
		}
		
		if(mIsPause) {
			mediaPlayer.start();
			handler.post(listenRunable);
			return;
		}
		if(isPlaying) {
			
			isPlaying = false;
			pauseMusic();
		}
		
		mediaPlayer = MediaPlayer.create(this, R.raw.song);
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	
		try {
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					
					Intent intent = new Intent();
					intent.setAction("com.yluo.yluomusic.complete");
					
					sendBroadcast(intent); // 发送结束的广播
					
					isPlaying = false; // 判断是否在播放音乐
				}
			});
			songInfo.songDuration = mediaPlayer.getDuration();
			mediaPlayer.start();
			isPlaying = true; // 判断是否在播放音乐
			
			handler.post(listenRunable); //监听进度的
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		broadcastSongLrc(songInfo); // 发送歌词
		
		broadcastSongDuration(songInfo); // 发送歌的长度
		
		// 解析歌词
	}
	
	private void broadcastSongDuration(SongInfo songInfo) {
		
		Intent intent = new Intent();
		intent.setAction("com.yluo.yluomusic.songduration");
		
		intent.putExtra("songduration", songInfo.songDuration);
		
		sendBroadcast(intent);
	}
	
	// 发送暂停的广播
	private void pauseMusic() {
		mIsPause = true;
		mediaPlayer.pause();
		Intent intent = new Intent();
		intent.setAction("com.yluo.yluomusic.songpause");
		sendBroadcast(intent);
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
	public void changePlaySongProgress(int progress) {
		if(isPlaying) {
			mediaPlayer.seekTo(progress);
		}
		
	}
	private void sendSongIntent(List<WordLine> wordLines) {
		Intent songLrcIntent = new Intent();
		songLrcIntent.setAction("com.yluo.yluomusic.songlrc");
		
		songLrcIntent.putParcelableArrayListExtra("wordLines", (ArrayList<? extends Parcelable>) wordLines);
		
		
		sendBroadcast(songLrcIntent);
	}

	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			
			Intent intent = new Intent();
			intent.setAction("com.yluo.yluomusic.songstop");
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
	
	class ListenSongPlayProgressRunable implements Runnable {

		@Override
		public void run() {
			
			songPlayProgressIntent.putExtra("curProgress", mediaPlayer.getCurrentPosition());
			
			sendBroadcast(songPlayProgressIntent);
			
			handler.postDelayed(this, 20);
		}
		
	}

	  class MusicBinder extends Binder implements SongManager {

		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public void startPlaySong(String songName) throws RemoteException {
			 PlayMusicService.this.startPlaySong(songName);
		}

		@Override
		public void stopPlaySong() throws RemoteException {
			PlayMusicService.this.stopMusic();
		}

		@Override
		public void changePlaySongProgress(int progress)
				throws RemoteException {
			
			PlayMusicService.this.changePlaySongProgress(progress);
		}

		@Override
		public int getSongDuration(String songName) throws RemoteException {
			
			return songWordLines.get(songName).songDuration;
		}

		@Override
		public List<WordLine> getSongWordLine(String songName)
				throws RemoteException {
			
			return songWordLines.get(songName).wordLines;
		}

		@Override
		public boolean isPlayingMusic() throws RemoteException {
			
			return PlayMusicService.this.isPlaying;
		}

		@Override
		public void pauseMusic() throws RemoteException {
			PlayMusicService.this.pauseMusic();
			
		}

	}

	

}
