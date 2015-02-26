package com.svaad.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.facebook.Session;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.adapter.RestaurantListAdapter;
import com.svaad.asynctask.PopularRestaurantAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.PopularReataurantRequestDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.whereDto.PopularRestaurantWhereDto;

public class PopularRestaurantsActivity extends BaseActivity implements
		SvaadFeedCallback, OnScrollListener,SvaadProgressCallback{

	ListView listView;
	RestaurantListResponseDto restaurantListResponseDto;

	RestaurantListAdapter adapter;
	private ProgressBar pbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.explore_fragment_layout);
		
		final ActionBar actionBar = getActionBar();
		setSupportProgressBarIndeterminateVisibility(false);

		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setDisplayShowTitleEnabled(true);
		

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		
		actionBar.setTitle("Popular Restaurants");
		
		init();
		listView.setOnScrollListener(this);

		initializeList();

		getRestaurantsList();

		adapter = new RestaurantListAdapter(PopularRestaurantsActivity.this, 0,
				restaurantListResponseDto.getResults());

		listView.setAdapter(adapter);

	}

	private void getRestaurantsList() {
		new PopularRestaurantAsynctask(PopularRestaurantsActivity.this, null,
				getPopularRestaurentRequestDto()).execute();

	}

	private PopularReataurantRequestDto getPopularRestaurentRequestDto() {

		PopularReataurantRequestDto restaurentRequestDto = new PopularReataurantRequestDto();

		PopularRestaurantWhereDto whereDto = new PopularRestaurantWhereDto();

		whereDto.setPublish(true);
		whereDto.setIsPopular(true);

		restaurentRequestDto.setWhere(whereDto);
		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setOrder("-updatedAt");

		restaurentRequestDto.setLimit(1000);

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

		if (restaurantListResponseDto instanceof RestaurantListResponseDto)
		{
			addResult((RestaurantListResponseDto) restaurantListResponseDto);
			RestaurantListAdapter restaurantListAdapter = (RestaurantListAdapter) listView.getAdapter();
			if (restaurantListAdapter != null) 
			{
				restaurantListAdapter.setRestaurantDetailsDtos(this.restaurantListResponseDto.getResults());
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


