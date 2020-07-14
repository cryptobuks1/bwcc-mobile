package com.inovasialfatih.klinikbwcc.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.MainActivity;
import com.inovasialfatih.klinikbwcc.utils.Tools;

public class ActivityWebView extends AppCompatActivity {

    private static final String EXTRA_OBJC = "key.EXTRA_OBJC";
    private static final String EXTRA_FROM_NOTIF = "key.EXTRA_FROM_NOTIF";

    public static void navigate(Activity activity, String url, boolean from_notif) {
        Intent i = navigateBase(activity, url, from_notif);
        activity.startActivity(i);
    }

    public static Intent navigateBase(Context context, String url, boolean from_notif) {
        Intent intent = new Intent(context, ActivityWebView.class);
        intent.putExtra(EXTRA_OBJC, url);
        intent.putExtra(EXTRA_FROM_NOTIF, from_notif);
        return intent;
    }

    private Toolbar toolbar;
    private ActionBar actionBar;

    private WebView webView;
    private String url;
    private View parent_view;
    private Boolean from_notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        parent_view = findViewById(android.R.id.content);

        webView = (WebView) findViewById(R.id.webView);

        // get extra object
        url = getIntent().getStringExtra(EXTRA_OBJC);
        from_notif = getIntent().getBooleanExtra(EXTRA_FROM_NOTIF, false);

        initToolbar();
        loadWebFromUrl();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Web View");
        Tools.changeOverflowMenuIconColor(toolbar, getResources().getColor(R.color.colorPrimary));
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private void loadWebFromUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings();
        webView.getSettings().setBuiltInZoomControls(false);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                actionBar.setTitle(getString(R.string.webview_loading) + progress + " %");
                if (progress == 100) {
                    actionBar.setTitle("Web View");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_refresh) {
            loadWebFromUrl();
        } else if (item.getItemId() == R.id.action_browser) {
            Tools.directLinkToBrowser(this, url);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_webview, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.colorPrimary));
        return true;
    }

    @Override
    public void onBackPressed() {
        if (from_notif) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }
}
