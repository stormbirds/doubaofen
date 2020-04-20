package com.tiangou.douxiaomi;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebActivity extends Activity implements View.OnClickListener {
    public Boolean isOnPause = false;
    ImageView ivTitleLeft;
    ProgressBar progressBar;
    String targetUrl;
    String title;
    TextView tvTitle;
    WebView webView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web);
        this.ivTitleLeft = (ImageView) findViewById(R.id.iv_title_left);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar);
        this.webView = (WebView) findViewById(R.id.webView);
        this.targetUrl = getIntent().getStringExtra("url");
        this.title = getIntent().getStringExtra("title");
        setListener();
        setOther();
    }

    private void setOther() {
        this.tvTitle.setText(this.title);
        this.webView.loadUrl(this.targetUrl);
    }

    private void setListener() {
        this.ivTitleLeft.setOnClickListener(this);
        this.progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_states));
        boolean booleanExtra = getIntent().getBooleanExtra("supportZoom", false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDefaultTextEncodingName("utf-8");
        this.webView.getSettings().setSupportZoom(booleanExtra);
        this.webView.getSettings().setBuiltInZoomControls(booleanExtra);
        this.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.webView.getSettings().setGeolocationEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    WebActivity.this.progressBar.setVisibility(8);
                } else {
                    if (8 == WebActivity.this.progressBar.getVisibility()) {
                        WebActivity.this.progressBar.setVisibility(0);
                    }
                    WebActivity.this.progressBar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }
        });
    }

    public void onClick(View view) {
        if (this.webView.canGoBack()) {
            solvebBack();
        } else {
            finish();
        }
    }

    public void solvebBack() {
        this.webView.goBack();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        try {
            if (this.webView != null) {
                this.webView.getClass().getMethod("onPause", new Class[0]).invoke(this.webView, (Object[]) null);
                this.isOnPause = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        try {
            if (this.isOnPause.booleanValue()) {
                if (this.webView != null) {
                    this.webView.getClass().getMethod("onResume", new Class[0]).invoke(this.webView, (Object[]) null);
                }
                this.isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 || !this.webView.canGoBack()) {
            return super.onKeyDown(i, keyEvent);
        }
        solvebBack();
        return true;
    }
}
