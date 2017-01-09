package org.huihui.lib.realm;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;

/**
 * User: Administrator
 * Date: 2017-01-09 {HOUR}:52
 */
public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Realm.init(this);
        }catch (Exception e){
          e.printStackTrace();
        }
        Log.e("application","realm我初始化完成了");
    }
}