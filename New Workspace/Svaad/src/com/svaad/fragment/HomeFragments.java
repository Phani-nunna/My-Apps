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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.DishPhotoDto;
import com.svaad.Dto.HomeDetailDto;
import com.svaad.activity.HomeSearchActivity;
import com.svaad.activity.NearByRestaurantsActivity;
import com.svaad.activity.PopularRestaurantsActivity;
import com.svaad.asynctask.HomeAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadNetworkCallBack;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.HomeResponseDto;
import com.svaad.utils.Constants;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class HomeFragments extends Fragment implements OnClickListener,
		SvaadFeedCallback, SvaadProgressCallback, SvaadNetworkCallBack {

	String queryText;

	SearchView searchview;
	// LinearLayout llNearBy;

	ImageView llNearBy;

	private EditText editext;

	ProgressBar pbar;
	// private HomeResponseDto homeResponseDto;
	private static final String ARTICLE_SCROLL_POSITION = "ARTICLE_SCROLL_POSITION";

	ImageView imagePopular, imageView1, i;

	List<HomeDetailDto> type1Lists = new ArrayList<HomeDetailDto>();
	List<HomeDetailDto> type2Lists = new ArrayList<HomeDetailDto>();

	private LayoutInflater layoutInflater;

	private View view;

	LinearLayout homeContainer, dishesContainer, near, popular;

	private ScrollView scrollView1;

	Context context;

	String imageurl = "http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/b60c0c9c-e8a3-4852-a2a8-ea17e67181ea-stetakhs.jpg";
	String nearByUrl = "http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/df2d1f37-ca92-41fc-95b5-ebcdd55b6246-nearby.jpg";
	String popularUrl = "http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/37da984f-c216-40ef-8a76-10a086d73784-popres.jpg";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if (savedInstanceState == null) {
		// getHomeList();
		// }
		// if (savedInstanceState != null) {
		// scrollView1.getViewTreeObserver().addOnPreDrawListener(
		// new ViewTreeObserver.OnPreDrawListener() {
		// @Override
		// public boolean onPreDraw() {
		// scrollView1.getViewTreeObserver()
		// .removeOnPreDrawListener(this);
		// scrollView1.setScrollY(savedInstanceState
		// .getInt(ARTICLE_SCROLL_POSITION));
		// return false;
		// }
		// });
		// }
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
		View view = inflater.inflate(R.layout.home_fragments_layout, container,
				false);

		initUi(view);

		context = getActivity();
		// initializeHome();
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

						Intent intent = new Intent(getActivity(),
								HomeSearchActivity.class);
						intent.putExtra(Constants.QUERYTEXT, queryText);
						intent.putExtra(Constants.SEE_MORE_DISHES,
								Constants.SEE_MORE_DISHES);
						startActivity(intent);
						return true;
					}
				}

				return false;
			}
		});

		if (type1Lists != null && type1Lists.size() > 0)
			setResCategoriesToContainer(type1Lists);
		if (type2Lists != null && type2Lists.size() > 0)
			setDishCategoriesToContainer(type2Lists);

		return view;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(ARTICLE_SCROLL_POSITION, scrollView1.getScrollY());

	}

	// private void initializeHome() {
	// if (homeResponseDto == null) {
	// homeResponseDto = new HomeResponseDto();
	// homeResponseDto.setResults(new ArrayList<HomeDetailDto>());
	// } else if (homeResponseDto.getResults() == null) {
	// homeResponseDto.setResults(new ArrayList<HomeDetailDto>());
	// }
	// }

	private void initUi(View v) {

		editext = (EditText) v.findViewById(R.id.editText1);

		near = (LinearLayout) v.findViewById(R.id.near);
		popular = (LinearLayout) v.findViewById(R.id.popular);

		llNearBy = (ImageView) v.findViewById(R.id.llNearBy);

		homeContainer = (LinearLayout) v.findViewById(R.id.homeContainer);

		dishesContainer = (LinearLayout) v.findViewById(R.id.dishesContainer);

		imagePopular = (ImageView) v.findViewById(R.id.imagePopular);

		scrollView1 = (ScrollView) v.findViewById(R.id.scrollView1);

		imageView1 = (ImageView) v.findViewById(R.id.imageView1);

		pbar = (ProgressBar) v.findViewById(R.id.pbar);

		if (imageurl != null && imageurl.length() > 0) {
			Picasso.with(getActivity()).load(imageurl)
					.placeholder(R.drawable.temp).error(R.drawable.temp)

					.into(imageView1);
		}
		if (popularUrl != null && popularUrl.length() > 0) {
			Picasso.with(getActivity()).load(popularUrl)
					.placeholder(R.drawable.temp).error(R.drawable.temp)

					.into(imagePopular);
		}
		if (nearByUrl != null && nearByUrl.length() > 0) {
			Picasso.with(getActivity()).load(nearByUrl)
					.placeholder(R.drawable.temp).error(R.drawable.temp)

					.into(llNearBy);
		}

		near.setOnClickListener(this);
		popular.setOnClickListener(this);

	}

	private void setDishImageToImageView(String url, ImageView imageView) {
		if (url != null && url.length() > 0) {
			Picasso.with(getActivity()).load(url).placeholder(R.drawable.temp)
					.error(R.drawable.temp)

					.into(imageView);
		} else {
			Picasso.with(getActivity()).load(R.drawable.temp)
					.placeholder(R.drawable.temp).error(R.drawable.temp)

					.into(imageView);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.near:

			getNearByRestaurant();
			break;

		case R.id.imageRes:

			HomeDetailDto home = (HomeDetailDto) v.getTag();

			// Intent in = new Intent(getActivity(),
			// CategoriesRestaurantsActivity.class);
			// in.putExtra(Constants.DATA, home);
			// startActivity(in);

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

		if (object != null) {
			if (object instanceof HomeResponseDto) {
				addResult((HomeResponseDto) object);

			}
		} else {
			return;
		}

	}

	private void addResult(HomeResponseDto homeResponseDto) {
		if (homeResponseDto != null && homeResponseDto.getResults() != null) {
			List<HomeDetailDto> homeLists = homeResponseDto.getResults();
			if (homeLists != null && homeLists.size() > 0) {
				// this.homeResponseDto.getResults().addAll(feedDto);

				for (HomeDetailDto home : homeLists) {
					int type = home.getType();
					if (type == 1) {
						type1Lists.add(home);

					} else if (type == 2) {
						type2Lists.add(home);
					}
				}

				if (type1Lists != null && type1Lists.size() > 0)
					setResCategoriesToContainer(type1Lists);
				if (type2Lists != null && type2Lists.size() > 0)
					setDishCategoriesToContainer(type2Lists);
			}
		}

	}

	private void setDishCategoriesToContainer(List<HomeDetailDto> type2Lists) {
		// layoutInflater = getActivity().getLayoutInflater();
		if (context != null) {

			layoutInflater = ((Activity) context).getLayoutInflater();
			TextView dishName;
			ImageView dishImage;

			for (int i = 0; i < type2Lists.size(); i++) {

				view = layoutInflater.inflate(R.layout.home_dishe_row,
						dishesContainer, false);
				dishName = (TextView) view.findViewById(R.id.dishName);
				dishImage = (ImageView) view.findViewById(R.id.dishImage);

				HomeDetailDto home = type2Lists.get(i);
				if (home != null) {
					DishPhotoDto photo = home.getPhoto();
					String name = home.getName();
					if (photo != null) {
						String url = photo.getUrl();
						if (url != null && url.length() > 0) {
							setDishImageToImageView(url, dishImage);
						}
					} else {
						setDishImageToImageView(null, dishImage);
					}

					if (name != null && name.length() > 0) {
						dishName.setText(name);
					}
				}

				dishImage.setTag(home);

				dishImage.setOnClickListener(this);
				dishesContainer.addView(view);
			}
		}

	}

	private void setResCategoriesToContainer(List<HomeDetailDto> type1Lists) {

		if (context != null) {

			layoutInflater = ((Activity) context).getLayoutInflater();
			// layoutInflater = getActivity().getLayoutInflater();
			TextView tvName;
			ImageView imagePopular;

			for (int i = 0; i < type1Lists.size(); i++) {

				view = layoutInflater.inflate(R.layout.home_restaurant_row,
						homeContainer, false);
				tvName = (TextView) view.findViewById(R.id.tvName);
				imagePopular = (ImageView) view.findViewById(R.id.imageRes);

				HomeDetailDto home = type1Lists.get(i);

				if (home != null) {
					String name = home.getName();
					DishPhotoDto photo = home.getPhoto();

					if (photo != null)
					{
						String url = photo.getUrl();

						if (url != null && url.length() > 0) 
						{
							setDishImageToImageView(url, imagePopular);
						}
					} 
					else 
					{
						setDishImageToImageView(null, imagePopular);
					}

					if (name != null && name.length() > 0) {
						tvName.setText(name);
					}

				}

				imagePopular.setTag(home);
				imagePopular.setOnClickListener(this);
				homeContainer.addView(view);
			}
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
