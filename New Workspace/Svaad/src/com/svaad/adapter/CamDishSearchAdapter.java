package com.svaad.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.activity.AddDishActivity;
import com.svaad.utils.Constants;

public class CamDishSearchAdapter extends ArrayAdapter<CamDishesDeatailDto>
		implements OnClickListener {
	Context context;
	LayoutInflater inflater;

	List<CamDishesDeatailDto> feedList;
	List<CamDishesDeatailDto> arraylist;
	RestaurantDetailsDto restaurantPageDetail;
	CamRestaurantDetailDto restaurantDetail;
	String path;

	public CamDishSearchAdapter(Context context, int gridviewId,
			List<CamDishesDeatailDto> feedList,
			RestaurantDetailsDto restaurantPageDetail,
			CamRestaurantDetailDto restaurantDetail, String path) {

		super(context, gridviewId, feedList);
		this.context = context;
		this.feedList = feedList;
		this.restaurantDetail = restaurantDetail;
		this.restaurantPageDetail = restaurantPageDetail;
		this.path = path;

		// this.arraylist = new ArrayList<CamDishesDeatailDto>();
		// this.arraylist.addAll(feedList);
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	@Override
	public CamDishesDeatailDto getItem(int arg0) {
		return feedList.get(arg0);
	}

	public List<CamDishesDeatailDto> getFeedDtos() {
		return feedList;
	}

	public void setFeedDtos(List<CamDishesDeatailDto> feedList) {
		this.feedList = feedList;

		this.arraylist = new ArrayList<CamDishesDeatailDto>();
		this.arraylist.addAll(feedList);
	}

	@Override
	public View getView(int arg0, View view, ViewGroup group) {
		final ViewHolder holder;

		CamDishesDeatailDto feed = getItem(arg0);

		if (view == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.search_dishes_item_row, group,
					false);

			holder = new ViewHolder();

			holder.tvDishname = (TextView) view.findViewById(R.id.tvDishname);
			holder.btnAddDish = (Button) view.findViewById(R.id.btnAddDish);
			holder.btnAddDish.setOnClickListener(this);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (feed != null)

		{
			String dishname = feed.getDishName();

			if (dishname != null && dishname.length() > 0) {

				if (dishname.equalsIgnoreCase("AddDish")) {
					holder.tvDishname.setText(null);
					holder.btnAddDish.setVisibility(View.VISIBLE);
				} else {
					holder.tvDishname.setText(dishname);
					holder.btnAddDish.setVisibility(View.GONE);
				}
			} else {
				holder.tvDishname.setText(null);
			}

		} else {
			holder.tvDishname.setText(null);
		}

		// holder.btnAddDish.setTag(feed);

		return view;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		feedList.clear();
		if (charText.length() == 0) {
			feedList.addAll(arraylist);
		} else {
			for (CamDishesDeatailDto list : arraylist) {
				if (list.getDishName().toLowerCase(Locale.getDefault())
						.contains(charText)) {

					feedList.add(list);

				}
			}
			CamDishesDeatailDto cam = new CamDishesDeatailDto();
			cam.setDishName("AddDish");
			feedList.add(cam);
		}
		notifyDataSetChanged();
	}

	public class ViewHolder {
		private TextView tvDishname;
		private Button btnAddDish;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnAddDish:

			// CamDishesDeatailDto dishDetail = (CamDishesDeatailDto)
			// v.getTag();
			// Intent i = new Intent(context, AddDishActivity.class);
			// context.startActivity(i);

			if (restaurantPageDetail != null) {

				// if (dishDetail != null)
				// {
				Intent i = new Intent(context, AddDishActivity.class);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.putExtra(Constants.CAM_DISHES_DATA, dishDetail);
				i.putExtra(Constants.BRANCH_DETAILS, restaurantPageDetail);
				if (path != null) {
					i.putExtra("path", path);
				}
				context.startActivity(i);

				// }
			} else {
				// if (dishDetail != null) {
				Intent i = new Intent(context, AddDishActivity.class);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.putExtra(Constants.CAM_DISHES_DATA, dishDetail);
				i.putExtra(Constants.CAM_RES_DATA, restaurantDetail);
				if (path != null) {
					i.putExtra("path", path);
				}
				context.startActivity(i);
				// }
			}

			break;

		default:
			break;
		}
	}

}
