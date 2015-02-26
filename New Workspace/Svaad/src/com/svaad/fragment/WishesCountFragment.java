package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
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
import com.svaad.adapter.WishesAdapter;
import com.svaad.asynctask.WishCountAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Constants;

public class WishesCountFragment extends Fragment implements OnScrollListener,
		SvaadFeedCallback,SvaadProgressCallback {

	GridView gv;
	public long skip = 0;
	private int totalItemCount = -1;
	TextView tvNoResults;
	FeedResponseDto feedResponseDto;
	View view;
	private FragmentTransaction fragmentTransaction;
	private List<FeedDetailDto> feedsList;
	private FeedDetailDto restaurantDetailsDto;
	private ProgressBar pbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 if (savedInstanceState == null)
//		 {
//			 getFeedsList();
//		 }

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.wish_fullmenu, container, false);

		init(view);

		gv.setOnScrollListener(this);

		initializeFeed();
		
		restaurantDetailsDto = (FeedDetailDto) (getArguments() != null ? getArguments()
				.getSerializable(Constants.DATA) : null);
		
		

		getFeedsList();
		gv.setAdapter(new WishesAdapter(getActivity(), R.id.gridView, gv,
				feedResponseDto.getResults()));

		return view;
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		getFeedsList();
//	}

	public void init(View view) {
		gv = (GridView) view.findViewById(R.id.gridView);
		tvNoResults = (TextView) view.findViewById(R.id.tvNoResults);
		pbar = (ProgressBar) view.findViewById(R.id.pbar);
		
	}

	private void getFeedsList() {

		new WishCountAsynctask(getActivity(), this,restaurantDetailsDto).execute();

	}

	private void initializeFeed() {
		if (feedResponseDto == null) {
			feedResponseDto = new FeedResponseDto();
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		} else if (feedResponseDto.getResults() == null) {
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount;
			getFeedsList();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void setResponse(Object feedResponseDto) {
		if (feedResponseDto == null) {
			return;
		}
		addResult((FeedResponseDto) feedResponseDto);
		WishesAdapter feedAdapter = (WishesAdapter) gv.getAdapter();
		if (feedAdapter != null) {
			feedAdapter.setFeedDtos(this.feedResponseDto.getResults());
			feedAdapter.notifyDataSetChanged();
		}

	}

	private void addResult(FeedResponseDto feedResponseDto) {
		if (feedResponseDto != null && feedResponseDto.getResults() != null) {
			List<FeedDetailDto> feedDetailDto = feedResponseDto.getResults();
			// if(feedDetailDto!=null && feedDetailDto.size()>0)
			// {
			// this.feedResponseDto.getResults().addAll(feedDetailDto);
			// }
			feedsList = new ArrayList<FeedDetailDto>();

			if (feedDetailDto != null && feedDetailDto.size() > 0) {

				for (FeedDetailDto feed : feedDetailDto) {
					// FeedDetailDto feedDetail=new FeedDetailDto();
					BranchDishIdDto branchDishIdDto = new BranchDishIdDto();

					DishIdDto dishIdDto = feed.getDishId();

					String branchDishId = feed.getObjectId();
					BranchIdDto branchIdDto = feed.getBranchId();
					CityDto cityDto = feed.getCity();
					LocationDto locationDto = feed.getLocation();
					PlaceDto placeDto = feed.getPlace();
					PointDto pointDto = feed.getPoint();

					String branchName = feed.getBranchName();
					int oneTag = feed.getOneTag();

					// String oneTagString=feed.getOneTag();
					// int oneTag=Integer.parseInt(oneTagString);

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
				this.feedResponseDto.getResults().addAll(feedsList);
			}

			// else
			// {
			// tvNoResults.setVisibility(View.VISIBLE);
			// tvNoResults.setText("No Data");
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

}
