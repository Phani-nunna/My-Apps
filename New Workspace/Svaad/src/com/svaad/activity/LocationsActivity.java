package com.svaad.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.LocationDto;
import com.svaad.adapter.LocationsAdapter;
import com.svaad.adapter.LocationsAdapter.ViewHolder;
import com.svaad.asynctask.LocationListAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.LocationResponseDto;
import com.svaad.utils.Utils;

public class LocationsActivity extends BaseActivity implements
		SvaadFeedCallback, SvaadProgressCallback {

	private ListView locationListview;

	LocationResponseDto locationResponseDto;

	List<LocationDto> locationsLists = null;

	private ProgressBar pbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.explore_fragment_layout);

		initUi();

		initializeLocation();

		locationsLists = SvaadApplication.getInstance().getLocationDtos();
		if (locationsLists != null && locationsLists.size() > 0) {

			LocationsAdapter adapter = new LocationsAdapter(
					LocationsActivity.this, R.id.dishDetailListView,
					locationsLists);
			locationListview.setAdapter(adapter);
		} else {
			getLocations();
			locationsLists = locationResponseDto.getResults();

			LocationsAdapter adapter = new LocationsAdapter(
					LocationsActivity.this, R.id.dishDetailListView,
					locationsLists);
			locationListview.setAdapter(adapter);
		}

		locationListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				LocationsAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				holder.radio.setChecked(true);
				Utils.saveToSharedPreferenceInt("radioLocaion", position);
				Utils.saveToSharedPreference("radioLocationValue",
						locationsLists.get(position).getName());
				Utils.saveToSharedPreference("radioLocationObjectId",
						locationsLists.get(position).getObjectId());
				if (locationListview.getAdapter() != null)
					((LocationsAdapter) locationListview.getAdapter())
							.notifyDataSetChanged();
				
				Intent i=new Intent(LocationsActivity.this,HomeActivity.class);
				 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(i);
				finish();

			}
		});

	}

	private void initializeLocation() {
		if (locationResponseDto == null) {
			locationResponseDto = new LocationResponseDto();
			locationResponseDto.setResults(new ArrayList<LocationDto>());
		} else if (locationResponseDto.getResults() == null) {
			locationResponseDto.setResults(new ArrayList<LocationDto>());
		}
	}

	private void initUi() {
		locationListview = (ListView) findViewById(R.id.listView);
		pbar = (ProgressBar) findViewById(R.id.pbar);
	}

	private void getLocations() {
		LocationListAsynctask asy = new LocationListAsynctask(
				LocationsActivity.this, null);
		asy.execute();
	}

	@Override
	public void setResponse(Object object) {

		if (object == null) {
			return;
		}
		addResult((LocationResponseDto) object);
		LocationsAdapter lcationAdapter = (LocationsAdapter) locationListview
				.getAdapter();
		if (lcationAdapter != null) {
			lcationAdapter.setLocationDtos(this.locationResponseDto
					.getResults());
			lcationAdapter.notifyDataSetChanged();
		}

	}

	private void addResult(LocationResponseDto locationResponseDto) {
		if (locationResponseDto != null
				&& locationResponseDto.getResults() != null) {
			List<LocationDto> locationsLists = locationResponseDto.getResults();
			if (locationsLists != null && locationsLists.size() > 0) {

				LocationDto dto = new LocationDto();

				dto.setName("Near By(5 Km)");

				locationsLists.set(1, dto);
				dto = new LocationDto();

				dto.setName("All Locations");
				locationsLists.set(0, dto);

				String radioLocationValue = Utils
						.getFromSharedPreference("radioLocationValue");
				if (radioLocationValue == null)
					Utils.saveToSharedPreference("radioLocationValue",
							"All Locations");
				this.locationResponseDto.getResults().addAll(locationsLists);
				SvaadApplication.getInstance().setLocationDtos(locationsLists);
			}

		}

	}

	@Override
	public void progressOn() {

		pbar.setVisibility(View.VISIBLE);

	}

	@Override
	public void progressOff() {

		pbar.setVisibility(View.GONE);
	}

}
