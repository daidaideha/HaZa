package com.lyl.haza.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyl.haza.BaseActivity;
import com.lyl.haza.R;
import com.lyl.haza.bean.TabBean;
import com.lyl.haza.common.AppManager;
import com.lyl.haza.common.ToastHelper;

import java.util.List;

public class NewsActivity extends BaseActivity {

    private static final String TAG = NewsActivity.class.getSimpleName();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<TabBean> mTabList;
    // 两次返回 退出 时间记录
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initToolbar(getString(R.string.app_name));

        initTab();
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initTab() {
        String jsonArray = "[{\"type\":\"top\",\"title\":\"头条\"},{\"type\":\"shehui\",\"title\":\"社会\"},{\"type\":\"guonei\",\"title\":\"国内\"},{\"type\":\"guoji\",\"title\":\"国际\"},{\"type\":\"yule\",\"title\":\"娱乐\"},{\"type\":\"tiyu\",\"title\":\"体育\"},{\"type\":\"junshi\",\"title\":\"军事\"},{\"type\":\"keji\",\"title\":\"科技\"},{\"type\":\"caijing\",\"title\":\"财经\"},{\"type\":\"shishang\",\"title\":\"时尚\"}]";
        mTabList = new Gson().fromJson(jsonArray, new TypeToken<List<TabBean>>() {
        }.getType());
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        findViewById(R.id.tab_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showShort("click");
            }
        });
    }

    public void exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastHelper.showShort("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().appExit(this);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsFragment.newInstance(mTabList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mTabList == null ? 0 : mTabList.size();
        }

    }
}
