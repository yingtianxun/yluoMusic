package com.yluo.yluomusic.presenter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;

public abstract class BasePlayMusicPresent extends BasePresenter {

	private static final String TAG = "BasePlayMusicPresent";
	private Fragment fragment;

	protected SongManager songManager;

	// 监听进度条
	protected IntentFilter progressFilter = new IntentFilter(
			"com.yluo.yluomusic.songprogress");

	// 监听歌词
	protected IntentFilter songFilter = new IntentFilter(
			"com.yluo.yluomusic.songlrc");

	protected IntentFilter songDurationFilter = new IntentFilter(
			"com.yluo.yluomusic.songduration");
	
	protected IntentFilter songStopFilter = new IntentFilter(
			"com.yluo.yluomusic.songstop");
	
	protected IntentFilter songPauseFilter = new IntentFilter(
			"com.yluo.yluomusic.songpause");
	
	protected IntentFilter songCompleteFilter = new IntentFilter(
			"com.yluo.yluomusic.complete");
	
	protected IntentFilter songRestartFilter = new IntentFilter(
			"com.yluo.yluomusic.restart");
	
	private SongProgressReceiver progressReceiver;
	private SongLrcReceiver lrcReceiver;
	private SongDurationReceiver durationReceiver;
	private SongStopReceiver songStopReceiver;
	public BasePlayMusicPresent(Fragment fragment) {

		this.fragment = fragment;
	}

	@Override
	public void onAttach() {
		Intent intent = new Intent(fragment.getActivity(),
				PlayMusicService.class);

		fragment.getActivity().bindService(intent, mPlayMusicConnection,
				Service.BIND_AUTO_CREATE);

		registerMusicReceiver(); // 注册服务

		// 判断是不是已经播放了,播放了就拿信息出来
	}

	@Override
	public void onDetach() {

		unRegisterMusicReceiver();

		fragment.getActivity().unbindService(mPlayMusicConnection);
	}
	
	
	
	protected ServiceConnection mPlayMusicConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			songManager = (SongManager) service;
			
			try {
				if (songManager.isPlayingMusic()) {
					updateSongDuration(songManager.getSongDuration("无字碑"));

					updateSongLrc(songManager.getSongWordLine("无字碑"));
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}
	};
	private SongPauseReceiver pauseReceiver;
	private SongCompleteReceiver completeReceiver;
	private SongRestartReceiver restartReceiver;


	protected void registerMusicReceiver() {
		// 监听播放进度

		progressReceiver = new SongProgressReceiver();
		fragment.getActivity().registerReceiver(progressReceiver,
				progressFilter);
		// 监听歌词
		lrcReceiver = new SongLrcReceiver();

		fragment.getActivity().registerReceiver(lrcReceiver,
				songFilter);
		// 监听总长度
		durationReceiver = new SongDurationReceiver();
		fragment.getActivity().registerReceiver(durationReceiver,
				songDurationFilter);
		// 监听停止
		songStopReceiver = new SongStopReceiver();
		fragment.getActivity().registerReceiver(songStopReceiver,
				songStopFilter);
		// 监听暂停的
		pauseReceiver = new SongPauseReceiver();
		fragment.getActivity().registerReceiver(pauseReceiver,
				songPauseFilter);
		// 播放完成
		
		completeReceiver = new SongCompleteReceiver();
		
		fragment.getActivity().registerReceiver(completeReceiver,
				songCompleteFilter);
		
		
		restartReceiver = new SongRestartReceiver();
		
		fragment.getActivity().registerReceiver(restartReceiver,
				songRestartFilter);
		
		
	}

	protected void unRegisterMusicReceiver() {
		fragment.getActivity().unregisterReceiver(progressReceiver);
		fragment.getActivity().unregisterReceiver(lrcReceiver);
		fragment.getActivity().unregisterReceiver(durationReceiver);
		
		fragment.getActivity().unregisterReceiver(songStopReceiver);
		
		fragment.getActivity().unregisterReceiver(pauseReceiver);
		
		fragment.getActivity().unregisterReceiver(completeReceiver);
		
		fragment.getActivity().unregisterReceiver(restartReceiver);
	}

	public void playMusic(String songName) {
		try {
			songManager.startPlaySong(songName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public abstract void updateSongLrc(List<WordLine> wordLines);

	class SongLrcReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			ArrayList<WordLine> wordLines = intent
					.getParcelableArrayListExtra("wordLines");
			updateSongLrc(wordLines);
		}
	}

	public abstract void updateStopSong();

	class SongStopReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			updateStopSong();
		}
	}

	public abstract void updateSongDuration(int songDuration);

	class SongDurationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			updateSongDuration(intent.getIntExtra("songduration", -1));
		}
	}

	public abstract void updateSongProgress(int progress);

	class SongProgressReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateSongProgress(intent.getIntExtra("curProgress", -1));
		}
	}
	public abstract void updateSongPause();

	class SongPauseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateSongPause();
		}
	}
	
	public abstract void updateSongComplete();

	class SongCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateSongComplete();
		}
	}
	
	public abstract void updateSongRestart();

	class SongRestartReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateSongRestart();
		}
	}
	
	
	
}
