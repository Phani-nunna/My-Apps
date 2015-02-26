package com.svaad.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.BranchIdDto;
import com.svaad.Dto.CityDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.Dto.LocationDto;
import com.svaad.Dto.PlaceDto;
import com.svaad.Dto.PointDto;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.Dto.SearchTagsDto;
import com.svaad.asynctask.RestaurantAsynctask;
import com.svaad.asynctask.SearchDishesAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.requestDto.RestaurentRequestDto;
import com.svaad.responseDto.FeedResponseDto;
import com.svaad.responseDto.RestaurantListResponseDto;
import com.svaad.utils.Constants;
import com.svaad.whereDto.DishWhereDto;
import com.svaad.whereDto.RestaurantListWhereDto;
import com.svaad.whereDto.UserIdWhereDto;

public class SearchOverviewActivity extends BaseActivity implements
		SvaadFeedCallback, OnClickListener, SvaadProgressCallback {

	// SearchView searchview;
	EditText editTextSearch;
	RelativeLayout rlDishes, rlRestaurants;
	LinearLayout linearDishes, linearResraurants;
	TextView tvDishes, tvseeMore, tvRestaurants, tvResSeeMore, tvDishName,
			tvbranchName, tvDishloves, branchNameTextView,
			branchAddresstextView;
	boolean scroll=false;
	String queryText;
	private LayoutInflater layoutInflater;
	private FeedResponseDto feedResponseDto;
	private View view;
	private ImageView imageView;
	private ImageButton dishImageButton, imageButton;
	List<RestaurantDetailsDto> restaurantDetailDtoLists;
	List<FeedDetailDto> feedsList;
	TextView tvNoReults;
	FeedResponseDto feedRes;
	String dishes, res = null;
	RestaurantListResponseDto resDto;
	private ProgressBar pbar;
	private TextView branchtimings;
	private ImageView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchoverview);

		final android.app.ActionBar actionBar = getActionBar();

		setSupportProgressBarIndeterminateVisibility(false);

		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Search");

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);

		initUi();

		initializeDishesList();

		if (this.getIntent().getExtras() != null) {
			queryText = this.getIntent().getExtras()
					.getString(Constants.QUERYTEXT);

			if (queryText != null && queryText.length() > 0) {
				// searchview.setQuery(queryText != null ? queryText : "",
				// false);
				editTextSearch.setText(queryText);
			}
		}
		getSearchDishesList();

		getSearchRestaurantList();

		editTextSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| actionId == EditorInfo.IME_ACTION_DONE
								|| event.getAction() == KeyEvent.ACTION_DOWN
								&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							// hide the keyboard
							InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

							in.hideSoftInputFromWindow(
									editTextSearch.getApplicationWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

							queryText = editTextSearch.getText().toString();
							if (queryText != null && queryText.length() > 0
									&& !queryText.equals("")) {

								rlDishes.setVisibility(View.GONE);
								rlRestaurants.setVisibility(View.GONE);

								linearDishes.removeAllViews();
								linearResraurants.removeAllViews();
								// tvNoReults.setVisibility(View.GONE);

								getSearchDishesList();

								getSearchRestaurantList();
								return true;
							}
						}

						return false;
					}
				});

	}

	private void getSearchRestaurantList() {
		RestaurantAsynctask asy = new RestaurantAsynctask(
				SearchOverviewActivity.this, null, getRestaurantRequestDto());
		asy.execute();
	}

	private void getSearchDishesList() {

		SearchDishesAsynctask asy = new SearchDishesAsynctask(
				SearchOverviewActivity.this, null, Constants.BRANCH_MENU_URL,
				getDishRequestDto(),scroll);
		asy.execute();

	}

	private void initUi() {
		// searchview = (SearchView) findViewById(R.id.searchView);
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);

		rlDishes = (RelativeLayout) findViewById(R.id.rlDishes);
		rlRestaurants = (RelativeLayout) findViewById(R.id.rlRestaurants);
		linearDishes = (LinearLayout) findViewById(R.id.linearDishes);
		linearResraurants = (LinearLayout) findViewById(R.id.linearRestaurants);
		tvDishes = (TextView) findViewById(R.id.tvDishes);
		tvseeMore = (TextView) findViewById(R.id.tvSeeMore);
		tvRestaurants = (TextView) findViewById(R.id.tvRestaurants);
		tvResSeeMore = (TextView) findViewById(R.id.tvResSeeMore);
		tvNoReults = (TextView) findViewById(R.id.tvNoReults);

		pbar = (ProgressBar) findViewById(R.id.pbar);

		tvseeMore.setOnClickListener(this);
		tvResSeeMore.setOnClickListener(this);
		rlDishes.setOnClickListener(this);
		rlRestaurants.setOnClickListener(this);

	}

	@Override
	public void setResponse(Object object) {
		if (object == null) {
			return;
		}

		if (object instanceof FeedResponseDto) {

			feedRes = (FeedResponseDto) object;
			if (feedRes != null) {
				if (feedRes.getResults() != null
						&& feedRes.getResults().size() > 0) {
					addResult((FeedResponseDto) object);
				} else {
					tvNoReults.setVisibility(View.VISIBLE);
					System.out.println("No data dishes");

					dishes = "dishes";
				}
			} else {
				System.out.println("feed object is null");
			}
		}

		if (object instanceof RestaurantListResponseDto) {
			resDto = (RestaurantListResponseDto) object;

			if (resDto != null) {
				if (resDto.getResults() != null
						&& resDto.getResults().size() > 0) {
					addResultRes((RestaurantListResponseDto) object);
				} else {
					System.out.println("No data res");
					res = "res";
				}
			}

			else {
				System.out.println("res object is null");
			}
		}

		if ((dishes != null && dishes.length() > 0)
				&& (res != null && res.length() > 0)) {
			if (dishes.equalsIgnoreCase("dishes")
					&& (res.equalsIgnoreCase("res"))) {

				tvNoReults.setVisibility(View.VISIBLE);
				String text = tvNoReults.getText().toString();

				System.out.println("Textview " + text);
				// tvNoReults.setText("No results found");
				// dishes=null;
				// res=null;
			}

		}

		// if((feedRes.getResults() == null && feedRes.getResults().size()==0)
		// &&(resDto.getResults() == null && resDto.getResults().size()==0))
		// {
		// tvNoReults.setVisibility(View.VISIBLE);
		// tvNoReults.setText("No results found");
		// }
		// else
		// {
		//
		// }

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlDishes:

			Intent i = new Intent(SearchOverviewActivity.this,
					HomeSearchActivity.class);
			i.putExtra(Constants.QUERYTEXT, queryText);
			i.putExtra(Constants.SEE_MORE_DISHES, Constants.SEE_MORE_DISHES);
			// Bundle args = new Bundle();
			// args.putSerializable(Constants.DATA, (Serializable) feedsList);
			// i.putExtras(args);
			startActivity(i);

			break;

		case R.id.rlRestaurants:

			Intent intent = new Intent(SearchOverviewActivity.this,
					HomeSearchActivity.class);
			intent.putExtra(Constants.QUERYTEXT, queryText);
			intent.putExtra(Constants.SEE_MORE_RES, Constants.SEE_MORE_RES);
			// Bundle arg = new Bundle();
			// arg.putSerializable(Constants.DATA_Res, (Serializable)
			// restaurantDetailDtoLists);
			// intent.putExtras(arg);
			startActivity(intent);
			break;

		case R.id.tvSeeMore:

			Intent intentTvSeemore = new Intent(SearchOverviewActivity.this,
					HomeSearchActivity.class);
			intentTvSeemore.putExtra(Constants.QUERYTEXT, queryText);
			intentTvSeemore.putExtra(Constants.SEE_MORE_DISHES,
					Constants.SEE_MORE_DISHES);
			// Bundle args = new Bundle();
			// args.putSerializable(Constants.DATA, (Serializable) feedsList);
			// i.putExtras(args);
			startActivity(intentTvSeemore);

			break;

		case R.id.tvResSeeMore:

			Intent intenttvResMore = new Intent(SearchOverviewActivity.this,
					HomeSearchActivity.class);
			intenttvResMore.putExtra(Constants.QUERYTEXT, queryText);
			intenttvResMore.putExtra(Constants.SEE_MORE_RES,
					Constants.SEE_MORE_RES);
			// Bundle arg = new Bundle();
			// arg.putSerializable(Constants.DATA_Res, (Serializable)
			// restaurantDetailDtoLists);
			// intent.putExtras(arg);
			startActivity(intenttvResMore);
			break;

		case R.id.imageView:

			FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

			Intent dishProfileIntent = new Intent(SearchOverviewActivity.this,
					DishProfileActivity.class);
			dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

			startActivity(dishProfileIntent);

			finish();

			break;

		case R.id.dishImageButton:

			RestaurantDetailsDto restaurantDetailsDto = (RestaurantDetailsDto) v
					.getTag();

			Intent intRes = new Intent(SearchOverviewActivity.this,
					RestaurantProfilesActivity.class);

			intRes.putExtra(Constants.DATA, restaurantDetailsDto);

			startActivity(intRes);

			finish();

			break;

		default:
			break;
		}

	}

	private void initializeDishesList() {
		if (feedResponseDto == null) {
			feedResponseDto = new FeedResponseDto();
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		} else if (feedResponseDto.getResults() == null) {
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		}
	}

	private void addResult(FeedResponseDto feedResponseDto) {

		List<FeedDetailDto> feedDetailDto = null;
		feedsList = new ArrayList<FeedDetailDto>();

		if (feedResponseDto != null && feedResponseDto.getResults() != null
				&& feedResponseDto.getResults().size() > 0) {
			feedDetailDto = feedResponseDto.getResults();

		}

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
				//
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

				if (oneTag != 0) {
					branchDishIdDto.setOneTag(oneTag);
				}

				feed.setBranchDishId(branchDishIdDto);

				feedsList.add(feed);

			}
		}

		// this.feedResponseDto.getResults().addAll(feedDetailDto);
		setDishesListtoContainer(feedsList);

	}

	private void addResultRes(RestaurantListResponseDto object) {
		if (object != null && object.getResults() != null
				&& object.getResults().size() > 0) {
			restaurantDetailDtoLists = object.getResults();
			setRestaurantListtoContainer(restaurantDetailDtoLists);
		}
		// else
		// {
		// tvNoReultsRes.setVisibility(View.VISIBLE);
		// tvNoReultsRes.setText("No results found");
		// }
	}

	private void setDishesListtoContainer(List<FeedDetailDto> dishesList) {

		layoutInflater = getLayoutInflater();

		if (dishesList != null && dishesList.size() > 0) {
			rlDishes.setVisibility(View.VISIBLE);
			tvseeMore.setText("View All");
			for (int i = 0; i < 2; i++) {
				view = layoutInflater.inflate(R.layout.searchdishes_row,
						linearDishes, false);

				initScrollviewUi(view);
				FeedDetailDto feed = dishesList.get(i);

				BranchDishIdDto branchDishIdDto = feed.getBranchDishId();
				if (branchDishIdDto != null) {
					DishIdDto dishIdDto = branchDishIdDto.getDishId();
					DishPicDto dishpicDto = dishIdDto.getDishPic();
					String branchName = branchDishIdDto.getBranchName();

					// String oneTag=branchDishIdDto.getOneTag();

					int oneTag = branchDishIdDto.getOneTag();
					if (dishIdDto != null) {
						String dishName = dishIdDto.getName();
						if (dishName != null && dishName.length() > 0) {
							tvDishName.setText(dishName);
						} else {
							tvDishName.setText(null);
						}
					}

					if (dishpicDto != null) {
						String pic = dishpicDto.getUrl();
						if (pic != null && pic.length() > 0) {
							Picasso.with(SearchOverviewActivity.this).load(pic)
									.placeholder(R.drawable.temp)
									.error(R.drawable.temp)

									.into(imageView);
						} else {
							Picasso.with(SearchOverviewActivity.this)
									.load(R.drawable.temp)
									.placeholder(R.drawable.temp)
									.error(R.drawable.temp)

									.into(imageView);
						}
					}

					if (branchName != null && branchName.length() > 0) {
						tvbranchName.setText(branchName);

					}

					if (oneTag != 0) {
						tvDishloves.setText(oneTag + " people lovedit");
					} else {
						tvDishloves.setText("0 people lovedit");
					}

					imageView.setOnClickListener(this);
					imageView.setTag(feed);

				}

				linearDishes.addView(view);

			}
		} else {
			rlDishes.setVisibility(View.GONE);
		}

	}

	private void setRestaurantListtoContainer(
			List<RestaurantDetailsDto> restaurantList) {

		layoutInflater = getLayoutInflater();

		if (restaurantList != null && restaurantList.size() > 0) {
			rlRestaurants.setVisibility(View.VISIBLE);
			tvResSeeMore.setText("View All");
			for (int i = 0; i < 2; i++) {
				view = layoutInflater.inflate(R.layout.restaurantthumbnail,
						linearResraurants, false);

				initScrollviewResUi(view);

				RestaurantDetailsDto restauranDetailDto = restaurantList.get(i);

				if (restaurantList.get(i).getName() == null) {

					branchNameTextView.setText((restaurantList.get(i)
							.getBranchName() != null ? restaurantList.get(i)
							.getBranchName() : ""));

				} else {

					branchNameTextView.setText((restaurantList.get(i)
							.getBranchName() != null ? restaurantList.get(i)
							.getBranchName() : ""));
				}

				if (restaurantList.get(i).getLocation() != null) {

					if (restaurantList.get(i).getLocation().getName() != null
							&& restaurantList.get(i).getLocation().getName()
									.length() > 0) {
						branchAddresstextView.setText((restaurantList.get(i)
								.getLocation().getName()));
					} else {
						branchAddresstextView.setText("");
					}
				}

				if (restaurantList.get(i).getTimings() != null) {
					String time = restaurantList.get(i).getTimings();
					if (restaurantList.get(i).getTimings() != null
							&& restaurantList.get(i).getTimings().length() > 0) {
						String p = time(time);
						if (p != null && p.length() > 0
								&& !p.equalsIgnoreCase("")) {
							status.setVisibility(View.VISIBLE);
							String status1 = p.substring(0, p.length() - 1);
							branchtimings.setText(status1 + "");
							char[] stat = p.toCharArray();
							int check = Integer.parseInt(stat[stat.length - 1]
									+ "");
							if (check == 0) {
								status.setImageResource(R.drawable.timings_green_96);
							} else if (check == 1) {
								status.setImageResource(R.drawable.timings_orange_96);
							} else {
								status.setImageResource(R.drawable.timings_red_96);
							}

						} else {
							branchtimings.setText(null);
							status.setVisibility(View.INVISIBLE);
						}

					} else {
						status.setVisibility(View.INVISIBLE);
						branchtimings.setText(null);
					}
				} else {
					status.setVisibility(View.INVISIBLE);
					branchtimings.setText(null);

				}

				String url = "";

				if (restaurantList.get(i).getPhoto() == null) {

					if (restaurantList.get(i).getCoverPic() != null
							&& restaurantList.get(i).getCoverPic().getUrl() != null) {
						url = restaurantList.get(i).getCoverPic().getUrl();

						Picasso.with(SearchOverviewActivity.this).load(url)
								.placeholder(R.drawable.temp)
								.error(R.drawable.temp)

								.into(dishImageButton);

					} else {
						Picasso.with(SearchOverviewActivity.this)
								.load(R.drawable.temp)
								.placeholder(R.drawable.temp)
								.error(R.drawable.temp)

								.into(dishImageButton);
					}

				}

				dishImageButton.setOnClickListener(this);
				dishImageButton.setTag(restauranDetailDto);

				linearResraurants.addView(view);

			}
		} else {
			rlRestaurants.setVisibility(View.GONE);
		}

	}

	private void initScrollviewResUi(View v) {

		dishImageButton = (ImageButton) v.findViewById(R.id.dishImageButton);

		branchNameTextView = (TextView) v.findViewById(R.id.branchNameTextView);
		branchAddresstextView = (TextView) v
				.findViewById(R.id.branchAddresstextView);

		branchtimings = (TextView) view.findViewById(R.id.branchtimings);
		status = (ImageView) view.findViewById(R.id.status);

	}

	private void initScrollviewUi(View view2) {
		imageView = (ImageView) view.findViewById(R.id.imageView);
		tvDishloves = (TextView) view.findViewById(R.id.tvDishloves);
		tvDishName = (TextView) view.findViewById(R.id.tvDishName);
		tvbranchName = (TextView) view.findViewById(R.id.tvbranchName);
		imageButton = (ImageButton) view.findViewById(R.id.imageButton);

		imageButton.setVisibility(View.GONE);
	}

	// For dishes requestdto with out location,geopoint and nearby
	public DishWhereDto getDishRequestDto() {

		UserIdWhereDto userIdWhereDto = new UserIdWhereDto();
		userIdWhereDto.setPublish(true);
		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {

			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
			userIdWhereDto.setSearchTags(searchTagsDto);
		}

		DishWhereDto dishwheredto = new DishWhereDto();
		dishwheredto.setWhere(userIdWhereDto);
		dishwheredto.set_method("GET");
		dishwheredto.setInclude("dishId,location");
		dishwheredto.setOrder("-comments");
		dishwheredto.setLimit(2);
		dishwheredto.setSkip(0);

		return dishwheredto;

	}

	// For restaurants requestdto with out location,geopoint and nearby

	public RestaurentRequestDto getRestaurantRequestDto() {
		RestaurentRequestDto restaurentRequestDto = new RestaurentRequestDto();
		RestaurantListWhereDto whereDto = new RestaurantListWhereDto();

		SearchTagsDto searchTagsDto = null;
		if (queryText != null && queryText.toLowerCase().trim().length() > 0) {
			searchTagsDto = new SearchTagsDto();
			searchTagsDto.set$all(queryText.toLowerCase().trim().split(" "));
			whereDto.setSearchTags(searchTagsDto);
		}
		whereDto.setPublish(true);

		restaurentRequestDto.setOrder("-createdAt");
		restaurentRequestDto.setInclude("location");
		restaurentRequestDto.setLimit(2);
		restaurentRequestDto.setSkip(0);
		restaurentRequestDto.set_method("GET");
		restaurentRequestDto.setWhere(whereDto);

		return restaurentRequestDto;

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

	@SuppressLint("SimpleDateFormat")
	private String time(String b) {
		// TODO Auto-generated method stub
		String[] tokens, timings, days1;
		int[] days;
		tokens = new String[120];
		days1 = new String[120];
		timings = new String[120];
		days = new int[50];
		int count = 0;
		String s = b.substring(b.indexOf("{") + 1, b.indexOf("}"));
		StringTokenizer tymsplttc = new StringTokenizer(s, "]");

		for (int u = 0; tymsplttc.hasMoreTokens(); u++) {
			tokens[u] = tymsplttc.nextToken();
			if (tokens[u].startsWith(",")) {
				days1[u] = tokens[u].substring(2, 4);
				timings[u] = tokens[u].substring(8, tokens[u].length() - 1);
			} else {
				days1[u] = tokens[u].substring(1, 3);
				timings[u] = tokens[u].substring(7, tokens[u].length() - 1);
			}
			count++;

		}

		for (int y = 0; count < 7; y++) {
			days[y] = Integer
					.parseInt(days1[y].substring(1, days1[y].length()));

		}

		Date dt = new Date();
		@SuppressWarnings("deprecation")
		int hours = dt.getHours();
		@SuppressWarnings("deprecation")
		int minutes = dt.getMinutes();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		String time1 = sdf.format(dt);
		String am = time1.substring(5, time1.length() - 1);
		if (am.equals("pm")) {
			if (hours <= 12) {
				hours = hours + 12;

			}
		}
		int currenttime = hours * 60 + minutes;

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day = day - 1;

		return getTimings(timings[day], currenttime);

	}

	public String getTimings(String timings, int x) {
		String timeret = "";
		if (timings.length() > 0 && timings != null
				&& !timings.equalsIgnoreCase("")) {
			timeret = checktime(timings, x);

		} else {
			timeret = "Holiday2";
		}

		return timeret;
	}

	private String checktime(String string, int currenttime)
			throws NullPointerException {
		int count = 0, count2 = 0;
		String[] tokens1, first, second;
		int[] alltime;
		tokens1 = new String[20];
		first = new String[120];
		second = new String[120];
		String ap = null, ap2 = null, ap1 = null, ap3 = null;

		if (string.contains("\",\"")) {

			StringTokenizer tymsplttc = new StringTokenizer(string, "\",\"");
			for (int u = 0; tymsplttc.hasMoreTokens(); u++) {
				tokens1[u] = tymsplttc.nextToken();
				count++;
			}

			count2 = 2 * count;
			StringTokenizer tymsplt;

			for (int y = 0; count > 0; y++) {
				tymsplt = new StringTokenizer(tokens1[y], "-");
				first[y] = tymsplt.nextToken();
				second[y] = tymsplt.nextToken();
				count = count - 1;
			}

			StringTokenizer hsplt, hsplt1;
			alltime = new int[count2];
			for (int p = 0, q = 0, i = 0; count2 > 0; p++, q++, i++) {

				if (first[p] != null && !first[p].equalsIgnoreCase("")
						&& first[p].length() > 0) {
					hsplt = new StringTokenizer(first[p], ":");
					ap = hsplt.nextToken();
					ap1 = hsplt.nextToken();

					if (ap != null && !ap.equalsIgnoreCase("")
							&& ap.length() > 0 && ap1 != null
							&& !ap1.equalsIgnoreCase("") && ap1.length() > 0) {
						int hour = Integer.parseInt(ap);
						int min = Integer.parseInt(ap1);
						alltime[i] = (hour * 60 + min);
					}
				}
				i++;
				if (second[q] != null && !second[q].equalsIgnoreCase("")
						&& second[q].length() > 0) {
					hsplt1 = new StringTokenizer(second[q], ":");
					ap2 = hsplt1.nextToken();
					ap3 = hsplt1.nextToken();
					if (ap2 != null && !ap2.equalsIgnoreCase("")
							&& ap2.length() > 0 && ap3 != null
							&& !ap3.equalsIgnoreCase("") && ap3.length() > 0) {
						int hour1 = Integer.parseInt(ap2);
						int min1 = Integer.parseInt(ap3);
						alltime[i] = (hour1 * 60 + min1);
					}
				}
				count2 = count2 - 1;
			}

			if (currenttime < alltime[0]) {
				int opentime = alltime[0];

				int h = opentime / 60;
				int m = opentime % 60;

				if ((m + "").length() <= 1) {
					return "Closed, Opens at " + h + ":0" + m + "" + 1;

				} else {
					return "Closed, Opens at " + h + ":" + m + "" + 1;
				}

			} else if (currenttime > alltime[alltime.length - 1]) {
				return "Closed Today2";

			} else {
				for (int j = 0; j < (alltime.length) - 1; j++) {
					// opens until
					if (currenttime > alltime[j]
							&& currenttime < alltime[j + 1]) {

						int opentime = alltime[j + 1];
						int h = opentime / 60;
						int m = opentime % 60;

						if ((m + "").length() <= 1) {
							return "Open Now, until " + h + ":0" + m + "0";
						} else {
							return "Open Now, until " + h + ":" + m + "0";
						}
					}
					j++;
				}

				for (int j = 0; j < (alltime.length) - 2; j++) {
					// opens at
					if (currenttime > alltime[j + 1]
							&& currenttime < alltime[j + 2]) {
						int opentime = alltime[j + 2];
						int h = opentime / 60;
						int m = opentime % 60;

						if ((m + "").length() <= 1) {
							return "Closed, Opens at " + h + ":0" + m + "1";
						} else {
							return "Closed, Opens at " + h + ":" + m + "1";
						}
					}
					j++;

				}

			}

		} else {

			int alltime1 = 0, alltime2 = 0;
			count2 = count * 2;
			StringTokenizer tymsplt;
			tymsplt = new StringTokenizer(string, "-");
			String a = tymsplt.nextToken();
			String b = tymsplt.nextToken();
			StringTokenizer hsplt, hsplt1;
			if (a != null && !a.equalsIgnoreCase("") && a.length() > 0) {
				hsplt = new StringTokenizer(a, ":");
				String ap6 = hsplt.nextToken();
				String ap7 = hsplt.nextToken();
				if (ap6 != null && !ap6.equalsIgnoreCase("")
						&& ap6.length() > 0 && ap7 != null
						&& !ap7.equalsIgnoreCase("") && ap7.length() > 0) {
					int hour = Integer.parseInt(ap6);
					int min = Integer.parseInt(ap7);
					alltime1 = (hour * 60 + min);
				}
			}

			if (b != null && !b.equalsIgnoreCase("") && b.length() > 0) {
				hsplt1 = new StringTokenizer(b, ":");
				String ap8 = hsplt1.nextToken();
				String ap9 = hsplt1.nextToken();
				if (ap8 != null && !ap8.equalsIgnoreCase("")
						&& ap8.length() > 0 && ap9 != null
						&& !ap9.equalsIgnoreCase("") && ap9.length() > 0) {
					int hour1 = Integer.parseInt(ap8);
					int min1 = Integer.parseInt(ap9);
					alltime2 = (hour1 * 60 + min1);
				}
			}

			if (currenttime < alltime1) {
				int opentime = alltime1;

				int h = opentime / 60;
				int m = opentime % 60;

				if ((m + "").length() <= 1) {
					return "CLosed,Opens at " + h + ":0" + m + "1";
				} else {
					return "Closed,Opens at " + h + ":" + m + "1";
				}

			} else if (currenttime > alltime2) {
				return "Closed Today2";

			} else if (currenttime > alltime1 && currenttime < alltime2) {
				int opentime = alltime2;
				int h = opentime / 60;
				int m = opentime % 60;

				if ((m + "").length() <= 1) {
					return "Open Now, until " + h + ":0" + m + "0";
				} else {
					return "Open Now, until " + h + ":" + m + "0";
				}

			}

		}
		return "";

	}
}
