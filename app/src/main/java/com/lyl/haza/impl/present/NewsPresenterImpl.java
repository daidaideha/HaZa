package com.lyl.haza.impl.present;

import com.lyl.haza.common.KeyHelper;
import com.lyl.haza.bean.NewsBean;
import com.lyl.haza.contract.NewsContract;
import com.lyl.haza.httprsp.NewsRsp;
import com.lyl.haza.impl.model.NewsModelImpl;
import com.lyl.haza.network.ApiException;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class NewsPresenterImpl implements NewsContract.NewsPresenter {

    private NewsContract.NewsView mNewsView;
    private NewsContract.NewsModel mNewsModel;

    private String mType;

    public NewsPresenterImpl(NewsContract.NewsView newsView, String type) {
        mNewsView = newsView;
        mNewsModel = new NewsModelImpl();
        this.mType = type;
        getData();
    }

    @Override
    public List<NewsBean> getItems() {
        return mNewsModel.getItems();
    }

    @Override
    public void getData() {
        mNewsView.addSubscription(requestData(mType, KeyHelper.API_KEY));
    }

    private Subscription requestData(String type, String key) {
        return mNewsModel.getData(type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsRsp>() {
                    @Override
                    public void call(NewsRsp response) {
                        if (response.getError_code() == 0) {
                            NewsRsp.ResultBean bean = response.getResult();
                            if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                                mNewsModel.setItems(bean.getData());
                                mNewsView.showData();
                            } else {
                                mNewsView.showEmpty();
                            }
                        } else {
                            mNewsView.showError();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiException) {
                            mNewsView.showErrToast(((ApiException) throwable).getReason());
                        } else {
                            mNewsView.showError();
                        }
                    }
                });
    }
}
