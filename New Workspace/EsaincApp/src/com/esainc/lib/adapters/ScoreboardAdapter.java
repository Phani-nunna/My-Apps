package com.esainc.lib.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esainc.lib.R;
import com.esainc.lib.SIDHelpApplication;
import com.esainc.lib.backend.DataLoader;
import com.esainc.lib.backend.DataLoader.DataType;
import com.esainc.lib.uc.Logger;
import com.esainc.lib.uc.WebViewListener;
import com.esainc.lib.ui.BrowserActivity;
import com.esainc.lib.ui.ScoreboardFragment;
import com.esainc.lib.ui.TeamsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScoreboardAdapter extends BaseAdapter {
	public enum Scoreboards {
		Recent, Today, Upcoming
	};
	
	private static final String LOG_TAG = "Scoreboard Adapter";
	private Context mContext;
	private JSONArray mRecent;
	private JSONArray mToday;
	private JSONArray mUpcoming;
	private Scoreboards mCurrentScoreboard;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private static boolean mTeams;
	
	public ScoreboardAdapter(Context context, JSONArray recent, JSONArray today, JSONArray upcoming, Scoreboards current, boolean teams) {
		mContext = context;
		mRecent = recent;
		mToday = today;
		mUpcoming = upcoming;
		mCurrentScoreboard = current;
		mTeams = teams;
	}
	
	@Override
	public int getCount() {
		switch (mCurrentScoreboard) {
			case Recent:
				return mRecent.length();
			case Today:
				return mToday.length();
			case Upcoming:
				return mUpcoming.length();
		}
		return 0;
	}
	
	@Override
	public Object getItem(int pos) {
		try {
			switch (mCurrentScoreboard) {
				case Recent:
					return mRecent.get(pos);
				case Today:
					return mToday.get(pos);
				case Upcoming:
					return mUpcoming.get(pos);
			}
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item", e);
		}
		return null;
	}
	
	@Override
	public long getItemId(int pos) {
		try {
			switch (mCurrentScoreboard) {
				case Recent:
					return Long.valueOf(mRecent.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_scheduleID))).longValue();
				case Today:
					return Long.valueOf(mToday.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_scheduleID))).longValue();
				case Upcoming:
					return Long.valueOf(mUpcoming.getJSONObject(pos).getString(mContext.getResources().getString(R.string.tag_scheduleID))).longValue();
			}
		} catch (NumberFormatException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (NotFoundException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting item ID", e);
		}
		return 0;
	}
	
	public void setData(JSONArray data, Scoreboards current) {
		switch (current) {
			case Recent:
				mRecent = data;
				break;
			case Today:
				mToday = data;
				break;
			case Upcoming:
				mUpcoming = data;
				break;
		}
		if (current == mCurrentScoreboard) {
			notifyDataSetChanged();
		}
	}
	
	public Scoreboards getCurrentScoreboard() {
		return mCurrentScoreboard;
	}
	
	public void switchScoreboard(Scoreboards current) {
		mCurrentScoreboard = current;
		notifyDataSetChanged();
	}
	
	public static void fixBackgroundRepeat(View view) {
		Drawable bg = view.getBackground();
		if(bg != null) {
	        if(bg instanceof BitmapDrawable) {
	             BitmapDrawable bmp = (BitmapDrawable) bg;
	             bmp.mutate(); // make sure that we aren't sharing state anymore
	             bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	        }
	   }
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "InlinedApi", "NewApi" })
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			view = new View(mContext);
			view = inflater.inflate(R.layout.activity_scoreboard_row, null);
		} else {
			view = convertView;
		}
		
		ImageView imgTeamLogo = (ImageView) view.findViewById(R.id.imgTeamLogo);
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		TextView txtVsTitle = (TextView) view.findViewById(R.id.txtVsTitle);
		TextView txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);
		TextView txtPlace = (TextView) view.findViewById(R.id.txtPlace);
		ImageView imgStats = (ImageView) view.findViewById(R.id.imgStats);
		ImageView imgRecap = (ImageView) view.findViewById(R.id.imgRecap);
		ImageView imgVideo = (ImageView) view.findViewById(R.id.imgVideo);
		ImageView imgAudio = (ImageView) view.findViewById(R.id.imgAudio);
		ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
		ImageView imgCalendar = (ImageView) view.findViewById(R.id.imgCalendar);
		TextView txtMatchPoints = (TextView) view.findViewById(R.id.txtMatchPoints);
		TextView txtFinalResult = (TextView) view.findViewById(R.id.txtFinalResult);
		WebView wvNote = (WebView) view.findViewById(R.id.webViewNote);
		
		JSONObject hdata = new JSONObject();
		try {
			switch (mCurrentScoreboard) {
				case Recent:
					hdata = mRecent.getJSONObject(pos);
					break;
				case Today:
					hdata = mToday.getJSONObject(pos);
					break;
				case Upcoming:
					hdata = mUpcoming.getJSONObject(pos);
					break;
			}
			txtTitle.setText(hdata.getString(mContext.getResources().getString(R.string.tag_sportName)) + hdata.getString(mContext.getResources().getString(R.string.CSE)));
			txtMatchPoints.setText(hdata.getString(mContext.getResources().getString(R.string.tag_result)));
			txtFinalResult.setText(hdata.getString(mContext.getResources().getString(R.string.tag_WLT)));
			
			// Home, Away, or Neutral
			String HAN = hdata.getString(mContext.getResources().getString(R.string.tag_HAN));
			if (HAN.equalsIgnoreCase("away")) {
				view.setBackgroundResource(R.drawable.sb_key_away);
				fixBackgroundRepeat(view);
				txtVsTitle.setText("at " + hdata.getString(mContext.getResources().getString(R.string.tag_opponent)));
			} else if (HAN.equalsIgnoreCase("home") || HAN.equalsIgnoreCase("neutral")) {
				if (HAN.equalsIgnoreCase("home")) {
					view.setBackgroundResource(R.drawable.sb_key_home);
					fixBackgroundRepeat(view);
				} else {
					view.setBackgroundResource(R.drawable.sb_key_neutral);
					fixBackgroundRepeat(view);
				}
				if (hdata.getString(mContext.getResources().getString(R.string.tag_xc)).equals("1") && hdata.getString(mContext.getResources().getString(R.string.tag_duel)).equals("0")) {
					txtVsTitle.setText("at " + hdata.getString(mContext.getResources().getString(R.string.tag_opponent)));
				} else {
					txtVsTitle.setText("vs. " + hdata.getString(mContext.getResources().getString(R.string.tag_opponent)));
				}
			}
			
			// Date/Time
			String icalDate = hdata.getString(mContext.getResources().getString(R.string.tag_icalDate));
			int timeZoneStart = icalDate.indexOf("TZID=");
			int timeZoneEnd = icalDate.indexOf(":", timeZoneStart);
			String schoolTimeZoneText = "";
			if (timeZoneStart != -1 && timeZoneEnd != 1) {
				schoolTimeZoneText = icalDate.substring((timeZoneStart + 5), timeZoneEnd);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.US);
			dateFormat.setTimeZone(TimeZone.getTimeZone(schoolTimeZoneText));
			String dateTime = "";
			try {
				if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_time)))) {
					dateTime = new SimpleDateFormat("MMM. d, yyyy", Locale.US).format(new Date(dateFormat.parse(icalDate.substring(icalDate.length() - 15)).getTime()));
				} else {
					dateTime = new SimpleDateFormat("MMM. d, yyyy 'at' h:mm a z", Locale.US).format(new Date(dateFormat.parse(icalDate.substring(icalDate.length() - 15)).getTime()));
				}
			} catch (ParseException e) {
				Logger.e(LOG_TAG, "Error parsing date/time", e);
			}
			if (dateTime.startsWith("May.")) {
				dateTime = dateTime.replace("May.", "May");
			}
			txtDateTime.setText(dateTime);
			
			// Location
			txtPlace.setText("");
			if (!TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_location)).trim())) {
				txtPlace.setText(hdata.getString(mContext.getResources().getString(R.string.tag_location)));
			} else {
				txtPlace.setVisibility(View.GONE);
			}
			// Note
			wvNote.loadUrl("about:blank");
			if (!TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.note)).trim())) {
				wvNote.setHorizontalScrollBarEnabled(false);
				wvNote.setVerticalScrollBarEnabled(false);
				wvNote.setVisibility(View.VISIBLE);
				wvNote.getSettings().setJavaScriptEnabled(true);
				if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB && VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
					wvNote.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				} 
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					wvNote.setBackgroundColor(Color.argb(1, 0, 0, 0));
				} else {
					wvNote.setBackgroundColor(Color.TRANSPARENT);
				}
				wvNote.setWebChromeClient(new WebChromeClient());
				ScoreboardFragment.getInstance();
				wvNote.setWebViewClient(new WebViewListener(ScoreboardFragment.getInstance(), ScoreboardFragment.mPos, mTeams, null));
				
				String data = "<html><head><style type=\"text/css\"> body {font-family: \"helvetica\"; font-size: 14;} a { color:#" + mContext.getResources().getString(R.color.HyperlinksColor).substring(3)
						+ "; text-decoration:none; } </style><meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'></head><body>" + hdata.getString(mContext.getResources().getString(R.string.tag_note)) + "<body></html>";
				wvNote.loadData(data, "text/html", "UTF-8");
				
			} else {
				
				wvNote.setVisibility(View.GONE);
			}
			
			String logoURL = hdata.getString(mContext.getResources().getString(R.string.tag_opponentLogo));
			if (((SIDHelpApplication) mContext.getApplicationContext()).isTablet()) {
				imgTeamLogo.setBackgroundResource(0);
				imgTeamLogo.setImageResource(R.drawable.noimage);
				if (!TextUtils.isEmpty(logoURL)) {
					imageLoader.displayImage(logoURL, imgTeamLogo);
				} else {
					imgTeamLogo.setImageResource(R.drawable.noimage);
				}
			}
			
			if (hdata.getString(mContext.getResources().getString(R.string.tag_played)).equalsIgnoreCase("Not Played")) {
				imgRecap.setVisibility(View.GONE);
				imgPhoto.setVisibility(View.GONE);
				
				imgCalendar.setVisibility(View.VISIBLE);
				imgCalendar.setClickable(true);
				imgCalendar.setEnabled(true);
				HashMap<String, String> tags = new HashMap<String, String>();
				tags.put("title", hdata.getString(mContext.getResources().getString(R.string.tag_sportName)));
				tags.put("description", hdata.getString(mContext.getResources().getString(R.string.tag_note)));
				tags.put("eventLocation", hdata.getString(mContext.getResources().getString(R.string.tag_location)));
				tags.put("icalDate", hdata.getString(mContext.getResources().getString(R.string.tag_icalDate)));
				imgCalendar.setTag(tags);
				imgCalendar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent intent = new Intent(Intent.ACTION_EDIT);
							intent.setType("vnd.android.cursor.item/event");
							
							@SuppressWarnings("unchecked")
							HashMap<String, String> tags = (HashMap<String, String>) v.getTag();
							
							intent.putExtra("title", tags.get("title"));
							intent.putExtra("description", tags.get("description"));
							intent.putExtra("eventLocation", tags.get("eventLocation"));
							
							String icalDate = tags.get("icalDate");
							int timeZoneStart = icalDate.indexOf("TZID=");
							int timeZoneEnd = icalDate.indexOf(":", timeZoneStart);
							String schoolTimeZoneText = "";
							if (timeZoneStart != -1 && timeZoneEnd != 1) {
								schoolTimeZoneText = icalDate.substring((timeZoneStart + 5), timeZoneEnd);
							}
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.US);
							dateFormat.setTimeZone(TimeZone.getTimeZone(schoolTimeZoneText));
							Date date = dateFormat.parse(icalDate.substring(icalDate.length() - 15));
							intent.putExtra("beginTime", date.getTime());
							intent.putExtra("endTime", date.getTime() + (6000 * 1000));
							ScoreboardFragment.getInstance().startActivity(intent);
						} catch (ParseException e) {
							Logger.e(this.getClass().getName(), "", e);
						}
					}
				});
				
				// Video
				if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_teamVideoURL)))) {
					imgVideo.setImageResource(R.drawable.btn_video_disabled);
					imgVideo.setClickable(false);
					imgVideo.setEnabled(false);
					imgVideo.setVisibility(View.VISIBLE);
				} else {
					imgVideo.setImageResource(R.drawable.btn_video);
					imgVideo.setClickable(true);
					imgVideo.setEnabled(true);
					imgVideo.setVisibility(View.VISIBLE);
					imgVideo.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_teamVideoURL)));
				}
				imgVideo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent browserIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), BrowserActivity.class);
						browserIntent.putExtra("pos", ScoreboardFragment.mPos);
						browserIntent.putExtra("teams", mTeams);
						browserIntent.putExtra("linkName", "");
						browserIntent.putExtra("url", v.getTag().toString());
						browserIntent.putExtra("stats", true);
						if (mTeams) {
							browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
							browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
							browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
							browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
						}
						ScoreboardFragment.getInstance().startActivity(browserIntent);
					}
				});
				
				// Audio
				if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_teamAudioURL)))) {
					imgAudio.setImageResource(R.drawable.sb_audio_disabled);
					imgAudio.setClickable(false);
					imgAudio.setEnabled(false);
					imgAudio.setVisibility(View.VISIBLE);
				} else {
					imgAudio.setImageResource(R.drawable.sb_audio);
					imgAudio.setClickable(true);
					imgAudio.setEnabled(true);
					imgAudio.setVisibility(View.VISIBLE);
					imgAudio.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_teamAudioURL)));
				}
				imgAudio.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent browserIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), BrowserActivity.class);
						browserIntent.putExtra("pos", ScoreboardFragment.mPos);
						browserIntent.putExtra("teams", mTeams);
						browserIntent.putExtra("linkName", "");
						browserIntent.putExtra("url", v.getTag().toString());
						browserIntent.putExtra("stats", true);
						if (mTeams) {
							browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
							browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
							browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
							browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
						}
						ScoreboardFragment.getInstance().startActivity(browserIntent);
					}
				});
				
				// Stats
				if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_teamStatsURL)))) {
					imgStats.setImageResource(R.drawable.btn_stats_disabled);
					imgStats.setClickable(false);
					imgStats.setEnabled(false);
					imgStats.setVisibility(View.VISIBLE);
				} else {
					imgStats.setImageResource(R.drawable.btn_stats);
					imgStats.setClickable(true);
					imgStats.setEnabled(true);
					imgStats.setVisibility(View.VISIBLE);
					imgStats.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_teamStatsURL)));
				}
				imgStats.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent browserIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), BrowserActivity.class);
						browserIntent.putExtra("pos", ScoreboardFragment.mPos);
						browserIntent.putExtra("teams", mTeams);
						browserIntent.putExtra("linkName", "");
						browserIntent.putExtra("url", v.getTag().toString());
						browserIntent.putExtra("stats", true);
						if (mTeams) {
							browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
							browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
							browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
							browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
						}
						ScoreboardFragment.getInstance().startActivity(browserIntent);
					}
				});
			} else {
				imgAudio.setVisibility(View.GONE);
				imgVideo.setVisibility(View.GONE);
				
				imgCalendar.setVisibility(View.GONE);
				imgCalendar.setClickable(false);
				imgCalendar.setEnabled(false);
				
				// Recap
				if (hdata.getString(mContext.getResources().getString(R.string.tag_recapID)).equals("0")) {
					imgRecap.setImageResource(R.drawable.btn_recap_disabled);
					imgRecap.setClickable(false);
					imgRecap.setEnabled(false);
					imgRecap.setVisibility(View.VISIBLE);
				} else {
					imgRecap.setImageResource(R.drawable.btn_recap);
					imgRecap.setClickable(true);
					imgRecap.setEnabled(true);
					imgRecap.setVisibility(View.VISIBLE);
					imgRecap.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_recapID)));
				}
				imgRecap.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Logger.i(LOG_TAG, "Loading Article");
						Intent dataIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), DataLoader.class);
						Messenger dataMessenger = new Messenger(ScoreboardFragment.mHandler);
						dataIntent.putExtra("MESSENGER", dataMessenger);
						dataIntent.putExtra("tabPos", ScoreboardFragment.mPos);
						dataIntent.putExtra("type", DataType.HeadlinesArticle.ordinal());
						String params[] = new String[] { v.getTag().toString() };
						dataIntent.putExtra("params", params);
						ScoreboardFragment.getInstance().getActivity().startService(dataIntent);
					}
				});
				
				// Stats
				if (TextUtils.isEmpty(hdata.getString(mContext.getResources().getString(R.string.tag_statsURL)))) {
					imgStats.setImageResource(R.drawable.btn_stats_disabled);
					imgStats.setClickable(false);
					imgStats.setEnabled(false);
					imgStats.setVisibility(View.VISIBLE);
				} else {
					imgStats.setImageResource(R.drawable.btn_stats);
					imgStats.setClickable(true);
					imgStats.setEnabled(true);
					imgStats.setVisibility(View.VISIBLE);
					imgStats.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_statsURL)));
				}
				imgStats.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent browserIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), BrowserActivity.class);
						browserIntent.putExtra("pos", ScoreboardFragment.mPos);
						browserIntent.putExtra("teams", mTeams);
						browserIntent.putExtra("linkName", "");
						browserIntent.putExtra("url", v.getTag().toString());
						browserIntent.putExtra("stats", true);
						if (mTeams) {
							browserIntent.putExtra("showLinks", TeamsActivity.showLinks);
							browserIntent.putExtra("showSchedule", TeamsActivity.showSchedule);
							browserIntent.putExtra("showRoster", TeamsActivity.showRoster);
							browserIntent.putExtra("showCoach", TeamsActivity.showCoach);
						}
						ScoreboardFragment.getInstance().startActivity(browserIntent);
					}
				});
				
				// Gallery
				if (hdata.getString(mContext.getResources().getString(R.string.tag_galleryID)).equals("0")) {
					imgPhoto.setImageResource(R.drawable.btn_photos_disabled);
					imgPhoto.setClickable(false);
					imgPhoto.setEnabled(false);
					imgPhoto.setVisibility(View.VISIBLE);
				} else {
					imgPhoto.setImageResource(R.drawable.btn_photos);
					imgPhoto.setClickable(true);
					imgPhoto.setEnabled(true);
					imgPhoto.setVisibility(View.VISIBLE);
					imgPhoto.setTag(hdata.getString(mContext.getResources().getString(R.string.tag_galleryID)));
				}
				imgPhoto.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Logger.i(LOG_TAG, "Loading Gallery");
						Intent dataIntent = new Intent(ScoreboardFragment.getInstance().getActivity(), DataLoader.class);
						Messenger dataMessenger = new Messenger(ScoreboardFragment.mHandler);
						dataIntent.putExtra("MESSENGER", dataMessenger);
						dataIntent.putExtra("tabPos", ScoreboardFragment.mPos);
						dataIntent.putExtra("type", DataType.GalleryPhotos.ordinal());
						String params[] = new String[] { v.getTag().toString() };
						dataIntent.putExtra("params", params);
						ScoreboardFragment.getInstance().getActivity().startService(dataIntent);
					}
				});
			}
		} catch (JSONException e) {
			Logger.e(LOG_TAG, "Error getting data", e);
		}
		return view;
	}
	
}

