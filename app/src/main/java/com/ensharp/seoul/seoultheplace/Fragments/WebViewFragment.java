package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ensharp.seoul.seoultheplace.R;

@SuppressLint("ValidFragment")
public class WebViewFragment extends Fragment{

    private String link;
    private Dialog webDialog;
    private WebView webView;

    @SuppressLint("ValidFragment")
    public WebViewFragment(String link) {
        this.link = link;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        webView = (WebView) rootView.findViewById(R.id.website_layout);
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollbarFadingEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);

        return rootView;
    }
}
