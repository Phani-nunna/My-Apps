package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PlaceDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RegularDto;
import com.svaad.Dto.SearchPointDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.TableObjectDto;
import com.svaad.activity.DishProfileActivity;
import com.svaad.adapter.SearchDishesAdapter;
import com.svaad.asynctask.SearchDishesAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;
import com.svaad.whereDto.DishWhereDto;
import com.svaad.whereDto.UserIdWhereDto;

public class SearchDishesFragemnt extends Fragment implements OnScrollListener,
		SvaadFeedCallback ,SvaadProgressCallback{
	ListView listview;
	private int distanceInKilometers = 5;
	private long skip = 0;
	Location location;

	private int totalItemCount = -1;
	String queryText;
	List<FeedDetailDto> dishesLists;
	TextView tvNoResults;
	String radioLocationValue, radioPriceValue, radioVegValue;
	private boolean scroll = false;

	private FeedResponseDto feedResponseDto;
	// private ArrayList<FeedDetailDto> searchDishesList;
	private ArrayList<FeedDetailDto> feedsList;
	private ProgressBar pbar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dish_res_list_layout, container,
				false);
		initializeList();
		tvNoResults = (TextView) view.findViewById(R.id.tvNoResults);
		listview = (ListView) view.findViewById(R.id.dishDetailListView);
		
		pbar = (ProgressBar) view.findViewById(R.id.pbar);


		// location = Utils.getMyLocation(getActivity());

		// radioLocationValue = Utils
		// .getFromSharedPreference("radioLocationValue");
		// radioPriceValue = Utils.getFromSharedPreference("radioPriceValue");
		// radioVegValue = Utils.getFromSharedPreference("radioVegValue");

		if (radioLocationValue == null && radioPriceValue == null
				&& radioVegValue == null) {
			radioLocationValue = "All Locations";
			radioPriceValue = "All Prices";
			radioVegValue = "All";

		}

		dishesLists = new ArrayList<FeedDetailDto>();
		Bundle bundle = getArguments();
		if (getArguments() != null) {
			queryText = bundle.getString("search");
			// searchDishesList = (ArrayList<FeedDetailDto>)
			// bundle.getSerializable(Constants.DATA);
			if (queryText != null && !queryText.equals("")) {

				try {
					// if(searchDishesList==null)
					getSearchLocation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	
		listview.setAdapter(new SearchDishesAdapter(getActivity(), 0,
				feedResponseDto.getResults()));
		
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				new SvaadDialogs().showToast(getActivity(), "Clicked");

				FeedDetailDto feedDetailDto = feedResponseDto.getResults().get(
						arg2);

				Intent dishProfileIntent = new Intent(getActivity(),
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				getActivity().startActivity(dishProfileIntent);

			}

		});

		listview.setDivider(null);
		listview.setOnScrollListener(this);

		
		return view;
	}

	private void initializeList() {
		if (feedResponseDto == null) {
			feedResponseDto = new FeedResponseDto();
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		} else if (feedResponseDto.getResults() == null) {
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		}
	}

	private void getSearchLocation() {

		SearchDishesAsynctask asy = new SearchDishesAsynctask(getActivity(),
				this, Constants.BRANCH_MENU_URL, getDishRequestDtoForLocation(),scroll);
		asy.execute();

	}

	private DishWhereDto getDishRequestDtoForLocation() {

		DishWhereDto dishWhereRequestDto = new DishWhereDto();
		UserIdWhereDto userIdWhereDto = new UserIdWhereDto();
		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {

			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
		}
		if (radioLocationValue != null) {
			if (radioLocationValue.equalsIgnoreCase("Near By(5 Km)")) {
				SearchPointDto searchPointDto = new SearchPointDto();
				PointDto pointDto = new PointDto();

				pointDto.set__type("GeoPoint");
				location = Utils.getMyLocation(getActivity());
				if (location != null) {
					pointDto.setLongitude(location.getLongitude());
					pointDto.setLatitude(location.getLatitude());
					searchPointDto.set$nearSphere(pointDto);
				}

				searchPointDto
						.set$maxDistanceInKilometers(distanceInKilometers);
				userIdWhereDto.setPoint(searchPointDto);
			}

			else if (radioLocationValue.equalsIgnoreCase("All Locations")) {
				System.out.println("No tag for nearby and locations");
			}

			else {
				TableObjectDto locationDto = new TableObjectDto();
				locationDto.set__type("Pointer");
				locationDto.setClassName("Location");

				String locationObjectId = Utils
						.getFromSharedPreference("radioLocationObjectId");

				if (locationObjectId != null) {

					locationDto.setObjectId(locationObjectId);
				}
				userIdWhereDto.setLocation(locationDto);
			}
		}
		if (radioVegValue != null) {
			if (radioVegValue.equalsIgnoreCase("Veg")) {
				userIdWhereDto.setNonVeg(false);
			} else if (radioVegValue.equalsIgnoreCase("Non Veg")) {
				userIdWhereDto.setNonVeg(true);
			} else if (radioVegValue.equalsIgnoreCase("All")) {

				System.out.println("No tag for Both");
			}

		}
		if (radioPriceValue != null) {
			if (radioPriceValue.equalsIgnoreCase("All Prices")) {

				System.out.println("No tag for all prices");

			} else {
				RegularDto regularDto = new RegularDto();
				String price;
				String priceRange[];
				if (radioVegValue != null) {
					String priveVal = radioPriceValue;

					if (priveVal.equalsIgnoreCase("Below Rs 50")) {
						price = priveVal.replaceAll("Below Rs ", "");

						regularDto.set$lte(Float.parseFloat(price.trim()));
						userIdWhereDto.setRegular(regularDto);

					}

					else if (priveVal.equalsIgnoreCase("Above Rs 1000")) {
						price = priveVal.replaceAll("Above Rs ", "");
						regularDto.set$gt(Float.parseFloat(price.trim()));
						userIdWhereDto.setRegular(regularDto);
					}

					else {

						price = priveVal.replaceAll("Rs ", "");
						priceRange = price.split("-");
						if (priceRange.length == 2) {
							regularDto.set$gt(Float.parseFloat(priceRange[0]
									.trim()));
							regularDto.set$lte(Float.parseFloat(priceRange[1]
									.trim()));
							userIdWhereDto.setRegular(regularDto);
						} else if (priceRange.length == 1) {
							regularDto.set$gt(Float.parseFloat(priceRange[0]
									.trim()));
							userIdWhereDto.setRegular(regularDto);
						}
					}

				}
			}

		}

		// userIdWhereDto.setLocation(locationDto);
		userIdWhereDto.setSearchTags(searchTagsDto);
		userIdWhereDto.setPublish(true);

		dishWhereRequestDto.set_method("GET");
		dishWhereRequestDto.setInclude("dishId,location");
		dishWhereRequestDto.setLimit(25);
		dishWhereRequestDto.setOrder("-comments,-offerId");
		dishWhereRequestDto.setSkip(skip);
		dishWhereRequestDto.setWhere(userIdWhereDto);
		return dishWhereRequestDto;

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {
			scroll = true;
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount;

			getSearchLocation();
			// refreshAdapter();
		}

	}

	// private void refreshAdapter() {
	// Object adapter = locationListview.getAdapter();
	// RestaurantSuggestedDisheAdapter suggestedDisheAdapter = null;
	//
	// suggestedDisheAdapter = (RestaurantSuggestedDisheAdapter) adapter;
	// if (suggestedDisheAdapter != null) {
	// suggestedDisheAdapter.setDishDetailDtos(dishesLists);
	// suggestedDisheAdapter.notifyDataSetChanged();
	// }
	// }

	private void refreshAdapter() {
		Object adapter = listview.getAdapter();
		SearchDishesAdapter suggestedDisheAdapter = null;
		if (!(adapter instanceof SearchDishesAdapter)) {
			suggestedDisheAdapter = new SearchDishesAdapter(getActivity(), 0,
					feedResponseDto.getResults());
			
			listview.setAdapter(suggestedDisheAdapter);
		} else {
			suggestedDisheAdapter = (SearchDishesAdapter) adapter;
			
			if (suggestedDisheAdapter != null) {
				suggestedDisheAdapter.setFeedDtos(this.feedResponseDto
						.getResults());
				suggestedDisheAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void setResponse(Object object) {

		if (object == null) {
			return;
		}

		if (object instanceof FeedResponseDto) {
			addResult((FeedResponseDto) object);
			listview.setOnItemClickListener(null);
			refreshAdapter();
		}

	}

	private void addResult(FeedResponseDto newDishResponseDto) {

		if (newDishResponseDto != null
				&& newDishResponseDto.getResults() != null
				&& newDishResponseDto.getResults().size() > 0) {
			List<FeedDetailDto> newDishDto = newDishResponseDto.getResults();

			feedsList = new ArrayList<FeedDetailDto>();

			if (newDishDto != null && newDishDto.size() > 0) {

				for (FeedDetailDto feed : newDishDto) {
					BranchDishIdDto branchDishIdDto = new BranchDishIdDto();
					DishIdDto dishIdDto = feed.getDishId();
					String branchName = feed.getBranchName();

					int oneTag = feed.getOneTag();
					// String oneTagString=feed.getOneTag();
					// int oneTag=Integer.parseInt(oneTagString);

					String branchDishId = feed.getObjectId();
					BranchIdDto branchIdDto = feed.getBranchId();
					CityDto cityDto = feed.getCity();
					LocationDto locationDto = feed.getLocation();
					PlaceDto placeDto = feed.getPlace();
					PointDto pointDto = feed.getPoint();

					if (branchDishId != null && branchDishId.length() > 0) {
						branchDishIdDto.setObjectId(branchDishId);
					}
					if (branchIdDto != null) {
						branchDishIdDto.setBranchId(branchIdDto);
					}
					if (cityDto != null) {
						branchDishIdDto.setCity(cityDto);
					}
					if (locationDto != null) {
						branchDishIdDto.setLocation(locationDto);
					}
					if (placeDto != null) {
						branchDishIdDto.setPlace(placeDto);
					}
					if (pointDto != null) {
						branchDishIdDto.setPoint(pointDto);
					}

					if (dishIdDto != null) {

						branchDishIdDto.setDishId(dishIdDto);
					}

					if (branchName != null && branchName.length() > 0) {
						branchDishIdDto.setBranchName(branchName);
					}

					// if(oneTag!=null && oneTag.length()>0)
					// {
					// branchDishIdDto.setOneTag(oneTag);
					// }

					if (oneTag != 0) {
						branchDishIdDto.setOneTag(oneTag);
					}

					feed.setBranchDishId(branchDishIdDto);

					feedsList.add(feed);

				}
			}

			this.feedResponseDto.getResults().addAll(feedsList);
		}

		else {
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
