package com.inovasialfatih.klinikbwcc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.txt_name) TextView mName;
    @BindView(R.id.txt_email) TextView mEmail;
    @BindView(R.id.btn_change_password) Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_profile);

        initControl();
    }

    private void initControl(){
        mToolbarTitle.setText("Profile");

        mName.setText(_cooky.getString(Cooky.NAME));
        mEmail.setText(_cooky.getString(Cooky.EMAIL));

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _nav.navigate(ChangePasswordActivity.class);
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
