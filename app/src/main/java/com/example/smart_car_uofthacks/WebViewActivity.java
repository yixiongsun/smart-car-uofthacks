package com.example.smart_car_uofthacks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webview_id);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("http://www.google.com");
        String test = getIntent().getStringExtra("url");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String firstUrl = extras.getString("url");
            webView.loadUrl(firstUrl);
            //The key argument here must match that used in the other activity
        }
        final boolean firstRedirect = true;
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                if (url.contains("/login/exchange")) {
                    Log.d("redirect", url);
                    Intent intent = new Intent();
                    intent.putExtra("exchange", url);;
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    view.loadUrl(url);

                }
                return false; // then it is not handled by default action
            }
        });
    }
}
