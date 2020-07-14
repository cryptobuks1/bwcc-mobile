package com.inovasialfatih.klinikbwcc.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.inovasialfatih.klinikbwcc.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(_cooky.token()))
                    _nav.navigate(UserLoginActivity.class, true);
                else _nav.navigate(MainActivity.class, true);
            }
        }, 2000);
    }
}
