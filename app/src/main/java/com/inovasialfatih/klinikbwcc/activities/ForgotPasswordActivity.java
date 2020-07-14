package com.inovasialfatih.klinikbwcc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.ForgotPasswordResponse;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.txt_email) EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_forgot_password);
    }

    private void forgotPassword(final String email){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Reseting password.");
        loader.show();

        Call<ForgotPasswordResponse> call = _gate.user().forgotPassword(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN",
                email);
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                ForgotPasswordResponse result = response.body();

                if(result != null) {
                    if(result.status.equalsIgnoreCase("success") && result.data != null && result.data.data != null) {
                        Toasty.success(_ctx,result.data.message + "Please check your email.");
                    } else {
                        Toasty.error(_ctx, result.data.message);
                    }

                } else {
                    Toasty.error(_ctx, "An error occurred creating a password");
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_submit) void submit(){
        mEmail.setError(null);

        String email = mEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(email)) {
            mEmail.setError(HRes.string(_ctx, R.string.error_email_empty));
            focusView = mEmail;
            cancel = true;
        } if (cancel) {
            focusView.requestFocus();
        } else {
            forgotPassword(email);
            //_nav.navigate(UserLoginActivity.class, true);
        }

    }
}
