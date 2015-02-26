package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class HeadlinesAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Headlines Adapter";
	private Context mContext;
	private JSONArray mData;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public HeadlinesAdapter(Context context, JSONArray data) {
		mContext = context;
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.length();
	}

	@Override
	public Object getItem(int pos) {
		try {
			return mData.getJSONObject(pos);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item", e);
		}
		return null;
	}

	@Override
	public long getItemId(int pos) {
		try {
			return Long.valueOf(mData.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_newsID))).longValue();
		} catch (NumberFormatException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (NotFoundException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		}
		return 0;
	}
	
	public void setData(JSONArray data) {
		mData = data;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			view = new View(mContext);
			view = inflater.inflate(R.layout.activity_headlines_row, null);
		} else {
			view = convertView;
		}
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			TextView headlineCategory = (TextView) view.findViewById(R.id.txtCategory);
			TextView headlineTitle = (TextView) view.findViewById(R.id.txtTitle);
			TextView headlineTime = (TextView) view.findViewById(R.id.txtTime);
			ImageView headlineImage = (ImageView) view.findViewById(R.id.imgArticle);
			RelativeLayout headlineRow = (RelativeLayout) view.findViewById(R.id.llHeadlineRow);
			
			headlineCategory.setText(hdata.getString(mContext.getResources().getString(R.string.tag_category)));
			headlineTitle.setText(hdata.getString(mContext.getResources().getString(R.string.tag_title)));
			headlineTime.setText(hdata.getString(mContext.getResources().getString(R.string.tag_time)));
			headlineRow.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_newsID)));
			headlineImage.setBackgroundResource(0);
			String photoURL = hdata.getString(mContext.getResources().getString(R.string.tag_photoURL)).replaceAll(" ", "%20");
			imageLoader.displayImage(photoURL, headlineImage, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						view.setBackgroundResource(R.drawable.image_stroke);
					} else {
						view.setBackgroundResource(0);
					}
				}
			});
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		return view;
	}

}
