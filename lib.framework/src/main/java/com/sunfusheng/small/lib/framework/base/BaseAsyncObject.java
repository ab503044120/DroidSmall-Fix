package com.sunfusheng.small.lib.framework.base;

import android.content.Context;
import android.os.Bundle;

import com.sunfusheng.small.lib.framework.proxy.MessageProxy;
import com.sunfusheng.small.lib.framework.proxy.ModelMap;
import com.sunfusheng.small.lib.framework.proxy.common.IActivityLifecycle;
import com.sunfusheng.small.lib.framework.proxy.common.IRefreshBack;
import com.sunfusheng.small.lib.framework.proxy.handler.AsyncObjectHandler;
import com.sunfusheng.small.lib.framework.proxy.helper.ObjectHelper;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class BaseAsyncObject<T extends BaseControl> implements IActivityLifecycle, IRefreshBack {

    protected T mControl;
    protected MessageProxy messageProxy;
    protected ModelMap mModel;
    private ObjectHelper mHelper;

    public void initParams() {
        mModel = mHelper.getModelMap();
        messageProxy = mHelper.getMessageProxy();
        mControl = (T) mHelper.getControl();
    }

    public void initParamManually() {
        mHelper = new ObjectHelper(this, new AsyncObjectHandler(this));
        mHelper.onCreate();
        initParams();
    }

    public void onCreate(Context context) {
        initParamManually();
    }

    @Override
    public void onCreate() {
        initParamManually();
    }

    @Override
    public void onStart() {
        mHelper.onStart();
    }

    @Override
    public void onResume() {
        mHelper.onResume();
    }

    @Override
    public void onPause() {
        mHelper.onPause();
    }

    @Override
    public void onStop() {
        mHelper.onStop();
    }

    @Override
    public void onDestory() {
        mHelper.onDestroy();
    }

    public Context getContext() {
        return mHelper.getContext();
    }

    @Override
    public void onRefresh(int requestCode, Bundle bundle) {

    }
}
