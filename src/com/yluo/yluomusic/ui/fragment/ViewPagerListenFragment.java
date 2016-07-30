package com.yluo.yluomusic.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.utils.DpTranToPx;

public class ViewPagerListenFragment extends BaseFragment {

	private LinearLayout llILove;  // 我喜欢

	private LinearLayout llSongList; //歌单

	private LinearLayout llDownload; // 下载

	private LinearLayout llRecent; // 最近
	
	private LinearLayout llMusicLibrary;  // 乐酷
	
	private LinearLayout llRadio;  // 电台
	
	private LinearLayout llCoolGroup;  // 酷群

	@Override
	protected int getLayoutId() {
		return R.layout.vp_pager_listen;
	}

	@SuppressLint("NewApi")
	@Override
	protected void initUI() {
		// 我喜欢
		llILove = findViewById(R.id.ll_i_love);

		// 歌单
		llSongList = findViewById(R.id.ll_song_list);

		// 下载
		llDownload = findViewById(R.id.ll_download);

		// 最近
		llRecent = findViewById(R.id.ll_recent);
		
		// 乐酷
		llMusicLibrary = findViewById(R.id.ll_music_library);
		
		// 电台
		llRadio = findViewById(R.id.ll_radio);
		
		// 酷群
		llCoolGroup = findViewById(R.id.ll_coolgroup);
	}

	private void setTextViewDrawable(TextView txView, int resourceId,int size) {
		Drawable drawable = ContextCompat.getDrawable(getContext(), resourceId);

		drawable.setBounds(0, 0, DpTranToPx.dp2px(getContext(), 50),
				DpTranToPx.dp2px(getContext(), 50));

		txView.setCompoundDrawables(null, drawable, null, null);// 只放左边
	}

	@Override
	protected void initEvent() {
		llILove.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		llSongList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		llDownload.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});

		llRecent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		llMusicLibrary.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		llRadio.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});
		
		llCoolGroup.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
			}
		});
	}

	@Override
	protected void initData() {

	}



	@Override
	protected void attchWindow(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void detachWindow() {
		// TODO Auto-generated method stub
		
	}


}
