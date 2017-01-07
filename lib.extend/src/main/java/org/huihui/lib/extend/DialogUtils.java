package org.huihui.lib.extend;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * User: huihui
 * Date: 2017-01-07 {HOUR}:05
 */
public class DialogUtils {
    public static void showProgre(Activity activity) {
        new MaterialDialog.Builder(activity).progress(true, 100).show();
    }
}  