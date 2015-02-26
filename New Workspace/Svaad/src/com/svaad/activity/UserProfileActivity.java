package com.svaad.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Session;
import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.adapter.FeedAdapter;
import com.svaad.asynctask.FollowUserProfileAsyncTask;
import com.svaad.asynctask.UnFollowUserProfileAsyncTask;
import com.svaad.asynctask.UserProfileAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class UserProfileActivity extends BaseActivity implements
		OnScrollListener, SvaadFeedCallback, OnClickListener,
		SvaadProgressCallback {

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

	private TextView userNameTextView, followerstextView;

	private Button btnFollow;

	private FeedDetailDto feedDetailDto;

	private FeedUserIdDto userId;

	List<String> followingUsers = null;
	private String userObjectId;
	SharedPreferences sharedPreferences;

	private LinearLayout wishedLl;

	private TextView ateItTextView;

	private TextView wishedTextView,photosTextView;

	private Button btnLogout;

	private ProgressBar pbar;

	private LinearLayout photosLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dish_detail_list_layout);

		initUi();

		LayoutInflater vi = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.userprofile_layout, null);

		feedDetailDto = (FeedDetailDto) getIntent().getExtras()
				.getSerializable(Constants.DATA);

		initHeaderView(convertView);

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());

		actionbar = getActionBar();

		setSupportProgressBarIndeterminateVisibility(false);

		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(true);

		listview.setOnScrollListener(this);
		listview.addHeaderView(convertView);

		initializeFeed();

		listview.setAdapter(new FeedAdapter(this, 0, null, feedResponseDto
				.getResults()));

		setDataToUserProfile();

		getUserDishes();

	}

	private void setDataToUserProfile() {

		if (feedDetailDto != null) {
			userId = feedDetailDto.getUserId();

			String userName = null;

			if (userId != null) {

				// For "following" we need to check the userId in login response
				// following users lists.
				userObjectId = userId.getObjectId();

				try {
					followingUsers = (List<String>) ObjectSerializer
							.deserialize(sharedPreferences.getString(
									Constants.FOLLOWING_USERS, ObjectSerializer
											.serialize(new ArrayList<String>())));

					if (followingUsers != null && followingUsers.size() > 0) {

						boolean follow = followingUsers.contains(userObjectId);
						if (follow == true) {
							btnFollow.setText("Following");
						} else {
							btnFollow.setText("Follow");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (userId.getSuggestCount() == 0) {
					ateItTextView.setText("0");
				} else {
					ateItTextView.setText("" + userId.getSuggestCount() + "");
				}

				if (userId.getWishlistCount() == 0) {
					wishedTextView.setText("0");
				} else {
					wishedTextView.setText("" + userId.getWishlistCount() + "");
				}

				 if (userId.getPhotoCount()== 0) {
				 photosTextView.setText("0");
				 } else {
				 photosTextView.setText("" + userId.getPhotoCount()
				 + "");
				 }

				userName = userId.getUname();

				if (userId.getDisplayPicUrl() != null
						&& userId.getDisplayPicUrl().length() > 0) {
					Picasso.with(UserProfileActivity.this)
							.load(userId.getDisplayPicUrl())
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile)

							.into(userProfilePicCover);

					Picasso.with(UserProfileActivity.this)
							.load(userId.getDisplayPicUrl())
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile)

							.into(userProfilePic);

				} else {
					Picasso.with(UserProfileActivity.this)
							.load(R.drawable.default_profile)
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile)
							.into(userProfilePicCover);
					Picasso.with(UserProfileActivity.this)
							.load(R.drawable.default_profile)
							.placeholder(R.drawable.default_profile)
							.error(R.drawable.default_profile)
							.into(userProfilePic);

				}

				if (userId.getFollowerCount() == 0) {
					followerstextView.setText("0 Followers");
				} else {
					followerstextView.setText("" + userId.getFollowerCount()
							+ " Followers");
				}
				if (userName != null && userName.length() > 0) {
					userNameTextView.setText(userName.toString().trim());
				} else {
					userNameTextView.setText(null);
				}
			}

		}

	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// getUserDishes();
	// }

	private void getUserDishes() {

		if (userId.getObjectId() != null && userId.getObjectId().length() > 0) {

			new UserProfileAsynctask(UserProfileActivity.this, null,
					userId.getObjectId()).execute();
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

		// pbar = (ProgressBar) convertView.findViewById(R.id.pbar);

		ateItTextView = (TextView) convertView.findViewById(R.id.ateItTextView);
		wishedTextView = (TextView) convertView
				.findViewById(R.id.wishedTextView);

		
		photosTextView = (TextView) convertView
				.findViewById(R.id.photosTextView);
		
		btnFollow = (Button) convertView.findViewById(R.id.btnFollow);

		btnFollow.setOnClickListener(this);

		btnLogout = (Button) convertView.findViewById(R.id.btnLogout);

		btnLogout.setVisibility(View.GONE);
		wishedLl = (LinearLayout) convertView.findViewById(R.id.wishedLl);
		wishedLl.setOnClickListener(this);
		
		photosLl = (LinearLayout) convertView.findViewById(R.id.photosLl);
		photosLl.setOnClickListener(this);

	}

	private void initUi() {

		listview = (ListView) findViewById(R.id.dishDetailListView);

		// pbar = (ProgressBar) findViewById(R.id.pbar);

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
		// else if(feedResponseDto instanceof FollowResponseDto)
		// {
		//
		// }

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
		case R.id.btnFollow:

			String text = btnFollow.getText().toString();

			String loginUserObjectId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (text != null && text.length() > 0) {
				if (text.equalsIgnoreCase("Follow")) {

					if (loginUserObjectId != null
							&& loginUserObjectId.length() > 0) {
						if (userId != null) {
							if (userId.getObjectId() != null
									&& userId.getObjectId().length() > 0
									&& loginUserObjectId != null
									&& loginUserObjectId.length() > 0) {

								// view=(Button)
								// view.findViewById(R.id.btnFollow);
								btnFollow.setText("Following");
								// view.refreshDrawableState();
								new FollowUserProfileAsyncTask(
										UserProfileActivity.this, btnFollow,
										loginUserObjectId, userId.getObjectId())
										.execute();
							}

						}
					} else {
						new SvaadDialogs().showSvaadLoginAlertDialog(
								UserProfileActivity.this, null,
								Constants.RES_MODE, Constants.USER_PROFILE,
								Constants.FOLLOW_ACTION);
					}
					// Toast.makeText(getApplicationContext(),
					// followButton.getText().toString(), 2000).show();
				}

				else if (text.equalsIgnoreCase("Following")) {
					if (loginUserObjectId != null
							&& loginUserObjectId.length() > 0) {
						if (userId != null) {
							if (userId.getObjectId() != null
									&& userId.getObjectId().length() > 0
									&& loginUserObjectId != null
									&& loginUserObjectId.length() > 0) {

								btnFollow.setText("Follow");
								new UnFollowUserProfileAsyncTask(
										UserProfileActivity.this, btnFollow,
										loginUserObjectId, userId.getObjectId())
										.execute();
							}

						}
						// Toast.makeText(getApplicationContext(),
						// followButton.getText().toString(), 2000).show();
						// followButton.setText("Follow");
					} else {
						new SvaadDialogs().showSvaadLoginAlertDialog(
								UserProfileActivity.this, null,
								Constants.RES_MODE, Constants.USER_PROFILE,
								Constants.FOLLOWING_ACTION);

					}
				}
			}
			break;
		case R.id.wishedLl:

			getUserProfileTabsActivity(0);
			break;

		case R.id.photosLl:

			
			if (userObjectId != null && userObjectId.length() > 0) {
				Intent i = new Intent(UserProfileActivity.this,
						PhotoActivity.class);
				i.putExtra("profileuserId", userObjectId);
				startActivity(i);
			}

			break;

		default:
			break;
		}

	}

	private void getUserProfileTabsActivity(int position) {

		if (feedDetailDto != null) {

			Intent i = new Intent(UserProfileActivity.this,
					UserProfileTabs_Activity.class);
			i.putExtra(Constants.PAGER, position);
			i.putExtra(Constants.DATA, feedDetailDto);
			startActivity(i);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
