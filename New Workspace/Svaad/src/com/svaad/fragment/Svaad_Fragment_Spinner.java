package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.FoodiesActivity;
import com.svaad.activity.HomeActivity;
import com.svaad.adapter.SvaadAdapter;
import com.svaad.asynctask.SvaadAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.LocationUtil;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class Svaad_Fragment_Spinner extends Fragment implements
		SvaadFeedCallback, OnClickListener, OnScrollListener,
		SvaadProgressCallback {

	LinearLayout llContainer;
	ListView listView;
	public long skip = 0;
	public int totalItemCount = -1;
	boolean scroll = false;
	RelativeLayout rlLogin;
	Location location;
	boolean noLocation = false;
	private ProgressBar pbar;
	FeedResponseDto feedResponseDto;
	private View convertView;
	Button btnLogin, btnJoin, btnFoodies, btnCamera;

	ImageButton btnRefresh;
	Spinner spinner;
	SvaadAdapter adapter;
	ArrayAdapter<String> spinnerAdapter;
	TextView tvMessage;
	List<String> spinnnerLists = new ArrayList<String>();
	View mFooterView;
	boolean processingRequest = false;
	String spinnerValue = null;
	String imageurl = "http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/b60c0c9c-e8a3-4852-a2a8-ea17e67181ea-stetakhs.jpg";
	private ImageView imageview;
	View view = null;

	Uri imageUri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.svaad_fragment_layout, container,
				false);
		init(view);
		LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.svaad_header_spinner_layout, null);

		mFooterView = LayoutInflater.from(getActivity()).inflate(
				R.layout.progress, null);

		initHeaderView(convertView);
		initializeSpinnerItems();
		initializeFeed();

		// getimageUri();

		// listView.addHeaderView(convertView);
		// listView.setAdapter(null);

		loadInitialListView();

		spinner.post(new Runnable() {

			@Override
			public void run() {

				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						if (spinnerValue != null
								&& spinnerValue.equalsIgnoreCase(spinner
										.getSelectedItem().toString()))
							return;
						spinnerValue = spinner.getSelectedItem().toString();
						loadInitialListView();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

			}
		});
		return view;

	}

	private void loadInitialListView() {
		if (spinnerValue == null){
			spinnerValue = Constants.EXPLORE_HYDERABAD;
			selectSpinner(spinnerValue);
		}

		scroll = false;
		clearResults();
		if (spinnerValue.equalsIgnoreCase(Constants.EXPLORE_HYDERABAD)) {
			tvMessage.setVisibility(View.GONE);
			getSvaadHomeAllLocationsList(
					Constants.DISH_LOCATION_SPINNER_ALL_LOCATION,
					Constants.DISH_CATEGORY_TYPE_EVERYONE);
			callListview();
		} else if (spinnerValue.equalsIgnoreCase(Constants.EXPLORE_NEAR_BY)) {
			tvMessage.setVisibility(View.GONE);
			checkGps(Constants.DISH_LOCATION_SPINNER_NEARBY,
					Constants.DISH_CATEGORY_TYPE_EVERYONE, false);
			callListview();
		} else if (spinnerValue.equalsIgnoreCase(Constants.FOLLOWING_NEAR_BY)) {
			if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
					&& Utils.getFromSharedPreference(
							Constants.USER_OBJECT_ID_KEY).length() > 0) {

				checkGps(Constants.DISH_LOCATION_SPINNER_NEARBY,
						Constants.DISH_CATEGORY_TYPE_FEED, false);

				callListview();
			} else {
				showLoginMessage();
			}
		} else if (spinnerValue.equalsIgnoreCase(Constants.FOLLOWING)) {
			if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
					&& Utils.getFromSharedPreference(
							Constants.USER_OBJECT_ID_KEY).length() > 0) {
				getSvaadHomeAllLocationsList(
						Constants.DISH_LOCATION_SPINNER_ALL_LOCATION,
						Constants.DISH_CATEGORY_TYPE_FEED);
				callListview();
			} else
				showLoginMessage();
		}

	}

	private void showLoginMessage() {
		if (listView.getFooterViewsCount() > 0) {
			listView.removeFooterView(mFooterView);
		}
		if (adapter != null) {
			adapter.clear();
			listView.setAdapter(null);
			adapter.notifyDataSetChanged();
		}
		tvMessage.setVisibility(View.VISIBLE);
		tvMessage
				.setText("This is your feed.When you follow friends and foodies their dish recommendations will show up here.");
	}

	private void loadScrollListView() {
		scroll = true;
		if (spinnerValue.equalsIgnoreCase(Constants.EXPLORE_HYDERABAD)) {
			if (listView.getFooterViewsCount() <= 0) {
				listView.addFooterView(mFooterView);
			}

			getSvaadHomeAllLocationsList(
					Constants.DISH_LOCATION_SPINNER_ALL_LOCATION,
					Constants.DISH_CATEGORY_TYPE_EVERYONE);
		} else if (spinnerValue.equalsIgnoreCase(Constants.FOLLOWING)) {
			if (listView.getFooterViewsCount() <= 0) {
				listView.addFooterView(mFooterView);
			}

			getSvaadHomeAllLocationsList(
					Constants.DISH_LOCATION_SPINNER_ALL_LOCATION,
					Constants.DISH_CATEGORY_TYPE_FEED);
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
				loadInitialListView();

				noLocation = false;

			}
		}

	}

	public void loadAllLocations() {
		String value = null;
		if (spinnerValue.equalsIgnoreCase(Constants.EXPLORE_NEAR_BY))
			value = Constants.EXPLORE_HYDERABAD;
		else if (spinnerValue.equalsIgnoreCase(Constants.FOLLOWING_NEAR_BY))
			value = Constants.FOLLOWING;
		selectSpinner(value);
		// loadInitialListView();
	}

	private void selectSpinner(String value) {

		if (value == null)
			return;
		if (value.equalsIgnoreCase(Constants.EXPLORE_NEAR_BY))
			spinner.setSelection(0);
		else if (value.equalsIgnoreCase(Constants.EXPLORE_HYDERABAD))
			spinner.setSelection(1);
		else if (value.equalsIgnoreCase(Constants.FOLLOWING_NEAR_BY))
			spinner.setSelection(2);
		else if (value.equalsIgnoreCase(Constants.FOLLOWING))
			spinner.setSelection(3);

	}

	public void locationFound() {

		loadInitialListView();
	}

	private void initializeSpinnerItems() {

		spinnnerLists.add(Constants.EXPLORE_NEAR_BY);
		spinnnerLists.add(Constants.EXPLORE_HYDERABAD);
		spinnnerLists.add(Constants.FOLLOWING_NEAR_BY);
		spinnnerLists.add(Constants.FOLLOWING);
		spinnerAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, spinnnerLists);
		// spinnerAdapter = new ArrayAdapter<String>(getActivity(),
		// R.layout.spinner_item, spinnnerLists);
		spinner.setAdapter(spinnerAdapter);
	}

	public void init(View v) {

		listView = (ListView) v.findViewById(R.id.listView);

		spinner = (Spinner) v.findViewById(R.id.spinner1);

		btnCamera = (Button) v.findViewById(R.id.btnCamera);

		btnRefresh = (ImageButton) v.findViewById(R.id.btnRefresh);

		btnRefresh.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		listView.setOnScrollListener(this);
		listView.setDivider(null);

	}

	private void initializeFeed() {
		if (feedResponseDto == null) {
			feedResponseDto = new FeedResponseDto();
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		} else if (feedResponseDto.getResults() == null) {
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		}
	}

	private void initHeaderView(View convertView) {

		pbar = (ProgressBar) convertView.findViewById(R.id.pbar);

		tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);

		rlLogin = (RelativeLayout) convertView.findViewById(R.id.rlLogin);
		imageview = (ImageView) convertView.findViewById(R.id.imageView1);

		if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
				&& Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY)
						.length() > 0) {
			rlLogin.setVisibility(View.GONE);
		} else {
			rlLogin.setVisibility(View.VISIBLE);
		}

		if (imageurl != null && imageurl.length() > 0) {
			Picasso.with(getActivity()).load(imageurl)
					.placeholder(R.drawable.temp).error(R.drawable.temp)

					.into(imageview);
		}

		// spinner = (Spinner) convertView.findViewById(R.id.spinner1);

		llContainer = (LinearLayout) convertView.findViewById(R.id.llContainer);
		btnLogin = (Button) convertView.findViewById(R.id.btnLogin);
		btnJoin = (Button) convertView.findViewById(R.id.btnJoin);
		// btnFoodies = (Button) convertView.findViewById(R.id.btnFoodies);

		// btnRefresh = (ImageButton) convertView.findViewById(R.id.btnRefresh);

		btnJoin.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		// btnFoodies.setOnClickListener(this);
		// btnRefresh.setOnClickListener(this);
	}

	@Override
	public void progressOn() {

		pbar.setVisibility(View.VISIBLE);

	}

	@Override
	public void progressOff() {

		pbar.setVisibility(View.GONE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {

			// if (totalItemCount < 5)
			// return;
			// scroll = true;
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount - listView.getHeaderViewsCount()
					- listView.getFooterViewsCount();

			if (totalItemCount < (5 + listView.getHeaderViewsCount() + listView
					.getFooterViewsCount()))
				return;

			loadScrollListView();
		}

	}
	
	

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnJoin:
			// new SvaadDialogs().showSvaadSignUpDialog(getActivity(), null,
			// 1,Constants.SVAAD,Constants.SVAAD_JOIN);
			String userId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (userId == null) {

				new SvaadDialogs().showSvaadSignUpDialog(getActivity(), null,
						1, Constants.SVAAD, Constants.SVAAD_JOIN);
			} else {
				Intent intent = new Intent(getActivity(), HomeActivity.class);
				intent.putExtra(Constants.PAGER, 1);
				getActivity().startActivity(intent);
				((Activity) getActivity()).finish();
			}

			break;
		case R.id.btnLogin:

			// new SvaadDialogs().showSvaadLoginDialog(getActivity(), null, 1,
			// Constants.SVAAD, Constants.SVAAD_LOGIN);

			String userId1 = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (userId1 == null) {

				new SvaadDialogs().showSvaadLoginDialog(getActivity(), null, 1,
						Constants.SVAAD, Constants.SVAAD_LOGIN);
			} else {
				Intent intent = new Intent(getActivity(), HomeActivity.class);
				// intent.putExtra(Constants.PAGER, Constants.PAGER_MODE_FEED);
				intent.putExtra(Constants.PAGER, 1);
				getActivity().startActivity(intent);
				((Activity) getActivity()).finish();
			}

			break;
		case R.id.facebookButton:
			break;

		case R.id.btnRefresh:

			// tvlt.setText(null);

			location = LocationUtil.getInstance(getActivity())
					.getCurrentLocation();

			loadInitialListView();

			// if (spinnerValue != null && spinnerValue.length() > 0) {
			// if (spinnerValue.equalsIgnoreCase(Constants.EXPLORE_HYDERABAD)) {
			// // tvMessage.setVisibility(View.GONE);
			// loadInitialListView();
			// } else if (spinnerValue
			// .equalsIgnoreCase(Constants.EXPLORE_NEAR_BY)) {
			// // tvMessage.setVisibility(View.GONE);
			// location = LocationUtil.getInstance(getActivity())
			// .getCurrentLocation();
			// loadInitialListView();
			// } else if (spinnerValue
			// .equalsIgnoreCase(Constants.FOLLOWING_NEAR_BY)) {
			// if (Utils
			// .getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
			// && Utils.getFromSharedPreference(
			// Constants.USER_OBJECT_ID_KEY).length() > 0) {
			// location = LocationUtil.getInstance(getActivity())
			// .getCurrentLocation();
			// loadInitialListView();
			//
			// } else {
			// showLoginMessage();
			// }
			// } else if (spinnerValue.equalsIgnoreCase(Constants.FOLLOWING)) {
			// if (Utils
			// .getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
			// && Utils.getFromSharedPreference(
			// Constants.USER_OBJECT_ID_KEY).length() > 0) {
			// loadInitialListView();
			// } else {
			// showLoginMessage();
			// }
			// }
			// }
			break;

		case R.id.btnFoodies:
			Intent i = new Intent(getActivity(), FoodiesActivity.class);
			startActivity(i);
			// getActivity().finish();
			break;

		case R.id.btnCamera:
			// Intent iCam = new Intent(getActivity(), MainActivity.class);
			// Intent iCam = new Intent(getActivity(),
			// CopyOfMainActivity.class);
			// startActivity(iCam);
			String userid = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
			if (userid != null && userid.length() > 0) {
				String mediaPath = Utils.getMediaPath();

				new SvaadDialogs().showPhotoDialog(getActivity(), mediaPath);
			} else {
				new SvaadDialogs().showSvaadLoginAlertDialog(getActivity(),
						null, Constants.RES_MODE, Constants.Cam_ate,
						Constants.ATE_IT);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void setResponse(Object feedResponseDto) {
		processingRequest = false;
		/*
		 * if(listView.getFooterViewsCount()>0)
		 * listView.addFooterView(mFooterView);
		 */
		if (adapter == null)
			callListview();
		if (feedResponseDto == null) {
			/*
			 * if (listView.getFooterViewsCount() > 0) {
			 * listView.removeFooterView(mFooterView); }
			 */
			return;
		}
		addResult((FeedResponseDto) feedResponseDto);

		refreshAdapter();
	}

	private void refreshAdapter() {
		HeaderViewListAdapter adapter = (HeaderViewListAdapter) listView
				.getAdapter();
		if (!(adapter.getWrappedAdapter() instanceof SvaadAdapter)) {
			return;
		}
		SvaadAdapter feedAdapter = (SvaadAdapter) adapter.getWrappedAdapter();
		if (feedAdapter != null) {
			feedAdapter.setFeedDtos(this.feedResponseDto.getResults());
			feedAdapter.notifyDataSetChanged();
		}
	}

	private void addResult(FeedResponseDto feedResponseDto) {
		if (feedResponseDto != null && feedResponseDto.getResults() != null
				&& feedResponseDto.getResults().size() > 0) {
			List<FeedDetailDto> feedDto = feedResponseDto.getResults();
			if (feedDto != null && feedDto.size() > 0) {
				this.feedResponseDto.getResults().addAll(feedDto);

			}

		}

	}

	private void getSvaadHomeAllLocationsList(String check, String btnText) {
		if (processingRequest)
			return;
		processingRequest = true;
		new SvaadAsynctask(getActivity(), this, check, btnText, scroll)
				.execute();

	}

	public void clearResults() {
		skip = 0;
		totalItemCount = -1;
		try {
			if (listView != null && listView.getFooterViewsCount() > 0)
				listView.removeFooterView(mFooterView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		feedResponseDto.getResults().clear();
	}

	public void callListview() {
		if (adapter != null) {
			adapter.clear();
			listView.setAdapter(null);
			adapter.notifyDataSetChanged();
		}
		if (listView.getHeaderViewsCount() > 0) {

		} else {
			listView.addHeaderView(convertView);
		}

		adapter = new SvaadAdapter(getActivity(), R.id.listView, listView,
				feedResponseDto.getResults());
		listView.setAdapter(adapter);

	}

	private void checkGps(String check, String btnText, boolean inScroll) {
		LocationUtil.getInstance(getActivity()).checkNetworkStatus();
		LocationUtil.getInstance(getActivity()).registerLocationListener();
		location = LocationUtil.getInstance(getActivity()).getCurrentLocation();
		if (!(LocationUtil.getInstance(getActivity()).isGpsEnabled())
				&& !(LocationUtil.getInstance(getActivity()).isNetworkEnabled())
				&& !inScroll) {
			noLocation = true;
			new SvaadDialogs().showGPSDisabledAlertToUserForHome(getActivity(),
					this);

		} else if (location == null && !inScroll) {
			new SvaadDialogs().showGPSLocationWaiting(getActivity(), this);

		}

		else if (location != null) {
			// new SvaadDialogs().showToast(getActivity(),
			// location.getLatitude()
			// + " " + location.getLongitude());

			// Toast.makeText(getActivity(),
			// location.getLatitude()+" "+location.getLongitude(),
			// 600000).show();

			if (adapter != null && !inScroll) {
				adapter.clear();
				listView.setAdapter(null);
				adapter.notifyDataSetChanged();
			}

			// listView.addHeaderView(convertView);

			if (btnText.equalsIgnoreCase("Feed")) {

				if (listView.getHeaderViewsCount() > 0) {

				} else {
					listView.addHeaderView(convertView);
				}

				getSvaadHomeNearbyList(check, btnText, location.getLatitude(),
						location.getLongitude());

			}

			else if (btnText.equalsIgnoreCase("Everyone")) {

				if (listView.getHeaderViewsCount() > 0) {

				} else {
					listView.addHeaderView(convertView);
				}

				getSvaadHomeNearbyList(check, btnText, location.getLatitude(),
						location.getLongitude());

			}

		} else {
			return;
		}
	}

	private void getSvaadHomeNearbyList(String check, String btnText,
			double latitude, double longitude) {
		if (processingRequest)
			return;
		processingRequest = true;
		new SvaadAsynctask(getActivity(), this, check, btnText, latitude,
				longitude, scroll).execute();

	}

}
