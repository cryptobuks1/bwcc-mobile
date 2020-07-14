package com.inovasialfatih.klinikbwcc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutAppsActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_about_apps);

        initControl();

    }

    private void initControl() {
        mToolbarTitle.setText("About");
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
