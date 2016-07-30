package com.yluo.yluomusic.presenter;

import java.util.ArrayList;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;
import com.yluo.yluomusic.ui.fragment.PlaySongPageFragment;

public class PlayMusicPagePresenter extends BasePlayMusicPresent{
	private static final String TAG = "PlayMusicPagePresenter";

	private PlaySongPageFragment pageFragment;

	protected SongManager songManager;

	public PlayMusicPagePresenter(PlaySongPageFragment pageFragment) {
		super(pageFragment);
		this.pageFragment = pageFragment;
	}

	@Override
	public void onAttach() {
		
		Intent intent = new Intent(pageFragment.getActivity(),PlayMusicService.class);
		
		pageFragment.getActivity().bindService(
				intent,
				mPlayMusicConnection,
				Service.BIND_AUTO_CREATE);
	}

	@Override
	public void onDetach() {
		pageFragment.getActivity().unbindService(mPlayMusicConnection);
	}
	
	public void playMusic() {
		
	}
	private ServiceConnection mPlayMusicConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "---绑定服务---成功");
			songManager = (SongManager) service ;
		}
	};

	@Override
	public void updateSongDuationTime(int songDuration) {
		
	}

	@Override
	public void updateSongLrc(ArrayList<WordLine> wordLines) {
		
	}

	@Override
	public void updateSongProgress(int progress) {
		
	}
	

}
