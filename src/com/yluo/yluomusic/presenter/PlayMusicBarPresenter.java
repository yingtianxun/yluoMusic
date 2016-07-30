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
import com.yluo.yluomusic.presenter.base.BasePresenter;
import com.yluo.yluomusic.service.PlayMusicService;
import com.yluo.yluomusic.ui.fragment.PlaySongBarFragment;

public class PlayMusicBarPresenter extends BasePresenter {

	private static final String TAG = "PlayMusicBarPresenter";

	private PlaySongBarFragment barFragment;

	private SongManager songManager;

	private boolean mIsHasWords = false;
	
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

			songManager = (SongManager) service ;//SongManager.Stub.asInterface(service);
			
//			songManager = SongManager.Stub.asInterface(service);
		}
	};

	public void playMusic() {
		try {
			int songDuration = songManager.startPlaySong("无字碑");
			Log.d(TAG, "---时间:" + songDuration);
			barFragment.onUpdateSongDuationTime(songDuration);
			
			// 监听进度的
			IntentFilter filter = new IntentFilter("com.yluo.yluomusic.songprogress");
			barFragment.getActivity().registerReceiver(new SongProgressReceiver(), filter);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IntentFilter filter = new IntentFilter("com.yluo.yluomusic.songlrc");
		barFragment.getActivity().registerReceiver(new SongLrcReceiver(), filter);
	}

	public void nextSong() {
		
	}
	
	
	class SongLrcReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			 ArrayList<WordLine> wordLines = intent.getParcelableArrayListExtra("wordLines");
			 
			 barFragment.onUpdateSongLrc(wordLines);
		}
	}
	
	class SongProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			barFragment.onUpdateSongProgress(intent.getIntExtra("curProgress", -1));
		}
		
	}
}



















