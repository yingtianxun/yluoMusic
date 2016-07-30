package com.yluo.yluomusic.presenter;

import java.util.ArrayList;

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

	@Override
	public void updateSongDuationTime(int songDuration) {
		barFragment.onUpdateSongDuationTime(songDuration);
		
		// ¼àÌý²¥·Å½ø¶È
		IntentFilter progressFilter = new IntentFilter("com.yluo.yluomusic.songprogress");
		barFragment.getActivity().registerReceiver(new SongProgressReceiver(), progressFilter);
		
		// ¼àÌý¸è´Ê
		IntentFilter songFilter = new IntentFilter("com.yluo.yluomusic.songlrc");
		barFragment.getActivity().registerReceiver(new SongLrcReceiver(), songFilter);
	}

	public void nextSong() {
	}

	@Override
	public void updateSongLrc(ArrayList<WordLine> wordLines) {
		barFragment.onUpdateSongLrc(wordLines);
	}

	@Override
	public void updateSongProgress(int progress) {
		barFragment.onUpdateSongProgress(progress);
	}

}
