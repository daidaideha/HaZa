package com.lyl.haza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lyl.haza.common.KeyHelper;
import com.lyl.haza.common.ToastHelper;
import com.lyl.haza.utils.log.Log;
import com.lyl.haza.wxapi.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by lyl on 2016/11/21.
 * </P>
 */
public class BaseShareActivity extends BaseActivity {

    private static final String TAG = BaseShareActivity.class.getSimpleName();

    private ProgressDialog mLoadingDialog = null;

    /**
     * 微信分享接口实例
     */
    protected IWXAPI mWxApi = null;
    /**
     * QQ分享接口实例
     */
    private Tencent mTencent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWxApi = null;
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(BaseShareActivity.this);
        }
        mLoadingDialog.setMessage(getString(R.string.waiting_more));
        mLoadingDialog.show();
    }

    protected void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /***
     * 加载网络图片分享
     *
     * @param buttonId 按钮id
     * @param imgUrl   图片url
     * @param title    分享标题
     * @param desc     分享内容
     * @param linkUrl  分享链接url
     */
    protected void shareWithBitmap(final int buttonId, final String imgUrl, final String title, final String desc,
                                   final String linkUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }
        Picasso.with(BaseShareActivity.this).load(imgUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "图片加载成功！" + imgUrl);
                if (!bitmap.isRecycled()) {
                    Bitmap loadedImage = bitmap.copy(bitmap.getConfig(), true);
                    switch (buttonId) {
                        case KeyHelper.AppShareTypeKey.SHARE_2_WECHAT:
                            shareWechat(title, desc, linkUrl, loadedImage, R.drawable.default_loading);
                            break;
                        case KeyHelper.AppShareTypeKey.SHARE_2_PENGYOUQUAN:
                            shareWechatCircle(title, desc, linkUrl, loadedImage, R.drawable.default_loading);
                            break;
                        case KeyHelper.AppShareTypeKey.SHARE_2_WEIBO:
//                            shareWeibo(title, desc, linkUrl, loadedImage, R.drawable.ic_shared_weibo_2);
                            break;
                    }
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(TAG, "图片加载失败！" + imgUrl);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "图片已经替换！" + imgUrl);
            }
        });
    }

    protected void shareWechat(String shareTitle, String shareContent, String shareUrl,
                               Bitmap shareImgBitmap, int shareImgDrawableID) {
        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        }

        if (!mWxApi.isWXAppInstalled()) {
            dismissDialog();
            ToastHelper.showShort("您还没有安装微信");
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareContent;
        if (shareImgBitmap == null) {
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), shareImgDrawableID);
            msg.thumbData = bmpToByteArray(thumb, true);
        } else {
//            msg.setThumbImage(shareImgBitmap);
            msg.thumbData = bmpToByteArray(shareImgBitmap, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        mWxApi.sendReq(req);
    }

    protected void shareWechatCircle(String shareTitle, String shareContent, String shareUrl,
                                     Bitmap shareImgBitmap, int shareImgDrawableID) {
        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        }

        if (!mWxApi.isWXAppInstalled()) {
            dismissDialog();
            ToastHelper.showShort("您还没有安装微信");
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareContent;
        if (shareImgBitmap == null) {
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), shareImgDrawableID);
            msg.thumbData = bmpToByteArray(thumb, true);
        } else {
            msg.thumbData = bmpToByteArray(shareImgBitmap, true);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mWxApi.sendReq(req);
    }

    protected void shareQQ(String shareTitle, String shareContent, String shareUrl, String shareImgUrl) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constants.TENCENT_APP_ID, getApplicationContext());
        }
        if (!isQQClientAvailable(this)) {
            dismissDialog();
            ToastHelper.showShort("您还没有安装QQ官方客户端");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);
        /**Const.URI_QQ_SHARE_LOC_URL*/
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareImgUrl);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        mTencent.shareToQQ(this, bundle, mQQIUiListener);
    }

    /**
     * 判断qq是否可用
     *
     * @param context 上下文对象
     * @return true 表示已安装 false 表示未安装
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    protected byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * qq 分享
     */
    protected IUiListener mQQIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            ToastHelper.showShort("分享成功");
        }

        @Override
        public void onError(UiError uiError) {
        }

        @Override
        public void onCancel() {
        }
    };
}
