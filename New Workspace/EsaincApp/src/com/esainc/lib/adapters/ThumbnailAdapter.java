package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ThumbnailAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Thumbmnail Adapter";
	private Context mContext;
	private JSONArray mData;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public ThumbnailAdapter(Context context, JSONArray data) {
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
		return pos;
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
			view = inflater.inflate(R.layout.activity_thumbnailview_row, null);
		} else {
			view = convertView;
		}
		
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			
			LinearLayout llThumbnail = (LinearLayout) view.findViewById(R.id.llThumbnail);
			ImageView imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
			
			int scale = (int) (110 * (mContext.getResources().getDisplayMetrics().densityDpi / 160f));
			llThumbnail.setLayoutParams(new GridView.LayoutParams(scale, scale));
			
			scale = (int) (100 * (mContext.getResources().getDisplayMetrics().densityDpi / 160f));
			imgThumbnail.setLayoutParams(new LinearLayout.LayoutParams(scale, scale));
			
			imgThumbnail.setBackgroundResource(0);
			imageLoader.displayImage(hdata.getString(mContext.getResources().getString(R.string.tag_thumbnail)), imgThumbnail, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						int height;
						int width;
						if (loadedImage.getWidth() > loadedImage.getHeight()) {
							height = (int) ((float) view.getLayoutParams().width * ((float) loadedImage.getHeight() / (float) loadedImage.getWidth()));
							width = view.getLayoutParams().width;
						} else {
							width = (int) ((float) view.getLayoutParams().height * ((float) loadedImage.getWidth() / (float) loadedImage.getHeight()));
							height = view.getLayoutParams().height;
						}
						view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
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
