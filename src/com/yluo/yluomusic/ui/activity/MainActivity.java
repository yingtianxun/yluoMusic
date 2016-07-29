package com.yluo.yluomusic.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.yluo.yluomusic.R;
import com.yluo.yluomusic.R.drawable;
import com.yluo.yluomusic.R.id;
import com.yluo.yluomusic.R.layout;
import com.yluo.yluomusic.adapter.viewpageradapter.MainContentFragmentAdapter;
import com.yluo.yluomusic.bean.LoveSongBean;
import com.yluo.yluomusic.db.dao.SongListDao;
import com.yluo.yluomusic.db.helper.SongInfoProviderSQLiteOpenHelper;
import com.yluo.yluomusic.provider.SongInfoProvider;
import com.yluo.yluomusic.ui.fragment.MainContentFragment;
import com.yluo.yluomusic.ui.fragment.SlideMenuFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerListenFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerLookFragment;
import com.yluo.yluomusic.ui.fragment.ViewPagerSingFragment;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.yluo.yluomusic.adapter.recyclerviewAdapter.LoveSongRecyclerViewAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {

	private static final String TAG="MainActivity";
    private CheckBox mCbWifiConnect;
    private LoveSongRecyclerViewAdapter adapter;
    private List<LoveSongBean> loveSongBeanList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
  //      setContentView(R.layout.activity_layout);
        
//        setContentView(R.layout.content);
        // configure the SlidingMenu
//        SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
////        menu.setShadowWidthRes(R.dimen.shadow_width);
////        menu.setShadowDrawable(R.drawable.shadow);
////        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.fragment_slide_menu);
        
        //setContentView(R.layout.main_content_bottom_play);

        setContentView(R.layout.recycle_view_love);
        loveSongBeanList = new ArrayList<LoveSongBean>();
        //新建数据库
        SongListDao songListDao = new SongListDao(this);
        for(int i = 0; i < 20; i++) {
            LoveSongBean loveSongBean = new LoveSongBean();
            loveSongBean.setId(i);
            loveSongBean.setSongName("记得我吗");
            loveSongBean.setSingerName("陈冠希");
            loveSongBean.setMv(true);
            loveSongBean.setLove(false);
        	
        	songListDao.addLoveSong(loveSongBean);
        	loveSongBeanList.add(loveSongBean);
        }
        	
        adapter = new LoveSongRecyclerViewAdapter(this,loveSongBeanList);
        RecyclerView rvLoveSongList = (RecyclerView) findViewById(R.id.rv_love);
        rvLoveSongList.setLayoutManager(new LinearLayoutManager(this));
        
//        rvLoveSongList.addItemDecoration(new DividerItemDecoration(this,
//				DividerItemDecoration.VERTICAL_LIST));
        rvLoveSongList.setAdapter(adapter);
        
        DisplayMetrics ddsds  = this.getResources().getDisplayMetrics();
        Toast.makeText(this, "像素密度:" + ddsds.density, Toast.LENGTH_SHORT).show();
        
//        List <LoveSongBean> loveSongBeans = songListDao.queryLoveSongBySongName("记得我吗");
//        Log.d("MainActivity", ""+loveSongBeans);

        //contentPrivider
//        SongInfoProvider songInfoProvider = new SongInfoProvider();
//        Log.d(TAG, "content://"+ SongInfoProvider.AUTHORITY+"/"+SongInfoProviderSQLiteOpenHelper.TABLENAME);
//        Uri uri = Uri.parse("content://"+ SongInfoProvider.AUTHORITY+"/"+SongInfoProviderSQLiteOpenHelper.TABLENAME);
//        for(int i = 0; i < 20; i++) {
//            LoveSongBean loveSongBean = new LoveSongBean();
//            loveSongBean.setId(i);
//            loveSongBean.setSongName("我的背包"+i);
//            loveSongBean.setSingerName("陈奕迅");
//            loveSongBean.setMv(true);
//            loveSongBean.setLove(false);
//            ContentValues values = new ContentValues();
//            values.put(SongInfoProviderSQLiteOpenHelper.ID, loveSongBean.getId());
//            values.put(SongInfoProviderSQLiteOpenHelper.SONGNAME, loveSongBean.getSongName());
//            values.put(SongInfoProviderSQLiteOpenHelper.SINGER, loveSongBean.getSingerName());
//            values.put(SongInfoProviderSQLiteOpenHelper.isLove, loveSongBean.isLove());
//            values.put(SongInfoProviderSQLiteOpenHelper.hasMv, loveSongBean.hasMv());
//            Uri uriInsert = getContentResolver().insert(uri, values);
//        }
//        
//		Cursor cursor=null;
//		cursor = getContentResolver().query(uri, null, null, null, null);
//		while (cursor.moveToNext()) {
//			String songName = cursor.getString(cursor.getColumnIndex(SongInfoProviderSQLiteOpenHelper.SONGNAME));
//			String singer = cursor.getString(cursor.getColumnIndex(SongInfoProviderSQLiteOpenHelper.SINGER));
//			Log.d(TAG, "songName="+songName+",singer="+singer);
//		}
//		cursor.close();
        
        
//	      android.support.v4.app.FragmentManager fManager =  this.getSupportFragmentManager();
//	      FragmentTransaction transaction = fManager.beginTransaction();
//	      MainContentFragment contentFragment = new MainContentFragment();
////	      transaction.add(contentFragment, "111");
////	      transaction.replace(R.id.fl_test, slideMenuFragment);
//	      transaction.add(R.id.fl_test, contentFragment);
//	      transaction.commit();

        //setContentView(R.layout.vp_pager_listen);
//        setContentView(R.layout.fragment_slide_menu);
//        mCbWifiConnect = (CheckBox)findViewById(R.id.cb_wifi_connect);
//        
//	mCbWifiConnect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				handleCbWifiOpenOrClose(isChecked);
//			}
//		});
//        android.support.v4.app.FragmentManager fManager =  this.getSupportFragmentManager();
//        FragmentTransaction transaction = fManager.beginTransaction();
//        SlideMenuFragment slideMenuFragment = new SlideMenuFragment();
//        transaction.add(slideMenuFragment, "111");
//        transaction.commit();
        
        
//    }
//	private void handleCbWifiOpenOrClose(boolean isOpen) {
//		
//		int bkDrawableID = isOpen ? 
//				R.drawable.slide_menu_checkbox_open : 
//					R.drawable.slide_menu_checkbox_close;
//		
//		mCbWifiConnect.setBackgroundResource(bkDrawableID);
		
//		ImageView imageView=new ImageView(null);
//		imageView.setImageResource(R.drawable.slide_menu_checkbox_open);
	}

}
