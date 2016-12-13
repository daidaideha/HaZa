package com.lyl.haza.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lyl.haza.BaseShareActivity;
import com.lyl.haza.R;
import com.lyl.haza.bean.NewsBean;
import com.lyl.haza.common.KeyHelper;
import com.lyl.haza.utils.ViewUtil;

import java.util.HashMap;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 * 新闻详情页面
 */
public class NewsDetailActivity extends BaseShareActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private static final String EXTRA_URL = "EXTRA_URL";

    private WebView mWebView;
    private BottomSheetDialog mBottomSheetDialog;

    private NewsBean mBean;

    public static void start(Activity activity, NewsBean bean) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(EXTRA_URL, bean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtra(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initToolbarWithBack(mBackOnClickListener).setOnMenuItemClickListener(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        releaseAllWebViewCallback();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                showBottomSheetDialog();
                break;
            default:
                break;
        }
        return false;
    }

    private void initExtra(Bundle bundle) {
        if (bundle == null)
            bundle = getIntent().getExtras();
        if (bundle != null) {
            mBean = bundle.getParcelable(EXTRA_URL);
            if (mBean != null)
                return;
        }
        Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        ViewUtil.setWebViewSetting(mWebView);
        mWebView.loadUrl(mBean.getUrl());
        mWebView.setWebViewClient(new WebViewClient());
    }

    public void showBottomSheetDialog() {
        View sheetDialogView = View.inflate(this, R.layout.news_share_dialog, null);
        sheetDialogView.findViewById(R.id.pop_share_ll_0).setOnClickListener(this);
        sheetDialogView.findViewById(R.id.pop_share_ll_1).setOnClickListener(this);
        sheetDialogView.findViewById(R.id.pop_share_ll_2).setOnClickListener(this);
        sheetDialogView.findViewById(R.id.pop_share_ll_3).setOnClickListener(this);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(sheetDialogView);
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
        mBottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_share_ll_0:
                shareWithBitmap(KeyHelper.AppShareTypeKey.SHARE_2_WECHAT, mBean.getThumbnail_pic_s(),
                        getString(R.string.app_name), mBean.getTitle(), mBean.getUrl());
                break;
            case R.id.pop_share_ll_1:
                shareWithBitmap(KeyHelper.AppShareTypeKey.SHARE_2_PENGYOUQUAN, mBean.getThumbnail_pic_s(),
                        getString(R.string.app_name), mBean.getTitle(), mBean.getUrl());
                break;
            case R.id.pop_share_ll_2:
                shareWithBitmap(KeyHelper.AppShareTypeKey.SHARE_2_WEIBO, mBean.getThumbnail_pic_s(),
                        getString(R.string.app_name), mBean.getTitle(), mBean.getUrl());
                break;
            case R.id.pop_share_ll_3:
                shareQQ(getString(R.string.app_name), mBean.getTitle(), mBean.getUrl(), mBean.getThumbnail_pic_s());
                break;
            default:
                break;
        }
        showLoadingDialog();
        mBottomSheetDialog.dismiss();
    }

    private View.OnClickListener mBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
}
