package com.svaad.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.slidinglayer.SlidingLayer;
import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.adapter.PhotoPagerAdapter;
import com.svaad.asynctask.SocialMenuAsynctask;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.fragment.Call_Fragment;
import com.svaad.fragment.Place_Fragment;
import com.svaad.fragment.PullToviewMenu_Fragment;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.KitkatMediaPath;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class RestaurantProfilesActivity extends BaseActivity implements
		OnClickListener, SvaadFeedCallback, SvaadProgressCallback {

	boolean open = false;
	String branchId;
	FragmentTransaction fragmentTransaction;
	private SlidingLayer mSlidingLayer;
	List<FeedDetailDto> photoslist;
	String mStickContainerToRightLeftOrMiddle = "top";
	String mStickContainerBottom = "bottom";
	DatabaseDAO da;
	List<FeedDetailDto> feedBackList;
	ListView lv;
	TextView textdrag, branchNameTextView, branchAddresstextView;
	RelativeLayout ctimages;
	LinearLayout ctWrapper;
	String username;
	LinearLayout viewMenuLl;
	private String mediaPath;
	View view;
	FeedResponseDto feedResponseDto;
	ImageView dishImageButton;

	LayoutInflater layoutInflater;
	LayoutParams rlp, rlp1;

	int textResource;
	Drawable d;
	RestaurantDetailsDto restaurantDetailsDto;

	ScrollView scrollView1;

	Map<String, List<FeedDetailDto>> mapList;
	private ImageView imageProfilePic;
	private TextView tvLovedDishes, tvUserName, tvNoReults, tvResName;
	private ViewPager mPager;

	Context context;
	private String resName;
	private ProgressBar pbar;
	private String image_url;
	private Button btnCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.restaurantprofiles_activity_layout);

		setSupportProgressBarIndeterminateVisibility(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		init();

		initializeFeed();

		context = RestaurantProfilesActivity.this;

		restaurantDetailsDto = (RestaurantDetailsDto) getIntent().getExtras()
				.getSerializable(Constants.DATA);

		rlp = (LayoutParams) mSlidingLayer.getLayoutParams();

		if (mStickContainerToRightLeftOrMiddle.equals("top")) {
			textResource = R.string.swipe_up_label;
			d = getResources().getDrawable(R.drawable.ic_launcher);

			mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_TOP);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rlp.width = LayoutParams.MATCH_PARENT;
			rlp.height = getResources().getDimensionPixelSize(
					R.dimen.layer_width);

		}

		setRestaurantHeaderView();

		getSocialMenuLists();

	}

	private void getSocialMenuLists() {

		if (restaurantDetailsDto != null) {
			branchId = restaurantDetailsDto.getObjectId();
			if (branchId != null && branchId.length() > 0) {
				new SocialMenuAsynctask(RestaurantProfilesActivity.this, null,
						branchId).execute();
			}
		}

	}

	private void setRestaurantHeaderView() {

		if (restaurantDetailsDto != null) {
			if (restaurantDetailsDto.getName() == null) {

				resName = restaurantDetailsDto.getBranchName();

				branchNameTextView.setText((restaurantDetailsDto
						.getBranchName() != null ? restaurantDetailsDto
						.getBranchName() : ""));

			} else {

				branchNameTextView
						.setText((restaurantDetailsDto.getName() != null ? restaurantDetailsDto
								.getName() : ""));
			}

			branchAddresstextView.setText((restaurantDetailsDto.getLocation()
					.getName() != null ? restaurantDetailsDto.getLocation()
					.getName() : ""));

			String url = "";

			if (restaurantDetailsDto.getPhoto() == null) {

				if (restaurantDetailsDto.getCoverPic() != null
						&& restaurantDetailsDto.getCoverPic().getUrl() != null) {
					url = restaurantDetailsDto.getCoverPic().getUrl();

					Picasso.with(RestaurantProfilesActivity.this).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(dishImageButton);

				} else if (restaurantDetailsDto.getCoverPicSmall() != null
						&& restaurantDetailsDto.getCoverPicSmall().getUrl() != null) {
					url = restaurantDetailsDto.getCoverPicSmall().getUrl();

					Picasso.with(RestaurantProfilesActivity.this).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(dishImageButton);

				} else {
					Picasso.with(RestaurantProfilesActivity.this)
							.load(R.drawable.temp).placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(dishImageButton);
				}

			} else {

				if (restaurantDetailsDto.getPhoto() != null
						&& restaurantDetailsDto.getPhoto().getUrl() != null) {
					url = restaurantDetailsDto.getPhoto().getUrl();

					Picasso.with(RestaurantProfilesActivity.this).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(dishImageButton);
				} else {
					Picasso.with(RestaurantProfilesActivity.this)
							.load(R.drawable.temp).placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(dishImageButton);
				}
			}
		}

	}

	public void init() {
		textdrag = (TextView) findViewById(R.id.textdrag);
		viewMenuLl = (LinearLayout) findViewById(R.id.touch);
		branchNameTextView = (TextView) findViewById(R.id.branchNameTextView);
		branchAddresstextView = (TextView) findViewById(R.id.branchAddresstextView);
		dishImageButton = (ImageView) findViewById(R.id.dishImageButton);
		ctWrapper = (LinearLayout) findViewById(R.id.ctWrapper);
		mSlidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		tvNoReults = (TextView) findViewById(R.id.tvNoReults);
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);

		pbar = (ProgressBar) findViewById(R.id.pbar);

		viewMenuLl.setOnClickListener(this);

		// scrollView1.setOnTouchListener(new OnTouchListener()
		// {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event)
		// {
		// scrollView1.getParent().requestDisallowInterceptTouchEvent(true);
		//
		// return false;
		// }
		// });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.restaurant_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Bundle bundle = new Bundle();
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			break;

		// case R.id.action_bookmark:
		// setSlidingLayer();
		//
		// BookMark_Fragment bookmarkFragment = new BookMark_Fragment();
		//
		// bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
		// bookmarkFragment.setArguments(bundle);
		//
		// fragmentTransaction.replace(R.id.resContainer, bookmarkFragment);
		// fragmentTransaction.commit();
		//
		// break;
		case R.id.action_call:

			setSlidingLayer();

			Call_Fragment callFragment = new Call_Fragment();

			bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
			callFragment.setArguments(bundle);
			fragmentTransaction.replace(R.id.resContainer, callFragment);
			fragmentTransaction.commit();

			break;
		case R.id.action_place:

			setSlidingLayer();

			Place_Fragment placeFragment = new Place_Fragment();
			bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
			placeFragment.setArguments(bundle);

			fragmentTransaction.replace(R.id.resContainer, placeFragment);
			fragmentTransaction.commit();

			break;
		// case R.id.action_time:
		//
		// setSlidingLayer();
		// Time_Fragment timefragment=new Time_Fragment();
		//
		// bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
		// timefragment.setArguments(bundle);
		// fragmentTransaction.replace(R.id.resContainer, timefragment);
		// fragmentTransaction.commit();
		//
		// break;

		// case R.id.action_features:
		//
		// setSlidingLayer();
		//
		// Features_Fragment featuresFragment = new Features_Fragment();
		// bundle.putSerializable(Constants.DATA, restaurantDetailsDto);
		// featuresFragment.setArguments(bundle);
		// fragmentTransaction.replace(R.id.resContainer, featuresFragment);
		// fragmentTransaction.commit();
		// break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setSlidingLayer() {

		if (mSlidingLayer.isOpened()) {
			open = false;
			mSlidingLayer.closeLayer(true);
		} else {
			open = true;
			mSlidingLayer.openLayer(true);

		}
	}

	public void setDishestoWrapperLayout(List<String> usersOrderList,
			Map<String, List<FeedDetailDto>> map) {

		if (context != null) {

			layoutInflater = ((Activity) context).getLayoutInflater();
			int i;

			mapList = map;
			for (i = 0; i < usersOrderList.size(); i++) {

				photoslist = new ArrayList<FeedDetailDto>();
				view = layoutInflater.inflate(
						R.layout.dishcollectionthumbnail_layout, ctWrapper,
						false);

				ctimages = (RelativeLayout) view.findViewById(R.id.ctimages);
				tvUserName = (TextView) view.findViewById(R.id.tvUserName);
				tvLovedDishes = (TextView) view
						.findViewById(R.id.tvLovedDishes);
				tvResName = (TextView) view.findViewById(R.id.tvResName);

				imageProfilePic = (ImageView) view.findViewById(R.id.imageView);
				mPager = (ViewPager) view.findViewById(R.id.pager);

				int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 16 * 2, getResources()
								.getDisplayMetrics());
				mPager.setPageMargin(-margin);

				mPager.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						v.getParent().requestDisallowInterceptTouchEvent(true);
						return false;
					}
				});

				mPager.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						mPager.getParent().requestDisallowInterceptTouchEvent(
								true);
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});

				username = usersOrderList.get(i);
				if (username != null && username.length() > 0) {
					tvUserName.setText(username);

					tvResName.setText("for " + resName);
					feedBackList = map.get(username);

					Collections.sort(feedBackList,
							new FeedCommmentsComparator());
					Collections.reverse(feedBackList);

					if (feedBackList != null && feedBackList.size() > 0) {
						if (feedBackList.get(0).getUserId() != null) {
							if (feedBackList.get(0).getUserId()
									.getDisplayPicUrl() != null
									&& feedBackList.get(0).getUserId()
											.getDisplayPicUrl().length() > 0) {
								image_url = feedBackList.get(0).getUserId()
										.getDisplayPicUrl();
								setProfileImageToPiccaso(imageProfilePic,
										feedBackList.get(0).getUserId()
												.getDisplayPicUrl());
							} else {
								image_url = null;
							}

						}

						for (int j = 0; j < feedBackList.size(); j++) {
							photoslist.add(feedBackList.get(j));

						}
						// Collections.sort(photoslist, new
						// FeedCommmentsComparator());
						// Collections.reverse(photoslist);

						PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(
								RestaurantProfilesActivity.this, photoslist,
								feedBackList, username, resName, image_url);
						mPager.setAdapter(photoPagerAdapter);

						String lovedText = getLovedDisheshText(feedBackList);

						if (lovedText != null && lovedText.length() > 0) {
							tvLovedDishes.setText("Loved " + lovedText);
						} else {
							tvLovedDishes.setVisibility(View.GONE);
						}
					}
				}

				ctimages.setOnClickListener(this);
				ctimages.setTag(username);
				ctWrapper.addView(view);

			}
		}

	}

	private String getLovedDisheshText(List<FeedDetailDto> feedBackList) {

		List<String> lovedItListSize = new ArrayList<String>();

		String lovedText = "";
		// int count = 0;
		for (FeedDetailDto feed : feedBackList) {
			if (feed.getDishTag() != null
					&& feed.getDishTag().equalsIgnoreCase("1")) {
				// count++;
				// if (count >= 3)
				// break;

				if (lovedItListSize != null && lovedItListSize.size() >= 3) {
					break;
				}

				if (lovedText.length() == 0) {
					if (feed.getBranchDishId() != null) {
						if (feed.getBranchDishId().getDishId() != null) {
							if (feed.getBranchDishId().getDishId().getName() != null
									&& feed.getBranchDishId().getDishId()
											.getName().length() > 0) {
								lovedText = feed.getBranchDishId().getDishId()
										.getName();

								lovedItListSize.add(lovedText);
							}
						}
					}
				} else {
					if (feed.getBranchDishId() != null) {
						if (feed.getBranchDishId().getDishId() != null) {
							if (feed.getBranchDishId().getDishId().getName() != null
									&& feed.getBranchDishId().getDishId()
											.getName().length() > 0) {
								lovedItListSize.add(feed.getBranchDishId()
										.getDishId().getName());
								lovedText = lovedText
										+ ","
										+ feed.getBranchDishId().getDishId()
												.getName();
							}
						}
					}
				}
			}
		}
		return lovedText;
	}

	private void setProfileImageToPiccaso(ImageView im, String url) {
		if (url != null && url.length() > 0) {

			Picasso.with(RestaurantProfilesActivity.this).load(url)
					.placeholder(R.drawable.default_profile)
					.error(R.drawable.default_profile).into(im);

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btnCamera:
			String userid = Utils
					.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);
			if (userid != null && userid.length() > 0) {
				String mediaPath = Utils.getMediaPath();

				new SvaadDialogs().showPhotoDialog(
						RestaurantProfilesActivity.this, mediaPath);
			} else {
				new SvaadDialogs()
						.showSvaadLoginAlertDialog(
								RestaurantProfilesActivity.this, null,
								Constants.RES_MODE, Constants.Cam_ate,
								Constants.ATE_IT);
			}
			break;

		case R.id.ctimages:
			String name = (String) v.getTag();
			getDishCollectoinIntent(name);
			break;

		case R.id.btnRefresh:

			da = new DatabaseDAO(RestaurantProfilesActivity.this);

			if (branchId != null & branchId.length() > 0) {

				int cursor = da.checkBranchIdInBranchMenu(branchId);

				if (cursor > 0) {
					da.deleteBranchid(branchId);
					da.deleteBranchMenu(branchId);

					fragmentTransaction = getSupportFragmentManager()
							.beginTransaction();
					PullToviewMenu_Fragment pulltoviewfragment = new PullToviewMenu_Fragment();
					Bundle b = new Bundle();
					b.putString(Constants.DATA, branchId);
					pulltoviewfragment.setArguments(b);
					fragmentTransaction.replace(R.id.resContainer1,
							pulltoviewfragment);
					fragmentTransaction.commit();

				}
				// else
				// {
				// getRestaurantmenuList(branchId);
				// }
			}
			break;

		case R.id.touch:

			String text = textdrag.getText().toString();

			if (text.equalsIgnoreCase("View Full Menu")) {

				if (restaurantDetailsDto != null) {
					branchId = restaurantDetailsDto.getObjectId();
					if (branchId != null && branchId.length() > 0) {

						Intent i = new Intent(RestaurantProfilesActivity.this,
								PullToviewMenu_Activity.class);
						i.putExtra("resName", resName);
						i.putExtra(Constants.DATA, branchId);
						startActivity(i);

					}
				}
			}

			break;

		default:
			break;
		}

	}

	private void getDishCollectoinIntent(String uname) {
		// new SvaadDialogs().showToast(RestaurantProfilesActivity.this, uname);

		List<FeedDetailDto> feedBackUnameList = mapList.get(uname);
		Intent i = new Intent(RestaurantProfilesActivity.this,
				DishCollectionActivity.class);
		Bundle bundleObject = new Bundle();
		bundleObject.putSerializable("key", (Serializable) feedBackUnameList);
		i.putExtras(bundleObject);
		i.putExtra("name", uname);
		startActivity(i);
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
	public void setResponse(Object socialMenuResponse) {

		if (socialMenuResponse == null) {
			return;
		}
		addResult((FeedResponseDto) socialMenuResponse);
	}

	private void addResult(FeedResponseDto socialMenuResponse) {
		if (socialMenuResponse != null
				&& socialMenuResponse.getResults() != null
				&& socialMenuResponse.getResults().size() > 0) {
			List<FeedDetailDto> feed = socialMenuResponse.getResults();
			this.feedResponseDto.getResults().addAll(feed);
			// setCommentsListtoContainer();

			Collections.sort(feed, new FeedComparator());
			Collections.reverse(feed);
			List<String> usersOrderList = getUsers(feed);
			System.out.println(usersOrderList);
			Map<String, List<FeedDetailDto>> map = convertListToMap(feed);

			setDishestoWrapperLayout(usersOrderList, map);
		} else {
			tvNoReults.setVisibility(View.VISIBLE);
			tvNoReults.setText("No recommendations here");
		}

	}

	private List<String> getUsers(List<FeedDetailDto> feedList) {
		List<String> users = new ArrayList<String>();
		for (FeedDetailDto feed : feedList) {
			if (!users.contains(feed.getUserId().getUname()))
				users.add(feed.getUserId().getUname());

		}
		return users;
	}

	class FeedComparator implements Comparator<FeedDetailDto> {
		@Override
		public int compare(FeedDetailDto a, FeedDetailDto b) {
			return a.getCreatedAt().compareTo(b.getCreatedAt());
		}
	}

	class FeedCommmentsComparator implements Comparator<FeedDetailDto> {
		@Override
		public int compare(FeedDetailDto a, FeedDetailDto b) {

			String first = a.getCommentText();
			String second = b.getCommentText();
			if (second == null) {
				second = " ";
			}
			if (first == null) {
				first = " ";
			}

			return first.compareTo(second);
		}
	}

	public Map<String, List<FeedDetailDto>> convertListToMap(
			List<FeedDetailDto> feedList) {
		Map<String, List<FeedDetailDto>> map = new HashMap<String, List<FeedDetailDto>>();
		for (FeedDetailDto feed : feedList) {

			if (feed.getUserId() != null) {
				if (feed.getUserId().getUname() != null
						&& feed.getUserId().getUname().length() > 0) {
					List<FeedDetailDto> temp = map.get(feed.getUserId()
							.getUname());
					if (temp == null)
						temp = new ArrayList<FeedDetailDto>();
					temp.add(feed);
					map.put(feed.getUserId().getUname(), temp);
				}
			}

		}
		System.out.println(map);
		return map;

	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// Session.getActiveSession().onActivityResult(this, requestCode,
	// resultCode, data);
	// }

	@Override
	public void progressOn() {

		pbar.setVisibility(View.VISIBLE);

	}

	@Override
	public void progressOff() {

		pbar.setVisibility(View.GONE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, intent);
			
			getMaediaPath(requestCode,resultCode,intent);
		} else {
			getMaediaPath(requestCode,resultCode,intent);
		}
	}

	private void reinitializeValues() {

		if (mediaPath == null) {
			mediaPath = Utils.getFromSharedPreference(Constants.IMAGE_PATH);
		}
	}

	@SuppressLint("NewApi")
	private void getMaediaPath(int requestCode,int resultCode,Intent intent ) {
		String media = Utils.getMediaPath();
		String mediaPath = media;
		reinitializeValues();

		if (mediaPath == null) {
			return;
		}
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case Constants.CAPTURE_IMAGE:

				Intent in = new Intent(RestaurantProfilesActivity.this,
						SearchDish_Activity.class);
				String path = Utils.getFromSharedPreference("cam_path");

				in.putExtra("path", path);
				in.putExtra(Constants.BRANCH_DETAILS, restaurantDetailsDto);
				startActivity(in);

				break;

			case Constants.GALLERY_IMAGE:
				if (mediaPath != null && intent != null
						&& intent.getData() != null) {

					Uri originalUri = intent.getData();
					Uri uri = intent.getData();

					String id = "";
					if (uri.getLastPathSegment().contains(":"))
						id = uri.getLastPathSegment().split(":")[1];
					else
						id = uri.getLastPathSegment();

					if (uri != null && "content".equals(uri.getScheme())) {
						Cursor cursor = getContentResolver()
								.query(uri,
										new String[] {
												MediaStore.Images.Media.DATA,
												MediaStore.Images.ImageColumns.MIME_TYPE },
										MediaStore.Images.Media._ID + "=" + id,
										null, null);

						if (cursor != null && cursor.moveToFirst()) {

							if (cursor.getString(0) != null) {

								String l = cursor
										.getString(cursor
												.getColumnIndex(MediaStore.Images.Media.DATA));
								Utils.copyFile(this, l, mediaPath);

								// Bitmap bitmap2 = Utils.compressImage(
								// mediaPath, this);
								Intent i = new Intent(
										RestaurantProfilesActivity.this,
										SearchDish_Activity.class);
								// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra(Constants.BRANCH_DETAILS,
										restaurantDetailsDto);
								i.putExtra("path", mediaPath);
								startActivity(i);
							}

							else {

//								ParcelFileDescriptor parcelFileDescriptor;
//								try {
//									parcelFileDescriptor = getContentResolver()
//											.openFileDescriptor(uri, "r");
//									FileDescriptor fileDescriptor = parcelFileDescriptor
//											.getFileDescriptor();
//									Bitmap image = BitmapFactory
//											.decodeFileDescriptor(fileDescriptor);
//									parcelFileDescriptor.close();
//
//									FileOutputStream out = null;
//									Bitmap bitmap2 = null;
//									try {
//										out = new FileOutputStream(mediaPath);
//										image.compress(
//												Bitmap.CompressFormat.PNG, 90,
//												out);
//										// bitmap2 = Utils.compressImage(
//										// mediaPath, this);
//									} catch (Exception e) {
//										e.printStackTrace();
//									} finally {
//										try {
//											out.close();
//										} catch (Throwable ignore) {
//										}
//									}
//
//									Intent inttent = new Intent(
//											RestaurantProfilesActivity.this,
//											SearchDish_Activity.class);
//									// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//									inttent.putExtra(Constants.BRANCH_DETAILS,
//											restaurantDetailsDto);
//									inttent.putExtra("path", mediaPath);
//									startActivity(inttent);
//
//								} catch (FileNotFoundException e) {
//									e.printStackTrace();
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
								
								
								Uri origUri = intent.getData();
								final int takeFlags = intent.getFlags()
										& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
								// Check for the freshest data.

								// String kitkatPath=origUri.toString();
								getContentResolver()
										.takePersistableUriPermission(origUri,
												takeFlags);
								String kitkathpath =KitkatMediaPath.getPath(RestaurantProfilesActivity.this, origUri);
								Utils.copyFile(this, kitkathpath, mediaPath);
								Intent inttent = new Intent(RestaurantProfilesActivity.this,
										SearchDish_Activity.class);
								// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								inttent.putExtra("path", mediaPath);
								inttent.putExtra(Constants.BRANCH_DETAILS,
										restaurantDetailsDto);
								startActivity(inttent);
							}

						}
						cursor.close();
					} else if (mediaPath != null && uri != null
							&& "file".equals(uri.getScheme())) {
						Utils.copyFile(this, uri.getPath(), mediaPath);
						Intent inttent = new Intent(
								RestaurantProfilesActivity.this,
								SearchDish_Activity.class);
						// inttent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						inttent.putExtra("path", mediaPath);
						inttent.putExtra(Constants.BRANCH_DETAILS,
								restaurantDetailsDto);
						startActivity(inttent);
					} else {
						Toast.makeText(
								this,
								"The item you picked is not an image. Please pick an image.",
								Toast.LENGTH_LONG).show();
						return;
					}

				}
				break;

			default:
				break;
			}
		}

	}

}
