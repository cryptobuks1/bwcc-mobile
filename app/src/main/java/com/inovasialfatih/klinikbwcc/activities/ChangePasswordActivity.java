package com.inovasialfatih.klinikbwcc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.ChangePasswordResponse;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.confirm_password) EditText mConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_change_password);

        initControl();
    }

    private void initControl() {
        mToolbarTitle.setText("Change Password");
    }

    private void forgotPassword(final String new_password, final String confirmPassword){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Changing password.");
        loader.show();

        Call<ChangePasswordResponse> call = _gate.user().changePassword(
                _cooky.token(),
                new_password,
                confirmPassword);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                ChangePasswordResponse result = response.body();

                if(result != null) {
                    if(result.status.equalsIgnoreCase("Success")) {
                        Toasty.success(_ctx, result.data.message);
                    } else {
                        Toasty.error(_ctx, result.data.message);
                    }

                } else {
                    Toasty.error(_ctx, "Terjadi kesalahan membuat password");
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_change_password) void changePassword(){
        mPassword.setError(null);
        mConfirmPass.setError(null);

        String mNewPass = mPassword.getText().toString();
        String mConfPass = mConfirmPass.getText().toString();
        String password = mPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(mNewPass)) {
            mPassword.setError(HRes.string(_ctx, R.string.error_new_password_empty));
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
        } else if(TextUtils.isEmpty(mConfPass)) {
            mConfirmPass.setError(HRes.string(_ctx, R.string.error_confirm_new_password_empty));
            focusView = mConfirmPass;
            cancel = true;
        } else if (!password.equals(mConfPass)) {
            mConfirmPass.setError(HRes.string(_ctx,R.string.error_password_mismatch));
            focusView = mConfirmPass;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        else {
            forgotPassword(mNewPass, mConfPass);
            //_nav.navigate(UserLoginActivity.class, true);
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
