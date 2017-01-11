package org.huihui.lib.greendao;

import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * User: huihui
 * Date: 2017-01-11 {HOUR}:54
 */
public class GreenDaoApplication extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("application","greendao初始化完成");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}