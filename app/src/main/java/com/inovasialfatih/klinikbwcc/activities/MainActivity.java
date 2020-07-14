package com.inovasialfatih.klinikbwcc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.ImageSlider;
import com.inovasialfatih.klinikbwcc.model.OpResult;
import com.inovasialfatih.klinikbwcc.services.Gate;
import com.inovasialfatih.klinikbwcc.utils.Tools;
import com.inovasialfatih.klinikbwcc.views.Toasty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    NavigationView mNavigationView;
    View mHeaderView;

    TextView userName;

    private ActionBar actionBar;
    private Toolbar toolbar;

    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();

    private ArrayList<ImageSlider.ImageSliderData> childItems = new ArrayList<>();

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.layout_dots) LinearLayout layout_dots;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.btn_online_register) CardView btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_main);

        initToolbar();
        initNavigationMenu();
        getFcmToken();
        getSliderBanner();
        initControl();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Bintaro Women and Children Clinic");
        Tools.setSystemBarColor(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_profil:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.nav_rate_app:
                        try {
                            Intent viewIntent =
                                    new Intent("android.intent.action.VIEW",
                                            Uri.parse("https://play.google.com/store/apps/details?id=com.inovasialfatih.klinikbwcc"));
                            startActivity(viewIntent);
                        }catch(Exception e) {
                            Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        break;
                    case R.id.nav_about:
                        Intent about = new Intent(MainActivity.this, AboutAppsActivity.class);
                        startActivity(about);
                        break;
                }
                drawer.closeDrawers();
                return true;
            }

        });
    }

    public void initControl() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView =  mNavigationView.getHeaderView(0);

        userName = (TextView) mHeaderView.findViewById(R.id.user_name);
        // userEmail= (TextView) mHeaderView.findViewById(R.id.user_email);

        // Set username & email
        userName.setText(_cooky.getString(Cooky.NAME));
        // userEmail.setText(_cooky.getString(Cooky.EMAIL));
    }

    private void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token : ", "getInstanceId failed", task.getException());
                            return;
                        }
                        final String token = task.getResult().getToken();
                        Log.d("Token : ", token);

                        if(token != null && !_cooky.getString(Cooky.FCM_TOKEN).equals(token)) {
                            Call<OpResult> call = new Gate(getApplicationContext()).user().updateTokenNotif(_cooky.token(), token);
                            call.enqueue(new Callback<OpResult>() {
                                @Override
                                public void onResponse(Call<OpResult> call, Response<OpResult> response) {
                                    OpResult result = response.body();
                                    if (result != null && result.isStatus()) {
                                        _cooky.addString(Cooky.FCM_TOKEN, token);
                                    }
                                }

                                @Override
                                public void onFailure(Call<OpResult> call, Throwable t) { }
                            });
                        }
                    }
                });
    }

    private void getSliderBanner(){
        Call<ImageSlider> call = _gate.sliderBanner().getSlider(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN");
        call.enqueue(new Callback<ImageSlider>() {
            @Override
            public void onResponse(Call<ImageSlider> call, Response<ImageSlider> response) {
                ImageSlider result = response.body();

                if(result != null) {
                    if(result.isStatus()){
                        childItems.addAll(result.data);
                        initSliderComponent();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageSlider> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
            }
        });
    }

    private void initSliderComponent() {

        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<ImageSlider.ImageSliderData>());

        adapterImageSlider.setItems(childItems);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(_ctx);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<ImageSlider.ImageSliderData> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, ImageSlider.ImageSliderData obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<ImageSlider.ImageSliderData> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public ImageSlider.ImageSliderData getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<ImageSlider.ImageSliderData> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageSlider.ImageSliderData o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            //Tools.displayImageOriginal(act, image, o);
            Glide.with(act).load(o.image_url)
                    .into(image);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }
    @OnClick(R.id.btn_add_patient) void doActionListPatient() {
        _nav.navigate(ListPatientActivity.class);
    }

    @OnClick(R.id.btn_online_register) void doActionBookingOnline() {
        _nav.navigate(BookingOnlineActivity.class);
    }

    @OnClick(R.id.btn_history) void doActionHistory() {
        _nav.navigate(HistoryActivity.class);
    }

    @OnClick(R.id.btn_doctor_schedule) void doActionDoctorSchedule() {
        _nav.navigate(DoctorsScheduleActivity.class);
    }

    @OnClick(R.id.btn_informasi) void doActionInformation() {
        _nav.navigate(InformationActivity.class);
    }

    @OnClick(R.id.btn_news) void doActionNews() {
        _nav.navigate(NewsActivity.class);
    }

   @OnClick(R.id.btn_education_class) void doActionEdClass(){
        _nav.navigate(EducationClassActivity.class);
   }

    @OnClick(R.id.btn_logout) void logout() {
        new AlertDialog.Builder(_ctx)
                .setTitle(R.string.confirm_logout_title)
                .setMessage(R.string.confirm_logout_desc)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _cooky.clearAll();
                        _nav.navigateClearAll(UserLoginActivity.class, true);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}
