package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.DishPhotoDto;
import com.svaad.Dto.HomeDetailDto;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.utils.Constants;

public class HomeSearchAdapter extends ArrayAdapter<HomeDetailDto> implements
		OnClickListener {

	HomeDetailDto detailsDto;
	ViewHolder holder;
	int index;
	private List<HomeDetailDto> restaurantDetailsDtos;
	private Context contex;

	public HomeSearchAdapter(Context context, int resource,
			List<HomeDetailDto> restaurantDetailsDtos) {
		super(context, resource, restaurantDetailsDtos);
		this.restaurantDetailsDtos = restaurantDetailsDtos;
		this.contex = context;
	}

	public List<HomeDetailDto> getRestaurantDetailsDtos() {
		return restaurantDetailsDtos;
	}

	public void setRestaurantDetailsDtos(
			List<HomeDetailDto> restaurantDetailsDtos) {
		this.restaurantDetailsDtos = restaurantDetailsDtos;
	}

	@Override
	public int getCount() {
		return restaurantDetailsDtos.size();
	}

	@Override
	public HomeDetailDto getItem(int position) {
		return restaurantDetailsDtos.get(position);
	}

	public class ViewHolder {
		private ImageView imagePopular;
		private TextView tvName;
	}

	private ViewHolder initHolder(View view) {
		holder = new ViewHolder();

		holder.tvName = (TextView) view.findViewById(R.id.tvName);
		holder.imagePopular = (ImageView) view.findViewById(R.id.imageRes);
		return holder;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		detailsDto = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.home_restaurant_row,
					parent, false);
			holder = initHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (detailsDto != null) {
			String name = detailsDto.getName();
			DishPhotoDto photo = detailsDto.getPhoto();

			if (photo != null) {
				String url = photo.getUrl();

				if (url != null && url.length() > 0) {
					Picasso.with(contex).load(url).placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.imagePopular);
				} else {
					Picasso.with(contex).load(R.drawable.temp)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.imagePopular);
				}
			} else {
				Picasso.with(contex).load(R.drawable.temp)
						.placeholder(R.drawable.temp).error(R.drawable.temp)

						.into(holder.imagePopular);
			}

			if (name != null && name.length() > 0) {
				holder.tvName.setText(name);
			} else {
				holder.tvName.setText(null);
			}
			
			
			holder.imagePopular.setTag(detailsDto);
			holder.imagePopular.setOnClickListener(this);

		}

		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageRes:
			HomeDetailDto homes = (HomeDetailDto) v.getTag();
			Intent intent = new Intent(contex, HomeSearchActivity.class);
			String search = homes.getSearch();
			if (search != null) {
				intent.putExtra(Constants.QUERYTEXT, search);
				intent.putExtra(Constants.SEE_MORE_DISHES,
						Constants.SEE_MORE_DISHES);
			}
			contex.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
