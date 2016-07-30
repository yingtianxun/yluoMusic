package com.yluo.yluomusic.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.usage.UsageEvents.Event;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.adapter.viewpageradapter.MainContentFragmentAdapter;
import com.yluo.yluomusic.eventbusmessage.OpenPlayMusicPageMessage;
import com.yluo.yluomusic.ui.fragment.base.BaseFragment;
import com.yluo.yluomusic.ui.widget.CircleImageView;
import com.yluo.yluomusic.ui.widget.HideSlideMenuLayout;
import com.yluo.yluomusic.ui.widget.RotateLayout;
import com.yluo.yluomusic.ui.widget.SacleSlideMenuLayout;
import com.yluo.yluomusic.ui.widget.ShowSongWordView;
import com.yluo.yluomusic.ui.widget.ViewPagerIndicator;
import com.yluo.yluomusic.utils.DpTranToPx;
import com.yluo.yluomusic.utils.StatusBarUtil;

public class MainContentFragment extends BaseFragment {
	private static final String TAG = "MainContentFragment";
	private List<Fragment> fragmentslist;
	private ViewPager maincontentPager;

	private SacleSlideMenuLayout slMainContent;

	private HideSlideMenuLayout hlSubMenu;

	private ViewPagerIndicator vpNavIndicator;// viewpager的指示器
	
	private RelativeLayout rlNav; //导航栏
	
	private RotateLayout rotateLayoutMain; // 旋转歌词
	private View playSongPager;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main_content;
	}

	@Override
	protected void initUI() {
		
		maincontentPager = findViewById(R.id.vp_maincontent);

		slMainContent = findViewById(R.id.sl_main_content);

		// 设置左菜单
		slMainContent.setLeftMenuView(R.layout.fragment_slide_menu);


		// 设置子菜单
		hlSubMenu = findViewById(R.id.hl_sub_menu);

		hlSubMenu.setRightMenuView(R.layout.test);
		// 导航
		rlNav = findViewById(R.id.rl_nav);
		
		// 导航栏的indicator
		vpNavIndicator = findViewById(R.id.vp_nav_indicator);
		// 初始化标签
		vpNavIndicator.addTagView(createTagView(R.drawable.nav_top_listen));

		vpNavIndicator.addTagView(createTagView(R.drawable.nav_top_watch));

		vpNavIndicator.addTagView(createTagView(R.drawable.nav_top_sing));

		vpNavIndicator.setCheck(0);
		
		// 播放时的旋转按钮
		
		// 旋转歌词
		rotateLayoutMain = findViewById(R.id.rotate_layout_main);
		//  这里有问题,等下再修复
		rotateLayoutMain.setPlayMusicView(R.layout.fragment_rotate_play_song);
		rotateLayoutMain.closeMusicViewByNow();
	}
	@Override
	protected void attchWindow(Context context) {
		// 注册eventbus
		EventBus.getDefault().register(this);
	}

	@Override
	protected void detachWindow() {
		// 取消注册eventBus
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void initData() {
		fragmentslist = new ArrayList<Fragment>(3);

		fragmentslist.add(new ViewPagerListenFragment());
		fragmentslist.add(new ViewPagerLookFragment());
		fragmentslist.add(new ViewPagerSingFragment());

		FragmentManager fm = ((FragmentActivity) getContext())
				.getSupportFragmentManager();
		MainContentFragmentAdapter adapter = new MainContentFragmentAdapter(fm,
				fragmentslist);

		maincontentPager.setAdapter(adapter);
		maincontentPager.setCurrentItem(0);
	}

	@Override
	protected void initEvent() {
		maincontentPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private View createTagView(int srcId) {

		ImageView view = new ImageView(getContext());

		MarginLayoutParams layoutParams = new MarginLayoutParams(
				DpTranToPx.dp2px(getContext(), 39), DpTranToPx.dp2px(
						getContext(), 39));

		view.setLayoutParams(layoutParams);

		view.setImageResource(srcId);

		return view;
	}


	
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onOpenRotationMusicView(OpenPlayMusicPageMessage event) {
		rotateLayoutMain.opemMusicView();
	}



}
