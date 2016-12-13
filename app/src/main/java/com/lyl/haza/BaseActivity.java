package com.lyl.haza;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lyl.haza.common.AppManager;
import com.lyl.haza.contract.BaseView;
import com.lyl.haza.utils.StringUtils;
import com.lyl.haza.utils.log.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class BaseActivity extends AppCompatActivity implements BaseView {

    private ArrayMap<String, Subscription> mSubMap;

    private CompositeSubscription mCompositeSubscription;

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }

    @Override
    public void addSubscription(Subscription s) {
        addSubscription(s, null);
    }

    public void addSubscription(Subscription s, String tag) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
        // 存放单个接口的引用
        if (!StringUtils.isEmpty(tag)) {
            mSubMap.put(tag, s);
        }
    }

    public void cancelSubscription(String tag) {
        if (mSubMap.containsKey(tag)) {
            try {
                mSubMap.get(tag).unsubscribe();
            } catch (Throwable e) {
                Log.e(this.getClass().getSimpleName(), "取消失败");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppManager.getAppManager().addActivity(this);
        mSubMap = new ArrayMap<>();
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
        if (mSubMap != null) {
            mSubMap.clear();
            mSubMap = null;
        }
        super.onDestroy();
    }

    public Toolbar initToolbar(String titleString) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titleString);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        return toolbar;
    }

    public Toolbar initToolbarWithBack(String titleString) {
        return initToolbarWithBack(titleString, null);
    }

    public Toolbar initToolbarWithBack(View.OnClickListener onClickListener) {
        return initToolbarWithBack("", onClickListener);
    }

    public Toolbar initToolbarWithBack(String titleString, View.OnClickListener onClickListener) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titleString);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        if (onClickListener == null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            toolbar.setNavigationOnClickListener(onClickListener);
        }
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
        return toolbar;
    }

    @Override
    public Context getContext() {
        WeakReference<Context> weakReference = new WeakReference<Context>(this);
        return weakReference.get();
    }

    /**
     * http://www.jianshu.com/p/d2f5ae6b4927
     * 尝试防止内存泄漏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 19) {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }
}
