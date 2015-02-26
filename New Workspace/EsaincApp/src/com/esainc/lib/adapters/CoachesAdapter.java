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
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class CoachesAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Coaches Adapter";
	private Context mContext;
	private JSONArray mData;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public CoachesAdapter(Context context, JSONArray data) {
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
			return Long.valueOf(mData.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_rosterID)));
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
			view = inflater.inflate(R.layout.activity_team_coaches_row, null);
		} else {
			view = convertView;
		}
		try {
			if(!((SIDHelpApplication) mContext.getApplicationContext()).isTablet()){
				JSONObject hdata = mData.getJSONObject(pos);
				TextView txtName = (TextView) view.findViewById(R.id.txtName);
				TextView txtCoachType = (TextView) view.findViewById(R.id.txtCoachType);
				TextView txtYearsCoached = (TextView) view.findViewById(R.id.txtYearsCoached);
				ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
				
				txtName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_fname)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_lname)));
				txtCoachType.setText(hdata.getString(mContext.getResources().getString(R.string.tag_coachType)));
				txtYearsCoached.setText(hdata.getString(mContext.getResources().getString(R.string.tag_yearsCoached)));
				String photoURL = hdata.getString(mContext.getResources().getString(R.string.tag_photo)).replaceAll(" ", "%20");
				imgPhoto.setBackgroundResource(0);
				imageLoader.displayImage(photoURL, imgPhoto, new SimpleImageLoadingListener() {
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
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return view;
	}
}
