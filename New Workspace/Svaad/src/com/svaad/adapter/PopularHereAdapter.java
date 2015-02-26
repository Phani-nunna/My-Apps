package com.svaad.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPicDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.AteIt_activity;
import com.svaad.activity.DishProfileActivity;
import com.svaad.asynctask.LikeAsyncTask;
import com.svaad.asynctask.UnlikeAsyncTask;
import com.svaad.utils.Constants;
import com.svaad.utils.ObjectSerializer;
import com.svaad.utils.SvaadDialogs;
import com.svaad.utils.Utils;

public class PopularHereAdapter extends ArrayAdapter<FeedDetailDto> {

	Context context;
	List<FeedDetailDto> categoriesList;
	private int listViewId;
	int count = 0;
	FeedDetailDto feed;
	List<String> wishlists = null;
	List<String> likesList = new ArrayList<String>();
	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());

	public PopularHereAdapter(Context context, int listViewId,
			List<FeedDetailDto> categoriesList) {
		super(context, listViewId, categoriesList);
		this.context = context;
		this.listViewId = listViewId;
		this.categoriesList = categoriesList;
	}

	@Override
	public int getCount() {
		return categoriesList.size();
	}

	@Override
	public FeedDetailDto getItem(int arg0) {
		return categoriesList.get(arg0);
	}

	public List<FeedDetailDto> getFeedDtos() {
		return categoriesList;
	}

	public void setFeedDtos(List<FeedDetailDto> feedList) {
		this.categoriesList = feedList;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup group) {
		final ViewHolder holder;

		feed = getItem(arg0);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.popularhereitem_row, group, false);

			holder = new ViewHolder();

			holder.dishImage = (ImageView) view.findViewById(R.id.imageView);

			holder.btnAddTo = (ImageButton) view.findViewById(R.id.imageButton);

			holder.tvDishName = (TextView) view.findViewById(R.id.tvDishName);

			holder.tvDishloves = (TextView) view.findViewById(R.id.tvDishloves);
			holder.tvDishPrice = (TextView) view.findViewById(R.id.tvDishPrice);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final BranchDishIdDto branchDishIdDto = feed.getBranchDishId();
		if (branchDishIdDto != null) {

			DishIdDto dishTdDto = branchDishIdDto.getDishId();
			if (dishTdDto != null) {
				String dishname = dishTdDto.getName();

				if (dishname != null && dishname.length() > 0) {
					holder.tvDishName.setText(dishname.toString().trim());
				} else {
					holder.tvDishName.setText(null);
				}

				DishPicDto dishPicDto = dishTdDto.getDishPic();
				if (dishPicDto != null) {
					String url = dishPicDto.getUrl();
					Picasso.with(context).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.dishImage);

				} else {
					Picasso.with(context).load(R.drawable.temp)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.dishImage);

				}

			}

			// String oneTag = branchDishIdDto.getOneTag();
			// if (oneTag != null && oneTag.length() > 0) {
			// holder.tvDishloves.setText(oneTag + " people lovedit");
			// } else {
			// holder.tvDishloves.setText("0 people lovedit");
			// }

			int oneTag = branchDishIdDto.getOneTag();
			if (oneTag != 0) {
				holder.tvDishloves.setText(oneTag + " people lovedit");
			} else {
				holder.tvDishloves.setText("0 people lovedit");
			}

			if (branchDishIdDto.getRegular() != 0) {
				holder.tvDishPrice
						.setText("Rs " + branchDishIdDto.getRegular());
			} else {
				holder.tvDishPrice.setText("NA");
			}

		}

		holder.dishImage.setTag(feed);
		holder.btnAddTo.setTag(feed);

		holder.dishImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.btnAddTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final FeedDetailDto feedDetailDto = (FeedDetailDto) arg0
						.getTag();

				final PopupMenu popup = new PopupMenu(context, arg0);

				popup.getMenuInflater().inflate(R.menu.popularhere_item_menu,
						popup.getMenu());
				final String loginUserId = Utils
						.getFromSharedPreference(Constants.USER_OBJECT_ID_KEY);

				if (loginUserId != null && loginUserId.length() > 0) {

					try {
						wishlists = (List<String>) ObjectSerializer.deserialize(sharedPreferences
								.getString(
										Constants.WISHLIST_ARRAY,
										ObjectSerializer
												.serialize(new ArrayList<String>())));

						if (wishlists != null && wishlists.size() > 0) {
							BranchDishIdDto branchDishIdDto = feedDetailDto
									.getBranchDishId();
							if (branchDishIdDto != null) {

								String branchDishObjectId = branchDishIdDto
										.getObjectId();
								if (branchDishObjectId != null
										&& branchDishObjectId.length() > 0) {
									if (wishlists.contains(branchDishObjectId)) {
										popup.getMenu().getItem(1)
												.setTitle("Wished");
									} else {
										popup.getMenu().getItem(1)
												.setTitle("Wish it");
									}

								}

							}

						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						BranchDishIdDto branchDishId = feedDetailDto
								.getBranchDishId();

						if (arg0.getTitle().equals("Ate it")) {
							if (loginUserId != null && loginUserId.length() > 0) {

								Intent ateItIntent = new Intent(context,
										AteIt_activity.class);
								ateItIntent.putExtra(Constants.DATA,
										feedDetailDto);
								ateItIntent.putExtra(Constants.SVAAD, Constants.RESTAURANT_MENU);
								context.startActivity(ateItIntent);
							} else {
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.RESTAURANT_MENU,
										Constants.ATE_IT);
							}
						}

						else {

							if (loginUserId != null && loginUserId.length() > 0) {

								if (arg0.getTitle().equals("Wish it")) {
									if (feedDetailDto != null) {

										if (branchDishId != null) {
											String objectid = branchDishId
													.getObjectId();

											if (objectid != null
													&& objectid.length() > 0) {

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
															// holder.btnAddToList.setText("Wanted");
															popup.getMenu()
																	.getItem(1)
																	.setTitle(
																			"Wished");

															new LikeAsyncTask(
																	context,
																	null,
																	feedDetailDto,Constants.RESTAURANT_MENU)
																	.execute();
														} catch (Exception e) {
															e.printStackTrace();
														}

													}

													else {
														likesList.add(objectid);

														if (likesList != null
																&& likesList
																		.size() > 0) {

															try {
																// holder.btnAddToList.setText("Wanted");
																popup.getMenu()
																		.getItem(
																				1)
																		.setTitle(
																				"Wished");
																Utils.saveToSharedPreferenceList(
																		Constants.WISHLIST_ARRAY,
																		likesList);

																new LikeAsyncTask(
																		context,
																		null,
																		feedDetailDto,Constants.RESTAURANT_MENU)
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
										wishlists = (List<String>) ObjectSerializer.deserialize(sharedPreferences
												.getString(
														Constants.WISHLIST_ARRAY,
														ObjectSerializer
																.serialize(new ArrayList<String>())));
										if (wishlists != null
												&& wishlists.size() > 0) {

											if (branchDishId != null) {

												String branchDishObjectId = branchDishId
														.getObjectId();
												if (branchDishObjectId != null
														&& branchDishObjectId
																.length() > 0) {
													for (int i = 0; i < wishlists
															.size(); i++) {
														String id = wishlists
																.get(i);
														if (id != null
																&& id.length() > 0) {
															if (id.equals(branchDishObjectId)) {
																wishlists
																		.remove(i);
																// holder.btnAddToList.setText("Want it");
																popup.getMenu()
																		.getItem(
																				1)
																		.setTitle(
																				"Wish it");
																Utils.saveToSharedPreferenceList(
																		Constants.WISHLIST_ARRAY,
																		wishlists);

																new UnlikeAsyncTask(
																		context,
																		null,
																		branchDishObjectId,Constants.RESTAURANT_MENU,feedDetailDto)
																		.execute();

															} else {
																// holder.btnAddToList.setText("Wanted");
																popup.getMenu()
																		.getItem(
																				1)
																		.setTitle(
																				"Wished");
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
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.RESTAURANT_MENU,
										Constants.WISH_IT);
							}
						}

						return false;
					}
				});

				popup.show();

			}
		});

		view.setTag(holder);

		return view;
	}

	public class ViewHolder {
		private ImageView dishImage;
		private TextView tvDishName, tvDishPrice, tvDishloves;
		private ImageButton btnAddTo;
	}

}
