package com.inovasialfatih.klinikbwcc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.LoginData;
import com.inovasialfatih.klinikbwcc.model.LoginModel;
import com.inovasialfatih.klinikbwcc.model.LoginResult;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends BaseActivity {

    @BindView(R.id.txt_email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.forgot_password) TextView mForgotPassword;
    @BindView(R.id.btn_login) MaterialRippleLayout mLogin;
    @BindView(R.id.btn_register) TextView mRegister;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_user_login);

        InitializeFields();
        initControl();
    }

    private void initControl() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerInten = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(registerInten);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(UserLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(inten);
            }
        });
    }

    private void login(final String email, final String password){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Authenticate.");
        loader.show();

        Call<LoginModel> call = _gate.user().login(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN",
                email,
                password);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
//                        LoginData ld = result.getData();
                        _cooky.addString(Cooky.TOKEN, result.data.token_user);
                        _cooky.addString(Cooky.NAME, result.data.user_param.name);
                        _cooky.addString(Cooky.EMAIL, result.data.user_param.email);

                        _nav.navigate(MainActivity.class, true);
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.login_success));
                    } else {
                        Toasty.error(_ctx, result.data.message);
                    }

                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void InitializeFields() {

        mEmail = findViewById(R.id.txt_email);
        mPassword = findViewById(R.id.password);
        mForgotPassword = findViewById(R.id.forgot_password);
        mRegister = findViewById(R.id.btn_register);
        mLogin = findViewById(R.id.btn_login);

        loadingBar = new ProgressDialog(this);
    }

    @OnClick(R.id.btn_login) void login() {
        mEmail.setError(null);
        mPassword.setError(null);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmail.setError(HRes.string(_ctx, R.string.error_email_empty));
            focusView = mEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(HRes.string(_ctx,R.string.error_password_empty));
            focusView = mPassword;
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError(HRes.string(_ctx, R.string.enter_validate_email));
            focusView = mEmail;
            cancel = true;
        } if (cancel) {
            focusView.requestFocus();
        } else {
            login(email, password);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
