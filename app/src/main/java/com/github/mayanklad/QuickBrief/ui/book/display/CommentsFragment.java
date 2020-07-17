package com.github.mayanklad.QuickBrief.ui.book.display;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mayanklad.QuickBrief.R;

public class CommentsFragment extends Fragment {

    private String comments_url;

    WebView comments = null;

    public CommentsFragment(String comments_url) {
        this.comments_url = comments_url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        comments = (WebView) view.findViewById(R.id.comments);
        /*
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        */

        comments.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ("www.goodreads.com".equals(Uri.parse(url).getHost())) {
                    // This is my website, so do not override; let my WebView load the page
                    return false;
                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        comments.loadUrl(comments_url);

        return view;
    }

    public boolean canGoBack() {
        return comments.canGoBack();
    }

    public void goBack() {
        comments.goBack();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }*/
}
