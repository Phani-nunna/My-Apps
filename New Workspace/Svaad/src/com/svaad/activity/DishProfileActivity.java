package com.svaad.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.facebook.Session;
import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CommentsDetailDto;
import com.svaad.Dto.CommentsDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPhotoDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.DishProfileResDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.FeedUserIdDto;
import com.svaad.Dto.FromRestaurantDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.adapter.CommentListAdapter;
import com.svaad.asynctask.CommentListAsynctask;
import com.svaad.asynctask.DishPRofileResAsynctask;
import com.svaad.asynctask.LikeAsyncTask;
import com.svaad.asynctask.UnlikeAsyncTask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.requestDto.DishCommentsRequestDto;
import com.svaad.responseDto.DishCommentsResponseDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.ExpandableTextView;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;
import com.svaad.whereDto.DishCommentsWhereDto;
import com.svaad.whereDto.DishProfileResWhereDto;

public class DishProfileActivity extends BaseActivity implements
		SvaadFeedCallback, OnScrollListener, OnClickListener {

	ActionBar actionbar;

	public long skip = 0;
	private int totalItemCount = -1;

	private FeedDetailDto feedDetailDto;

	ImageButton dishImageButton;

	ListView commentsListView;

	TextView dishNameTextView, tvResturantName, tvResLocation, priceTextView,
			tvUserName, tvCommentText;

	ExpandableTextView tvDishDesc;

	ImageView imageView;
	
	LinearLayout llEmpty;

	String locationName;
	DishPhotoDto dishPhotoDto;
	BranchDishIdDto branchDishId;
	private Button btnAddToList, btnAteIt, btnAddActionbar;

	DishIdDto dishIdDto = null;
	FeedUserIdDto userId;
	PointDto ponitDto;
	LocationDto locationDto;
	String dishId = null;
	LayoutInflater layoutInflater;
	double latitude, longitude;
	// LinearLayout commentsContainer;
	String popular;
	RelativeLayout relativelayout2;

	View view;
	DishCommentsResponseDto dishCommentsResponseDto;

	// ObservableScrollView scrollview;
	private View convertView;

	private ImageView btnMap;
	
	LinearLayout dishBodyNameLL;

	private List<String> wishlists;
	SharedPreferences sharedPreferences;
	List<String> likesList = new ArrayList<String>();
	String loginUserId;

	private LinearLayout llRes, llMap;

	private RestaurantListResponseDto restaurantListResponseDto;

	ImageButton btnLoved, btnGood, btnItsOk, btnNever;
	private String branchDishIdShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.dish_list_layout);
		

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(SvaadApplication.getInstance()
						.getApplicationContext());

		initUi();

		LayoutInflater vi = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = vi.inflate(R.layout.dishprofile_layout, null);

		initHeaderView(convertView);

		actionbar = getActionBar();

		// actionbar.setBackgroundDrawable(new ColorDrawable(
		// R.drawable.actionbar_bg));

		setSupportProgressBarIndeterminateVisibility(false);
		actionbar.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));

		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayUseLogoEnabled(true);

		// Adding custom view
		// LayoutInflater li = LayoutInflater.from(DishProfileActivity.this);
		// View customview = li.inflate(R.layout.actionbar_wish_layout, null);

		// btnAddActionbar = (Button) customview.findViewById(R.id.btnAddTo);
		// btnAddActionbar.setOnClickListener(this);
		// actionbar.setCustomView(customview);
		// actionbar.setDisplayShowCustomEnabled(true);

		feedDetailDto = (FeedDetailDto) getIntent().getExtras()
				.getSerializable(Constants.DATA);

		if (this.getIntent().getExtras() != null) {
			popular = this.getIntent().getExtras().getString("popularhere");
		}

		commentsListView.setOnScrollListener(this);
		commentsListView.addHeaderView(convertView);

		initializeFeed();

		commentsListView.setAdapter(new CommentListAdapter(this, 0,
				dishCommentsResponseDto.getResults()));

		setDataToDishProfile();

		getcomments();

	}

	// private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
	// @Override
	// public void invalidateDrawable(Drawable who) {
	// getActionBar().setBackgroundDrawable(who);
	// }
	//
	// @Override
	// public void scheduleDrawable(Drawable who, Runnable what, long when) {
	// }
	//
	// @Override
	// public void unscheduleDrawable(Drawable who, Runnable what) {
	// }
	// };

	private void getcomments() {

		new CommentListAsynctask(DishProfileActivity.this, null,
				getCommentsListRequestDto()).execute();

	}

	private void getDishProfileResList() {

		new DishPRofileResAsynctask(DishProfileActivity.this, null,
				getDishprofileRequestDto()).execute();

	}

	private void initUi() {

		commentsListView = (ListView) findViewById(R.id.dishDetailListView);
		// commentsListView.setOnScrollListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// getcomments();
	}

	private void initHeaderView(View convertView) {
		
		llEmpty=(LinearLayout)convertView.findViewById(R.id.llEmpty);
		
		
		dishBodyNameLL=(LinearLayout)convertView.findViewById(R.id.dishBodyNameLL);
		dishImageButton = (ImageButton) convertView
				.findViewById(R.id.dishImageButton);

		dishNameTextView = (TextView) convertView
				.findViewById(R.id.dishNameTextView);

		// tvDishDesc = (TextView) convertView.findViewById(R.id.tvDishDesc);
		tvDishDesc = (ExpandableTextView) convertView
				.findViewById(R.id.tvDishDesc);
		tvResturantName = (TextView) convertView
				.findViewById(R.id.tvResturantName);
		tvResLocation = (TextView) convertView.findViewById(R.id.tvResLocation);
		priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);

		relativelayout2 = (RelativeLayout) convertView
				.findViewById(R.id.relativelayout2);

		btnMap = (ImageView) convertView.findViewById(R.id.btnMap);

		btnAddToList = (Button) convertView.findViewById(R.id.btnAddTo);

		btnAteIt = (Button) convertView.findViewById(R.id.btnAteIt);

		llRes = (LinearLayout) convertView.findViewById(R.id.llRes);
		llMap = (LinearLayout) convertView.findViewById(R.id.llMap);

		btnGood = (ImageButton) convertView.findViewById(R.id.btnGood);
		btnLoved = (ImageButton) convertView.findViewById(R.id.btnLoved);
		btnItsOk = (ImageButton) convertView.findViewById(R.id.btnItsOk);
		btnNever = (ImageButton) convertView.findViewById(R.id.btnNever);

		btnGood.setOnClickListener(this);
		btnLoved.setOnClickListener(this);
		btnItsOk.setOnClickListener(this);
		btnNever.setOnClickListener(this);

		btnMap.setOnClickListener(this);
		btnAddToList.setOnClickListener(this);
		btnAteIt.setOnClickListener(this);
		llMap.setOnClickListener(this);
		llRes.setOnClickListener(this);

	}

	private void setDataToDishProfile() {

		if (feedDetailDto != null) {

			btnAddToList.setTag(feedDetailDto);
			btnAteIt.setTag(feedDetailDto);

			userId = feedDetailDto.getUserId();

			dishPhotoDto = feedDetailDto.getDishPhoto();

			branchDishId = feedDetailDto.getBranchDishId();

			try {
				wishlists = (List<String>) ObjectSerializer
						.deserialize(sharedPreferences.getString(
								Constants.WISHLIST_ARRAY, ObjectSerializer
										.serialize(new ArrayList<String>())));

				if (wishlists != null && wishlists.size() > 0) {

					if (branchDishId != null) {

						String branchDishObjectId = branchDishId.getObjectId();
						branchDishIdShare = branchDishId.getObjectId();
						if (branchDishObjectId != null
								&& branchDishObjectId.length() > 0) {
							if (wishlists.contains(branchDishObjectId)) {
								btnAddToList.setText("Wished");
								btnAddToList
										.setCompoundDrawablesWithIntrinsicBounds(
												R.drawable.hearto, 0, 0, 0);
							} else {
								btnAddToList.setText("Wish it");
								btnAddToList
										.setCompoundDrawablesWithIntrinsicBounds(
												R.drawable.heart, 0, 0, 0);
							}

						}

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (branchDishId != null) {
				dishIdDto = branchDishId.getDishId();

				locationDto = branchDishId.getLocation();

				ponitDto = branchDishId.getPoint();

				if (ponitDto != null) {
					latitude = ponitDto.getLatitude();
					longitude = ponitDto.getLongitude();
				}

				String branchName = branchDishId.getBranchName();

				if (branchDishId.getRegular() == 0) {
					priceTextView.setText("NA");
				} else {
					priceTextView.setText("Rs " + branchDishId.getRegular());
				}

				if (locationDto != null) {
					locationName = locationDto.getName();

					if (locationName != null && locationName.length() > 0) {
						tvResLocation.setText(locationName);
					} else {
						tvResLocation.setText(null);
					}
				} else {
					tvResLocation.setText(null);
				}

				if (dishIdDto != null) {
					String dishname = dishIdDto.getName();
					String dishDesc = dishIdDto.getDesc();
					dishId = dishIdDto.getObjectId();

					if (dishname != null && dishname.length() > 0) {
						dishNameTextView.setText(dishname.toString().trim());
					} else {
						dishNameTextView.setText(null);
					}

					if (dishDesc != null && dishDesc.length() > 0) {
						tvDishDesc.setText(dishDesc.toString().trim());

						// makeTextViewResizable(tvDishDesc, 1, "View More",
						// true);
					} else {
						// tvDishDesc.setText(null);
						relativelayout2.setVisibility(View.GONE);

					}

				} else {
					dishNameTextView.setText(null);
					tvDishDesc.setText(null);
					// tvResturantName.setText(null);
				}

				if (branchName != null && branchName.length() > 0) {
					tvResturantName.setText(branchName.toString().trim());
				} else {
					tvResturantName.setText(null);
				}

			}

			if (dishPhotoDto != null) {
				String dishPhotoImage = dishPhotoDto.getUrl();

				if (dishPhotoImage != null && dishPhotoImage.length() > 0) {
					
//					actionbar.setBackgroundDrawable(new ColorDrawable(
//							android.R.color.transparent));
					Picasso.with(DishProfileActivity.this).load(dishPhotoImage)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(dishImageButton);
				}

				else {

//					Picasso.with(DishProfileActivity.this)
//							.load(R.drawable.temp).placeholder(R.drawable.temp)
//							.error(R.drawable.temp)
//							.into(dishImageButton);
					dishImageButton.setVisibility(View.GONE);
					llEmpty.setVisibility(View.VISIBLE);
					actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#555555")));
					
					
				}
			}

			if (dishPhotoDto == null) {

				if (branchDishId != null) {

					if (dishIdDto != null) {

						DishPicDto dishPicDto = dishIdDto.getDishPic();
						if (dishPicDto != null) {
							String dishPicUrl = dishPicDto.getUrl();
							if (dishPicUrl != null && dishPicUrl.length() > 0) {
								
//								actionbar.setBackgroundDrawable(new ColorDrawable(
//										android.R.color.transparent));

								Picasso.with(DishProfileActivity.this)
										.load(dishPicUrl)
										.placeholder(R.drawable.temp)
										.error(R.drawable.temp)

										.into(dishImageButton);
							}

						} else {

//							Picasso.with(DishProfileActivity.this)
//									.load(R.drawable.temp)
//									.placeholder(R.drawable.temp)
//									.error(R.drawable.temp)
//
//									.into(dishImageButton);
							llEmpty.setVisibility(View.VISIBLE);
							
							dishImageButton.setVisibility(View.GONE);
							actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#555555")));
						}
					}
				}

			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_send:
			// shareit();
			shareFb();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void shareFb() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"http://svaad.com/dish/" + branchDishIdShare);
		startActivity(Intent.createChooser(sharingIntent, "Share via Svaad"));
		PackageManager pm = getApplicationContext().getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(
				sharingIntent, 0);
		for (final ResolveInfo app : activityList) {
			if ((app.activityInfo.name).contains("facebook")) {
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				sharingIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				sharingIntent.setComponent(name);

			}
		}
		// startActivity(Intent.createChooser(sharingIntent,
		// "Share via Svaad"));

	}

	public void shareit() {
		// Intent sharingIntent = new
		// Intent(android.content.Intent.ACTION_SEND);
		// sharingIntent.setType("text/plain");
		// String shareBody = "Here is the share content body";
		// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		// "Subject Here");
		// sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		// startActivity(Intent.createChooser(sharingIntent,
		// "Share via Svaad"));

		Resources resources = getResources();

		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		// Native email client doesn't currently support HTML, but it doesn't
		// hurt to try in case they fix it
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Share email native");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Share email subject");
		emailIntent.setType("message/rfc822");

		PackageManager pm = getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");

		Intent openInChooser = Intent.createChooser(emailIntent,
				"Share via svaad");

		List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;
			if (packageName.contains("android.email")) {
				emailIntent.setPackage(packageName);
			} else if (packageName.contains("twitter")
					|| packageName.contains("facebook")
					|| packageName.contains("mms")
					|| packageName.contains("android.gm")) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(packageName,
						ri.activityInfo.name));
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				if (packageName.contains("twitter")) {
					intent.putExtra(Intent.EXTRA_TEXT, "Shara twitter");
				} else if (packageName.contains("facebook")) {
					// Warning: Facebook IGNORES our text. They say
					// "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
					// One workaround is to use the Facebook SDK to post, but
					// that doesn't allow the user to choose how they want to
					// share. We can also make a custom landing page, and the
					// link
					// will show the <meta content ="..."> text from that page
					// with our link in Facebook.
					// intent.putExtra(Intent.EXTRA_TEXT,
					// Uri.parse("https://www.facebook.com/CodeOfANinja"));
					intent.putExtra(android.content.Intent.EXTRA_TITLE,
							"Church Application");
					intent.putExtra(android.content.Intent.EXTRA_TEXT,
							"https://play.google.com/store/apps/details?id=com.facebook.katana");
					intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"A new world begin");

					final ActivityInfo activity = ri.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					intent.setComponent(name);

				} else if (packageName.contains("mms")) {
					intent.putExtra(Intent.EXTRA_TEXT, "Share sms");
				} else if (packageName.contains("android.gm")) {
					intent.putExtra(Intent.EXTRA_TEXT, "Shaare gmail");
					intent.putExtra(Intent.EXTRA_SUBJECT, "Share gmail subject");
					intent.setType("message/rfc822");
				}

				intentList.add(new LabeledIntent(intent, packageName, ri
						.loadLabel(pm), ri.icon));
			}
		}

		// convert intentList to array
		LabeledIntent[] extraIntents = intentList
				.toArray(new LabeledIntent[intentList.size()]);

		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);
	}

	private DishCommentsRequestDto getCommentsListRequestDto() {

		DishCommentsRequestDto dishCommentsRequestDto = new DishCommentsRequestDto();

		DishCommentsWhereDto dishCommentsWhereDto = new DishCommentsWhereDto();
		CommentsDishIdDto dishIdDto = new CommentsDishIdDto();
		dishIdDto.set__type("Pointer");
		dishIdDto.setClassName("Dishes");

		String branchId = null;

		if (feedDetailDto != null) {
			if (branchDishId != null) {
				BranchIdDto branchIdDto = branchDishId.getBranchId();

				if (branchIdDto != null) {
					branchId = branchIdDto.getObjectId();
				}

				if (dishId != null && dishId.length() > 0) {
					dishIdDto.setObjectId(dishId);
					dishCommentsWhereDto.setDishId(dishIdDto);
				}
			}
		}

		BranchIdDto branch = new BranchIdDto();
		branch.set__type("Pointer");
		branch.setClassName("Branches");
		if (branchId != null && branchId.length() > 0) {
			branch.setObjectId(branchId);

			dishCommentsWhereDto.setBranchId(branch);
		}

		FromRestaurantDto fromRestaurantDto = new FromRestaurantDto();
		fromRestaurantDto.set$exists(false);
		dishCommentsWhereDto.setFromRestaurant(fromRestaurantDto);

		com.svaad.Dto.CommentTextDto commentTextDto = new com.svaad.Dto.CommentTextDto();
		commentTextDto.set$exists(true);
		commentTextDto.set$ne("");
		dishCommentsWhereDto.setCommentText(commentTextDto);

		dishCommentsRequestDto.setWhere(dishCommentsWhereDto);
		dishCommentsRequestDto.setInclude("userId");
		dishCommentsRequestDto.setKeys("userId,commentText,dishPhoto");
		dishCommentsRequestDto.setLimit(1000);
		dishCommentsRequestDto.setSkip(skip);
		dishCommentsRequestDto.set_method("GET");
		dishCommentsRequestDto.setOrder("-createdAt");

		return dishCommentsRequestDto;
	}

	@Override
	public void setResponse(Object commentResponseDto) {

		if (commentResponseDto == null) {
			return;
		}

		if (commentResponseDto instanceof DishCommentsResponseDto) {
			DishCommentsResponseDto commentListResponseDto = (DishCommentsResponseDto) commentResponseDto;
			this.dishCommentsResponseDto.getResults().clear();
			addResult(commentListResponseDto);

			refreshAdapter();
		} else if (commentResponseDto instanceof RestaurantListResponseDto) {
			// initializeRes();
			RestaurantListResponseDto resListResponseDto = (RestaurantListResponseDto) commentResponseDto;
			// this.restaurantListResponseDto.getResults().clear();

			List<RestaurantDetailsDto> resDetailList = resListResponseDto
					.getResults();

			if (resDetailList != null) {
				if (resDetailList.get(0) != null) {

					llRes.setBackgroundColor(Color.TRANSPARENT);

					Intent intentRes = new Intent(DishProfileActivity.this,
							RestaurantProfilesActivity.class);
					// Intent intentRes = new Intent(DishProfileActivity.this,
					// NewRestaurantProfileActivity.class);
					intentRes.putExtra(Constants.DATA, resDetailList.get(0));
					startActivity(intentRes);
				}
			}
		}

	}

	private void refreshAdapter() {
		HeaderViewListAdapter adapter = (HeaderViewListAdapter) commentsListView
				.getAdapter();
		if (!(adapter.getWrappedAdapter() instanceof CommentListAdapter)) {
			return;
		}
		CommentListAdapter commentListAdapter = (CommentListAdapter) adapter
				.getWrappedAdapter();
		if (commentListAdapter != null) {
			commentListAdapter
					.setCommentListDetailDtos(this.dishCommentsResponseDto
							.getResults());
			commentListAdapter.notifyDataSetChanged();
		}
	}

	private void initializeFeed() {
		if (dishCommentsResponseDto == null) {
			dishCommentsResponseDto = new DishCommentsResponseDto();
			dishCommentsResponseDto
					.setResults(new ArrayList<CommentsDetailDto>());
		} else if (dishCommentsResponseDto.getResults() == null) {
			dishCommentsResponseDto
					.setResults(new ArrayList<CommentsDetailDto>());
		}
	}

	private void addResult(DishCommentsResponseDto commentResponseDto) {
		if (commentResponseDto != null
				&& commentResponseDto.getResults() != null) {
			List<CommentsDetailDto> commetsDetailDto = commentResponseDto
					.getResults();
			this.dishCommentsResponseDto.getResults().addAll(commetsDetailDto);
		}

	}

	// public int getScrollY() {
	// View c = commentsListView.getChildAt(0);
	// if (c == null) {
	// return 0;
	// }
	//
	// int firstVisiblePosition = commentsListView.getFirstVisiblePosition();
	// int top = c.getTop();
	//
	// int headerHeight = 0;
	// if (firstVisiblePosition >= 1) {
	// headerHeight = convertView.getHeight();
	// }
	//
	// return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	// // return headerHeight
	// }

	// public static float clamp(float value, float max, float min) {
	// return Math.max(Math.min(value, min), max);
	// }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount != 0
				&& (firstVisibleItem + visibleItemCount) >= totalItemCount
				&& this.totalItemCount != totalItemCount) {
			if (totalItemCount < 1000)
				return;
			this.totalItemCount = totalItemCount;
			this.skip = totalItemCount;
			getcomments();
		}
		// int scrollY = getScrollY();
		// // sticky actionbar
		// dishImageButton.setTranslationY(Math.max(-scrollY,
		// mMinHeaderTranslation));
		// float ratio = clamp(dishImageButton.getTranslationY() /
		// mMinHeaderTranslation, 0.0f, 1.0f);
		// final int newAlpha = (int) (ratio * 255);
		// mActionBarBackgroundDrawable.setAlpha(newAlpha);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnGood:
			callIntent("2", feedDetailDto);

			break;
		case R.id.btnLoved:
			callIntent("1", feedDetailDto);
			break;
		case R.id.btnItsOk:
			callIntent("3", feedDetailDto);
			break;
		case R.id.btnNever:
			callIntent("4", feedDetailDto);
			break;

		case R.id.llRes:

			if (popular != null && popular.length() > 0) {

			} else {

				llRes.setBackgroundColor(Color.parseColor("#dedede"));

				try {
					getDishProfileResList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			break;

		case R.id.llMap:

			if (latitude != 0 && longitude != 0) {

				String label = null;
				double latitude = this.latitude;
				double longitude = this.longitude;

				if (locationName != null && locationName.length() > 0) {
					label = locationName;
				}
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = latitude + "," + longitude + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						uri);
				startActivity(intent);

			}

			break;
		case R.id.btnMap:

			if (latitude != 0 && longitude != 0) {

				String label = null;
				double latitude = this.latitude;
				double longitude = this.longitude;

				if (locationName != null && locationName.length() > 0) {
					label = locationName;
				}
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = latitude + "," + longitude + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						uri);
				startActivity(intent);

			}

			break;

		case R.id.btnAteIt:

			loginUserId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

			if (loginUserId != null && loginUserId.length() > 0) {
				FeedDetailDto feedDetailDtos = (FeedDetailDto) v.getTag();
				Intent ateItIntent = new Intent(DishProfileActivity.this,
						AteIt_activity.class);
				ateItIntent.putExtra(Constants.DATA, feedDetailDtos);
				ateItIntent.putExtra(Constants.SVAAD, Constants.DISH_PROFILE);
				startActivity(ateItIntent);
			} else {
				new SvaadDialogs().showSvaadLoginAlertDialog(
						DishProfileActivity.this, null, Constants.RES_MODE,
						Constants.DISH_PROFILE, Constants.ATE_IT);
			}
			break;
		case R.id.btnAddTo:

			FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();
			String text = btnAddToList.getText().toString();

			loginUserId = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
			if (loginUserId != null && loginUserId.length() > 0) {

				if (text != null && text.equalsIgnoreCase("Wish it")) {

					if (feedDetailDto != null) {
						BranchDishIdDto branchDishId = feedDetailDto
								.getBranchDishId();
						if (branchDishId != null) {
							String objectid = branchDishId.getObjectId();

							if (objectid != null && objectid.length() > 0) {

								try {
									wishlists = (List<String>) ObjectSerializer
											.deserialize(sharedPreferences
													.getString(
															Constants.WISHLIST_ARRAY,
															ObjectSerializer
																	.serialize(new ArrayList<String>())));

									if (wishlists != null
											&& wishlists.size() > 0) {
										wishlists.add(objectid);

										try {

											Utils.saveToSharedPreferenceList(
													Constants.WISHLIST_ARRAY,
													wishlists);
											btnAddToList.setText("Wished");
											btnAddToList
													.setCompoundDrawablesWithIntrinsicBounds(
															R.drawable.hearto,
															0, 0, 0);

											// MeFragment m=new MeFragment();
											// m.wishedTextView.setText("" +
											// wishlists.size()+ "");

											new LikeAsyncTask(
													DishProfileActivity.this,
													null, feedDetailDto,
													Constants.DISH_PROFILE)
													.execute();
										} catch (Exception e) {
											e.printStackTrace();
										}

									}

									else {
										likesList.add(objectid);

										if (likesList != null
												&& likesList.size() > 0) {

											try {
												btnAddToList.setText("Wished");
												btnAddToList
														.setCompoundDrawablesWithIntrinsicBounds(
																R.drawable.hearto,
																0, 0, 0);
												Utils.saveToSharedPreferenceList(
														Constants.WISHLIST_ARRAY,
														likesList);

												new LikeAsyncTask(
														DishProfileActivity.this,
														null, feedDetailDto,
														Constants.DISH_PROFILE)
														.execute();
											} catch (Exception e) {
												e.printStackTrace();
											}

										}

									}
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}
						}
					}
				} else {
					try {
						wishlists = (List<String>) ObjectSerializer
								.deserialize(sharedPreferences
										.getString(
												Constants.WISHLIST_ARRAY,
												ObjectSerializer
														.serialize(new ArrayList<String>())));
						if (wishlists != null && wishlists.size() > 0) {

							if (branchDishId != null) {

								String branchDishObjectId = branchDishId
										.getObjectId();
								if (branchDishObjectId != null
										&& branchDishObjectId.length() > 0) {
									for (int i = 0; i < wishlists.size(); i++) {
										String id = wishlists.get(i);
										if (id != null && id.length() > 0) {
											if (id.equals(branchDishObjectId)) {
												wishlists.remove(i);
												btnAddToList.setText("Wish it");
												btnAddToList
														.setCompoundDrawablesWithIntrinsicBounds(
																R.drawable.heart,
																0, 0, 0);
												Utils.saveToSharedPreferenceList(
														Constants.WISHLIST_ARRAY,
														wishlists);

												// MeFragment m=new
												// MeFragment();
												// m.wishedTextView.setText("" +
												// wishlists.size()+ "");

												new UnlikeAsyncTask(
														DishProfileActivity.this,
														null,
														branchDishObjectId,
														Constants.DISH_PROFILE,
														feedDetailDto)
														.execute();

											} else {
												btnAddToList.setText("Wished");
												btnAddToList
														.setCompoundDrawablesWithIntrinsicBounds(
																R.drawable.hearto,
																0, 0, 0);
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				new SvaadDialogs().showSvaadLoginAlertDialog(
						DishProfileActivity.this, null, Constants.RES_MODE,
						Constants.DISH_PROFILE, Constants.WISH_IT);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	private DishProfileResDto getDishprofileRequestDto() {
		DishProfileResDto dishProfileResDto = new DishProfileResDto();
		dishProfileResDto.set_method("GET");
		// dishProfileResDto.setInclude("include");
		dishProfileResDto.setInclude("location");

		DishProfileResWhereDto dishProfileResWhereDto = new DishProfileResWhereDto();

		if (feedDetailDto != null) {
			BranchDishIdDto branchDishIdDto = feedDetailDto.getBranchDishId();
			if (branchDishIdDto != null) {
				BranchIdDto branchId = branchDishIdDto.getBranchId();
				if (branchId != null) {
					String objectId = branchId.getObjectId();

					if (objectId != null) {
						dishProfileResWhereDto.setObjectId(objectId);
					}
				}
			}
		}

		dishProfileResWhereDto.setPublish(true);

		dishProfileResDto.setWhere(dishProfileResWhereDto);
		return dishProfileResDto;

	}

	public void callIntent(String code, FeedDetailDto feedDetailDto) {
		Intent i = new Intent(DishProfileActivity.this,
				DishProfile_AteIt_activity.class);
		i.putExtra(Constants.DATA, feedDetailDto);
		i.putExtra("code", code);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.share_menu, menu);
		return true;

	}

	public static void makeTextViewResizable(final TextView tv,
			final int maxLine, final String expandText, final boolean viewMore) {

		if (tv.getTag() == null) {
			tv.setTag(tv.getText());
		}
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {

				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (maxLine == 0) {
					int lineEndIndex = tv.getLayout().getLineEnd(0);
					String text = tv.getText().subSequence(0,
							lineEndIndex - expandText.length() + 1)
							+ " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(
							addClickablePartTextViewResizable(
									Html.fromHtml(tv.getText().toString()), tv,
									maxLine, expandText, viewMore),
							BufferType.SPANNABLE);
				} else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
					int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
					String text = tv.getText().subSequence(0,
							lineEndIndex - expandText.length() + 1)
							+ " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(
							addClickablePartTextViewResizable(
									Html.fromHtml(tv.getText().toString()), tv,
									maxLine, expandText, viewMore),
							BufferType.SPANNABLE);
				} else {
					int lineEndIndex = tv.getLayout().getLineEnd(
							tv.getLayout().getLineCount() - 1);
					String text = tv.getText().subSequence(0, lineEndIndex)
							+ " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(
							addClickablePartTextViewResizable(
									Html.fromHtml(tv.getText().toString()), tv,
									lineEndIndex, expandText, viewMore),
							BufferType.SPANNABLE);
				}
			}
		});

	}

	private static SpannableStringBuilder addClickablePartTextViewResizable(
			final Spanned strSpanned, final TextView tv, final int maxLine,
			final String spanableText, final boolean viewMore) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(spanableText)) {
			ssb.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {

					if (viewMore) {
						tv.setLayoutParams(tv.getLayoutParams());
						tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
						tv.invalidate();
						makeTextViewResizable(tv, -1, "View Less", false);
					} else {
						tv.setLayoutParams(tv.getLayoutParams());
						tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
						tv.invalidate();
						makeTextViewResizable(tv, 3, "View More", true);
					}

				}
			}, str.indexOf(spanableText), str.indexOf(spanableText)
					+ spanableText.length(), 0);

		}
		return ssb;

	}

}
