package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.LocationDto;

public class CamSearchAdapter extends ArrayAdapter<CamRestaurantDetailDto> {
	Context context;
	LayoutInflater inflater;

	List<CamRestaurantDetailDto> feedList;

	public CamSearchAdapter(Context context, int gridviewId,
			List<CamRestaurantDetailDto> feedList) {

		super(context, gridviewId, feedList);
		this.context = context;
		this.feedList = feedList;
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public CamRestaurantDetailDto getItem(int arg0) {
		return feedList.get(arg0);
	}

	public List<CamRestaurantDetailDto> getFeedDtos() {
		return feedList;
	}

	public void setFeedDtos(List<CamRestaurantDetailDto> feedList) {
		this.feedList = feedList;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup group) {
		final ViewHolder holder;

		CamRestaurantDetailDto feed = getItem(arg0);

		if (view == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.search_item_row, group, false);

			holder = new ViewHolder();

			holder.tvResname = (TextView) view.findViewById(R.id.tvResname);
			holder.tvResLocation = (TextView) view
					.findViewById(R.id.tvResLocation);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (feed != null)

		{
			String resname = feed.getBranchName();
			LocationDto locationDto = feed.getLocation();
			if (locationDto != null) {

				String locationname = locationDto.getName();
				if (locationname != null && locationname.length() > 0) {
					holder.tvResLocation.setText(locationname);
				} else {
					holder.tvResLocation.setText(null);
				}

			} else {
				holder.tvResLocation.setText(null);
			}
			if (resname != null && resname.length() > 0) {

				holder.tvResname.setText(resname);
			} else {
				holder.tvResname.setText(null);
			}

		} else {
			holder.tvResname.setText(null);
			holder.tvResLocation.setText(null);
		}

		return view;
	}

	public class ViewHolder {
		private TextView tvResname, tvResLocation;
	}

}
