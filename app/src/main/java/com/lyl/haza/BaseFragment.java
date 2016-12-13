package com.lyl.haza;

import android.support.v4.app.Fragment;

import com.lyl.haza.contract.BaseView;

import rx.Subscription;

/**
 * Created by lyl on 2016/11/17.
 * </P>
 */
public class BaseFragment extends Fragment implements BaseView{

    public void setTitle(String title) {
        ((BaseActivity)getActivity()).initToolbar(title);
    }

    @Override
    public void addSubscription(Subscription subscription) {

    }
}
