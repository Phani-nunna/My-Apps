package com.esainc.lib.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

public class RosterAdapter extends BaseAdapter {
	private static final String LOG_TAG = "Roster Adapter";
	private Context mContext;
	private JSONArray mData;
	private JSONArray mFullData;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public RosterAdapter(Context context, JSONArray data) {
		mContext = context;
		mData = data;
		mFullData = data;
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
		mFullData = data;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			view = new View(mContext);
			view = inflater.inflate(R.layout.activity_team_roster_row, null);
		} else {
			view = convertView;
		}
		try {
			JSONObject hdata = mData.getJSONObject(pos);
			TextView txtName = (TextView) view.findViewById(R.id.txtName);
			TextView txtClass = (TextView) view.findViewById(R.id.txtClass);
			ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
			
			
			if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_number)))) {
				txtName.setText(hdata.getString(mContext.getResources().getString(R.string.tag_fname)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_lname)));
			} else {
				txtName.setText("#" + hdata.getString(mContext.getResources().getString(R.string.tag_number)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_fname)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_lname)));
			}
			txtClass.setText(hdata.getString(mContext.getResources().getString(R.string.tag_class)) + " " + hdata.getString(mContext.getResources().getString(R.string.tag_pos)));
			String photoURL = hdata.getString(mContext.getResources().getString(R.string.tag_photo)).replaceAll(" ", "%20");
			imgPhoto.setBackgroundResource(0);
			if (((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
				int multiplier = 2;
				if (mContext.getResources().getInteger(R.integer.RosterImageWidth) >= 200){
					multiplier = 1; 
				}
				imgPhoto.getLayoutParams().width = mContext.getResources().getInteger(R.integer.RosterImageWidth) * multiplier;
				imgPhoto.getLayoutParams().height = mContext.getResources().getInteger(R.integer.RosterImageHeight) * multiplier;
				imgPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
			}
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
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		
		return view;
	}
	
	public void filter(String filterValue, String sortValue) {
		if (filterValue == mContext.getResources().getString(R.string.All)) {
			mData = mFullData;
		} else {
			ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			for (int i = 0; i < mFullData.length(); i++) {
				try {
					JSONObject obj = mFullData.getJSONObject(i);
					if (obj.getString(mContext.getResources().getString(R.string.tag_class)).equalsIgnoreCase(filterValue)) {
						jsonObjects.add(obj);
					}
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error filtering list", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error filtering list", e);
				}
			}
			mData = new JSONArray(jsonObjects);
		}
		sort(sortValue);
		notifyDataSetChanged();
	}
	
	public void sort(String sortValue) {
		try {
			ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			for (int i = 0; i < mData.length(); i++) {
				jsonObjects.add(mData.getJSONObject(i));
			}
			Collections.sort(jsonObjects, new JSONComparator(sortValue));
			mData = new JSONArray(jsonObjects);
			notifyDataSetChanged();
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error sorting list", e);
		}
	}
	
	private class JSONComparator implements Comparator<JSONObject> {
		private String mSortValue;
		
		public JSONComparator(String sortValue) {
			mSortValue = sortValue;
		}
		
		@Override
		public int compare(JSONObject obj1, JSONObject obj2) {
			try {
				String value1 = obj1.getString(mSortValue);
				String value2 = obj2.getString(mSortValue);
				if (mSortValue.equalsIgnoreCase(mContext.getResources().getString(R.string.tag_number))) {
					Integer val1 = 0;
					Integer val2 = 0;
					if (TextUtils.isEmpty(value1) || value1.equalsIgnoreCase(" ") || value1.equalsIgnoreCase("&nbsp;")) {
						val1 = 0;
					} else if (value1.equalsIgnoreCase("RS")) {
						val1 = 999;
					} else {
						val1 = Integer.valueOf(obj1.getString(mContext.getResources().getString(R.string.tag_number_int)));
					}
					if (TextUtils.isEmpty(value2) || value2.equalsIgnoreCase(" ") || value2.equalsIgnoreCase("&nbsp;")) {
						val2 = 0;
					} else if (value2.equalsIgnoreCase("RS")) {
						val2 = 999;
					} else {
						val2 = Integer.valueOf(obj2.getString(mContext.getResources().getString(R.string.tag_number_int)));
					}
					return val1.compareTo(val2);
				}
				return value1.compareToIgnoreCase(value2);
			} catch (JSONException e) {
				Logger.e(LOG_TAG, "Error sorting list", e);
			}
			return 0;
		}
	}
}
