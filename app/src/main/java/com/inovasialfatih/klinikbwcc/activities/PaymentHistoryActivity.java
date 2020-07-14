package com.inovasialfatih.klinikbwcc.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.inovasialfatih.klinikbwcc.fragments.DonePaymentFragment;
import com.inovasialfatih.klinikbwcc.fragments.PendingPaymentFragment;
import com.inovasialfatih.klinikbwcc.R;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import droidninja.filepicker.adapters.SectionsPagerAdapter;

public class PaymentHistoryActivity extends BaseActivity {

    private ViewPager view_pager;
    private TabLayout tab_layout;

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_payment_history);

        initControl();
        initComponent();
    }

    private void initControl() {
        mToolbarTitle.setText("Payment History");
    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PendingPaymentFragment.newInstance(), "PENDING");
        adapter.addFragment(DonePaymentFragment.newInstance(), "DONE");
        viewPager.setAdapter(adapter);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
