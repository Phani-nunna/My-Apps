package com.svaad.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.SvaadApplication;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.DishIdDto;
import com.svaad.Dto.DishPhotoDto;
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

public class PhotosGridAdapter extends ArrayAdapter<FeedDetailDto> {

	Context context;
	List<FeedDetailDto> categoriesList;
	private int listViewId;
	int getr = 0;
	int count = 0;
	String[] rcolors = { "#D3C120", "#F26348", "#712B82", "#0099CC", "#2D8631",
			"#E67E22", "#F39C12", "#8E44AD" };
	FeedDetailDto feed;
	List<String> wishlists = null;
	List<String> likesList = new ArrayList<String>();
	SharedPreferences sharedPreferences = PreferenceManager
			.getDefaultSharedPreferences(SvaadApplication.getInstance()
					.getApplicationContext());

	public PhotosGridAdapter(Context context, int listViewId,
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
			view = inflater.inflate(R.layout.photos_griditem, group,
					false);

			holder = new ViewHolder();
			holder.withImage = (RelativeLayout) view
					.findViewById(R.id.withImage);
			holder.withoutImage = (RelativeLayout) view
					.findViewById(R.id.withoutImage);
			holder.dishImage = (ImageView) view.findViewById(R.id.wimageView);
			holder.btnAddTo = (ImageView) view.findViewById(R.id.wimageButton);
			holder.tvDishName = (TextView) view.findViewById(R.id.wtvDishName);
			holder.tvDishloves = (TextView) view
					.findViewById(R.id.wtvDishloves);
			holder.tvbranchName = (TextView) view
					.findViewById(R.id.wtvbranchName);

			holder.ndishImage = (ImageView) view.findViewById(R.id.woImageView);
			holder.nbtnAddTo = (ImageView) view
					.findViewById(R.id.woimageButton);
			holder.ntvDishName = (TextView) view
					.findViewById(R.id.wotvDishName);
			holder.ntvDishloves = (TextView) view
					.findViewById(R.id.wotvDishloves);
			holder.ntvbranchName = (TextView) view
					.findViewById(R.id.wotvbranchName);

		} else {
			holder = (ViewHolder) view.getTag();
		}


		if (getr < 7)
			getr++;
		else
			getr = 0;
		final BranchDishIdDto branchDishIdDto = feed.getBranchDishId();
		if (branchDishIdDto != null) {

			DishIdDto dishTdDto = branchDishIdDto.getDishId();
			if (dishTdDto != null) {
				String dishname = dishTdDto.getName();
				if (dishname != null && dishname.length() > 0) {
					holder.tvDishName.setText(dishname.toString().trim());
					holder.ntvDishName.setText(dishname.toString().trim());
				} else {
					holder.tvDishName.setText(null);
					holder.ntvDishName.setText(null);
				}

				DishPicDto dishPicDto = dishTdDto.getDishPicThumbnail();
				
				DishPhotoDto dishPhotoDto=feed.getDishPhoto();
				
				DishPhotoDto dishPhotoThumbnailDto=feed.getDishPhotoThumbnail();
				
				if(dishPhotoThumbnailDto!=null)
				{
					String url = dishPhotoThumbnailDto.getUrl();
					holder.withImage.setVisibility(View.VISIBLE);
					holder.withoutImage.setVisibility(View.GONE);
					Picasso.with(context).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(holder.dishImage);
				}
				
				
				else if(dishPhotoDto!=null)
				{
					String url = dishPhotoDto.getUrl();
					holder.withImage.setVisibility(View.VISIBLE);
					holder.withoutImage.setVisibility(View.GONE);
					Picasso.with(context).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(holder.dishImage);
				}
				
				
				
				else if (dishPicDto != null) {
					String url = dishPicDto.getUrl();
					holder.withImage.setVisibility(View.VISIBLE);
					holder.withoutImage.setVisibility(View.GONE);
					Picasso.with(context).load(url)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp).into(holder.dishImage);

				}
				
				else {
					// Picasso.with(context).load(R.drawable.temp)
					// .placeholder(R.drawable.temp)
					// .error(R.drawable.temp)
					// .into(holder.dishImage);
					holder.withImage.setVisibility(View.GONE);
					holder.withoutImage.setVisibility(View.VISIBLE);
					String col = rcolors[getr];
					// Toast.makeText(context, col+"",Toast.LENGTH_LONG).show();
					holder.withoutImage.setBackgroundColor(Color.parseColor(col
							+ ""));

				}

			}

			// String oneTag = branchDishIdDto.getOneTag();

			int oneTag = branchDishIdDto.getOneTag();
			if (oneTag != 0) {
				holder.tvDishloves.setText(oneTag + " people lovedit");
				holder.ntvDishloves.setText(oneTag + " people lovedit");
			} else {
				holder.tvDishloves.setText("0 people lovedit");
				holder.ntvDishloves.setText("0 people lovedit");
			}

			if (branchDishIdDto.getBranchName() != null) {
				holder.tvbranchName.setText(branchDishIdDto.getBranchName());
				holder.ntvbranchName.setText(branchDishIdDto.getBranchName());
			} else {
				holder.tvbranchName.setText(null);
				holder.ntvbranchName.setText(null);

			}

		}

		holder.dishImage.setTag(feed);
		holder.btnAddTo.setTag(feed);
		holder.tvDishName.setTag(feed);
		holder.tvDishloves.setTag(feed);
		holder.tvbranchName.setTag(feed);
		holder.ndishImage.setTag(feed);
		holder.nbtnAddTo.setTag(feed);
		holder.ntvDishName.setTag(feed);
		holder.ntvDishloves.setTag(feed);
		holder.ntvbranchName.setTag(feed);

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

		holder.tvDishName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.tvDishloves.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.tvbranchName.setOnClickListener(new OnClickListener() {

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
								ateItIntent.putExtra(Constants.SVAAD,
										Constants.SEARCH);
								context.startActivity(ateItIntent);
							} else {
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.SEARCH, Constants.ATE_IT);
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
																	feedDetailDto,
																	Constants.SEARCH)
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
																		feedDetailDto,
																		Constants.SEARCH)
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
																		branchDishObjectId,
																		Constants.SEARCH,
																		feedDetailDto)
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

							}

							else {
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.SEARCH, Constants.WISH_IT);
							}
						}

						return false;
					}
				});

				popup.show();

			}
		});

		holder.ndishImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.ntvDishName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.ntvDishloves.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.ntvbranchName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedDetailDto feedDetailDto = (FeedDetailDto) v.getTag();

				Intent dishProfileIntent = new Intent(context,
						DishProfileActivity.class);
				dishProfileIntent.putExtra(Constants.DATA, feedDetailDto);

				context.startActivity(dishProfileIntent);
			}
		});

		holder.nbtnAddTo.setOnClickListener(new OnClickListener() {

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
								ateItIntent.putExtra(Constants.SVAAD,
										Constants.SEARCH);
								context.startActivity(ateItIntent);
							} else {
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.SEARCH, Constants.ATE_IT);
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
																	feedDetailDto,
																	Constants.SEARCH)
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
																		feedDetailDto,
																		Constants.SEARCH)
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
																		branchDishObjectId,
																		Constants.SEARCH,
																		feedDetailDto)
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

							}

							else {
								// new SvaadDialogs().showToast(context,
								// "Please login");
								new SvaadDialogs().showSvaadLoginAlertDialog(
										context, null, Constants.RES_MODE,
										Constants.SEARCH, Constants.WISH_IT);
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
		private ImageView dishImage, ndishImage;
		private TextView tvDishName, tvbranchName, tvDishloves, ntvDishName,
				ntvbranchName, ntvDishloves;
		private ImageView btnAddTo, nbtnAddTo;
		private RelativeLayout withImage, withoutImage;
	}

}
