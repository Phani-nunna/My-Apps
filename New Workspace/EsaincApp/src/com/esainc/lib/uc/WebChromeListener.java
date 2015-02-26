package com.esainc.lib.uc;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebChromeListener extends WebChromeClient {
	private Fragment mFragment;
	
	public WebChromeListener(Fragment fragment) {
		mFragment = fragment;
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
		WebView newWebView = new WebView(view.getContext());
		newWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				mFragment.startActivity(browserIntent);
				return true;
			}
		});
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		transport.setWebView(newWebView);
		resultMsg.sendToTarget();
		return true;
	}

}
