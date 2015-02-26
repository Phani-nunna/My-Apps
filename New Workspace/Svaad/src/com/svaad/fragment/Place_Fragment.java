package com.svaad.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.utils.Constants;

public class Place_Fragment extends Fragment implements OnClickListener {
	RestaurantDetailsDto restaurantDetailsDto;
	View view;

	TextView tvPlace;
	String address;
	Button btnGetDirections;
	String location;
	double latitude,longitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restaurantDetailsDto = (RestaurantDetailsDto) (getArguments() != null ? getArguments()
				.getSerializable(Constants.DATA) : null);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.place_row, container, false);

		initUI(view);

		// Bundle bundle = new Bundle();
		// bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
		// // bundle.putSerializable(Constants.PLACE, placeDto);
		// MyGoogleMap myGoogleMap = new MyGoogleMap();
		// myGoogleMap.setArguments(bundle);
		//
		// FragmentTransaction fragmentTransaction = getActivity()
		// .getSupportFragmentManager().beginTransaction();
		// fragmentTransaction.replace(R.id.mapLayout, myGoogleMap);
		// fragmentTransaction.commit();

		if (restaurantDetailsDto != null) {
		
			String building = restaurantDetailsDto.getBuilding();
			String street = restaurantDetailsDto.getStreet();
			String landmark = restaurantDetailsDto.getLandmark();
			LocationDto locationDto = restaurantDetailsDto.getLocation();
			PointDto pointDto=restaurantDetailsDto.getPoint();
			if(pointDto!=null)
			{
				latitude=pointDto.getLatitude();
				longitude=pointDto.getLongitude();
			}

			if (building != null && building.length() > 0) {
				address = building;
			}
			if (street != null && street.length() > 0) {
				address = address + "," + street;
			}
			if (landmark != null && landmark.length() > 0) {
				address = address + "," + landmark;
			}
			if (locationDto != null) {
				location = locationDto.getName();

				address = address + "," + location;
			}
			if (address != null && address.length() > 0) {
				tvPlace.setText(address);
			} else {
				tvPlace.setText("No information available");
			}
		}
		return view;
	}

	private void initUI(View view) {
		tvPlace = (TextView) view.findViewById(R.id.tvPlace);
		btnGetDirections=(Button)view.findViewById(R.id.btnGetDirections);
		btnGetDirections.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnGetDirections:
			
			if(latitude!=0 && longitude!=0)
			{
				
				String label = null;
				double latitude = this.latitude;
				double longitude = this.longitude;
				
				if(location!=null && location.length()>0)
				{
				 label = location;
				}
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = latitude + "," + longitude + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
				startActivity(intent);
				
			}
			break;

		default:
			break;
		}
		
	}

}
