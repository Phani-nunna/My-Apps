package com.svaad.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.Dto.SearchPointDto;
import com.svaad.adapter.RestaurantListAdapter;
import com.svaad.asynctask.RestaurantAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.RestaurentRequestDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.LocationUtil;
import com.svaad.utils.SvaadDialogs;
import com.svaad.whereDto.RestaurantListWhereDto;

public class NearByRestaurantsActivity extends BaseActivity implements
		SvaadFeedCallback, OnScrollListener,SvaadProgressCallback {

	ListView listView;
	RestaurantListResponseDto restaurantListResponseDto;
	private int distanceInKilometers = 5;
	RestaurantListAdapter adapter;
	private Location location;
	boolean noLocation=false;
	private ProgressBar pbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.explore_fragment_layout);
		LocationUtil.getInstance(getApplicationContext()).checkNetworkStatus();
		LocationUtil.getInstance(getApplicationContext()).registerLocationListener();
		
		

		final ActionBar actionBar = getActionBar();
		setSupportProgressBarIndeterminateVisibility(false);

		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setTitle("Near by Restaurants");

		init();
		listView.setOnScrollListener(this);

		initializeList();
		
		

		location = LocationUtil.getInstance(getApplicationContext()).getCurrentLocation();
		getNearByLocation();

		adapter = new RestaurantListAdapter(NearByRestaurantsActivity.this, 0,
				restaurantListResponseDto.getResults());

		listView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.refresh_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void getNearByLocation() {

		if(!(LocationUtil.getInstance(getApplicationContext()).isGpsEnabled() )&& !(LocationUtil.getInstance(getApplicationContext()).isNetworkEnabled()))
		{
//			new SvaadDialogs().showToast(getApplicationContext(),
//					" No network provider is enabled");	
			noLocation=true;			
			new SvaadDialogs().showGPSDisabledAlertToUser(NearByRestaurantsActivity.this);
		}
		else if (location != null) {
			
//			new SvaadDialogs().showToast(NearByRestaurantsActivity.this, "latilude:"+location.getLatitude()+" Longitude:"+location.getLongitude());

			RestaurantAsynctask asy = new RestaurantAsynctask(
					NearByRestaurantsActivity.this, null,
					getNearByRestaurentRequestDto());
			asy.execute();
		} else {
			return;
		}

	}
	@Override
	protected void onResume() {
	
		super.onResume();
		if(noLocation)
		{
			LocationUtil.getInstance(getApplicationContext()).registerLocationListener();
			LocationUtil.getInstance(getApplicationContext()).checkNetworkStatus();
			if(LocationUtil.getInstance(getApplicationContext()).isGpsEnabled() || LocationUtil.getInstance(getApplicationContext()).isNetworkEnabled())
			{
				Intent i = new Intent(NearByRestaurantsActivity.this, NearByRestaurantsActivity.class);
				startActivity(i);
				finish();
			}
		}
	}

	private RestaurentRequestDto getNearByRestaurentRequestDto() {
		RestaurentRequestDto restaurentRequestDto = new RestaurentRequestDto();
		RestaurantListWhereDto whereDto = new RestaurantListWhereDto();

		if (location != null) {

			SearchPointDto searchPointDto = new SearchPointDto();
			PointDto pointDto = new PointDto();

			pointDto.set__type("GeoPoint");
			pointDto.setLongitude(location.getLongitude());
			pointDto.setLatitude(location.getLatitude());

			searchPointDto.set$nearSphere(pointDto);
			searchPointDto.set$maxDistanceInKilometers(distanceInKilometers);
			whereDto.setPoint(searchPointDto);
		}

		whereDto.setPublish(true);

		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setLimit(30);
		restaurentRequestDto.setWhere(whereDto);
		return restaurentRequestDto;
	}

	public void init() {

		listView = (ListView) findViewById(R.id.listView);
		
		pbar = (ProgressBar) findViewById(R.id.pbar);

	}

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

	@Override
	public void setResponse(Object restaurantListResponseDto) {

		if (restaurantListResponseDto == null) {
			return;
		}

		if (restaurantListResponseDto instanceof RestaurantListResponseDto) {
			addResult((RestaurantListResponseDto) restaurantListResponseDto);
			RestaurantListAdapter restaurantListAdapter = (RestaurantListAdapter) listView
					.getAdapter();
			if (restaurantListAdapter != null) {
				restaurantListAdapter
						.setRestaurantDetailsDtos(this.restaurantListResponseDto
								.getResults());
				restaurantListAdapter.notifyDataSetChanged();
			}

		}

	}

	private void addResult(RestaurantListResponseDto restaurantListResponseDto) {
		if (restaurantListResponseDto != null
				&& restaurantListResponseDto.getResults() != null) {
			List<RestaurantDetailsDto> restaurantDeatailDto = restaurantListResponseDto
					.getResults();
			this.restaurantListResponseDto.getResults().addAll(
					restaurantDeatailDto);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case R.id.menu_load:

			Intent i = new Intent(NearByRestaurantsActivity.this, NearByRestaurantsActivity.class);
			startActivity(i);
			finish();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
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
