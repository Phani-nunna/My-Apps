package com.svaad.fragment;



import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.LocationDto;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.adapter.LocationsAdapter;
import com.svaad.adapter.LocationsAdapter.ViewHolder;
import com.svaad.asynctask.LocationListAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.responseDto.LocationResponseDto;
import com.svaad.utils.Utils;

public class NewLocationFragemnt extends Fragment implements SvaadFeedCallback {

	ListView locationListview;

	List<LocationDto> locationsLists = null;
	
	LocationResponseDto locationResponseDto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			// getLocations();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.dish_detail_list_layout, container, false);
		locationListview = (ListView) view.findViewById(R.id.dishDetailListView);

		initializeLocation();

		locationsLists = SvaadApplication.getInstance().getLocationDtos();

		if (locationsLists != null && locationsLists.size() > 0) {
			LocationsAdapter adapter = new LocationsAdapter(getActivity(),
					R.id.dishDetailListView, locationsLists);
			locationListview.setAdapter(adapter);
		}
		else
		{
			getLocations();
			locationsLists=locationResponseDto.getResults();
			LocationsAdapter adapter = new LocationsAdapter(getActivity(),
					R.id.dishDetailListView, locationsLists);
			locationListview.setAdapter(adapter);
		}

		locationListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {


				 LocationsAdapter.ViewHolder holder = (ViewHolder)view.getTag();
				 holder.radio.setChecked(true);
				 ((HomeSearchActivity)getActivity()).setLocationText(locationsLists.get(position).getName());
				 	Utils.saveToSharedPreferenceInt("radioLocaion", position);
					Utils.saveToSharedPreference("radioLocationValue",locationsLists.get(position).getName());
					Utils.saveToSharedPreference("radioLocationObjectId",locationsLists.get(position).getObjectId());
				 if(locationListview.getAdapter()!=null)
					 ((LocationsAdapter)locationListview.getAdapter()).notifyDataSetChanged();
				 
			}
		});

		return view;
	}
	
	private void getLocations() {
		LocationListAsynctask asy = new LocationListAsynctask(
				getActivity(), NewLocationFragemnt.this);
		asy.execute();		
	}

	private void initializeLocation() {
		if (locationResponseDto == null) {
			locationResponseDto = new LocationResponseDto();
			locationResponseDto.setResults(new ArrayList<LocationDto>());
		} else if (locationResponseDto.getResults() == null) {
			locationResponseDto.setResults(new ArrayList<LocationDto>());
		}
	}

	

	@Override
	public void setResponse(Object object) {
		
		if (object == null) {
			return;
		}
		addResult((LocationResponseDto) object);
		LocationsAdapter lcationAdapter = (LocationsAdapter) locationListview.getAdapter();
		if (lcationAdapter != null) {
			lcationAdapter.setLocationDtos(this.locationResponseDto.getResults());
			lcationAdapter.notifyDataSetChanged();
		}
		
	}
	
	private void addResult(LocationResponseDto locationResponseDto) 
	{
		if (locationResponseDto != null && locationResponseDto.getResults() != null)
		{
			List<LocationDto> locationsLists = locationResponseDto.getResults();
			if(locationsLists!=null && locationsLists.size()>0)
			{
				
				
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

}
