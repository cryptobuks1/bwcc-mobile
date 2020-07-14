package com.inovasialfatih.klinikbwcc.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder;
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.NewsItem;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.main_list) RecyclerView rvMain;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<NewsItem.NewsResult> childItems = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_news);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                childItems.clear();
                //mAdapter.notifyDataSetChanged();
                getNews();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        getNews();
        initControl();
    }

    private void initControl() {
        mToolbarTitle.setText("News");
    }

    private void initList() {
        mAdapter = new RendererRecyclerViewAdapter();
        mAdapter.registerRenderer(new ViewBinder<>(R.layout.item_news, NewsItem.NewsResult.class, new ViewBinder.Binder<NewsItem.NewsResult>() {
            @Override
            public void bindView(@NonNull final NewsItem.NewsResult model, @NonNull ViewFinder finder, @NonNull List<Object> payloads) {
//                CircularImageView newsImage = finder.find(R.id.image);
//                Glide.with(_ctx).load(model.image_url).into(newsImage);
//
//                if (model.type.equals("video")){
//                    Picasso.with(_ctx)
//                            .load(model.image_url)
//                            .into(newsImage);
//                }
                finder.setText(R.id.news_title, model.title)
                        .setText(R.id.news_category, model.category)
                        .setText(R.id.sort_content, model.short_content)
                        .setText(R.id.created_date, model.date_created)
                        .setOnClickListener(new View.OnClickListener() {
                            @Optional
                            public void onClick(View v) {
                                Bundle b = new Bundle();
                                b.putString("TRX_ID", model.news_id);
                                b.putBoolean("FROM_REQUEST", false   );
                                _nav.navigate(NewsDetailActivity.class, b);
                            }
                        });

            }
        })) ;

        mAdapter.setItems(childItems);
        rvMain.setLayoutManager(new LinearLayoutManager(_ctx));
        rvMain.setAdapter(mAdapter);
    }

    private void getNews() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get News Data.");
        loader.show();

        Call<NewsItem> call = _gate.news().getNews(_cooky.token());
        call.enqueue(new Callback<NewsItem>() {
            @Override
            public void onResponse(Call<NewsItem> call, Response<NewsItem> response) {
                NewsItem result = response.body();

                if(result != null) {
                    if (result.status.equals("Success")){
                        if(result.data.data != null) {
                            childItems.addAll(result.data.data);
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
            public void onFailure(Call<NewsItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
