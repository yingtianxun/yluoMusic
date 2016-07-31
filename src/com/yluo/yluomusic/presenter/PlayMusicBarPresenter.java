package com.yluo.yluomusic.presenter;

import java.util.ArrayList;
import java.util.List;

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
import com.yluo.yluomusic.presenter.BasePlayMusicPresent.SongLrcReceiver;
import com.yluo.yluomusic.presenter.BasePlayMusicPresent.SongProgressReceiver;
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;
import com.yluo.yluomusic.ui.fragment.PlaySongBarFragment;

public class PlayMusicBarPresenter extends BasePlayMusicPresent {

	private static final String TAG = "PlayMusicBarPresenter";

	private PlaySongBarFragment barFragment;

	public PlayMusicBarPresenter(PlaySongBarFragment barFragment) {
		super(barFragment);
		this.barFragment = barFragment;
	}
	
	// 获取歌曲的长度,并且在这里监听进度和歌词
	
	public void nextSong() {
	
	}

	@Override
	public void updateSongLrc(List<WordLine> wordLines) {
		barFragment.onUpdateSongLrc(wordLines);
	}

	@Override
	public void updateSongProgress(int progress) {
		barFragment.onUpdateSongProgress(progress);
	}

	@Override
	public void updateSongDuration(int songDuration) {
		Log.d(TAG, "--------songDuration:" + songDuration);
		barFragment.onUpdateSongDuationTime(songDuration);
		
		
	}

	@Override
	public void updateStopSong() {
		barFragment.onUpdateStopSong();
		
	}

	@Override
	public void updateSongPause() {
		barFragment.onUpdatePauseSong();
		
	}

	@Override
	public void updateSongComplete() {
		barFragment.onUpdateStopSong();
		
	}

	public void changeSongProgress(int progress) {
		try {
			songManager.changePlaySongProgress(progress);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateSongRestart() {
		barFragment.onUpdateSongRestart();
		
	}

}
