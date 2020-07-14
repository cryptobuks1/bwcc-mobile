package com.inovasialfatih.klinikbwcc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class QueueActivity extends BaseActivity {

    @BindView(R.id.main_list) RecyclerView rvMain;
    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.et_poli) EditText etPoli;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_queue);

        initControl();
    }

    private void initControl() {
        mToolbarTitle.setText("Lihat Antrian");

    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
