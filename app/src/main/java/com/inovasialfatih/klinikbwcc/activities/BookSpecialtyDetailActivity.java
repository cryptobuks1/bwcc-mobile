package com.inovasialfatih.klinikbwcc.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceteck.silicompressorr.SiliCompressor;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.IdentityUploadResult;
import com.inovasialfatih.klinikbwcc.model.RequestBooking;
import com.inovasialfatih.klinikbwcc.model.RequestHistoryDetail;
import com.inovasialfatih.klinikbwcc.utils.Tools;
import com.inovasialfatih.klinikbwcc.utils.ViewAnimation;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inovasialfatih.klinikbwcc.utils.Tools.toggleArrow;

public class BookSpecialtyDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.result_layout) LinearLayout mResultLayout;
    @BindView(R.id.lyt_btn_choose_image) LinearLayout mButtonLayout;
    @BindView(R.id.lyt_instruction) LinearLayout lytInstruction;
    @BindView(R.id.lyt_checked) LinearLayout lytChecked;
    @BindView(R.id.ic_not_found) LinearLayout mNotFound;
    @BindView(R.id.lyt_photo) LinearLayout lytPhoto;
    @BindView(R.id.payment_notes) LinearLayout mPaymentNotes;
    @BindView(R.id.confirm_notes) LinearLayout mConfirmNotes;
    @BindView(R.id.alert_notes) LinearLayout mAlertNotes;
    @BindView(R.id.alert_notes_2) LinearLayout mAlertNotes2;

    @BindView(R.id.lyt_expand_instruction) CardView lytExpand;

    @BindView(R.id.payment_photo) ImageView photoPreview;
    @BindView(R.id.bt_toggle_arrow) ImageView toggleArrow;

    @BindView(R.id.patient_name) TextView txtPatientName;
    @BindView(R.id.register_date) TextView txtRegisterDate;
    @BindView(R.id.booking_date) TextView txtBookingDate;
    @BindView(R.id.poly) TextView txtPoly;
    @BindView(R.id.doctor) TextView txtDoctor;
    @BindView(R.id.time_start) TextView txtTimeStart;
    @BindView(R.id.time_finish) TextView txtTimeFinish;
    @BindView(R.id.queue_number) TextView txtQueueNumber;
    @BindView(R.id.status_approved) TextView txtStatus;

    @BindView(R.id.btn_upload) Button btnUpload;
    @BindView(R.id.btn_chose_image) Button btnChooseImage;
    @BindView(R.id.btn_edit_booking) Button btnEditBooking;
    @BindView(R.id.btn_cancel_booking) Button btnCancelBooking;

    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView;

    private String mBookingId = "";
    private boolean mFromRequest = false;

    private ArrayList<RequestHistoryDetail.RequestBookingData> reqStatus = new ArrayList<>();
    private ArrayList<String> photoPath = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_booking_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mBookingId = extras.getString("BOOKING_ID");
            mFromRequest = extras.getBoolean("FROM_REQUEST");
        }

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDetailHistory();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        initControl();
        requestDetailHistory();
    }

    private void initControl() {
        mToolbarTitle.setText("Booking Details");

        toggleArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lytExpand);
            }

        });
    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nestedScrollView, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public void pickPhoto(){
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(photoPath)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(true)
                .showGifs(false)
                .showFolderView(true)
                .pickPhoto(this);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (_ctx.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("SpecialtyDetailActivity","Permission is granted");
                return true;
            } else {
                Log.v("SpecialtyDetailActivity","Permission is revoked");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v("SpecialtyDetailActivity","Permission is granted");
            return true;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("SpecialtyDetailActivity","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            pickPhoto();
        } else {
            Toast.makeText(_ctx, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    photoPath = new ArrayList<>();
                    photoPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    if(photoPath.size() > 0)
                        for (int i = 0; i < photoPath.size(); i++) {
                            File file = new File(photoPath.get(0));
                            Glide.with(_ctx).load(file).into(photoPreview);
                            lytPhoto.setVisibility(View.VISIBLE);
                        }
                } break;
        }
    }

    private void requestDetailHistory(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Checking Data.");
        loader.show();

        Call<RequestHistoryDetail> call = _gate.requestBooking().requestHistoryDetail(
                _cooky.token(),
                mBookingId);

        call.enqueue(new Callback<RequestHistoryDetail>() {
            @Override
            public void onResponse(Call<RequestHistoryDetail> call, Response<RequestHistoryDetail> response) {
                RequestHistoryDetail result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            txtPatientName.setText(result.data.data.details.patient_name);
                            txtBookingDate.setText(result.data.data.details.date);
                            txtRegisterDate.setText(result.data.data.details.booking_date);
                            txtPoly.setText(result.data.data.details.poly_name);
                            txtDoctor.setText(result.data.data.details.doctor_name);
                            txtTimeStart.setText(result.data.data.details.time_start);
                            txtTimeFinish.setText(result.data.data.details.time_finish);
                            txtQueueNumber.setText(result.data.data.details.queue_number);

                            if(result.data.data.status.size() > 0)
                                txtStatus.setText(result.data.data.status.get(result.data.data.status.size() - 1).status);

                            // ketika upload payment
                            if (result.data.data.status.size() == 3){
                                mConfirmNotes.setVisibility(View.GONE);
                                mPaymentNotes.setVisibility(View.VISIBLE);
                                mButtonLayout.setVisibility(View.VISIBLE);
                                lytInstruction.setVisibility(View.VISIBLE);
                            }
                            // ketika nunggu checking payment
                            else if (result.data.data.status.size() == 5){
                                mConfirmNotes.setVisibility(View.GONE);
                                mPaymentNotes.setVisibility(View.GONE);
                                mButtonLayout.setVisibility(View.GONE);
                                lytInstruction.setVisibility(View.GONE);
                            }
                            // ketika status booking is succes
                            else if (result.data.data.status.size() == 6){
                                mConfirmNotes.setVisibility(View.GONE);
                                btnCancelBooking.setVisibility(View.VISIBLE);
                                mAlertNotes.setVisibility(View.VISIBLE);
                                mAlertNotes2.setVisibility(View.VISIBLE);
                            }
                            // ketika status schedule was cancel
                            else if (result.data.data.status.size() == 7){
                                mConfirmNotes.setVisibility(View.GONE);
                                btnCancelBooking.setVisibility(View.VISIBLE);
                                btnEditBooking.setVisibility(View.VISIBLE);
                            }
                            // ketika status schedule was cancel and user cancel
                            else if (result.data.data.status.size() == 8){
                                mConfirmNotes.setVisibility(View.GONE);
                                btnCancelBooking.setVisibility(View.GONE);
                                btnEditBooking.setVisibility(View.GONE);
                            }

                            RequestHistoryDetail.RequestBookingStatus lastStatus = result.data.data.status
                                    .get(result.data.data.status.size() - 1);

                            if (lastStatus.status.equals("PAYMENT NOT RECEIVED. BOOKING REJECT AUTOMATICALLY")){
                                mConfirmNotes.setVisibility(View.GONE);
                            }
                            if (lastStatus.status.equals("ADMIN REJECT BOOKING")){
                                mConfirmNotes.setVisibility(View.GONE);
                            }

                            mResultLayout.setVisibility(View.VISIBLE);
                            mNotFound.setVisibility(View.GONE);
                        } else {
                            mNotFound.setVisibility(View.VISIBLE);
                            Toasty.error(_ctx, HRes.string(_ctx, R.string.error_booking_detail_));
                        }
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<RequestHistoryDetail> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void doUpload(String path){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Uploading File.");
        loader.show();

        File destinationFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                _ctx.getPackageName() + "/src/uploads/transfer_receipt");
        String filePath = SiliCompressor.with(_ctx).compress(path, destinationFile);
        File file = new File(filePath);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );

        MultipartBody.Part body = MultipartBody.Part.createFormData("payment_receipt", file.getName(), requestFile);
        Call<IdentityUploadResult> call = _gate.payment().upload(
                _cooky.token(),
                RequestBody.create(okhttp3.MultipartBody.FORM, mBookingId),
                body );

        call.enqueue(new Callback<IdentityUploadResult>() {
            @Override
            public void onResponse(Call<IdentityUploadResult> call, Response<IdentityUploadResult> response) {
                IdentityUploadResult result = response.body();
                if (result != null) {
                    if (result.isStatus()) {
                        Toasty.success(_ctx, "Upload success");
                        _nav.navigateClearAll(HistorySpecialtyActivity.class, true);

                    } else {
                        Toasty.error(_ctx, result.data.message);
                    }
                } else {
                    Toasty.error(_ctx, getString(R.string.error_request_failed));
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<IdentityUploadResult> call, Throwable t) {
                Toasty.error(_ctx, getString(R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    public void cancelBooking() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Canceling Schedule.");
        loader.show();

        Call<RequestBooking> call = _gate.requestBooking().cancelBooking(
                _cooky.token(),
                mBookingId);

        call.enqueue(new Callback<RequestBooking>() {
            @Override
            public void onResponse(Call<RequestBooking> call, Response<RequestBooking> response) {
                RequestBooking result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        BookSpecialtyDetailActivity.super.onBackPressed();
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.success_cancel_booking));
                    } else {
                        Toasty.error(_ctx, HRes.string(_ctx, R.string.failed_cancel_booking));
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestBooking> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
            }
        });
    }

    @OnClick(R.id.btn_cancel_booking) void canceledBooking(){
        new android.support.v7.app.AlertDialog.Builder(_ctx)
                .setTitle(R.string.confirm_cancel_booking)
                .setMessage(R.string.confirm_cancel_booking_desc)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelBooking();


                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @OnClick(R.id.btn_edit_booking) void editBooking(){
        Bundle b = new Bundle();
        b.putString("BOOKING_ID", mBookingId);
        _nav.navigate(BookingOnlineActivity.class, b);
    }

    @OnClick(R.id.btn_chose_image) void chooseImage(){
        if(isStoragePermissionGranted())
            pickPhoto();
    }

    @OnClick(R.id.btn_upload) void doActionUpload(){
        if (photoPath.size() > 0) {
            doUpload(photoPath.get(0));
        } else {
            Toasty.info(_ctx, "Choose image first.");
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        if(mFromRequest)
            _nav.navigateClearAll(MainActivity.class, true);
        else
            super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(mFromRequest)
            _nav.navigateClearAll(MainActivity.class, true);
        else
            super.onBackPressed();
    }
}
