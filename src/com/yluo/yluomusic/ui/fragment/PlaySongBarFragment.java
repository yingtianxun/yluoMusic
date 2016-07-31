package com.yluo.yluomusic.ui.fragment;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.aidl.WordLine;
import com.yluo.yluomusic.eventbusmessage.OpenPlayMusicPageMessage;
import com.yluo.yluomusic.presenter.PlayMusicBarPresenter;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.CircleImageView;
import com.yluo.yluomusic.ui.widget.ShowSongWordView;
import com.yluo.yluomusic.ui.widget.YluoSeekBar;

public class PlaySongBarFragment extends BaseFragment {

	private static final String TAG = "PlaySongBarFragment";
	
	private PlayMusicBarPresenter barPresenter;

	private CircleImageView cimvRotateIcon; // 播放时旋转的图片按钮

	private ShowSongWordView sVsongword; // 显示歌词的

	private YluoSeekBar seekBarSongProgress; // 进度条

	private TextView tvSongName; // 歌名

	private TextView tvSingerName; // 歌手名

	private ImageButton iBtnPlaySong; // 播放按钮

	private ImageButton iBtnNextOne; // 下一首

	private ImageButton iBtnShowCurSongList; // 显示当前播放列表的

	private boolean mIsPressSeekBar = false;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_play_music_bar;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		barPresenter.onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
		barPresenter.onAttach();
	}


	@Override
	protected void attchWindow(Context context) {
		if (barPresenter == null) {
			barPresenter = new PlayMusicBarPresenter(this);
		}
	}

	@Override
	protected void detachWindow() {
	}
	@Override
	protected void initUI() {
		// 旋转的按钮
		cimvRotateIcon = findViewById(R.id.cimv_rotate_icon);
		// 设置歌词
		sVsongword = findViewById(R.id.sv_songword);
		// 播放进度条
		seekBarSongProgress = findViewById(R.id.sb_song_progress);
		// 歌名
		tvSongName = findViewById(R.id.tv_song_name);
		// 歌手名
		tvSingerName = findViewById(R.id.tv_singer_name);
		// 播放按钮
		iBtnPlaySong = findViewById(R.id.ibtn_play_song);
		// 下一首
		iBtnNextOne = findViewById(R.id.ibtn_next_one);
		// 显示当前播放列表
		iBtnShowCurSongList = findViewById(R.id.ibtn_show_cur_song_list);
	}
	@Override
	protected void initData() {

	}

	@Override
	protected void initEvent() {
		cimvRotateIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openRotationMusicView();
			}
		});

		iBtnPlaySong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onPlayMusic();
			}
		});

		iBtnNextOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNextSong();
			}
		});
		
		seekBarSongProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				barPresenter.changeSongProgress(seekBar.getProgress());
				
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

	private void openRotationMusicView() {
		EventBus.getDefault().post(new OpenPlayMusicPageMessage());
	}

	private void onPlayMusic() {
		barPresenter.playMusic("无字碑");
	}

	private void onNextSong() {
		barPresenter.nextSong();
	}
	
	// 更新总时间
	public void onUpdateSongDuationTime(int songDurationTime) {
		if(songDurationTime == 0) {
			return;
		}
		
		iBtnPlaySong.setImageResource(R.drawable.skin_kg_ic_playing_bar_pause_default);
		
		seekBarSongProgress.setMax(songDurationTime);
		
		cimvRotateIcon.startRotation();
		
		sVsongword.setSongDuration(songDurationTime);
	}
	// 更新播放进度
	public void onUpdateSongProgress(int curProgress) {
		if(curProgress == -1) {
			return;
		}
		
		if(!mIsPressSeekBar ) {
			seekBarSongProgress.setProgress(curProgress);
		}
		
		sVsongword.setCurPlayTime(curProgress);
	}
	public void onUpdateSongLrc(List<WordLine> wordLines) {
		if(wordLines == null) {
			return;
		}
		
		sVsongword.setSongWords(wordLines);
	}

	public void onUpdateStopSong() {
		cimvRotateIcon.stopRotation();
		iBtnPlaySong.setImageResource(R.drawable.skin_kg_ic_playing_bar_play_default);
	}

	public void onUpdatePauseSong() {
		cimvRotateIcon.stopRotation();
		iBtnPlaySong.setImageResource(R.drawable.skin_kg_ic_playing_bar_play_default);
		
	}

	public void onUpdateSongRestart() {
		iBtnPlaySong.setImageResource(R.drawable.skin_kg_ic_playing_bar_pause_default);
		
		
		cimvRotateIcon.startRotation();
		
	}

}
