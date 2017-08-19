package com.msgque.play.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msgque.play.R;
import com.msgque.play.common.constant.IntentConstant;
import com.msgque.play.dagger.component.InternetComponent;
import com.msgque.play.databinding.ActivityWebBinding;
import com.msgque.play.model.UserModel;

import java.util.Locale;

public class WebActivity extends NavBaseActivity {
  private int overrideCount = 0;
  private ActivityWebBinding binding;
  @Override
  protected void injectDependencies(InternetComponent component) {
    component.inject(this);
  }

  protected void openUrl(String url) {
    if (url == null || url.isEmpty()) return;
    UserModel user = spHelper.getCurrentUser();
    if (!url.startsWith("http")) {
      url = "http://" + url;
    }
    if (url.indexOf('?') == -1) url += "?";
    if (user != null) url += String.format(Locale.ENGLISH, "&email=%s", user.email);
    binding.webView.loadUrl(url);
    binding.progressBar.setProgress(0);
    binding.progressBar.setVisibility(View.VISIBLE);
  }

  public static Intent createIntent(Context context, String title, String url) {
    Intent i = new Intent(context, WebActivity.class);
    i.putExtra(IntentConstant.OPEN_URL, url);
    i.putExtra(IntentConstant.TITLE, title);
    return i;
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = setUpNewLayout(R.layout.activity_web);
    markRowSelected(getIntent().getStringExtra(IntentConstant.TITLE));

    binding.webView.getSettings().setBuiltInZoomControls(false);

    binding.webView.setWebViewClient(new CustomWebViewClient());
    binding.webView.setWebChromeClient(new CustomWebChromeClient());

    binding.progressBar.setMax(100);

    binding.webView.getSettings().setJavaScriptEnabled(true);

    openUrl(getIntent().getStringExtra(IntentConstant.OPEN_URL));
  }

  public void setValue(int progress) {
    binding.progressBar.setProgress(progress);
    if (progress == 100) binding.progressBar.setVisibility(View.GONE);
  }

  private class CustomWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      return handleUri(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      return handleUri(view, request.getUrl().toString());
    }

    private boolean handleUri(WebView view, String url) {
      if (url.startsWith("http")) {
        view.loadUrl(url);
      } else if (overrideCount == 0) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        finish();
        overrideCount++;
      }
      return false;
    }
  }

  private class CustomWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      WebActivity.this.setValue(newProgress);
      super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
//      if (getSupportActionBar() != null) {
//        getSupportActionBar().setTitle(title);
//      }
    }
  }

  @Override
  public void onBackPressed() {
    if (binding.webView.canGoBack()) {
      binding.webView.goBack();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        super.onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
