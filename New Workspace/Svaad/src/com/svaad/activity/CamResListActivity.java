package com.svaad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.Dto.CamDishesDeatailDto;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.adapter.CamTabAdapter;
import com.svaad.utils.Constants;

public class CamResListActivity extends BaseActivity {
	private String queryText;
	ActionBar actionBar;
	private RelativeLayout listRL;
	private EditText editext;
	private CamTabAdapter adapter;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private final String[] TITLES = {"Search", "Near by", "Recent" };
	private String path;
	private CamRestaurantDetailDto restaurantDetail;
	private CamDishesDeatailDto dishDetail;
	private ImageButton btnBack;
	public static Activity mactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam_res_activity);

		init();
		
		hideKeyboard();
		actionBar = getActionBar();
//		actionBar.hide();
		
		mactivity=this;
		setSupportProgressBarIndeterminateVisibility(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle("Choose Restaurant");

		path = getIntent().getExtras().getString("path");
		restaurantDetail = (CamRestaurantDetailDto) (this.getIntent()
				.getExtras() != null ? this.getIntent().getExtras()
				.getSerializable(Constants.CAM_RES_DATA) : null);

		dishDetail = (CamDishesDeatailDto) (this.getIntent().getExtras() != null ? this
				.getIntent().getExtras()
				.getSerializable(Constants.CAM_DISHES_DATA)
				: null);
		setpager(queryText);
	}

	private void init() {
		listRL = (RelativeLayout) findViewById(R.id.listRL);
		editext = (EditText) findViewById(R.id.editTextRes);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		
		btnBack=(ImageButton)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				finish();
				
			}
		});
		editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					hideKeyboard();
					queryText = editext.getText().toString();
					if (queryText != null && queryText.length() > 0
							&& !queryText.equals("")) {


						setpager(queryText);
						return true;
					}
				}

				return false;
			}
		});

	}

	public void setpager(String searchText) {

		if (searchText != null) {

			adapter = new CamTabAdapter(getSupportFragmentManager(), TITLES,
					searchText, path);

			pager.setAdapter(adapter);

			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			pager.setPageMargin(pageMargin);

			pager.setOffscreenPageLimit(5);

			tabs.setViewPager(pager);

			tabs.setBackgroundColor(getResources().getColor(
					R.color.tabbackground));
			tabs.setIndicatorHeight(10);

			tabs.setTextColor(getResources().getColor(R.color.tabtext));
			tabs.setShouldExpand(true);
			tabs.setIndicatorColor(getResources()
					.getColor(R.color.tabindicator));

		} else {

			adapter = new CamTabAdapter(getSupportFragmentManager(), TITLES,
					searchText, path);

			pager.setAdapter(adapter);

			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			pager.setPageMargin(pageMargin);

			pager.setOffscreenPageLimit(5);

			tabs.setViewPager(pager);

			tabs.setBackgroundColor(getResources().getColor(
					R.color.tabbackground));
			tabs.setIndicatorHeight(10);

			tabs.setTextColor(getResources().getColor(R.color.tabtext));
			tabs.setShouldExpand(true);
			tabs.setIndicatorColor(getResources()
					.getColor(R.color.tabindicator));
		}
	}

	
	private void hideKeyboard()
	{
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		in.hideSoftInputFromWindow(
				editext.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

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
}
