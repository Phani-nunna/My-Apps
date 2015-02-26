package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.Dto.SvaadPointDto;
import com.svaad.activity.SearchDish_Activity;
import com.svaad.adapter.CamSearchAdapter;
import com.svaad.asynctask.CamSearchRestaurantAsynctask;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.CamRestaurentRequestDto;
import com.svaad.responseDto.CamResSearchResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.LocationUtil;
import com.svaad.utils.SvaadDialogs;
import com.svaad.whereDto.CamRestaurantListWhereDto;

public class Search_Res_Fragment extends Fragment implements OnClickListener,
		SvaadFeedCallback, SvaadProgressCallback {

	ListView listview;

	String queryText, path;

	private CamResSearchResponseDto restaurantListResponseDto;

	private ProgressBar pbar;
	CamSearchAdapter adapter;

	private TextView tvNoRes;

	private Location location;
	boolean noLocation = false;

	private EditText editext;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.search_res_layout, container, false);
		init(view);
		initializeList();

		Bundle bundle = getArguments();
		if (bundle != null) {
			path = bundle.getString("path");
		}
		
		editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					hideKeyboard();
					queryText = editext.getText().toString();
					if (queryText != null && queryText.length() > 0
							&& !queryText.equals("")) {


						getSearchList();
						
						if(adapter!=null)
						{
							tvNoRes.setVisibility(View.GONE);
							adapter.clear();
							listview.setAdapter(null);
							adapter.notifyDataSetChanged();
							adapter=new  CamSearchAdapter(getActivity(), 0,
									restaurantListResponseDto.getResults());
							listview.setAdapter(adapter);
						}
						else
						{
							adapter=new  CamSearchAdapter(getActivity(), 0,
									restaurantListResponseDto.getResults());
							listview.setAdapter(adapter);
						}
						
					
//						listview.setAdapter(new CamSearchAdapter(getActivity(), 0,
//								restaurantListResponseDto.getResults()));
						return true;
					}
				}

				return false;
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			private DatabaseDAO da;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (restaurantListResponseDto.getResults() != null) {
					CamRestaurantDetailDto restautantDetail = restaurantListResponseDto
							.getResults().get(arg2);
					if (restautantDetail != null) {

						try {
							da = new DatabaseDAO(getActivity());
							da.createResHistoryTable(restautantDetail);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Intent i = new Intent(getActivity(),
								SearchDish_Activity.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
						i.putExtra(Constants.DATA, restautantDetail);
						if (path != null) {
							i.putExtra("path", path);
						}
						startActivity(i);
//						getActivity().finish();

//						getActivity().overridePendingTransition(
//								R.anim.slide_up, R.anim.slide_out);
					}

				}
			}
		});

		return view;
	}

	private CamRestaurentRequestDto getRestaurentRequestDtoForLocation() {
		CamRestaurentRequestDto restaurentRequestDto = new CamRestaurentRequestDto();
		CamRestaurantListWhereDto whereDto = new CamRestaurantListWhereDto();

		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {

			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
			whereDto.setNameTags(searchTagsDto);
		}

		whereDto.setPublish(true);

		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setKeys("branchName,location");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setSkip(0);
		restaurentRequestDto.setLimit(20);
		restaurentRequestDto.setWhere(whereDto);
		return restaurentRequestDto;
	}

	private CamRestaurentRequestDto getNearByDtoForLocation() {
		CamRestaurentRequestDto restaurentRequestDto = new CamRestaurentRequestDto();
		CamRestaurantListWhereDto whereDto = new CamRestaurantListWhereDto();

		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {

			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
			whereDto.setNameTags(searchTagsDto);
		}

		if (location != null) {
			SvaadPointDto svaadPointDto = new SvaadPointDto();

			PointDto pointDto = new PointDto();
			pointDto.setLatitude(location.getLatitude());
			pointDto.setLongitude(location.getLongitude());
			pointDto.set__type("GeoPoint");

			svaadPointDto.set$nearSphere(pointDto);
			whereDto.setPoint(svaadPointDto);
		}

		whereDto.setPublish(true);

		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setKeys("branchName,location");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setSkip(0);
		restaurentRequestDto.setLimit(20);
		restaurentRequestDto.setWhere(whereDto);
		return restaurentRequestDto;
	}

	private void initializeList() {
		if (restaurantListResponseDto == null) {
			restaurantListResponseDto = new CamResSearchResponseDto();
			restaurantListResponseDto
					.setResults(new ArrayList<CamRestaurantDetailDto>());
		} else if (restaurantListResponseDto.getResults() == null) {
			restaurantListResponseDto
					.setResults(new ArrayList<CamRestaurantDetailDto>());
		}
	}

	private void getSearchList() {
		CamSearchRestaurantAsynctask asy = new CamSearchRestaurantAsynctask(
				getActivity(), this, getRestaurentRequestDtoForLocation());
		asy.execute();
	}

	private void getNearBySearchList() {
		CamSearchRestaurantAsynctask asy = new CamSearchRestaurantAsynctask(
				getActivity(), this, getNearByDtoForLocation());
		asy.execute();
	}

	private void init(View view) {
		listview = (ListView) view.findViewById(R.id.listView);
		pbar = (ProgressBar) view.findViewById(R.id.pbar);
		tvNoRes = (TextView) view.findViewById(R.id.tvNoRes);
		editext = (EditText)view. findViewById(R.id.editTextRes);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void setResponse(Object restaurantListResponseDto) {

		if (restaurantListResponseDto == null
				&& (restaurantListResponseDto instanceof CamResSearchResponseDto)) {
			return;
		}
		addResult((CamResSearchResponseDto) restaurantListResponseDto);
		CamSearchAdapter restaurantListAdapter = (CamSearchAdapter) listview
				.getAdapter();
		if (restaurantListAdapter != null) {
			restaurantListAdapter.setFeedDtos(this.restaurantListResponseDto
					.getResults());
			restaurantListAdapter.notifyDataSetChanged();
		}

	}

	private void addResult(CamResSearchResponseDto newRestaurantListResponseDto) {
		if (newRestaurantListResponseDto != null
				&& newRestaurantListResponseDto.getResults() != null
				&& newRestaurantListResponseDto.getResults().size() > 0) {
			List<CamRestaurantDetailDto> newRestaurantDetailsDtos = newRestaurantListResponseDto
					.getResults();
			this.restaurantListResponseDto.getResults().addAll(
					newRestaurantDetailsDtos);
		} else {

			tvNoRes.setVisibility(View.VISIBLE);
			// if (scroll == true) {
			// tvNoResults.setVisibility(View.INVISIBLE);
			// } else {
			// tvNoResults.setVisibility(View.VISIBLE);
			// tvNoResults.setText("No results found");
			// }
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

	private void checkGps() {
		LocationUtil.getInstance(getActivity()).checkNetworkStatus();
		LocationUtil.getInstance(getActivity()).registerLocationListener();
		location = LocationUtil.getInstance(getActivity()).getCurrentLocation();

		if (!(LocationUtil.getInstance(getActivity()).isGpsEnabled())
				&& !(LocationUtil.getInstance(getActivity()).isNetworkEnabled())) {
			noLocation = true;
			new SvaadDialogs().showGPSDisabledAlertToCamActivity(getActivity(),
					this);

		} else if (location == null) {
			// new SvaadDialogs().showGPSLocationWaiting(getActivity(), this);

			new SvaadDialogs().showToast(getActivity(), "Waiting for Locatoin");

		}

		else if (location != null) {

			getNearBySearchList();
			listview.setAdapter(new CamSearchAdapter(getActivity(), 0,
					restaurantListResponseDto.getResults()));

		}

		else {
			return;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (noLocation) {

			LocationUtil.getInstance(getActivity()).registerLocationListener();
			LocationUtil.getInstance(getActivity()).checkNetworkStatus();
			if (LocationUtil.getInstance(getActivity()).isGpsEnabled()
					|| LocationUtil.getInstance(getActivity())
							.isNetworkEnabled()) {
				// Toast.makeText(getActivity(), "On Resume", 2000).show();
				checkGps();
				noLocation = false;

			}
		}

	}

	public void loadText() {
		tvNoRes.setVisibility(View.VISIBLE);
		tvNoRes.setText("Search for restaurants");
	}
	
	private void hideKeyboard()
	{
		InputMethodManager in = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);

		in.hideSoftInputFromWindow(
				editext.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

}
