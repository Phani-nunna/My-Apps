package com.esainc.lib.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ArticleFragment extends SherlockFragment {
	private static final String LOG_TAG = "Article Fragment";
	private static final int FONT_INITIAL_COND = 16;
	private static final int FONT_MAX_SIZE = 22;
	private static final int FONT_SIZE_INCREASE_BY = 1;
	
	private ShareActionProvider mShareActionProvider;
	private Intent shareIntent;
	private WebView webDetailView;
	private ImageView articleImage;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private int fontSize;
	private int mPos;
	private boolean mTeams;
	private JSONArray mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPos = getArguments().getInt("pos");
		mTeams = getArguments().getBoolean("teams");
		try {
			mData = new JSONArray(getArguments().getString("data"));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error loading article data", e);
		}
		
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setHasOptionsMenu(true);
		fontSize = FONT_INITIAL_COND;
		shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.article, menu);
		
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
		mShareActionProvider.setShareIntent(shareIntent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_share) {
			startActivity(Intent.createChooser(shareIntent, "Share via"));
			return true;
		} else if (itemId == R.id.action_font) {
			fontSize = (fontSize + FONT_SIZE_INCREASE_BY <= FONT_MAX_SIZE) ? fontSize + FONT_SIZE_INCREASE_BY : FONT_INITIAL_COND;
			webDetailView.getSettings().setDefaultFontSize(fontSize);
			return true;
		} else if (itemId == android.R.id.home) {
			getSherlockActivity().getSupportFragmentManager().popBackStack();
			return true;
		}
		return (super.onOptionsItemSelected(item));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_headlines_detail_view, container, false);
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "InlinedApi" })
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		try {
			JSONObject hdata = mData.getJSONObject(0);
			
			// Set article image
			String photoURL = hdata.getString(getResources().getString(R.string.tag_photoURL)).replaceAll(" ", "%20");
			
			// Setup HTML for article
			String data = null;
			StringBuffer dataBuffer = new StringBuffer();
			
			// Start HTML and Head
			dataBuffer.append("<html>" +
					"<head>" +
						"<style type\"text/css\">" +
							"* {" +
							"} " +
							"hr { " +
								"border: none; " +
								"height: 1.5px; " +
								"color: #D9D9D9; " +
								"background-color: #D9D9D9; " +
							"} " +
							"p { " +
								"color:black; " +
							"} " +
							"a { " +
								"color:" + getResources().getString(R.color.HyperlinksColor).substring(3) + "; " +
							"} " +
							".right { " +
								"float:right; " +
								"width:130px; " +
							"} " +
							".newsPhoto { " +
								"text-align:center; " +
								"padding-bottom:10px;" +
							"} " +
							".newsPhoto img { " +
								"max-width:100%; " +
								"border:2px solid #" + getResources().getString(R.color.Border).substring(3) + "; " +
							"} " +
							".title { " +
								"font-size:1.2em; " +
							"} " +
							".category { " +
								"font-size:1.0em; " +
							"} " +
							".time { " +
								"font-size:1.0em; " +
							"} " +
							".gallery { " +
								"text-decoration:none; " +
								"position:relative; " +
								"text-align:right; " +
								"padding:5px 10px; " +
								"border-style:solid; " +
								"-webkit-border-radius:4px; " +
								"background-color:" + getResources().getString(R.color.HyperlinksColor).substring(3) +"; " +
								"-moz-border-radius:4px; " +
								"border-radius: 4px; " +
								"border-color:#ffffff; " +
								"border-width: 1px; " +
								"font:12px Arial, Helvetica, sans-serif; " +
								"color:#ffffff; " +
								"inset 0px 0px 1px #ffffff; " +
							"} " +
							".video { " +
								"position:relative; " +
								"height: 360px; " +
								"width: 480px" +
							"} " +
							".video a { " +
								"position:absolute; display: " +
								"block; background-image: url(https://lh6.ggpht.com/NrQdFAdPSI9-hreB4C7HNhj3yXRiW1jqOOi7eFyakIx_IA-Im0huIeYCs5jTidMT2qA=w70); " +
								"background-repeat: no-repeat; " +
								"background-position: center; " +
								"height: 360px; " +
								"width: 480px; " +
							"}" +
						"</style>" +
						"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\">" +
					"</head><body>");
			
			// News Photo
			if (!TextUtils.isEmpty(photoURL)) {
				articleImage = (ImageView) getActivity().findViewById(R.id.articleImage);
				if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
					imageLoader.displayImage(photoURL, articleImage, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							if (loadedImage != null) {
								view.setBackgroundResource(R.drawable.image_stroke);
							} else {
								view.setBackgroundResource(0);
							}
						}
					});
				} else { // Honeycomb and up don't need height and width set in html
					articleImage.setVisibility(View.GONE);
					dataBuffer.append("<div class=\"newsPhoto\"><img alt=\"\" src=\"" + photoURL + "\"></div>");
				}
			}
			
			// Title and category
			dataBuffer.append("<div class=\"title\"><strong>" + hdata.getString(getResources().getString(R.string.tag_title)) + "</strong></div><div class=\"category\">" + hdata.getString(getResources().getString(R.string.tag_category)) + "</div>");
			
			// Gallery
			if (!hdata.getString(getResources().getString(R.string.tag_galleryID)).equals("0")) {
				dataBuffer.append("<div class=\"right\"><a href=\"gallery:" + hdata.getString(getResources().getString(R.string.tag_galleryID)) + "\" class=\"gallery\"/>Photo Gallery</a></div>");
			}
			
			// Time
			dataBuffer.append("<div class=\"time\">" + hdata.getString(getResources().getString(R.string.tag_time)) + "</div><hr />");
			
			// Check content for youtube embed
			Pattern youtubePattern = Pattern.compile("<iframe .*?src=\"http://www.youtube(-nocookie)?.com/embed/(.*?)(\\?rel=0)?\".*?>");
            Matcher matcher = youtubePattern.matcher(hdata.getString(getResources().getString(R.string.tag_body)));
            while (matcher.find()) {
                  matcher.appendReplacement(dataBuffer, "<div class=\"video\"><a href=\"youtube:$2\"></a><img src=\"http://img.youtube.com/vi/$2/0.jpg\"></div>");
            }
            matcher.appendTail(dataBuffer);


			// Close Body and HTML
			dataBuffer.append("</body></html>");
			data = dataBuffer.toString();
			
			webDetailView = (WebView) getActivity().findViewById(R.id.webDetailView);
			webDetailView.getSettings().setJavaScriptEnabled(true);
			webDetailView.getSettings().setDefaultFontSize(fontSize);
			webDetailView.getSettings().setLoadWithOverviewMode(true);
			webDetailView.getSettings().setUserAgentString(webDetailView.getSettings().getUserAgentString() + " sidhelpmobileapp");
			webDetailView.getSettings().setUseWideViewPort(true);
			if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB && VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
				webDetailView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			} 
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				webDetailView.setBackgroundColor(Color.argb(1, 0, 0, 0));
			} else {
				webDetailView.setBackgroundColor(Color.TRANSPARENT);
			}
			webDetailView.setWebChromeClient(new WebChromeClient());
			webDetailView.setWebViewClient(new WebViewListener(this, mPos, mTeams, webDetailView));
			webDetailView.loadDataWithBaseURL(null, data, null, "UTF-8", null);
			webDetailView.refreshDrawableState();

			shareIntent.putExtra(Intent.EXTRA_TEXT, hdata.getString(getResources().getString(R.string.tag_title)) + " " + getResources().getString(R.string.SiteURL) + "/article/" + hdata.getString(getResources().getString(R.string.tag_newsID)) + ".php");
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data for display", e);
		}
	}
	public void onResume() {
		super.onResume();
		
		if (!mTeams) {
			try {
				JSONObject hdata = mData.getJSONObject(0);
				getSherlockActivity().getSupportActionBar().setTitle(hdata.getString(getResources().getString(R.string.tag_category)));
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error getting title", e);
			}
		} else {
			getSherlockActivity().getSupportActionBar().setTitle(TeamsActivity.mSportName);
			TeamsActivity.stacked = true;
		}
	}
}
