package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.HomeDetailDto;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.activity.NearByRestaurantsActivity;
import com.svaad.activity.PopularRestaurantsActivity;
import com.svaad.adapter.HomeSearchAdapter;
import com.svaad.asynctask.HomeAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadNetworkCallBack;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.HomeResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class HomeSearchFragments extends Fragment implements OnClickListener,
		SvaadFeedCallback, SvaadProgressCallback, SvaadNetworkCallBack {

	String queryText;

	SearchView searchview;
	// LinearLayout llNearBy;

	ImageView llNearBy;

	private EditText editext;

	ProgressBar pbar;

	ImageView imagePopular, imageView1, i;

	List<HomeDetailDto> type1Lists = new ArrayList<HomeDetailDto>();
	List<HomeDetailDto> type2Lists = new ArrayList<HomeDetailDto>();

	LinearLayout near, popular;

	Context context;

	private HomeResponseDto homeResponseDto;

	private GridView gv;

	private HomeSearchAdapter adapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	private void getHomeList() {

		try {
			boolean check = Utils.isOnline(getActivity());
			if (check == true) {

				new HomeAsynctask(getActivity(), this).execute();
			}

			else {

				new SvaadDialogs().showNoNetworkDialog(
						getActivity(),
						getActivity().getResources().getString(
								R.string.signin_success), this);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// new HomeAsynctask(getActivity(), this).execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_search_layout, container,
				false);

		initUi(view);

		context = getActivity();
		initializeHome();
		getHomeList();
				
		editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					queryText = editext.getText().toString();
					if (queryText != null && queryText.length() > 0
							&& !queryText.equals("")) {

						// Intent i = new Intent(getActivity(),
						// SearchOverviewActivity.class);
						// i.putExtra(Constants.QUERYTEXT, queryText);
						// startActivity(i);

						Intent intent = new Intent(getActivity(),HomeSearchActivity.class);
						intent.putExtra(Constants.QUERYTEXT, queryText);
						intent.putExtra(Constants.SEE_MORE_DISHES,Constants.SEE_MORE_DISHES);
						startActivity(intent);
						return true;
					}
				}

				return false;
			}
		});

		adapter = new HomeSearchAdapter(getActivity(), 0,
				homeResponseDto.getResults());

		gv.setAdapter(adapter);
		return view;

	}

	private void initializeHome() {
		if (homeResponseDto == null) {
			homeResponseDto = new HomeResponseDto();
			homeResponseDto.setResults(new ArrayList<HomeDetailDto>());
		} else if (homeResponseDto.getResults() == null) {
			homeResponseDto.setResults(new ArrayList<HomeDetailDto>());
		}
	}

	private void initUi(View v) {

		editext = (EditText) v.findViewById(R.id.editText1);

		near = (LinearLayout) v.findViewById(R.id.near);
		popular = (LinearLayout) v.findViewById(R.id.popular);
		

		gv = (GridView) v.findViewById(R.id.gridview);

		llNearBy = (ImageView) v.findViewById(R.id.llNearBy);

		pbar = (ProgressBar) v.findViewById(R.id.pbar);

		near.setOnClickListener(this);
		popular.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.near:

			getNearByRestaurant();
			break;

		case R.id.imageRes:

			HomeDetailDto home = (HomeDetailDto) v.getTag();

			Intent i = new Intent(getActivity(), HomeSearchActivity.class);
			String text = home.getSearch();
			if (text != null) {
				i.putExtra(Constants.QUERYTEXT, text);
				// i.putExtra(Constants.SEE_MORE_RES, Constants.SEE_MORE_RES);
				i.putExtra(Constants.SEE_MORE_DISHES, Constants.SEE_MORE_DISHES);

			}

			startActivity(i);

			break;

		case R.id.dishImage:
			HomeDetailDto homes = (HomeDetailDto) v.getTag();
			Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
			String search = homes.getSearch();
			if (search != null) {
				intent.putExtra(Constants.QUERYTEXT, search);
				intent.putExtra(Constants.SEE_MORE_DISHES,
						Constants.SEE_MORE_DISHES);
			}
			startActivity(intent);
			break;

		case R.id.popular:
			Intent in = new Intent(getActivity(),
					PopularRestaurantsActivity.class);
			startActivity(in);
			break;

		default:
			break;
		}
	}

	private void getNearByRestaurant() {
		Intent i = new Intent(getActivity(), NearByRestaurantsActivity.class);
		// i.putExtra("Near by", "Near by");
		startActivity(i);

	}

	@Override
	public void setResponse(Object object) {

		if (object == null) {
			return;
		}

		if (object instanceof HomeResponseDto) {

			addResult((HomeResponseDto) object);
			// HomeSearchAdapter homeSearchAdapter = (HomeSearchAdapter) gv
			// .getAdapter();
			// if (homeSearchAdapter != null) {
			// homeSearchAdapter.setRestaurantDetailsDtos(type1Lists);
			// homeSearchAdapter.notifyDataSetChanged();
			// }

		}

	}

	private void addResult(HomeResponseDto homeResponseDto) {

		if (homeResponseDto != null && homeResponseDto.getResults() != null)

		{
			List<HomeDetailDto> homeLists = homeResponseDto.getResults();
			if (homeLists != null && homeLists.size() > 0) {

				for (HomeDetailDto home : homeLists) {
					int type = home.getType();
					if (type == 1) {
						type1Lists.add(home);

					} else if (type == 2) {
						type2Lists.add(home);
					}
				}

			}

			List<HomeDetailDto> restaurantDeatailDto = type1Lists;
			this.homeResponseDto.getResults().addAll(restaurantDeatailDto);
		}

		HomeSearchAdapter homeSearchAdapter = (HomeSearchAdapter) gv
				.getAdapter();
		if (homeSearchAdapter != null) {
			homeSearchAdapter.setRestaurantDetailsDtos(type1Lists);
			homeSearchAdapter.notifyDataSetChanged();
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

	@Override
	public void onRetryNetwork() {
		getHomeList();
	}

}
