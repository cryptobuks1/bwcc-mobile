package com.inovasialfatih.klinikbwcc.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.extensions.Navigator;
import com.inovasialfatih.klinikbwcc.services.Gate;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    protected Context _ctx;
    protected Navigator _nav;
    protected Gate _gate;
    protected Cooky _cooky;

    protected void init(Context context, int resLayoutId){
        _ctx = context;
        setContentView(resLayoutId);

        ButterKnife.bind((Activity) context);
        _nav = new Navigator(_ctx);
        _cooky = new Cooky(_ctx);
        _gate = new Gate(_ctx);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
