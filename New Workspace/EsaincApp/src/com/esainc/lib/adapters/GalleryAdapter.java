package com.esainc.lib.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Gallery Adapter";
	private Context mContext;
	private JSONArray mData;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public GalleryAdapter(Context context, JSONArray data) {
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
			return Long.valueOf(mData.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_galleryID))).longValue();
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
			view = inflater.inflate(R.layout.activity_photos_row, null);
		} else {
			view = convertView;
		}
		
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			TextView txtCategory = (TextView) view.findViewById(R.id.txtCategory);
			TextView txtGalleryName = (TextView) view.findViewById(R.id.txtGalleryName);
			TextView txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);
			ImageView imgPhotos = (ImageView) view.findViewById(R.id.imgPhotos);
			
						txtGalleryName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_name)));
			
			if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_seasonName)))) {
				txtCategory.setText(hdata.getString(mContext.getResources().getString(R.string.tag_sportName)));
			} else {
				txtCategory.setText(hdata.getString(mContext.getResources().getString(R.string.tag_seasonName)) + " - " + hdata.getString(mContext.getResources().getString(R.string.tag_sportName)));
			}
			
			if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_galleryDate)))) {
				txtDateTime.setText(hdata.getString(mContext.getResources().getString(R.string.tag_photoCount)) + " Photos");
			} else {
				txtDateTime.setText(hdata.getString(mContext.getResources().getString(R.string.tag_galleryDate)) + " - " + hdata.getString(mContext.getResources().getString(R.string.tag_photoCount)) + " Photos");
			}
			
			if (!((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
				imgPhotos.setVisibility(View.GONE);
			} else {
				imgPhotos.setBackgroundResource(0);
				imageLoader.displayImage(hdata.getString(mContext.getResources().getString(R.string.tag_thumbnail)), imgPhotos, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage != null) {
							view.setBackgroundResource(R.drawable.image_stroke);
						} else {
							view.setBackgroundResource(0);
						}
					}
				});
			}
			
			view.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_galleryID)));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return view;
	}
	
}
