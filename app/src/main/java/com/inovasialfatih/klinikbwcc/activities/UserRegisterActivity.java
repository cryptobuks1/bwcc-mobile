package com.inovasialfatih.klinikbwcc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.LoginData;
import com.inovasialfatih.klinikbwcc.model.LoginResult;
import com.inovasialfatih.klinikbwcc.model.RegisterData;
import com.inovasialfatih.klinikbwcc.model.RegisterResult;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends BaseActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");

    @BindView(R.id.fullname) EditText mFullname;
    @BindView(R.id.phone_number) EditText mPhoneNumber;
    @BindView(R.id.txt_email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.confirm_password) EditText mConfirmPassword;
    @BindView(R.id.btn_login) TextView mLogin;
    @BindView(R.id.btn_register) MaterialRippleLayout mRegister;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_user_register);

        InitializeFields();
        initControl();
    }

    private void initControl() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doRegister(final String nama,final String no_hp, final String email, final String password, final String confirmation_password){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Registering User.");
        loader.show();

        Call<RegisterResult> call = _gate.user().register(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN",
                nama,
                no_hp,
                email,
                password,
                confirmation_password
        );

        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                RegisterResult result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        RegisterData rd = result.getData();
                        _cooky.addString(Cooky.TOKEN, rd.getToken_user());
                        _cooky.addString(Cooky.NAME, rd.user_param.nama);
                        _cooky.addString(Cooky.PHONE_NUMBER, rd.user_param.no_hp);
                        _cooky.addString(Cooky.EMAIL, rd.user_param.email);

                        _nav.navigate(MainActivity.class, true);
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.register_success));
                    } else {
                        Toasty.error(_ctx, HRes.string(_ctx, R.string.error_register_failed));
                    }
                }
                loader.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });

    }

    private void InitializeFields() {

        mEmail = findViewById(R.id.txt_email);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.btn_login);
        mRegister = findViewById(R.id.btn_register);

        loadingBar = new ProgressDialog(this);

    }

    @OnClick(R.id.btn_register) void register() {
        mFullname.setError(null);
        mPhoneNumber.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        String fullname = mFullname.getText().toString();
        String phone = mPhoneNumber.getText().toString();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fullname)) {
            mFullname.setError(HRes.string(_ctx, R.string.error_fullname_empty));
            focusView = mFullname;
            cancel = true;
        } else if (TextUtils.isEmpty(phone)) {
            mPhoneNumber.setError(HRes.string(_ctx, R.string.error_phone_empty));
            focusView = mPhoneNumber;
            cancel = true;
        } else if (phone.length() < 10) {
            mPhoneNumber.setError(HRes.string(_ctx, R.string.error_min_phone_number));
            focusView = mPhoneNumber;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(HRes.string(_ctx, R.string.error_email_empty));
            focusView = mEmail;
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError(HRes.string(_ctx, R.string.enter_validate_email));
            focusView = mEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(HRes.string(_ctx, R.string.error_password_empty));
            focusView = mPassword;
            cancel = true;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()){
            mPassword.setError(HRes.string(_ctx, R.string.week_password));
            focusView = mPassword;
            cancel = true;
        } else if (password.length() < 6) {
            mPassword.setError(HRes.string(_ctx, R.string.error_min_password));
            focusView = mPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError(HRes.string(_ctx,R.string.error_confirm_password_empty));
            focusView = mConfirmPassword;
            cancel = true;
        } else if (!password.equals(confirmPassword)) {
            mConfirmPassword.setError(HRes.string(_ctx,R.string.error_password_mismatch));
            focusView = mConfirmPassword;
            cancel = true;
        } if (cancel) {
            focusView.requestFocus();
        } else {
            doRegister(fullname, phone, email, password, confirmPassword);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
