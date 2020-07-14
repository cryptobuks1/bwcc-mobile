package com.inovasialfatih.klinikbwcc.activities;

import android.os.Bundle;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.inovasialfatih.klinikbwcc.R;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.btn_specialty_history) MaterialRippleLayout btnSpecialty;
    @BindView(R.id.btn_class_history) MaterialRippleLayout btnClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this ,R.layout.activity_history);

        initControl();
    }

    private void initControl(){
        mToolbarTitle.setText("History");

        btnSpecialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _nav.navigate(HistorySpecialtyActivity.class);
            }
        });

        btnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _nav.navigate(HistoryClassActivity.class);
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
