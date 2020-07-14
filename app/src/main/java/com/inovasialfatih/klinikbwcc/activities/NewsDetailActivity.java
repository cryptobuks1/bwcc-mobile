package com.inovasialfatih.klinikbwcc.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.DetailNewsItem;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends BaseVideoActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.image) ImageView mImage;
    @BindView(R.id.news_video) YouTubePlayerView newsVideo;
    @BindView(R.id.news_title) TextView mNewsTitle;
    @BindView(R.id.news_category) TextView mCategory;
    @BindView(R.id.created_date) TextView mCreatedDate;
    @BindView(R.id.news_content) TextView mNewsContent;
    @BindView(R.id.news_layout) LinearLayout mNewsLayout;
    @BindView(R.id.ic_not_found) ImageView mNotFound;

    private String mTrxId = "";
    private boolean mFromRequest = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_news_detail);

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
                getDetailNews();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        initControl();
        getDetailNews();
    }

    private void initControl() {
        mToolbarTitle.setText("News Detail");
    }

    private void getDetailNews(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get News Detail.");
        loader.show();

        Call<DetailNewsItem> call = _gate.news().getDetailNews(mTrxId, _cooky.token());
        call.enqueue(new Callback<DetailNewsItem>() {
            @Override
            public void onResponse(Call<DetailNewsItem> call, Response<DetailNewsItem> response) {
                DetailNewsItem result = response.body();

                if(result != null) {
                    if(result.data.data != null) {
                        mNewsTitle.setText(result.data.data.title);
                        mCategory.setText(result.data.data.category);
                        mCreatedDate.setText(result.data.data.date_created);
                        mNewsContent.setText(result.data.data.content);
                        Glide.with(_ctx).load(result.data.data.image_url).into(mImage);

                        if(result.data.data.type.equals("video")){
                            newsVideo.setVisibility(View.VISIBLE);

                            Uri uri = Uri.parse(result.data.data.image_url);
                            final String vId = uri.getQueryParameter("v");
                            newsVideo.initialize("AIzaSyAXl0MN_qpUDWU7AHwi-36kRxga4Bmhrvs", new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    if(!b) {
                                        youTubePlayer.loadVideo(vId);
                                    }
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                        } else {
                            mImage.setVisibility(View.GONE);
                        }
                        mImage.setVisibility(View.VISIBLE);

                        mNewsLayout.setVisibility(View.VISIBLE);
                        mNotFound.setVisibility(View.GONE);

                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<DetailNewsItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
