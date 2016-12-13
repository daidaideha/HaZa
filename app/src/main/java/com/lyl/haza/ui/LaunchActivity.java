package com.lyl.haza.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by lyl on 2016/11/17.
 * </P>
 */
public class LaunchActivity extends Activity {

    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        launch(2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }

    private void launch(long delayMillis) {
        mRunnable = new Runnable() {
            public void run() {
                handleProtocol();
            }
        };
        mHandler.postDelayed(mRunnable, delayMillis);
    }

    private synchronized void handleProtocol() {
        if (isFinishing()) return;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = new Intent(LaunchActivity.this, NewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
