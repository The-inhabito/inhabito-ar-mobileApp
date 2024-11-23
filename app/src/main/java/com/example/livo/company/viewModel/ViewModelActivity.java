package com.example.livo.company.viewModel;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.livo.R;

public class ViewModelActivity extends AppCompatActivity {
    private WebView modelWebView;
    private String modelUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        modelUrl = getIntent().getStringExtra("modelUrl");
        if (modelUrl == null || modelUrl.isEmpty()) {
            Log.e("ViewModelActivity", "Model URL is null or empty!");
        } else {
            Log.d("ViewModelActivity", "Model URL: " + modelUrl);
        }

        modelWebView = findViewById(R.id.modelWebView);
        WebSettings webSettings = modelWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        modelWebView.setWebViewClient(new WebViewClient());
        modelWebView.loadUrl("file:///android_asset/view.html");

        modelWebView.addJavascriptInterface(new Object() {
            @android.webkit.JavascriptInterface
            public String getModelUrl() {
                return modelUrl;
            }
        }, "Android");
    }
}
