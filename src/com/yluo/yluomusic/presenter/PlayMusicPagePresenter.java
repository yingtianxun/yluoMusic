package com.yluo.yluomusic.presenter;

import java.util.List;

import android.os.RemoteException;

import com.yluo.yluomusic.aidl.SongManager;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.ui.fragment.PlaySongPageFragment;

public class PlayMusicPagePresenter extends BasePlayMusicPresent{
	private static final String TAG = "PlayMusicPagePresenter";

	private PlaySongPageFragment pageFragment;

	public PlayMusicPagePresenter(PlaySongPageFragment pageFragment) {
		super(pageFragment);
		this.pageFragment = pageFragment;
	}
	@Override
	public void updateSongLrc(List<WordLine> wordLines) {
		pageFragment.onUpdateSongLrc(wordLines);
	}

	@Override
	public void updateSongProgress(int progress) {
		pageFragment.onUpdateSongProgress(progress);
	}

	@Override
	public void updateSongDuration(int songDuration) {
		pageFragment.onUpdateSongDuationTime(songDuration);
	}

	@Override
	public void updateStopSong() {
		pageFragment.onUpdateStopSong();
	}

	@Override
	public void updateSongPause() {
		pageFragment.onUpdatePauseSong();
	}

	@Override
	public void updateSongComplete() {
		pageFragment.onUpdateStopSong();
	}
	public void changeSongProgress(int progress) {
		try {
			songManager.changePlaySongProgress(progress);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateSongRestart() {
		pageFragment.onUpdateSongRestart();
		
	}

}
