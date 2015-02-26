package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.Dto.SearchPointDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.TableObjectDto;
import com.svaad.adapter.RestaurantListAdapter;
import com.svaad.asynctask.RestaurantAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.RestaurentRequestDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Utils;
import com.svaad.whereDto.RestaurantListWhereDto;

public class SearchRestaurantsFragemnt extends Fragment implements
		OnScrollListener, SvaadFeedCallback,SvaadProgressCallback {
	ListView locationListview;
	private int distanceInKilometers = 5;
	private long skip = 0;
	Location location;
	private int totalItemCount = -1;
	String queryText = null;
	List<RestaurantDetailsDto> restaurantLists;
	// RestaurantSuggestionResponseDto res;
	private RestaurantListResponseDto restaurantListResponseDto;
	String radioLocationValue, radioVegValue;
	Context context;
	// private ArrayList<RestaurantDetailsDto> restaurantList;
	private TextView tvNoResults;
	private boolean scroll = false;
	private ProgressBar pbar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dish_res_list_layout, container,
				false);
		initializeList();
		locationListview = (ListView) view
				.findViewById(R.id.dishDetailListView);
		pbar = (ProgressBar) view.findViewById(R.id.pbar);
		
		tvNoResults = (TextView) view.findViewById(R.id.tvNoResults);

		// radioLocationValue = Utils
		// .getFromSharedPreference("radioLocationValue");
		// radioVegValue = Utils.getFromSharedPreference("radioVegValue");
		//
		// restaurantLists = new ArrayList<RestaurantDetailsDto>();

		if (radioLocationValue == null && radioVegValue == null) {
			radioLocationValue = "All Locations";
			radioVegValue = "All";

		}

		Bundle bundle = getArguments();
		if (getArguments() != null) {

			queryText = bundle.getString("search");
//			nearby = bundle.getString("Near by");
			// restaurantList = (ArrayList<RestaurantDetailsDto>)
			// bundle.getSerializable(Constants.DATA_Res);

			// if (queryText != null || !queryText.equals(""))
			if (queryText != null) {

				try {
					// if(restaurantList==null)
					getSearchLocation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// if(nearby!=null && nearby.length()>0 &&
			// nearby.equalsIgnoreCase("Near by"))
			// {
			// try
			// {
			// location = Utils.getMyLocation(getActivity());
			// getNearByLocation();
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			// }

			// try {
			// getSearchLocation();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

		}
		// if(restaurantList!=null && restaurantList.size()>0)
		// {
		// locationListview.setAdapter(new RestaurantListAdapter(getActivity(),
		// 0, restaurantList));
		// }
		// else
		// {
		locationListview.setAdapter(new RestaurantListAdapter(getActivity(), 0,
				restaurantListResponseDto.getResults()));
		// }
		// locationListview.setAdapter(new RestaurantListAdapter(getActivity(),
		// 0,
		// restaurantListResponseDto.getResults()));
		locationListview.setDivider(null);
		locationListview.setOnScrollListener(this);

		return view;
	}

//	private void getNearByLocation() {
//
//		if (location != null) {
//
//			RestaurantAsynctask asy = new RestaurantAsynctask(getActivity(),
//					this, getNearByRestaurentRequestDto());
//			asy.execute();
//		} else {
//			return;
//		}
//
//	}

//	private RestaurentRequestDto getNearByRestaurentRequestDto() {
//		RestaurentRequestDto restaurentRequestDto = new RestaurentRequestDto();
//		RestaurantListWhereDto whereDto = new RestaurantListWhereDto();
//
//		if (location != null) {
//
//			SearchPointDto searchPointDto = new SearchPointDto();
//			PointDto pointDto = new PointDto();
//
//			pointDto.set__type("GeoPoint");
//			pointDto.setLongitude(location.getLongitude());
//			pointDto.setLatitude(location.getLatitude());
//
//			searchPointDto.set$nearSphere(pointDto);
//			searchPointDto.set$maxDistanceInKilometers(distanceInKilometers);
//			whereDto.setPoint(searchPointDto);
//		}
//
//		whereDto.setPublish(true);
//
//		restaurentRequestDto.set_method("GET");
//		restaurentRequestDto.setInclude("location");
//		restaurentRequestDto.setSkip(skip);
//		restaurentRequestDto.setLimit(20);
//		restaurentRequestDto.setOrder("-updatedAt");
//		restaurentRequestDto.setWhere(whereDto);
//		return restaurentRequestDto;
//	}

	private void initializeList() {
		if (restaurantListResponseDto == null) {
			restaurantListResponseDto = new RestaurantListResponseDto();
			restaurantListResponseDto
					.setResults(new ArrayList<RestaurantDetailsDto>());
		} else if (restaurantListResponseDto.getResults() == null) {
			restaurantListResponseDto
					.setResults(new ArrayList<RestaurantDetailsDto>());
		}
	}

	private void getSearchLocation() {

		RestaurantAsynctask asy = new RestaurantAsynctask(getActivity(), this,
				getRestaurentRequestDtoForLocation());
		asy.execute();

	}

	private RestaurentRequestDto getRestaurentRequestDtoForLocation() {
		RestaurentRequestDto restaurentRequestDto = new RestaurentRequestDto();
		RestaurantListWhereDto whereDto = new RestaurantListWhereDto();

		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {

			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
			whereDto.setSearchTags(searchTagsDto);
		}

		if (radioLocationValue.equalsIgnoreCase("Near By(5 Km)")) {
			SearchPointDto searchPointDto = new SearchPointDto();
			PointDto pointDto = new PointDto();

			pointDto.set__type("GeoPoint");
			if (location != null) {
				pointDto.setLongitude(location.getLongitude());
				pointDto.setLatitude(location.getLatitude());
			}
			searchPointDto.set$nearSphere(pointDto);
			searchPointDto.set$maxDistanceInKilometers(distanceInKilometers);
			whereDto.setPoint(searchPointDto);
		}

		else if (radioLocationValue.equalsIgnoreCase("All Locations")) {
			System.out.println("No tag for nearby and locations");
		} else {
			TableObjectDto locationDto = new TableObjectDto();
			locationDto.set__type("Pointer");
			locationDto.setClassName("Location");

			String locationObjectId = Utils
					.getFromSharedPreference("radioLocationObjectId");

			if (locationObjectId != null) {

				locationDto.setObjectId(locationObjectId);
				whereDto.setLocation(locationDto);
			}

		}

		if (radioVegValue != null) {
			if (radioVegValue.equalsIgnoreCase("Veg")) {
				whereDto.setPureVeg(true);
			} else if (radioVegValue.equalsIgnoreCase("Non Veg")) {
				whereDto.setPureVeg(false);
			} else if (radioVegValue.equalsIgnoreCase("All")) {

				System.out.println("No tag for Both");
			}

		}

		whereDto.setPublish(true);

		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setSkip(skip);
		restaurentRequestDto.setLimit(20);
		restaurentRequestDto.setOrder("-updatedAt");
		restaurentRequestDto.setWhere(whereDto);
		return restaurentRequestDto;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount;

			scroll = true;
			// if(nearby!=null && nearby.length()>0 &&
			// nearby.equalsIgnoreCase("Near by"))
			// {
			// try
			// {
			// getNearByLocation();
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			// }
			// else
			// {

			getSearchLocation();
			// }
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void setResponse(Object restaurantListResponseDto) {
		if (restaurantListResponseDto == null
				&& (restaurantListResponseDto instanceof RestaurantListResponseDto)) {
			return;
		}
		addResult((RestaurantListResponseDto) restaurantListResponseDto);
		RestaurantListAdapter restaurantListAdapter = (RestaurantListAdapter) locationListview
				.getAdapter();
		if (restaurantListAdapter != null) {
			restaurantListAdapter
					.setRestaurantDetailsDtos(this.restaurantListResponseDto
							.getResults());
			restaurantListAdapter.notifyDataSetChanged();
		}

	}

	private void addResult(
			RestaurantListResponseDto newRestaurantListResponseDto) {
		if (newRestaurantListResponseDto != null
				&& newRestaurantListResponseDto.getResults() != null
				&& newRestaurantListResponseDto.getResults().size() > 0) {
			List<RestaurantDetailsDto> newRestaurantDetailsDtos = newRestaurantListResponseDto
					.getResults();
			this.restaurantListResponseDto.getResults().addAll(
					newRestaurantDetailsDtos);
		} else {
			if (scroll == true) {
				tvNoResults.setVisibility(View.INVISIBLE);
			} else {
				tvNoResults.setVisibility(View.VISIBLE);
				tvNoResults.setText("No results found");
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
