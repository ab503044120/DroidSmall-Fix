package com.sunfusheng.small;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.wequick.small.Small;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Small.setUp(LaunchActivity.this, new Small.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        if (Small.openUri("main1", LaunchActivity.this)) {
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"启动失败",Toast.LENGTH_SHORT).show();
                            throw new IllegalStateException("找不到main1");
                        }
                    }
                });
            }
        }, 1000);
    }

}
