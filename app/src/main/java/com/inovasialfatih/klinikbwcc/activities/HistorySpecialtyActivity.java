package com.inovasialfatih.klinikbwcc.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.HistoryItem;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorySpecialtyActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.main_list) RecyclerView rvMain;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    private String mTrxId = "";
    private boolean mFromRequest = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<HistoryItem.HistoryItemList> childItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_history_specialty);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                childItems.clear();
                //mAdapter.notifyDataSetChanged();
                getHistoryList();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        initControl();
//        getHistoryList();
    }

    private void initControl(){
        mToolbarTitle.setText("Booking History");
    }

    private void initList() {
        mAdapter = new RendererRecyclerViewAdapter();
        mAdapter.registerRenderer(new ViewBinder<>(R.layout.item_history_booking, HistoryItem.HistoryItemList.class, new ViewBinder.Binder<HistoryItem.HistoryItemList>() {
            @Override
            public void bindView(@NonNull final HistoryItem.HistoryItemList model, @NonNull ViewFinder finder, @NonNull List<Object> payloads) {

                finder.setText(R.id.txt_register_date, model.created_date)
                        .setText(R.id.txt_name, model.patient_name)
                        .setText(R.id.txt_booking_date, model.date)
                        .setText(R.id.txt_time_start, model.time_start)
                        .setText(R.id.txt_time_finish, model.time_finish)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle b = new Bundle();
                                b.putString("BOOKING_ID", model.booking_id);
                                b.putBoolean("FROM_REQUEST", false   );
                                _nav.navigate(BookSpecialtyDetailActivity.class, b);
                            }
                        });
            }
        }));

        mAdapter.setItems(childItems);
        rvMain.setLayoutManager(new LinearLayoutManager(_ctx));
        rvMain.setAdapter(mAdapter);
    }

    private void getHistoryList(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get History Data.");
        loader.show();

        Call<HistoryItem> call = _gate.history().getHistoryList(
                _cooky.token());
        call.enqueue(new Callback<HistoryItem>() {
            @Override
            public void onResponse(Call<HistoryItem> call, Response<HistoryItem> response) {
                HistoryItem result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            childItems.addAll(result.data.status);
                            initList();

                            mNotFound.setVisibility(View.GONE);
                        } else {
                            mNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<HistoryItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
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

        getHistoryList();
    }

}
