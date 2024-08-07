package com.tutorconnect.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PdfViewActivity extends AppCompatActivity {
    private static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    private WebView webView;

    public static void start(Context context, String fileUrl) {
        Intent intent = new Intent(context, PdfViewActivity.class);
        intent.putExtra(EXTRA_FILE_URL, fileUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript for Google Docs Viewer
        webView.setWebViewClient(new WebViewClient());

        String fileUrl = getIntent().getStringExtra(EXTRA_FILE_URL);
        if (fileUrl != null && !fileUrl.isEmpty()) {
            loadPdfInWebView(fileUrl);
        } else {
            Toast.makeText(this, "Invalid file URL.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadPdfInWebView(String fileUrl) {
        // Construct the URL for Google Docs Viewer
        String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + Uri.encode(fileUrl);

        // Load the URL into the WebView
        webView.setVisibility(WebView.VISIBLE);
        webView.loadUrl(googleDocsUrl);
    }
}
