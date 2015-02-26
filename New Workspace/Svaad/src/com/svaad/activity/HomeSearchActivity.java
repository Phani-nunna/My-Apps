package com.svaad.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.slidinglayer.SlidingLayer;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.fragment.NewLocationFragemnt;
import com.svaad.fragment.NewPriceFragemnt;
import com.svaad.fragment.SearchDishesGridFragment;
import com.svaad.fragment.SearchRestaurantsFragemnt;
import com.svaad.fragment.VegOrNonvegFragemnt;
import com.svaad.utils.Constants;
import com.svaad.utils.Utils;

public class HomeSearchActivity extends BaseActivity implements OnClickListener {

	private SlidingLayer mSlidingLayer;
	// SearchView searchView;
	EditText searchView;
	Button btnClose, btnLocation, btnPrice, btnDishes, btnRestaurant;
	String mStickContainerToRightLeftOrMiddle = "top";
	Button btnVegOrNonveg;
	FragmentTransaction fragmentTransaction;
	boolean open = false;
	String dishAndRes;
	String queryText;
	LinearLayout llinear, linearResultsContainer;
	String dishesSeeMore, resSeeMore, nearby;

	// private List<RestaurantDetailsDto> restaurantList;
	// private List<FeedDetailDto> searchDishesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_search_layout);

		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		final ActionBar actionBar = getActionBar();
		setSupportProgressBarIndeterminateVisibility(false);

//		actionBar.setLogo(R.drawable.home_icon);
		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setDisplayShowTitleEnabled(true);
		

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
//		Utils.saveToSharedPreference("radioVegValue",null);
//		Utils.saveToSharedPreferenceInt("radioVeg", 0);
//		
//	 	Utils.saveToSharedPreferenceInt("radioLocaion", 0);
//		Utils.saveToSharedPreference("radioLocationValue",null);
//		Utils.saveToSharedPreference("radioLocationObjectId",null);
		
		initUI();
		if (this.getIntent().getExtras() != null) {
			queryText = this.getIntent().getExtras()
					.getString(Constants.QUERYTEXT);

			nearby = this.getIntent().getExtras().getString("Near by");
			
			if(nearby!=null)
			{
				actionBar.setTitle("Nearby Restaurants");
			}
			else
			{
				actionBar.setTitle("Search");
			}
			
			// restaurantList = (ArrayList<RestaurantDetailsDto>)
			// this.getIntent().getExtras().getSerializable(Constants.DATA_Res);
			// searchDishesList = (ArrayList<FeedDetailDto>)
			// this.getIntent().getExtras().getSerializable(Constants.DATA);

			dishesSeeMore = this.getIntent().getExtras()
					.getString(Constants.SEE_MORE_DISHES);
			resSeeMore = this.getIntent().getExtras()
					.getString(Constants.SEE_MORE_RES);

			if (queryText != null && queryText.length() > 0) {
				// searchView.setQuery(queryText != null ? queryText : "",
				// false);
				searchView.setText(queryText);
			}
		}

		String radioLocationValue = Utils
				.getFromSharedPreference("radioLocationValue");

		String radioVegValue = Utils.getFromSharedPreference("radioVegValue");

		if (radioLocationValue != null) {
			btnLocation.setText(radioLocationValue);
		}

		if (radioVegValue != null) {
			btnVegOrNonveg.setText(radioVegValue);
		}

		btnPrice.setVisibility(View.GONE);

		// btnDishes.setBackgroundColor(Color.GREEN);
		btnDishes.setBackgroundResource(R.drawable.blueline);
		btnRestaurant.setBackgroundColor(Color.parseColor("#fbfbfbfb"));

		dishAndRes = "dishes";

		LayoutParams rlp = (LayoutParams) mSlidingLayer.getLayoutParams();

		searchView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| actionId == EditorInfo.IME_ACTION_DONE
								|| event.getAction() == KeyEvent.ACTION_DOWN
								&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							
							//hide the keyboard
							InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			                in.hideSoftInputFromWindow(searchView
			                        .getApplicationWindowToken(),
			                        InputMethodManager.HIDE_NOT_ALWAYS);

							queryText = searchView.getText().toString();
							if (queryText != null && queryText.length() > 0
									&& !queryText.equals("")) {

								if (dishAndRes.equalsIgnoreCase("dishes")) {

									if (queryText != null
											&& queryText.equalsIgnoreCase("")) {

										Toast.makeText(
												HomeSearchActivity.this,
												"Please enter something in search",
												Toast.LENGTH_LONG).show();
									} else {

										FragmentTransaction fragmentTransactions = getSupportFragmentManager()
												.beginTransaction();

//										SearchDishesFragemnt search = new SearchDishesFragemnt();
										SearchDishesGridFragment search = new SearchDishesGridFragment();
										Bundle bundle = new Bundle();
										bundle.putString("search", queryText);

										search.setArguments(bundle);

										fragmentTransactions.replace(
												R.id.linearResultsContainer,
												search);

										fragmentTransactions.commit();
									}
								} else {
									FragmentTransaction fragmentTransactions = getSupportFragmentManager()
											.beginTransaction();

									SearchRestaurantsFragemnt search = new SearchRestaurantsFragemnt();
									Bundle bundle = new Bundle();
									bundle.putString("search", queryText);

									search.setArguments(bundle);

									fragmentTransactions
											.replace(
													R.id.linearResultsContainer,
													search);

									fragmentTransactions.commit();
								}

								return true;
							}
						}

						return false;
					}
				});

		int textResource;
		Drawable d;

		if (mStickContainerToRightLeftOrMiddle.equals("top")) {
			textResource = R.string.swipe_up_label;
			d = getResources().getDrawable(R.drawable.ic_launcher);

			mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_TOP);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rlp.width = LayoutParams.MATCH_PARENT;
			rlp.height = getResources().getDimensionPixelSize(
					R.dimen.layer_width);

		}

		// to prevent dragging...
		mSlidingLayer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mSlidingLayer.isOpened())
					return true;
				else
					return false;
			}
		});

//		FragmentTransaction fragmentTransactions = getSupportFragmentManager()
//				.beginTransaction();
//		fragmentTransactions.replace(R.id.linear, new NewLocationFragemnt());
//
//		fragmentTransactions.commit();

		// if(searchDishesList!=null && searchDishesList.size()>0)
		// {
		if (dishesSeeMore != null && dishesSeeMore.length() > 0) {

//			SearchDishesFragemnt search = new SearchDishesFragemnt();
			SearchDishesGridFragment search = new SearchDishesGridFragment();
			Bundle bundle = new Bundle();
			bundle.putString("search", queryText);
			// bundle.putSerializable(Constants.DATA, (Serializable)
			// searchDishesList);
			search.setArguments(bundle);

			fragmentTransaction.replace(R.id.linearResultsContainer, search);

			fragmentTransaction.commit();
		}
		// else if(restaurantList!=null && restaurantList.size()>0)
		// {
		else if (resSeeMore != null && resSeeMore.length() > 0) {

			dishAndRes = "restaurant";
			btnPrice.setVisibility(View.GONE);
			btnRestaurant.setBackgroundResource(R.drawable.blueline);
			btnDishes.setBackgroundColor(Color.parseColor("#ffffff"));

			SearchRestaurantsFragemnt search = new SearchRestaurantsFragemnt();
			Bundle bundle = new Bundle();
			bundle.putString("search", queryText);
			// bundle.putSerializable(Constants.DATA_Res, (Serializable)
			// restaurantList);
			search.setArguments(bundle);

			fragmentTransaction.replace(R.id.linearResultsContainer, search);

			fragmentTransaction.commit();
		}

		if (nearby != null && nearby.equalsIgnoreCase("Near by")) {
			dishAndRes = "restaurant";
			btnPrice.setVisibility(View.GONE);
			btnRestaurant.setBackgroundResource(R.drawable.blueline);
			btnDishes.setBackgroundColor(Color.parseColor("#ffffff"));

			SearchRestaurantsFragemnt search = new SearchRestaurantsFragemnt();
			Bundle bundle = new Bundle();
			bundle.putString("Near by", "Near by");
			// bundle.putSerializable(Constants.DATA_Res, (Serializable)
			// restaurantList);
			search.setArguments(bundle);

			fragmentTransaction.replace(R.id.linearResultsContainer, search);

			fragmentTransaction.commit();
		}

	}

//	 @Override
//	 public boolean onCreateOptionsMenu(Menu menu) {
//	 MenuInflater inflater = getMenuInflater();
//	 inflater.inflate(R.menu.viewer_menu, menu);
//	
//	 return super.onCreateOptionsMenu(menu);
//	 }

	public void initUI() {

		mSlidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer1);
		// searchView = (SearchView) findViewById(R.id.searchView1);
		searchView = (EditText) findViewById(R.id.searchView1);

		btnClose = (Button) findViewById(R.id.buttonClose);
		llinear = (LinearLayout) findViewById(R.id.linear);
		linearResultsContainer = (LinearLayout) findViewById(R.id.linearResultsContainer);
		btnLocation = (Button) findViewById(R.id.btnLocation);
		btnPrice = (Button) findViewById(R.id.btnPrice);
		btnVegOrNonveg = (Button) findViewById(R.id.btnVegOrNonveg);
		btnDishes = (Button) findViewById(R.id.btnDishes);
		btnRestaurant = (Button) findViewById(R.id.btnRestaurant);

		btnPrice.setOnClickListener(this);
		btnLocation.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		btnRestaurant.setOnClickListener(this);
		btnDishes.setOnClickListener(this);
		btnVegOrNonveg.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.buttonClose:

			if (mSlidingLayer.isOpened()) {
				open = false;

				if (dishAndRes.equals("dishes")) {

					if (queryText == null || queryText.equals("")) {
						Toast.makeText(HomeSearchActivity.this,
								"Please enter some dishes in search",
								Toast.LENGTH_SHORT).show();
						mSlidingLayer.closeLayer(true);
						return;
					}
				}

				mSlidingLayer.closeLayer(true);

				// try {
				// Thread.sleep(500);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				if (dishAndRes.equalsIgnoreCase("dishes")) {
					FragmentTransaction fragmentTransactions = getSupportFragmentManager()
							.beginTransaction();

//					SearchDishesFragemnt search = new SearchDishesFragemnt();
					SearchDishesGridFragment search = new SearchDishesGridFragment();
					Bundle bundle = new Bundle();
					bundle.putString("search", queryText);
					search.setArguments(bundle);

					fragmentTransactions.replace(R.id.linearResultsContainer,
							search);

					fragmentTransactions.commit();
				} else {
					// FragmentTransaction fragmentTransactions =
					// getSupportFragmentManager()
					// .beginTransaction();
					//
					// SearchRestaurantsFragemnt search = new
					// SearchRestaurantsFragemnt();
					// Bundle bundle = new Bundle();
					// bundle.putString("search", queryText);
					//
					// search.setArguments(bundle);
					//
					// fragmentTransactions.replace(R.id.linearResultsContainer,
					// search);
					//
					// fragmentTransactions.commit();
				}

			}

			break;
		case R.id.btnDishes:

			dishAndRes = "dishes";

			btnPrice.setVisibility(View.GONE);
			
			queryText=searchView.getText().toString();
			
//			btnPrice.setVisibility(View.VISIBLE);
			btnDishes.setBackgroundResource(R.drawable.blueline);
			getSupportActionBar().setTitle("Search Dishes");
			btnRestaurant.setBackgroundColor(Color.parseColor("#ffffff"));

			if (queryText == null || queryText.equals("")) {
				Toast.makeText(HomeSearchActivity.this,
						"Please enter something in Search", Toast.LENGTH_LONG)
						.show();
				FragmentTransaction fragmentTransactions = getSupportFragmentManager()
						.beginTransaction();

//				SearchDishesFragemnt search = new SearchDishesFragemnt();
				SearchDishesGridFragment search = new SearchDishesGridFragment();

				fragmentTransactions.replace(R.id.linearResultsContainer,
						search);

				fragmentTransactions.commit();

			}

			else {
				FragmentTransaction fragmentTransactions = getSupportFragmentManager()
						.beginTransaction();

//				SearchDishesFragemnt search = new SearchDishesFragemnt();
				SearchDishesGridFragment search = new SearchDishesGridFragment();
				Bundle bundle = new Bundle();
				bundle.putString("search", queryText);

				search.setArguments(bundle);

				fragmentTransactions.replace(R.id.linearResultsContainer,
						search);

				fragmentTransactions.commit();
			}
			break;

		case R.id.btnRestaurant:

			dishAndRes = "restaurant";
			
			queryText=searchView.getText().toString();

			btnPrice.setVisibility(View.GONE);

			btnRestaurant.setBackgroundResource(R.drawable.blueline);
			btnDishes.setBackgroundColor(Color.parseColor("#ffffff"));

			getSupportActionBar().setTitle("Search Restaurants");

			FragmentTransaction fragmentTransactions = getSupportFragmentManager()
					.beginTransaction();

			
			if(queryText!=null)
			{
				SearchRestaurantsFragemnt search = new SearchRestaurantsFragemnt();
				Bundle bundle = new Bundle();
				bundle.putString("search", queryText);

			search.setArguments(bundle);
				fragmentTransactions.replace(R.id.linearResultsContainer,
						search);

				fragmentTransactions.commit();
			}

			

			break;

		case R.id.btnLocation:

			String radioLocationValue = Utils
					.getFromSharedPreference("radioLocationValue");

			if (radioLocationValue != null) {
				btnLocation.setText(radioLocationValue);
			}

			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.linear, new NewLocationFragemnt());

			fragmentTransaction.commit();

			break;
		case R.id.btnPrice:
			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.linear, new NewPriceFragemnt());

			fragmentTransaction.commit();
			break;

		case R.id.btnVegOrNonveg:
			fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.linear, new VegOrNonvegFragemnt());

			fragmentTransaction.commit();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		 case R.id.action_filter:
		
		 if (mSlidingLayer.isOpened()) {
		 open = false;
		 mSlidingLayer.closeLayer(true);
		 } else {
		 open = true;
		 mSlidingLayer.openLayer(true);
		
		 }
		
		 break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}



	public void setLocationText(String text) {
		btnLocation.setText(text);
	}

	public void setAllText(String text) {
		btnVegOrNonveg.setText(text);
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
