package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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

import com.svaad.R;
import com.svaad.Dto.AllFoodiesDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.activity.UserProfileActivity;
import com.svaad.adapter.AllFoodiesListAdapter;
import com.svaad.asynctask.AllFoodiesAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.AllFoodiesResponseDto;
import com.svaad.utils.Constants;

public class AllFoodiesFragment extends Fragment implements OnScrollListener,
		SvaadFeedCallback,SvaadProgressCallback {

	private AllFoodiesResponseDto allFoodiesResponseDto;
	public long skip = 0;
	private int totalItemCount = -1;

	ListView foodiesListView;
	private ProgressBar pbar;

	// Button btnInviteFbFriends;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			getAllFoodiesList();
		}
	}

	private void getAllFoodiesList() {
		try {

			new AllFoodiesAsynctask(getActivity(), this).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.all_foodies_layout, container,
				false);

		foodiesListView = (ListView) view.findViewById(R.id.foodiesListview);
		
		pbar=(ProgressBar)view.findViewById(R.id.pbar);

		// btnInviteFbFriends = (Button) view
		// .findViewById(R.id.btnInviteFbFriends);

		foodiesListView.setDivider(null);
		foodiesListView.setOnScrollListener(this);

		initializeFoodies();

		foodiesListView.setAdapter(new AllFoodiesListAdapter(getActivity(), 0,
				allFoodiesResponseDto.getResults()));
		foodiesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				AllFoodiesDto allFoodiesDto = allFoodiesResponseDto
						.getResults().get(arg2);

				// AllFoodiesDto allFoodiesDto=(AllFoodiesDto)arg0.getTag();

				String userObjectId = allFoodiesDto.getObjectId();
				long suggestCount = allFoodiesDto.getSuggestCount();
				long wishlistCount = allFoodiesDto.getWishlistCount();
				String uname = allFoodiesDto.getUname();
				String imageUrl = allFoodiesDto.getDisplayPicUrl();
				int foollowerCount = allFoodiesDto.getFollowerCount();
				List<String> followingUsers = allFoodiesDto.getWishlistArr();

				FeedDetailDto feedDetailDto = new FeedDetailDto();
				FeedUserIdDto feedUserIdDto = new FeedUserIdDto();
				if (userObjectId != null && userObjectId.length() > 0) {
					feedUserIdDto.setObjectId(userObjectId);
				}
				feedUserIdDto.setSuggestCount(suggestCount);
				feedUserIdDto.setWishlistCount(wishlistCount);

				if (uname != null && uname.length() > 0) {
					feedUserIdDto.setUname(uname);
				}

				if (imageUrl != null && imageUrl.length() > 0) {
					feedUserIdDto.setDisplayPicUrl(imageUrl);
				}

				feedUserIdDto.setFollowerCount(foollowerCount);
				feedUserIdDto.setWishlistArr(followingUsers);
				feedDetailDto.setUserId(feedUserIdDto);

				Intent userProfileIntent = new Intent(getActivity(),
						UserProfileActivity.class);

				userProfileIntent.putExtra(Constants.DATA, feedDetailDto);
				// userProfileIntent.putExtra("me", "me");
				startActivity(userProfileIntent);

			}
		});
		// allFoodiesBody = (LinearLayout)
		// view.findViewById(R.id.allFoodiesBody);
		// allFoodiesBody.removeAllViews();

		// getAllFoodiesList();
		// setViewToBody();
		// btnInviteFbFriends.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// try {
		// // FacebookLoginUtil.sendRequestDialog(getActivity());
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// });
		return view;
	}

	private void initializeFoodies() {
		if (allFoodiesResponseDto == null) {
			allFoodiesResponseDto = new AllFoodiesResponseDto();
			allFoodiesResponseDto.setResults(new ArrayList<AllFoodiesDto>());
			// allFoodiesDtos = allFoodiesResponseDto.getAllFoodiesresults();
		} else if (allFoodiesResponseDto.getResults() == null) {
			allFoodiesResponseDto.setResults(new ArrayList<AllFoodiesDto>());
			// allFoodiesDtos = allFoodiesResponseDto.getAllFoodiesresults();
		}
	}

	@Override
	public void setResponse(Object object) {

		if (allFoodiesResponseDto == null) {
			return;
		}
		addResult((AllFoodiesResponseDto) object);
		AllFoodiesListAdapter foodiesListAdapter = (AllFoodiesListAdapter) foodiesListView
				.getAdapter();
		if (foodiesListAdapter != null) {
			foodiesListAdapter.setFoodiesDtos(this.allFoodiesResponseDto
					.getResults());
			foodiesListAdapter.notifyDataSetChanged();
		}

	}

	private void addResult(AllFoodiesResponseDto allFoodiesResponseDto) {
		if (allFoodiesResponseDto != null
				&& allFoodiesResponseDto.getResults() != null) {
			List<AllFoodiesDto> newFoodiesDtos = allFoodiesResponseDto
					.getResults();
			this.allFoodiesResponseDto.getResults().addAll(newFoodiesDtos);
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
			getAllFoodiesList();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

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
