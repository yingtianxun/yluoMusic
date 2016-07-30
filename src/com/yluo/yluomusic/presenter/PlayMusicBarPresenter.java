package com.yluo.yluomusic.presenter;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;
import com.yluo.yluomusic.ui.fragment.PlaySongBarFragment;

public class PlayMusicBarPresenter extends BasePresenter {

	private static final String TAG = "PlayMusicBarPresenter";

	private PlaySongBarFragment barFragment;

	private SongManager songManager;

	public PlayMusicBarPresenter(PlaySongBarFragment barFragment) {
		this.barFragment = barFragment;
	}
	
	public void onAttach() {
		Intent intent = new Intent(barFragment.getActivity(),PlayMusicService.class);
		
		barFragment.getActivity().bindService(
				intent,
				mPlayMusicConnection,
				Service.BIND_AUTO_CREATE);
	}
	public void onDetach() {
		barFragment.getActivity().unbindService(mPlayMusicConnection);
	}
	

	private ServiceConnection mPlayMusicConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "---绑定服务---成功");

			songManager = SongManager.Stub.asInterface(service);

		}
	};
}
