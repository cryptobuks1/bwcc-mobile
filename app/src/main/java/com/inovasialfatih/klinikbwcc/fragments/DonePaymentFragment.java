package com.inovasialfatih.klinikbwcc.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BookSpecialtyDetailActivity;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.HistoryItem;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonePaymentFragment extends BaseFragment {

    @BindView(R.id.main_list) RecyclerView rvMain;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<HistoryItem.HistoryItemList> childItems = new ArrayList<>();
    private OnFragmentInteractionListener mListener;


    public DonePaymentFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        DonePaymentFragment fragment = new DonePaymentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_done_payment, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_layout);
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

        getHistoryList();

        return root;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initList() {
        mAdapter = new RendererRecyclerViewAdapter();
        mAdapter.registerRenderer(new ViewBinder<>(R.layout.item_history_booking, HistoryItem.HistoryItemList.class, new ViewBinder.Binder<HistoryItem.HistoryItemList>() {
            @Override
            public void bindView(@NonNull final HistoryItem.HistoryItemList model, @NonNull ViewFinder finder, @NonNull List<Object> payloads) {
                finder.setText(R.id.txt_booking_date, model.created_date)
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

}
