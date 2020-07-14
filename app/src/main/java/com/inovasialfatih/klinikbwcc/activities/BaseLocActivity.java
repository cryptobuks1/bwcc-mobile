package com.inovasialfatih.klinikbwcc.activities;

import android.app.Activity;
import android.content.Context;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.extensions.Navigator;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseLocActivity extends EasyLocationAppCompatActivity {
    protected Context _ctx;
    protected Navigator _nav;
    protected Cooky _cooky;

    protected void init(Context context, int resLayoutId){
        _ctx = context;
        setContentView(resLayoutId);

        ButterKnife.bind((Activity) context);
        _nav = new Navigator(_ctx);
        _cooky = new Cooky(_ctx);
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

