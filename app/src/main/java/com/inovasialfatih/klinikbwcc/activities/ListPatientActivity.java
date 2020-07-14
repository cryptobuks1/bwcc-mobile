package com.inovasialfatih.klinikbwcc.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;
import com.inovasialfatih.klinikbwcc.utils.ViewAnimation;
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

public class ListPatientActivity extends BaseActivity {

    private View back_drop;
    // private View lyt_old_patient;
    private View lyt_new_patient;
    private boolean rotate = false;

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.toolbar_back) ImageView mToolbarBack;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.main_list) RecyclerView rvMain;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    @BindView(R.id.search_action) EditText mActionSearch;
    @BindView(R.id.search_back) ImageView mSearchBack;
    @BindView(R.id.nav_search) ImageView navSearch;

    //@BindView(R.id.image) CircularImageView mImage;

    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<PatientListItem.PatientDetail> childItems = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    private String mTrxId = "";
    private boolean mFromRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_list_patient);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTrxId = extras.getString("TRX_ID");
            mFromRequest = extras.getBoolean("FROM_REQUEST");
        }

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                childItems.clear();
                //mAdapter.notifyDataSetChanged();
                getPatientList();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        mActionSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filterPatient(s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPatient(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterPatient(s.toString());

            }
        });

        //getPatientList();
        initControl();
    }

    private void initControl(){
        mToolbarTitle.setText("Patients");
        back_drop = findViewById(R.id.back_drop);

        // final FloatingActionButton fab_old_patient = (FloatingActionButton) findViewById(R.id.fab_old_patient);
        final FloatingActionButton fab_new_patient = (FloatingActionButton) findViewById(R.id.fab_new_patient);
        final FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        // lyt_old_patient = findViewById(R.id.lyt_old_patient);
        lyt_new_patient = findViewById(R.id.lyt_new_patient);
        // ViewAnimation.initShowOut(lyt_old_patient);
        ViewAnimation.initShowOut(lyt_new_patient);
        back_drop.setVisibility(View.GONE);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

//        fab_old_patient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _nav.navigate(AddOldPatientActivity.class);
//
//                rotate = ViewAnimation.rotateFab(fab_add, !rotate);
//                ViewAnimation.showOut(lyt_old_patient);
//                ViewAnimation.showOut(lyt_new_patient);
//                back_drop.setVisibility(View.GONE);
//            }
//        });

        fab_new_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _nav.navigate(AddNewPatientActivity.class);

                rotate = ViewAnimation.rotateFab(fab_add, !rotate);
                // ViewAnimation.showOut(lyt_old_patient);
                ViewAnimation.showOut(lyt_new_patient);
                back_drop.setVisibility(View.GONE);


            }
        });

    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            // ViewAnimation.showIn(lyt_old_patient);
            ViewAnimation.showIn(lyt_new_patient);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            // ViewAnimation.showOut(lyt_old_patient);
            ViewAnimation.showOut(lyt_new_patient);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void filterPatient(String keyword){

        ArrayList<PatientListItem.PatientDetail> filteredList = new ArrayList<>();
        for(int i = 0; i < childItems.size(); i++) {
            PatientListItem.PatientDetail pasien = childItems.get(i);
            if(pasien.nama.toLowerCase().contains(keyword) || pasien.pasien_id.equals(keyword))
                filteredList.add(pasien);
        }

        mAdapter.setItems(new ArrayList<PatientListItem.PatientDetail>());
        mAdapter.notifyDataSetChanged();
        mAdapter.setItems(filteredList);
        mAdapter.notifyDataSetChanged();
    }

    private void initList() {
        mAdapter = new RendererRecyclerViewAdapter();
        mAdapter.registerRenderer(new ViewBinder<>(R.layout.item_list_patient, PatientListItem.PatientDetail.class, new ViewBinder.Binder<PatientListItem.PatientDetail>() {
            @Override
            public void bindView(@NonNull final PatientListItem.PatientDetail model, @NonNull ViewFinder finder, @NonNull List<Object> payloads) {
                CircularImageView imgPatient = finder.find(R.id.image_patient);

                if (model.jenis_kelamin.equals("LK")) {
                    finder.setText(R.id.patient_gender, "MAN");
                    Glide.with(_ctx).load(R.drawable.man_patient).into(imgPatient);
                } else if (model.jenis_kelamin.equals("PR")){
                    finder.setText(R.id.patient_gender, "WOMAN");
                    Glide.with(_ctx).load(R.drawable.woman_patient).into(imgPatient);
                }

                finder.setText(R.id.patient_name, model.nama)
                        .setText(R.id.patient_dob, model.tanggal_lahir)
                        .setText(R.id.patient_id, model.pasien_id)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle b = new Bundle();
                                b.putString("TRX_ID", model.pasien_id);
                                b.putBoolean("FROM_REQUEST", false   );
                                _nav.navigate(PatientDetailActivity.class, b);
                            }
                        });
            }
        }));

        mAdapter.setItems(childItems);
        rvMain.setLayoutManager(new LinearLayoutManager(_ctx));
        rvMain.setAdapter(mAdapter);
    }

    private void getPatientList(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get Patient Data.");
        loader.show();

        Call<PatientListItem> call = _gate.patient().getPatientList(
                _cooky.token());
        call.enqueue(new Callback<PatientListItem>() {
            @Override
            public void onResponse(Call<PatientListItem> call, Response<PatientListItem> response) {
                PatientListItem result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            childItems.addAll(result.data);
                            initList();

                            mNotFound.setVisibility(View.GONE);
                        }
                    } else {
                        mNotFound.setVisibility(View.VISIBLE);
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<PatientListItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.nav_search) void search(){
        mToolbarBack.setVisibility(View.GONE);
        mToolbarTitle.setVisibility(View.GONE);
        navSearch.setVisibility(View.GONE);
        mActionSearch.setVisibility(View.VISIBLE);
        mSearchBack.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.search_back) void searchBack(){
        mToolbarBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        navSearch.setVisibility(View.VISIBLE);
        mActionSearch.setVisibility(View.GONE);
        mSearchBack.setVisibility(View.GONE);
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

    @Override
    public void onResume(){
        super.onResume();
        childItems = new ArrayList<>();
        if (mAdapter != null){
            mAdapter.setItems(childItems);
            mAdapter.notifyDataSetChanged();
        }

        getPatientList();
    }
}
