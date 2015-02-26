package com.svaad.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.activity.HomeActivity;
import com.svaad.asynctask.LogInAsyncTask;
import com.svaad.fragment.FeedbeferLoginFragment;
import com.svaad.fragment.Search_Fragment;
import com.svaad.fragment.Svaad_Fragment_Spinner;
import com.svaad.interfaces.SvaadNetworkCallBack;
import com.svaad.requestDto.LogInRequestDto;

public class SvaadDialogs {

	// GoogleAnalyticsTracker tracker;

	public void showNoNetworkDialog(final Context context, String message,
			final SvaadNetworkCallBack callback) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle(context.getResources().getString(
				R.string.check_internet));
		// builder.setMessage(message);
		builder.setInverseBackgroundForced(true);

		builder.setPositiveButton("Retry",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
						callback.onRetryNetwork();
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
						((Activity) context).finish();

					}
				});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

	public void showPhotoDialog(final Context context, final String mediaPath) {

		final Dialog gpsDialog = new Dialog(context);
		// gpsDialog.getWindow().setTitle("Click a Photo or Choose from Gallery");
		gpsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		gpsDialog.setContentView(R.layout.photo_dilaog_layout);
		gpsDialog.setCanceledOnTouchOutside(false);
		gpsDialog.setCancelable(true);
		Button btnGaleery = (Button) gpsDialog.findViewById(R.id.btnGallery);
		Button btnCam = (Button) gpsDialog.findViewById(R.id.btnCamera);
		btnGaleery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				MediaHandeler.launchImageGallery(context, mediaPath);

			}
		});

		btnCam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				MediaHandeler.captureImg(context, mediaPath);
			}
		});

		gpsDialog.show();
	}
	
	
	

	public void showLogoutDilog(final Context context) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle("Log out");
		builder.setMessage("Do you want to exit ?");
		builder.setInverseBackgroundForced(true);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Utils.saveToSharedPreference(Constants.USER_OBJECT_ID_KEY,
							null);
					Utils.saveToSharedPreference(Constants.SESSION_TOCKEN_KEY,
							null);
					Utils.saveToSharedPreferenceList(Constants.WISHLIST_ARRAY,
							null);
					Utils.saveToSharedPreferenceList(Constants.FOLLOWING_USERS,
							null);
					Utils.saveToSharedPreference(Constants.SVAADLOGIN_RESPONSE,
							null);

					Utils.saveToSharedPreference(Constants.USER_NAME_MIXPANEL,
							null);

					Session session = Session.getActiveSession();
					if (session != null && !session.isClosed()) {
						session.closeAndClearTokenInformation();
						// clear your preferences if saved
					}

					Intent intent = new Intent(context, HomeActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					((Activity) context).finish();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

	public void showSvaadLoginAlertDialog(final Context context,
			final Fragment fragment, final int mode,
			final String mixpanelscreen, final String action) {
		final Dialog loginDialog = new Dialog(context);
		loginDialog.getWindow().setTitle("Login or Join to continue");
		loginDialog.setContentView(R.layout.feedbefore_fragement);
		loginDialog.setCanceledOnTouchOutside(true);
		loginDialog.setCancelable(true);
		Button loginBtn = (Button) loginDialog.findViewById(R.id.btnLogin);
		Button joinBtn = (Button) loginDialog.findViewById(R.id.btnJoin);
		TextView textView1 = (TextView) loginDialog
				.findViewById(R.id.textView1);
		TextView textView2 = (TextView) loginDialog
				.findViewById(R.id.textView2);
		textView1.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginDialog.dismiss();
				showSvaadLoginDialog(context, fragment, mode, mixpanelscreen,
						action);

				// tracker = GoogleAnalyticsTracker.getInstance();
				// tracker.start("UA-51874654-1",5, context);
				//
				// tracker.trackEvent("LoginClick", "Loginbutton", "Clicked",
				// 1);

			}
		});

		joinBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginDialog.dismiss();
				try {
					showSvaadSignUpDialog(context, fragment, mode,
							mixpanelscreen, action);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		loginDialog.show();
	}

	public void showSvaadLoginDialog(final Context context,
			final Fragment fragment, final int mode,
			final String mixpanelscreen, final String action) {
		final Dialog loginDialog = new Dialog(context);
		loginDialog.getWindow().setTitle("Login");
		loginDialog.setContentView(R.layout.dialog_login_layout);
		loginDialog.setCanceledOnTouchOutside(true);
		loginDialog.setCancelable(true);
		Button btnlogInButton = (Button) loginDialog
				.findViewById(R.id.logInButton);
		Button btnFb = (Button) loginDialog.findViewById(R.id.facebookButton);

		final EditText passwordEditText = (EditText) loginDialog
				.findViewById(R.id.passwordEditText);
		final EditText emailEditText = (EditText) loginDialog
				.findViewById(R.id.emailIdEditText);
		//
		// try {
		//
		// new MyMixpanel().getMixPanelSvaadLoginEvent(context);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		btnlogInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// String email=null;
				String email = emailEditText.getText().toString().trim();
				// if(emailCap!=null && emailCap.length()>0)
				// {
				// email = emailCap.toLowerCase().trim();
				// }

				String password = passwordEditText.getText().toString().trim();

				if (email.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {
					showToast(context, "Please enter  email id and password");
				} else if ((email != null && email.length() > 0)) {
					boolean emailValid = validateEmail(email);

					String emailLower = email.toLowerCase().trim();

					if (emailValid == true) {
						if (password != null && password.length() > 0) {
							LogInRequestDto logInRequestDto = getLoginRequestDto(
									emailLower, password, context);
							if (logInRequestDto == null) {
								return;
							}
							try {
								if (mode != 0) {
									loginDialog.dismiss();
									new LogInAsyncTask(context,
											logInRequestDto,
											Constants.LOG_IN_SCREEN, mode,
											mixpanelscreen, action).execute();
								} else {
									loginDialog.dismiss();
									new LogInAsyncTask(context,
											logInRequestDto,
											Constants.LOG_IN_SCREEN, mode,
											mixpanelscreen, action).execute();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							showToast(context, "Please enter password");
						}
					} else {
						showToast(context, "Please enter valid emailid");
					}
				} else {
					showToast(context, "Please enter  emailid");
				}

			}
		});

		btnFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// try {
				//
				// new MyMixpanel().getMixPanelFacebookEvent(context);
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				loginDialog.dismiss();
				FacebookLoginUtil.loginToFacebook((Activity) context,
						Constants.LOG_IN_FB_SCREEN, mode, mixpanelscreen,
						action);
			}
		});

		loginDialog.show();
	}

	public void showSvaadSignUpDialog(final Context context,
			final Fragment fragment, final int mode,
			final String mixpanelscreen, final String action) {
		final Dialog signUpDialog = new Dialog(context);

		signUpDialog.getWindow().setTitle("Join");
		signUpDialog.setContentView(R.layout.dialog_signup_layout);
		signUpDialog.setCanceledOnTouchOutside(true);

		Button btnsignUpButton = (Button) signUpDialog
				.findViewById(R.id.signUpButton);

		final EditText nameEditText = (EditText) signUpDialog
				.findViewById(R.id.nameEditText);
		final EditText emailEditText = (EditText) signUpDialog
				.findViewById(R.id.emailIdEditText);
		final EditText passEditText = (EditText) signUpDialog
				.findViewById(R.id.passwordEditText);

		// try {
		//
		// new MyMixpanel().getMixPanelSvaadJoinEvent(context);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		Button facebookSignUpButton = (Button) signUpDialog
				.findViewById(R.id.facebookSignUpButton);
		btnsignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameEditText.getText().toString().trim();
				String email = emailEditText.getText().toString().trim();
				// if(emailCap!=null && emailCap.length()>0)
				// {
				// email=emailCap.toLowerCase().trim();
				// }
				String password = passEditText.getText().toString().trim();

				if (email.equalsIgnoreCase("") && name.equalsIgnoreCase("")
						&& password.equalsIgnoreCase("")) {
					showToast(context,
							"Please enter email id,name and password");
				}

				else if ((email != null && email.length() > 0)) {
					boolean emailValid = validateEmail(email);
					String emailLower = email.toLowerCase().trim();
					if (emailValid == true) {
						if (password != null && password.length() > 0) {
							if (name != null && name.length() > 0) {

								LogInRequestDto logInRequestDto = getSignUpRequestDto(
										name, emailLower, password, context);
								if (logInRequestDto == null) {
									return;
								}
								try {
									if (mode != 0) {
										signUpDialog.dismiss();
										new LogInAsyncTask(context,
												logInRequestDto,
												Constants.SIGN_UP_SCREEN, mode,
												mixpanelscreen, action)
												.execute();
									} else {
										signUpDialog.dismiss();
										new LogInAsyncTask(context,
												logInRequestDto,
												Constants.SIGN_UP_SCREEN, mode,
												mixpanelscreen, action)
												.execute();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								showToast(context, "Please enter your name");
							}

						} else {
							showToast(context, "Please enter password");
						}
					} else {
						showToast(context, "Please enter valid emailid");
					}
				} else {
					showToast(context, "Please enter  emailid");
				}

			}
		});

		facebookSignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// try {
				//
				// new MyMixpanel().getMixPanelFacebookEvent(context);
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				signUpDialog.dismiss();
				FacebookLoginUtil.loginToFacebook((Activity) context,
						Constants.SIGN_UP_FB_SCREEN, mode, mixpanelscreen,
						action);
			}
		});

		signUpDialog.show();
	}

	public void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public boolean validateEmail(String email) {

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}

	private LogInRequestDto getSignUpRequestDto(String name, String email,
			String password, Context context) {

		LogInRequestDto logInRequestDto = null;
		if (name.length() > 0 && email.length() > 0 && password.length() > 0) {
			logInRequestDto = new LogInRequestDto();
			logInRequestDto.setEmail(email);
			logInRequestDto.setPassword(password);
			logInRequestDto.setUname(name);
			logInRequestDto.setUsername(email);
		} else {
			Toast.makeText(context,
					"Please fill the feilds and then tap on the Join",
					Toast.LENGTH_LONG).show();
		}
		return logInRequestDto;
	}

	private LogInRequestDto getLoginRequestDto(String email, String password,
			Context context) {

		LogInRequestDto logInRequestDto = null;
		if (email.length() > 0 && password.length() > 0) {
			logInRequestDto = new LogInRequestDto();
			logInRequestDto.setEmail(email);
			logInRequestDto.setPassword(password);
			logInRequestDto.setUsername(email);
		} else {
			Toast.makeText(context,
					"Please fill the feilds and then tap on the Join",
					Toast.LENGTH_LONG).show();
		}
		return logInRequestDto;
	}

	public void showsetLoginDailog(final Context context, String message) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle("Please Login to continue");
		// builder.setMessage(message);
		builder.setInverseBackgroundForced(true);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context,
						FeedbeferLoginFragment.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(true);
		alert.show();

	}

	public void setImageToPiccaso(ImageView im, String url, Context context) {
		Picasso.with(context).load(url).placeholder(R.drawable.temp)
				.error(R.drawable.temp)

				.into(im);
	}

	public void setDefaultImageToPiccaso(ImageView im, Context context) {
		Picasso.with(context).load(R.drawable.temp)
				.placeholder(R.drawable.temp).error(R.drawable.temp)

				.into(im);
	}

	public void showGPSDisabledAlertToUser(final Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder
				.setTitle("Location services disabled")
				.setMessage(
						"Svaad needs access to your location.Please turn on location access.")
				.setCancelable(false)
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								context.startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	@Deprecated
	public void showGPSDisabledAlertToUserForHome(final Context context) {
		final Dialog gpsDialog = new Dialog(context);
		gpsDialog.getWindow().setTitle("GPS Alert");
		gpsDialog.setContentView(R.layout.gps_dialog);
		gpsDialog.setCanceledOnTouchOutside(false);

		Button btnHyderabad = (Button) gpsDialog
				.findViewById(R.id.btnHyderabad);
		Button btnGps = (Button) gpsDialog.findViewById(R.id.btnGps);
		btnHyderabad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				// Utils.saveToSharedPreference("radioLocationValue",
				// "All Locations");
				Utils.saveToSharedPreference("spinner", "All Locations");

				Intent i = new Intent(context, HomeActivity.class);
				context.startActivity(i);
				((Activity) context).finish();

			}
		});

		btnGps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				Intent callGPSSettingIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(callGPSSettingIntent);
			}
		});

		gpsDialog.show();
	}

	public void showGPSDisabledAlertToUserForHome(final Context context,
			final Fragment fragment) {
		final Dialog gpsDialog = new Dialog(context);
		gpsDialog.getWindow().setTitle("Location serviecs disabled");
		gpsDialog.setContentView(R.layout.gps_dialog);
		gpsDialog.setCanceledOnTouchOutside(false);

		Button btnHyderabad = (Button) gpsDialog
				.findViewById(R.id.btnHyderabad);
		Button btnGps = (Button) gpsDialog.findViewById(R.id.btnGps);
		btnHyderabad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				if (fragment instanceof Svaad_Fragment_Spinner)
					((Svaad_Fragment_Spinner) fragment).loadAllLocations();

			}
		});

		btnGps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				Intent callGPSSettingIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(callGPSSettingIntent);
			}
		});

		gpsDialog.show();
	}

	public void showGPSDisabledAlertToCamActivity(final Context context,
			final Fragment fragment) {
		final Dialog gpsDialog = new Dialog(context);
		gpsDialog.getWindow().setTitle("Location serviecs disabled");
		gpsDialog.setContentView(R.layout.cam_gps_dialog);
		gpsDialog.setCanceledOnTouchOutside(false);

		Button btnCancel = (Button) gpsDialog.findViewById(R.id.btnCancel);
		Button btnGps = (Button) gpsDialog.findViewById(R.id.btnGps);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				if (fragment instanceof Search_Fragment)
					((Search_Fragment) fragment).loadText();

			}
		});

		btnGps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();

				Intent callGPSSettingIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(callGPSSettingIntent);
			}
		});

		gpsDialog.show();
	}

	public void showGPSLocationWaiting(final Activity context,
			final Fragment fragment) {
		final Dialog gpsDialog = new Dialog(context);
		gpsDialog.getWindow().setTitle("Waiting for GPS Location");
		gpsDialog.setContentView(R.layout.gps_waiting_dialog);
		gpsDialog.setCanceledOnTouchOutside(false);

		Button btnHyderabad = (Button) gpsDialog
				.findViewById(R.id.btnHyderabad);

		gpsDialog.show();

		final Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// If you want to modify a view in your Activity
				context.runOnUiThread(new Runnable() {

					private Location location = null;

					@Override
					public void run() {
						location = LocationUtil.getInstance(context)
								.getCurrentLocation();
						if (location != null) {
							myTimer.cancel();

							if (fragment instanceof Svaad_Fragment_Spinner
									&& gpsDialog.isShowing()) {
								gpsDialog.dismiss();
								((Svaad_Fragment_Spinner) fragment)
										.locationFound();
							}

						}

					}
				});

			}
		}, 500, 5000);

		btnHyderabad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsDialog.dismiss();
				myTimer.cancel();
				if (fragment instanceof Svaad_Fragment_Spinner)
					((Svaad_Fragment_Spinner) fragment).loadAllLocations();

			}
		});

		gpsDialog.show();
	}

	// public void showGPSDisabledAlertToUserForHome(final Context context) {
	//
	// // Builder builder = new AlertDialog.Builder(context);
	// // builder.setCancelable(true);
	// // builder.setTitle("Turn on GPS");
	// // builder.setMessage("Please turn on Gps ");

	// // builder.setInverseBackgroundForced(true);
	// //
	// // builder.setPositiveButton("Yes", new
	// // DialogInterface.OnClickListener() {
	// // @Override
	// // public void onClick(DialogInterface dialog, int which) {
	// // dialog.cancel();
	// //
	// // }
	// // });
	// Builder builder = new AlertDialog.Builder(context);
	// builder.setMessage(
	// "GPS is disabled in your device. Would you like to enable it?")
	// .setCancelable(false)
	// .setPositiveButton("Goto Settings Page To Enable GPS",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	//
	// Intent callGPSSettingIntent = new Intent(
	// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	// context.startActivity(callGPSSettingIntent);
	// }
	// });
	// builder.setNegativeButton("All Locations in Hyderabad",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dialog.dismiss();
	//
	// // Utils.saveToSharedPreference("radioLocationValue",
	// // "All Locations");
	// //
	// // Intent i=new Intent(context,HomeActivity.class);
	// // context.startActivity(i);
	// // ((Activity) context).finish();
	//
	// }
	// });
	// AlertDialog alert = builder.create();
	// alert.show();
	// }

	public void showGpsTurnOnDialog(final Context context) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setTitle("Turn on GPS");
		builder.setMessage("Please turn on Gps ");
		builder.setInverseBackgroundForced(true);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

}
