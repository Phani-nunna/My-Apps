package com.esainc.lib.backend;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.esainc.lib.R;
import com.esainc.lib.uc.Logger;

public class DataLoader extends IntentService {
	private static final String LOG_TAG = "Data Loader";
	
	public enum DataType {
		Headlines, HeadlinesSearch, HeadlinesArticle, SportHeadlines, SportHeadlinesSearch, ScoreboardRecent, ScoreboardToday, ScoreboardUpcoming, Schedule, Teams, Staff, StaffBio, Roster, Players, PlayersBio, Coaches, CoachesBio, CoachesBioSport, Links, SportLinks, Galleries, GalleryPhotos, Youtube, Custom, SportInfo, CheckCoach,MediaSchedule;
	}
	
	public DataLoader() {
		super("Data Loader");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		DataType type = DataType.values()[intent.getIntExtra("type", 0)];
		int tabPos = intent.getIntExtra("tabPos", 0);
		boolean team = intent.getBooleanExtra("team", false);
		String[] params = intent.getStringArrayExtra("params");
		String version = "a-0.0.0";
		try {
			version = "a-" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Logger.e(LOG_TAG, "Error getting version name", e);
		}
		String api = getResources().getString(R.string.api);
		String url = getResources().getString(R.string.SiteURL);
		String sportID = null;
		String customTag = null;
		TypedArray tabs;
		String tabInfo[];
		boolean useAPI = true;
		
		switch (type) {
			case Headlines:
				Logger.i(LOG_TAG, "Fetching Headlines");
				url += getResources().getString(R.string.api_news_articles, api, version);
				break;
			case HeadlinesSearch:
				Logger.i(LOG_TAG, "Fetching Headlines Search");
				url += getResources().getString(R.string.api_search_news_criteria, api, version, params[0]);
				break;
			case HeadlinesArticle:
				Logger.i(LOG_TAG, "Fetching Headlines Article");
				url += getResources().getString(R.string.api_news_articles_articleID, api, version, params[0]);
				break;
			case SportHeadlines:
				Logger.i(LOG_TAG, "Fetching Sport Headlines");
				url += getResources().getString(R.string.api_news_articles_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case SportHeadlinesSearch:
				Logger.i(LOG_TAG, "Fetching Sport Headlines Search");
				url += getResources().getString(R.string.api_search_news_sportID_criteria, api, version, params[0], params[1]);
				sportID = params[0];
				break;
			case ScoreboardRecent:
				Logger.i(LOG_TAG, "Fetching Scoreboard Recent");
				url += getResources().getString(R.string.api_schedule_events_recent, api, version);
				break;
			case ScoreboardToday:
				Logger.i(LOG_TAG, "Fetching Scoreboard Today");
				url += getResources().getString(R.string.api_schedule_events_today, api, version);
				break;
			case ScoreboardUpcoming:
				Logger.i(LOG_TAG, "Fetching Scoreboard Upcoming");
				url += getResources().getString(R.string.api_schedule_events_upcoming, api, version);
				break;
			case Schedule:
				Logger.i(LOG_TAG, "Fetching Schedule");
				url += getResources().getString(R.string.api_schedule_events_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case MediaSchedule:
				Logger.i(LOG_TAG, "Fetching MediaSchedule");
				url += getResources().getString(R.string.api_media_schedule, api, version);
				break;
			case Teams:
				Logger.i(LOG_TAG, "Fetching Teams");
				url += getResources().getString(R.string.api_sports_teams, api, version);
				break;
			case Staff:
				Logger.i(LOG_TAG, "Fetching Staff");
				url += getResources().getString(R.string.api_staff_members, api, version);
				break;
			case StaffBio:
				Logger.i(LOG_TAG, "Fetching Staff Bio");
				url += getResources().getString(R.string.api_staff_members_staffID, api, version, params[0]);
				break;
			case Roster:
				Logger.i(LOG_TAG, "Fetching Roster");
				url += getResources().getString(R.string.api_roster_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case CheckCoach:
				Logger.i(LOG_TAG, "Fetching Coach");
				url += getResources().getString(R.string.api_checkcoach_rosterID, api, version, params[0]);
			case Players:
				Logger.i(LOG_TAG, "Fetching Players");
				url += getResources().getString(R.string.api_roster_players_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case PlayersBio:
				Logger.i(LOG_TAG, "Fetching Player Bio");
				url += getResources().getString(R.string.api_roster_players_rosterID, api, version, params[0]);
				break;
			case Coaches:
				Logger.i(LOG_TAG, "Fetching Coaches");
				url += getResources().getString(R.string.api_roster_coach_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case CoachesBio:
				Logger.i(LOG_TAG, "Fetching Coach Bio");
				url += getResources().getString(R.string.api_roster_coaches_rosterID, api, version, params[0]);
				break;
			case CoachesBioSport:
				Logger.i(LOG_TAG, "Fetching Coach Bios, sport #" + params[0]);
				url += getResources().getString(R.string.api_roster_coaches_bios_sportID, api, version, params[0]);
				break;
			case Links:
				Logger.i(LOG_TAG, "Fetching Links");
				url += getResources().getString(R.string.api_misc_listLinks, api, version);
				break;
			case SportLinks:
				Logger.i(LOG_TAG, "Fetching Sport Links");
				url += getResources().getString(R.string.api_misc_listlinks_sportID, api, version, params[0]);
				sportID = params[0];
				break;
			case Galleries:
				Logger.i(LOG_TAG, "Fetching Galleries");
				url += getResources().getString(R.string.api_photos_galleries, api, version);
				break;
			case GalleryPhotos:
				Logger.i(LOG_TAG, "Fetching Gallery Photos");
				url += getResources().getString(R.string.api_photos_galleries_galleriesID, api, version, params[0]);
				break;
			case SportInfo:
				Logger.i(LOG_TAG, "Fetching Sport Info");
				url += getResources().getString(R.string.api_sport_info, api, version, params[0]);
				break;
			
			case Youtube:
				Logger.i(LOG_TAG, "Fetching Youtube Playlist");
				if (!team) {
					tabs = getResources().obtainTypedArray(R.array.TabBars);
				} else {
					tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
				}
				tabInfo = getResources().getStringArray(tabs.getResourceId(tabPos, 0));
				url = "http://gdata.youtube.com/feeds/api/playlists/"+tabInfo[6]+"?v=2&alt=jsonc&max-results=50";	// Playlist ID
				tabs.recycle();
				break;
			default:
				Logger.i(LOG_TAG, "Fetching Custom Tab");
				url += getResources().getString(R.string.api_default, api, version);
				if (!team) {
					tabs = getResources().obtainTypedArray(R.array.TabBars);
				} else {
					tabs = getResources().obtainTypedArray(R.array.TeamWiseTabBars);
				}
				tabInfo = getResources().getStringArray(tabs.getResourceId(tabPos, 0));
				String customURL = tabInfo[6];	// API URL
				if (customURL.startsWith("/")) {
					url += customURL;
				} else {
					useAPI = false;
					url = customURL;
				}
				customTag = tabInfo[0];
				tabs.recycle();
				break;
		}
		
		JSONObject jsonResponse = new JSONObject();
		String data = new String();
		JSONObject extra = new JSONObject();
		String tag = null;
		String record = null;
		int code = 200;
		if (useAPI) {
			APICaller caller = new APICaller();
			String response = caller.loadDataFromUrl(url);
		
			if (response.startsWith("Error:")) {
				// handle failure to load
				data = response;
			} else {
				// convert to JSONObject
				try {
					jsonResponse = new JSONObject(response);
					code = jsonResponse.optInt("code", 200);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error parsing data", e);
				}
				
				try {
					if (jsonResponse.optJSONObject(getResources().getString(R.string.tag_data)) == null) {
						String msg = jsonResponse.getString(getResources().getString(R.string.tag_errors));
						if (msg.indexOf("|") != -1) {
							data = "Error:newVersion: " + msg;
						} else {
							data = "Error:unsupported: " + msg;
						}
					} else {
						switch (type) {
							case Headlines:
								if (tag == null) tag = "Headlines Fetched";
							case HeadlinesSearch:
								if (tag == null) tag = "Headlines Search Fetched";
							case SportHeadlines:
								if (tag == null) tag = "Sport Headlines Fetched";
							case SportHeadlinesSearch:
								if (tag == null) tag = "Sport Headlines Search Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_articles)).toString();
								break;
							case HeadlinesArticle:
								if (tag == null) tag = "Headlines Article Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_articles)).toString();
								break;
							case ScoreboardRecent:
								if (tag == null) tag = "Scoreboard Recent Fetched";
							case ScoreboardToday:
								if (tag == null) tag = "Scoreboard Today Fetched";
							case ScoreboardUpcoming:
								if (tag == null) tag = "Scoreboard Upcoming Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_events)).toString();
								extra = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONObject(getResources().getString(R.string.tag_keys));
								break;
							case MediaSchedule:
								if (tag == null) tag = "MediaSchedule Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_events)).toString();
								extra = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONObject(getResources().getString(R.string.tag_keys));
								break;
							case Schedule:
								if (tag == null) tag = "Schedule Fetched";
								if (code != 419) {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_events)).toString();
								} else {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_msg));
								}
								extra = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONObject(getResources().getString(R.string.tag_keys));
								record = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_record));
								break;
							
							case Teams:
								if (tag == null) tag = "Teams Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_sports)).toString();
								break;
							case Staff:
								if (tag == null) tag = "Staff Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_staff)).toString();
								break;
							case StaffBio:
								if (tag == null) tag = "Staff Bio Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_staff)).toString();
								break;
							case Roster:
								if (tag == null) tag = "Roster Fetched";
								break;
							case CheckCoach:
								if (tag == null) tag = "Coach Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_coaches)).toString();
								break;
							case Players:
								if (tag == null) tag = "Players Fetched";
								if (code != 419) {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_players)).toString();
								} else {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_msg));
								}
								break;
							case Coaches:
								if (tag == null) tag = "Coaches Fetched";
								if (code != 419) {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_coaches)).toString();
								} else {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_msg));
								}
								break;
							case PlayersBio:
								if (tag == null) tag = "Players Bio Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_players)).toString();
								break;
							case CoachesBio:
								if (tag == null) tag = "Coaches Bio Fetched";
							case CoachesBioSport:
								if (tag == null) tag = "Coaches Bio for Sport Fetched";
								if (code != 419) {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_coaches)).toString();
								} else {
									data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_msg));
								}
								break;
							case Links:
								if (tag == null) tag = "Links Fetched";
							case SportLinks:
								if (tag == null) tag = "Sport Links Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_links)).toString();
								break;
							case Galleries:
								if (tag == null) tag = "Galleries Fetched";
							case GalleryPhotos:
								if (tag == null) tag = "Gallery Photos Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_galleries)).toString();
								break;
							case SportInfo:
								if (tag == null) tag = "Sport Info Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_sports)).toString();
								break;
							case Youtube:
								if (tag == null) tag = "Youtube Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getJSONArray(getResources().getString(R.string.tag_items)).toString();
								break;
							default: // Custom Tab
								if (tag == null) tag = "Custom Fetched";
								data = jsonResponse.getJSONObject(getResources().getString(R.string.tag_data)).getString(getResources().getString(R.string.tag_page)).toString();
								break;
						}
						if (tag != null) Logger.i(LOG_TAG, tag);
					}
				} catch (NotFoundException e) {
					Logger.e(LOG_TAG, "Error parsing data", e);
				} catch (JSONException e) {
					Logger.e(LOG_TAG, "Error parsing data", e);
				}
			}
		} else {
			data = url;
		}
		
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Bundle bundle = new Bundle();
			bundle.putInt("result", Activity.RESULT_OK);
			bundle.putInt("tabPos", tabPos);
			bundle.putInt("type", type.ordinal());
			bundle.putString("data", data);
			bundle.putString("extra", extra.toString());
			bundle.putString("record", record);
			bundle.putString("sportID", sportID);
			bundle.putString("customTag", customTag);
			bundle.putInt("code", code);
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.setData(bundle);
			try {
				messenger.send(msg);
			} catch (RemoteException e) {
				Logger.w(LOG_TAG, "Exception sending message", e);
			}
		}
	}
}
