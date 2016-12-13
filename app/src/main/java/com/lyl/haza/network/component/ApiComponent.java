package com.lyl.haza.network.component;

import com.lyl.haza.impl.model.BaseModelImpl;
import com.lyl.haza.network.scope.ActivityScope;

import dagger.Component;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
@ActivityScope
@Component(dependencies = NetComponent.class)
public interface ApiComponent {

    void inject(BaseModelImpl model);
}
