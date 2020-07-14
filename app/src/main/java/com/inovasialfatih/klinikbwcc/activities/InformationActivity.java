package com.inovasialfatih.klinikbwcc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.ContactUsResponse;
import com.inovasialfatih.klinikbwcc.model.DoctorScheduleResponse;
import com.inovasialfatih.klinikbwcc.model.PatientDetailItem;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;
import com.inovasialfatih.klinikbwcc.utils.Tools;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.main_list) RecyclerView rvMain;

    @BindView(R.id.address_bwcc) TextView mAddress;
    @BindView(R.id.email_bwcc) TextView mEmail;
    @BindView(R.id.whatsapp_bwcc) TextView mWhatsApp;
    @BindView(R.id.facebook_bwcc) TextView mFacebook;
    @BindView(R.id.youtube_bwcc) TextView mYoutube;
    @BindView(R.id.instagram_bwcc) TextView mInstagram;
    @BindView(R.id.twitter_bwcc) TextView mTwitter;

    @BindView(R.id.email_address) LinearLayout mEmailAddress;
    @BindView(R.id.open_whatsapp) LinearLayout mOpenWhatsapp;
    @BindView(R.id.url_facebook) LinearLayout mUrlFacebook;
    @BindView(R.id.url_instagram) LinearLayout mUrlInstagram;
    @BindView(R.id.url_youtube) LinearLayout mUrlYoutube;
    @BindView(R.id.url_twitter) LinearLayout mUrlTwitter;

    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<ContactUsResponse.PhoneNumber> childItems = new ArrayList<>();
    private ContactUsResponse.ContactUsData contactData = null;

    private String mTrxId = "";
    private boolean mFromRequest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_information);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTrxId = extras.getString("TRX_ID");
            mFromRequest = extras.getBoolean("FROM_REQUEST");
        }

        initControl();
        getContactUs();
        // showPhoneNumber();
    }

    private void initControl() {
        mToolbarTitle.setText("Contact Us");
    }

    private void initList(){
        mAdapter = new RendererRecyclerViewAdapter();
        mAdapter.registerRenderer(new ViewBinder<>(R.layout.item_phone_number, ContactUsResponse.PhoneNumber.class, new ViewBinder.Binder<ContactUsResponse.PhoneNumber>() {
            @Override
            public void bindView(@NonNull final ContactUsResponse.PhoneNumber model, @NonNull ViewFinder finder, @NonNull List<Object> payloads) {
                finder.setText(R.id.numberPhone, model.number)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle b = new Bundle();
                                b.putString("TRX_ID", model.number);
                                b.putBoolean("FROM_REQUEST", false);
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + model.number));
                                startActivity(callIntent);
                            }
                        });
            }
        }));

        mAdapter.setItems(childItems);
        rvMain.setLayoutManager(new LinearLayoutManager(_ctx));
        rvMain.setAdapter(mAdapter);
    }

    private void getContactUs() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get data.");
        loader.show();

        Call<ContactUsResponse> call = _gate.contactUs().getContact(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN");
        call.enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                final ContactUsResponse result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                            mAddress.setText(result.data.alamat_klinik);
                            mEmail.setText(result.data.email);
                            mWhatsApp.setText(result.data.whatsapp_number);
                            mFacebook.setText(result.data.facebook_name);
                            mYoutube.setText(result.data.youtube_name);
                            mInstagram.setText(result.data.instagram_name);
                            mTwitter.setText(result.data.twitter_name);

                            if (result.data.phone_number != null){
                                childItems.addAll(result.data.phone_number);
                                initList();
                            }

                        mEmailAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{result.data.email});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                emailIntent.putExtra(Intent.EXTRA_TEXT,"");
                                startActivity(Intent.createChooser(emailIntent, "Open with"));
                            }
                        });

                        mOpenWhatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               String whatsapp = mWhatsApp.getText().toString().substring(1);
                               openWhatsApp("+62" + whatsapp);
                            }
                        });

                        mUrlFacebook.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Tools.openInAppBrowser((Activity) _ctx, result.data.facebook, false);
                            }
                        });

                        mUrlInstagram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Tools.openInAppBrowser((Activity) _ctx, result.data.instagram, false);
                            }
                        });

                        mUrlYoutube.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Tools.openInAppBrowser((Activity) _ctx, result.data.youtube, false);
                            }
                        });

                        mUrlTwitter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Tools.openInAppBrowser((Activity) _ctx, result.data.twitter, false);
                            }
                        });

                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void openWhatsApp(String number) {
        String url = "https://api.whatsapp.com/send?phone=" + number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.url_address) void doActionAddressMaps(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<-6.282192>,<106.7108753>?q=Bintaro Women And Children Clinic"));
        _ctx.startActivity(intent);
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
