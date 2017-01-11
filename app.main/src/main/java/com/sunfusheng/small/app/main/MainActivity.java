package com.sunfusheng.small.app.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sunfusheng.small.lib.framework.base.BaseActivity;
import com.sunfusheng.small.lib.framework.util.ToastTip;

import net.wequick.small.Small;

import org.greenrobot.greendao.query.Query;
import org.huihui.lib.greendao.DaoSession;
import org.huihui.lib.greendao.GreenDaoApplication;
import org.huihui.lib.greendao.Note;
import org.huihui.lib.greendao.NoteDao;
import org.huihui.lib.greendao.NoteType;
import org.huihui.lib.realm.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_beijing_weather)
    TextView tvBeijingWeather;
    @Bind(R.id.tv_shanghai_weather)
    TextView tvShanghaiWeather;

    private LocalBroadcastManager mLocalBroadcastManager;
    private MyBroadcastReceiver mBroadcastReceiver;
    private NoteDao noteDao;
    private Query<Note> notesQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
        Realm defaultInstance = Realm.getDefaultInstance();
        User first = defaultInstance.where(User.class).findFirst();
        if (first != null) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(first.getName() + first.getAge());
        } else {
            first = new User();
            first.setUID("1");
            first.setAge(1);
            first.setName("张三");
            defaultInstance.beginTransaction();
            defaultInstance.copyToRealm(first);
            defaultInstance.commitTransaction();
        }

        org.huihui.lib.extend.DialogUtils.showProgre(this);

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        DaoSession daoSession = GreenDaoApplication.getDaoSession();
        noteDao = daoSession.getNoteDao();
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
        List<Note> list = notesQuery.list();
        if (list.isEmpty()) {
            Note note = new Note(null, "123", comment, new Date(), NoteType.TEXT);
            noteDao.insert(note);
        }else{
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.append(list.get(0).getComment());
        }


    }

    private void initData() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmallService.ACTION_TYPE_STATUS);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);

        if (getSettingsSharedPreferences().manifest_code() <= 0) {
            getSettingsSharedPreferences().manifest_code(0);
        }
        if (getSettingsSharedPreferences().updates_code() <= 0) {
            getSettingsSharedPreferences().updates_code(0);
        }
        if (getSettingsSharedPreferences().additions_code() <= 0) {
            getSettingsSharedPreferences().additions_code(0);
        }

    }

    private void initView() {
        initToolBar(toolbar, false, "Android Small 插件化示例");

        tvStatus.setVisibility(View.GONE);

        if (getSettingsSharedPreferences().small_update() == 1) {
            getSettingsSharedPreferences().small_update(0);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("恭喜你！更新插件成功！");
        }

        if (getSettingsSharedPreferences().small_add() == 1) {
            getSettingsSharedPreferences().small_add(0);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("恭喜你！增加插件成功！");
        }
    }

    private void initListener() {
        tvPhone.setOnClickListener(this);
        tvWeather.setOnClickListener(this);
        tvNumber.setOnClickListener(this);
        tvBeijingWeather.setOnClickListener(this);
        tvShanghaiWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_weather:
                Small.openUri("weather", mContext);
                break;
            case R.id.tv_phone:
                Small.openUri("phone", mContext);
                break;
            case R.id.tv_number:
                Small.openUri("phone/Number?num=18600604600&toast=Fucking amazing!", mContext);

//                Intent intent = Small.getIntentOfUri("phone/Number", mContext);
//                intent.putExtra("num", "18600604600");
//                intent.putExtra("toast", "这样也可以");
//                startActivity(intent);
                break;
            case R.id.tv_beijing_weather:
                Small.openUri("weather", mContext);
                break;
            case R.id.tv_shanghai_weather:
                Small.openUri("shanghai.weather", mContext);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SmallService.class);
        switch (item.getItemId()) {
            case R.id.action_update_plugin: // 更新插件
                intent.putExtra("small", SmallService.SMALL_CHECK_UPDATE);
                startService(intent);
                return true;
            case R.id.action_add_plugin: // 增加插件
                intent.putExtra("small", SmallService.SMALL_CHECK_ADD);
                startService(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SmallService.class);
        intent.putExtra("small", SmallService.SMALL_UPDATE_BUNDLES);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SmallService.ACTION_TYPE_STATUS:
                    ToastTip.show(intent.getStringExtra("status"));
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000 && data != null) {
            String result = data.getStringExtra("result");
            if (!TextUtils.isEmpty(result)) {
                ToastTip.show("回传数据：" + result);
            }
        }
    }

}
