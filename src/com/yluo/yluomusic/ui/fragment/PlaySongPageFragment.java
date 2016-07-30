package com.yluo.yluomusic.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.presenter.PlayMusicPagePresenter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.MusicWordView;
import com.yluo.yluomusic.ui.widget.YluoSeekBar;
import com.yluo.yluomusic.utils.TimeToString;

public class PlaySongPageFragment extends BaseFragment{
	
	private static final String TAG = "PlaySongPageFragment";
	
	private PlayMusicPagePresenter pagePresenter;
	
	private MusicWordView mwvSongwords; //��ʿؼ�
	
	private YluoSeekBar sbSongProgress; // ������
	
	private ImageButton ibtnPlayMusic;// ���Ű�ť
	
	private TextView txShowCurPlaytime; // ��ߵ�ʱ�����
	
	private TextView txShowTotalPlaytime; // �ұߵ���ʱ��
	
	private boolean mIsPressSeekBar = false;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_playing_song;
	}
	@Override
	protected void initUI() {
		adjustViewPaddingTop();// ����paddingTop
		// ��ʿؼ�
		mwvSongwords = findViewById(R.id.mwv_songwords);
		// ������
		sbSongProgress = findViewById(R.id.sb_song_progress);
		//���Ű�ť
		ibtnPlayMusic = findViewById(R.id.ibtn_play_music);
		// ��ߵ�ʱ�����
		txShowCurPlaytime = findViewById(R.id.tx_show_cur_playtime);
		// �ұߵ���ʱ��
		txShowTotalPlaytime = findViewById(R.id.tx_show_total_playtime);
	}

	@Override
	protected void initData() {}

	@Override
	protected void initEvent() {
		ibtnPlayMusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPlayMusic();
			}
		});
		sbSongProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				pagePresenter.changeSongProgress(seekBar.getProgress());
				
				mIsPressSeekBar = false;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mIsPressSeekBar = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
			}
		});
	}
	@Override
	protected void attchWindow(Context context) {
		if (pagePresenter == null) {
			pagePresenter = new PlayMusicPagePresenter(this);
		}
		pagePresenter.onAttach();
	}

	@Override
	protected void detachWindow() {
		pagePresenter.onDetach();
	}
	
	public void onUpdateSongDuationTime(int songDuration) {
		mwvSongwords.setSongDuration(songDuration);
		sbSongProgress.setMax(songDuration);
		
		ibtnPlayMusic.setImageResource(R.drawable.djc);
		
		txShowTotalPlaytime.setText(TimeToString.millisecondToStr(songDuration));
		
		
	}
	private void onPlayMusic() {
		pagePresenter.playMusic("���ֱ�");
	}

	public void onUpdateSongLrc(List<WordLine> wordLines) {
		mwvSongwords.setSongWords(wordLines);
	}
	public void onUpdateSongProgress(int progress) {
		mwvSongwords.setCurPlayTime(progress);
		
		if(!mIsPressSeekBar) {
			sbSongProgress.setProgress(progress);
		}
		txShowCurPlaytime.setText(TimeToString.millisecondToStr(progress));
		
	}
	public void onUpdateStopSong() {
		ibtnPlayMusic.setImageResource(R.drawable.djd);
	}
	public void onUpdatePauseSong() {
		ibtnPlayMusic.setImageResource(R.drawable.djd);
	}
	
}
