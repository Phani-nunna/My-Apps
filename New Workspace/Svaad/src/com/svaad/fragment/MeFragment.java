package com.svaad.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.PhotoActivity;
import com.svaad.activity.UserProfileTabs_Activity;
import com.svaad.adapter.FeedAdapter;
import com.svaad.asynctask.UserProfileAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.responseDto.LogInResponseDto;
import com.svaad.utils.Api;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class MeFragment extends Fragment implements OnScrollListener,
		SvaadFeedCallback, OnClickListener, SvaadProgressCallback {

	ActionBar actionbar;

	LayoutInflater layoutInflater;

	View view;
	ImageView userProfilePicCover;
	ListView listview;

	public long skip = 0;
	private int totalItemCount = -1;

	FeedResponseDto feedResponseDto;

	private View convertView;

	private ImageView userProfilePic;

	public TextView userNameTextView, followerstextView, ateItTextView,
			wishedTextView, photosTextView;

	private Button btnFollow;

	private LinearLayout ateItLl, wishedLl, photosLl, followersLl;
	LogInResponseDto logInResponseDto = null;

	private List<String> wishlists;

	private ProgressBar pbar;

	private Button btnLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (savedInstanceState == null) {
		// logInResponseDto = convertLoginResponsetoObject();
		// if(logInResponseDto!=null)
		// {
		// getUserDishes();
		// }
		// }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.dish_detail_list_layout,
				container, false);

		initUi(view);

		initializeFeed();

		LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.userprofile_layout, null);

		initHeaderView(convertView);

		logInResponseDto = convertLoginResponsetoObject();

		listview.setOnScrollListener(this);
		listview.addHeaderView(convertView);

		getUserDishes();

		listview.setAdapter(new FeedAdapter(getActivity(), 0, null,
				feedResponseDto.getResults()));

		setDataToUserProfile();

		return view;
	}

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		if (Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY) != null
//				&& Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY)
//						.length() > 0) {
//
//			inflater.inflate(R.menu.settings, menu);
//		}
//	}

	
	
	private LogInResponseDto convertLoginResponsetoObject() {

		String loginresponse = Utils
				.getFromSharedPreference(Constants.SVAADLOGIN_RESPONSE);

		try {
			if (loginresponse != null) {
				logInResponseDto = (LogInResponseDto) Api.fromJson(
						loginresponse, LogInResponseDto.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logInResponseDto;
	}

	private void setDataToUserProfile() {

		if (logInResponseDto != null) {
			String userName = logInResponseDto.getUname();

			if (logInResponseDto.getDisplayPicUrl() != null
					&& logInResponseDto.getDisplayPicUrl().length() > 0) {
				Picasso.with(getActivity())
						.load(logInResponseDto.getDisplayPicUrl())
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(userProfilePicCover);

				Picasso.with(getActivity())
						.load(logInResponseDto.getDisplayPicUrl())
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(userProfilePic);

			} else {
				Picasso.with(getActivity()).load(R.drawable.default_profile)
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(userProfilePicCover);
				Picasso.with(getActivity()).load(R.drawable.default_profile)
						.placeholder(R.drawable.default_profile)
						.error(R.drawable.default_profile)

						.into(userProfilePic);

			}

			if (logInResponseDto.getFollowerCount() == 0) {
				followerstextView.setText("0 Followers");
			} else {
				followerstextView.setText(""
						+ logInResponseDto.getFollowerCount() + " Followers");
			}

			if (logInResponseDto.getSuggestCount() == 0) {
				ateItTextView.setText("0");
			} else {
				ateItTextView.setText("" + logInResponseDto.getSuggestCount()
						+ "");
			}

			// if (logInResponseDto.getWishlistCount() == 0) {
			// wishedTextView.setText("0");
			// } else {
			// wishedTextView.setText("" + logInResponseDto.getWishlistCount()
			// + "");
			// }
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SvaadApplication.getInstance()
							.getApplicationContext());

			try {
				wishlists = (List<String>) ObjectSerializer
						.deserialize(sharedPreferences.getString(
								Constants.WISHLIST_ARRAY, ObjectSerializer
										.serialize(new ArrayList<String>())));

				if (wishlists != null && wishlists.size() > 0) {
					wishedTextView.setText("" + wishlists.size() + "");
				} else {
					wishedTextView.setText("0");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (logInResponseDto.getPhotoCount() == 0) {
				photosTextView.setText("0");
			} else {
				photosTextView.setText("" + logInResponseDto.getPhotoCount()
						+ "");
			}

			if (userName != null && userName.length() > 0) {
				userNameTextView.setText(userName.toString().trim());
			} else {
				userNameTextView.setText(null);
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// getUserDishes();
		setDataToUserProfile();
	}

	@Override
	public void onPause() {
		super.onPause();
		setDataToUserProfile();
	}

	private void getUserDishes() {

		if (logInResponseDto != null) {

			if (logInResponseDto.getObjectId() != null) {
				if (logInResponseDto.getObjectId().length() > 0) {

					new UserProfileAsynctask(getActivity(), MeFragment.this,
							logInResponseDto.getObjectId()).execute();
				}
			}
		}
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

		userProfilePicCover = (ImageView) convertView
				.findViewById(R.id.userProfilePicCover);
		userProfilePic = (ImageView) convertView
				.findViewById(R.id.userProfilePic);
		userNameTextView = (TextView) convertView
				.findViewById(R.id.userNameTextView);
		followerstextView = (TextView) convertView
				.findViewById(R.id.followerstextView);

		photosTextView = (TextView) convertView
				.findViewById(R.id.photosTextView);
		ateItTextView = (TextView) convertView.findViewById(R.id.ateItTextView);
		wishedTextView = (TextView) convertView
				.findViewById(R.id.wishedTextView);

		btnFollow = (Button) convertView.findViewById(R.id.btnFollow);

		btnLogout = (Button) convertView.findViewById(R.id.btnLogout);
//		pbar = (ProgressBar) convertView.findViewById(R.id.pbar);

		ateItLl = (LinearLayout) convertView.findViewById(R.id.ateItLl);
		wishedLl = (LinearLayout) convertView.findViewById(R.id.wishedLl);
		photosLl = (LinearLayout) convertView.findViewById(R.id.photosLl);
		followersLl = (LinearLayout) convertView.findViewById(R.id.followersLl);

		ateItLl.setOnClickListener(this);
		wishedLl.setOnClickListener(this);
		photosLl.setOnClickListener(this);
		followersLl.setOnClickListener(this);
		btnLogout.setOnClickListener(this);

		// btnFollow.setOnClickListener(this);
		btnFollow.setVisibility(View.GONE);

	}

	private void initUi(View view) {

		listview = (ListView) view.findViewById(R.id.dishDetailListView);

//		pbar = (ProgressBar) view.findViewById(R.id.pbar);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().finish();
			break;
		case R.id.action_logout:

			new SvaadDialogs().showLogoutDilog(getActivity());

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {
			if (totalItemCount < 20)
				return;
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount;
			getUserDishes();
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

		if (feedResponseDto instanceof FeedResponseDto) {
			FeedResponseDto feedListResponseDto = (FeedResponseDto) feedResponseDto;
			this.feedResponseDto.getResults().clear();
			addResult(feedListResponseDto);

			refreshAdapter();
		}

	}

	private void addResult(FeedResponseDto feedResponseDto) {
		if (feedResponseDto != null && feedResponseDto.getResults() != null) {
			List<FeedDetailDto> feedDetailDto = feedResponseDto.getResults();
			this.feedResponseDto.getResults().addAll(feedDetailDto);
		}

	}

	private void refreshAdapter() {
		HeaderViewListAdapter adapter = (HeaderViewListAdapter) listview
				.getAdapter();
		if (!(adapter.getWrappedAdapter() instanceof FeedAdapter)) {
			return;
		}
		FeedAdapter feedListAdapter = (FeedAdapter) adapter.getWrappedAdapter();
		if (feedListAdapter != null) {
			feedListAdapter.setFeedDtos(this.feedResponseDto.getResults());
			feedListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.ateItLl:
		//
		// getUserProfileTabsActivity(0);
		//
		// break;

		case R.id.wishedLl:

			getUserProfileTabsActivity(0);
			break;

		case R.id.photosLl:
			
			
			
			String userId=Utils.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
			if(userId!=null&& userId.length()>0)
			{
				Intent i = new Intent(getActivity(), PhotoActivity.class);
				i.putExtra("loginUserId", userId);
				startActivity(i);
			}
			

			
			break;

		case R.id.followersLl:
			getUserProfileTabsActivity(2);
			break;
		case R.id.btnLogout:
			new SvaadDialogs().showLogoutDilog(getActivity());

			break;

		default:
			break;
		}

	}

	private void getUserProfileTabsActivity(int position) {

		Intent i = new Intent(getActivity(), UserProfileTabs_Activity.class);
		i.putExtra(Constants.PAGER, position);
		startActivity(i);

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
