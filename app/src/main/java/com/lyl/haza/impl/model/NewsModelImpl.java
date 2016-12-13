package com.lyl.haza.impl.model;


import com.lyl.haza.bean.NewsBean;
import com.lyl.haza.contract.NewsContract;
import com.lyl.haza.httprsp.NewsRsp;

import java.util.List;

import rx.Observable;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class NewsModelImpl extends BaseModelImpl implements NewsContract.NewsModel {

    private List<NewsBean> mItems;

    @Override
    public void setItems(List<NewsBean> items) {
        this.mItems = items;
    }

    @Override
    public List<NewsBean> getItems() {
        return mItems;
    }

    @Override
    public Observable<NewsRsp> getData(String type, String key) {
        return mApiService.newsGet(type, key);
    }
}
