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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class YoutubeAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Youtube Adapter";
	private Context mContext;
	private JSONArray mData;
	private boolean mCrop;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public YoutubeAdapter(Context context, JSONArray data, boolean crop) {
		mContext = context;
		mData = data;
		mCrop = crop;
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
			view = inflater.inflate(R.layout.activity_youtube_row, null);
		} else {
			view = convertView;
		}
		
		try {
			JSONObject hdata = mData.getJSONObject(pos).getJSONObject(mContext.getResources().getString(R.string.tag_video));
			TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
			TextView txtUploader = (TextView) view.findViewById(R.id.txtUploader);
			TextView txtDuration = (TextView) view.findViewById(R.id.txtDuration);
			TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
			ImageView imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
			
			RelativeLayout rlThumbnail = (RelativeLayout) view.findViewById(R.id.rlThumbnail);
			if (mCrop)  {
				rlThumbnail.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.video_Thumbnail_Crop_Height);
				imgThumbnail.setScaleType(ScaleType.CENTER_CROP);
			} else {
				rlThumbnail.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.video_Thumbnail_Height);
				imgThumbnail.setScaleType(ScaleType.FIT_CENTER);
			}

			
			txtTitle.setText(hdata.getString(mContext.getResources().getString(R.string.tag_title)));
			if (((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
				txtUploader.setText(mContext.getResources().getString(R.string.uploaded_by) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_uploader)));
			} else {
				txtUploader.setText(hdata.getString(mContext.getResources().getString(R.string.tag_uploader)));
			}
			
			int totalSecs = Integer.parseInt(hdata.getString(mContext.getResources().getString(R.string.tag_duration)));
			int hours = totalSecs / 3600;
			int minutes = (totalSecs % 3600) / 60;
			int seconds = totalSecs % 60;
			String duration = "";
			if (hours > 0) duration = duration.concat(String.valueOf(hours) + ":");
			duration = duration.concat(String.format("%02d", minutes) + ":");
			duration = duration.concat(String.format("%02d", seconds));
			txtDuration.setText(duration);
			
			if (((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
				txtDescription.setText(hdata.getString(mContext.getResources().getString(R.string.tag_description)));
			}
			
			String photoURL = hdata.getJSONObject(mContext.getResources().getString(R.string.tag_thumbnail)).getString(mContext.getResources().getString(R.string.tag_hqDefault));
			imgThumbnail.setBackgroundResource(0);
			imageLoader.displayImage(photoURL, imgThumbnail, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage != null) {
						view.setBackgroundResource(R.drawable.image_stroke);
					} else {
						view.setBackgroundResource(0);
					}
				}
			});
			
			ImageView playButton = (ImageView) view.findViewById(R.id.imgPlay);
			DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			    .showImageOnLoading(0)
				.showImageForEmptyUri(0)
				.showImageOnFail(0)
				.cacheInMemory(true)
				.build();
			
			int width = (int) ((mContext.getResources().getInteger(R.integer.video_thumbnail_width) * (mContext.getResources().getDisplayMetrics().densityDpi / 160f)) / 4);
			imageLoader.displayImage("https://lh6.ggpht.com/NrQdFAdPSI9-hreB4C7HNhj3yXRiW1jqOOi7eFyakIx_IA-Im0huIeYCs5jTidMT2qA=w" + width, playButton, displayOptions);
			
			
			view.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_id)));
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return view;
	}
	
}
