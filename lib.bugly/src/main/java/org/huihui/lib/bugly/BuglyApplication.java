package org.huihui.lib.bugly;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * User: huihui
 * Date: 2017-01-07 {HOUR}:52
 */
public class BuglyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "0fea2661d6", false);
        Log.e("bugly","我初始化完成了");
    }
}