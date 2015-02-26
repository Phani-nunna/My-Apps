package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.LocationDto;
import com.svaad.utils.Utils;

public class LocationsAdapter extends ArrayAdapter<LocationDto> {

	private int listViewId;

	// boolean[] checkBoxState;

	private int selected = -1;

	List<LocationDto> locationsLists;
	Context context;
	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());

	public LocationsAdapter(Context context, int resource,
			List<LocationDto> objects) {
		super(context, resource, objects);
		this.context = context;
		this.locationsLists = objects;
		listViewId = resource;
		// checkBoxState = new boolean[locationsLists.size()];
	}

	@Override
	public int getCount() {
		return locationsLists.size();
	}

	@Override
	public LocationDto getItem(int arg0) {
		return locationsLists.get(arg0);
	}

	
	public List<LocationDto> getLocationDtos() {
		return locationsLists;
	}

	public void setLocationDtos(List<LocationDto> locationsLists) {
		this.locationsLists = locationsLists;
	}
	
	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public class ViewHolder {
		private TextView tvSiteName;
		// private CheckBox checkLocation;

		public RadioButton radio;
	}

	private ViewHolder initHolder(View convertView) {

		ViewHolder holder = new ViewHolder();

		holder.tvSiteName = (TextView) convertView
				.findViewById(R.id.tvSiteName);
		// holder.checkLocation = (CheckBox) convertView
		// .findViewById(R.id.checkLocation);

		holder.radio = (RadioButton) convertView.findViewById(R.id.radio);
		return holder;
	}

	@Override
	public View getView(final int position, View view, ViewGroup group) {

		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.locations_row, group, false);

			holder = new ViewHolder();

			holder.tvSiteName = (TextView) view.findViewById(R.id.tvSiteName);
			// holder.checkLocation = (CheckBox) view
			// .findViewById(R.id.checkLocation);
			holder.radio = (RadioButton) view.findViewById(R.id.radio);

			// holder = initHolder(view);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		LocationDto location = locationsLists.get(position);
		holder.tvSiteName.setText(location.getName());

		holder.radio.setTag(position);
		holder.radio.setChecked(position == selected);

		int userId = sharedPreferences.getInt("radioLocaion", 0);
		if (userId == position) {
			holder.radio.setTag(position);
			holder.radio.setChecked(true);
			Utils.saveToSharedPreference("radioLocationValue", locationsLists
					.get(position).getName());
			Utils.saveToSharedPreference("radioLocationObjectId",
					locationsLists.get(position).getObjectId());
		}

		/*holder.radio.setOnClickListener(new View.OnClickListener() {

			// private int arg0;

			@Override
			public void onClick(View arg0) {
				selected = (Integer) arg0.getTag();
				((NewHomeSearchActivity)context).setLocationText(locationsLists.get(position).getName());
				Utils.saveToSharedPreference("radioLocaion", position);
				Utils.saveToSharedPreference("radioLocationValue",locationsLists.get(position).getName());
				Utils.saveToSharedPreference("radioLocationObjectId",locationsLists.get(position).getObjectId());

				notifyDataSetChanged();
			}

		});*/

		// holder.checkLocation.setChecked(checkBoxState[arg0]);
		//
		// holder.checkLocation.setOnClickListener(new View.OnClickListener() {
		//
		// public void onClick(View v) {
		// if (((CheckBox) v).isChecked()) {
		// checkBoxState[arg0] = true;
		// } else {
		// checkBoxState[arg0] = false;
		// }
		//
		// }
		// });

		return view;
	}

}
