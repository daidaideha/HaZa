package com.lyl.haza.contract;


import com.lyl.haza.bean.NewsBean;
import com.lyl.haza.httprsp.NewsRsp;

import java.util.List;

import rx.Observable;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public interface NewsContract {

    interface NewsPresenter {

        List<NewsBean> getItems();

        void getData();
    }

    interface NewsView extends BaseView{

        void showData();

        void showEmpty();

        void showError();

        void showErrToast(String errMsg);
    }

    interface NewsModel {

        void setItems(List<NewsBean> items);

        List<NewsBean> getItems();

        Observable<NewsRsp> getData(String type, String key);
    }
}
