package com.paradigmcreatives.audit;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.paradigmcreatives.audit.beans.Audit_Category;
import com.paradigmcreatives.audit.beans.Audit_Questions;
import com.paradigmcreatives.audit.beans.Auditor_Schedules;
import com.paradigmcreatives.audit.beans.Banner_Images;
import com.paradigmcreatives.audit.beans.Controls;
import com.paradigmcreatives.audit.beans.Districts;
import com.paradigmcreatives.audit.beans.Employees;
import com.paradigmcreatives.audit.beans.Logins;
import com.paradigmcreatives.audit.beans.Processes;
import com.paradigmcreatives.audit.beans.Responses;
import com.paradigmcreatives.audit.beans.Roles;
import com.paradigmcreatives.audit.beans.States;
import com.paradigmcreatives.audit.beans.Talukas;
import com.paradigmcreatives.audit.beans.TotalDataBase;
import com.paradigmcreatives.audit.beans.Villages;
import com.paradigmcreatives.audit.database.AuditDataBase;
import com.paradigmcreatives.audit.util.Utility;

public class SplashScreenActivity extends Activity {
	public Handler mHandler = new Handler();
	public Dialog progressDialog;
	private boolean isNetwork;
	protected boolean _active = true;
	protected int _splashTime = 3000;
	private AuditDataBase mDatabase;
	private DownloadAsyncTask mDownloadAsyncTask;
	public static SplashScreenActivity mSplashScreenIns;

	/*
	 * private final Runnable mPendingLauncherRunnable = new Runnable() { public
	 * void run() {
	 * 
	 * Intent mIntent = new Intent(SplashScreenActivity.this,
	 * AuditLoginActivity.class); startActivityForResult(mIntent, 1); //
	 * loginscreen(); } };
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		mSplashScreenIns = this;
		showProgressDialog(this);
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// progressDialog.dismiss();
					File file = new File(
							"/data/data/com.paradigmcreatives.audit/databases/"
									+ AuditDataBase.MYDATABASE_NAME);
					if (file.exists()) {
						System.out.println("FILE EXIT");
						loginscreen();
					} else {
						isNetwork = Utility.getInstance().isOnline(
								SplashScreenActivity.this);

						if (isNetwork) {

							System.out.println("isnetwork " + isNetwork);

							mDatabase = new AuditDataBase(
									SplashScreenActivity.this);
							Looper.prepare();
							mDownloadAsyncTask = new DownloadAsyncTask(
									SplashScreenActivity.this);
							mDownloadAsyncTask.execute(new Handler() {
								public void handleMessage(Message msg) {

									if (msg.what == 0) {
										TotalDataBase totalDataBase = (TotalDataBase) msg.obj;
										AuditDataBase auditDataBase = new AuditDataBase(
												SplashScreenActivity.this);
										auditDataBase.openToWrite();

										if (totalDataBase != null) {
											// Audit_Category
											ArrayList<Audit_Category> auditCategory = new ArrayList<Audit_Category>();
											auditCategory = totalDataBase
													.getAuditCategory();
											if (auditCategory != null
													&& auditCategory.size() > 0) {
												for (int i = 0; i < auditCategory
														.size(); i++) {
													auditDataBase
															.insertIntoAudit_Category(auditCategory
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Audit_Category in response");
											}
											// Audit_Questions
											ArrayList<Audit_Questions> auditQuestions = new ArrayList<Audit_Questions>();
											auditQuestions = totalDataBase
													.getAuditQuestions();
											if (auditQuestions != null
													&& auditQuestions.size() > 0) {
												for (int i = 0; i < auditQuestions
														.size(); i++) {
													auditDataBase
															.insertIntoAudit_Questions(auditQuestions
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Audit_Questions in response");
											}
											// Auditor_Schedules
											ArrayList<Auditor_Schedules> auditSchedules = new ArrayList<Auditor_Schedules>();
											auditSchedules = totalDataBase
													.getAuditorSchedules();
											if (auditSchedules != null
													&& auditSchedules.size() > 0) {
												for (int i = 0; i < auditSchedules
														.size(); i++) {
													auditDataBase
															.insertIntoAuditor_Schedules(auditSchedules
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Auditor_Schedules in response");
											}
											// Banner_Images
											ArrayList<Banner_Images> bannerImages = new ArrayList<Banner_Images>();
											bannerImages = totalDataBase
													.getBannerImages();
											if (bannerImages != null
													&& bannerImages.size() > 0) {
												for (int i = 0; i < bannerImages
														.size(); i++) {
													auditDataBase
															.insertIntoBanner_Images(bannerImages
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Banner_Images in response");
											}
											// Controls
											ArrayList<Controls> controls = new ArrayList<Controls>();
											controls = totalDataBase
													.getControls();
											if (controls != null
													&& controls.size() > 0) {
												for (int i = 0; i < controls
														.size(); i++) {
													auditDataBase
															.insertIntoControls(controls
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to controls in response");
											}
											// Districts
											ArrayList<Districts> districts = new ArrayList<Districts>();
											districts = totalDataBase
													.getDistricts();
											if (districts != null
													&& districts.size() > 0) {
												for (int i = 0; i < districts
														.size(); i++) {
													auditDataBase
															.insertIntoDistricts(districts
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to districts in response");
											}
											// employees
											ArrayList<Employees> employees = new ArrayList<Employees>();
											employees = totalDataBase
													.getEmployees();
											if (employees != null
													&& employees.size() > 0) {
												for (int i = 0; i < employees
														.size(); i++) {
													auditDataBase
															.insertIntoEmployees(employees
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to employees in response");
											}
											// logins
											ArrayList<Logins> logins = new ArrayList<Logins>();
											logins = totalDataBase.getLogins();
											if (logins != null
													&& logins.size() > 0) {
												for (int i = 0; i < logins
														.size(); i++) {
													auditDataBase
															.insertIntoLogins(logins
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to logins in response");
											}
											// Processes
											ArrayList<Processes> processes = new ArrayList<Processes>();
											processes = totalDataBase
													.getProcesses();
											if (processes != null
													&& processes.size() > 0) {
												for (int i = 0; i < processes
														.size(); i++) {
													auditDataBase
															.insertIntoProcesses(processes
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Processes in response");
											}
											// Responses
											ArrayList<Responses> responses = new ArrayList<Responses>();
											responses = totalDataBase
													.getResponses();
											if (responses != null
													&& responses.size() > 0) {
												for (int i = 0; i < responses
														.size(); i++) {
													auditDataBase
															.insertIntoResponses(responses
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Responses in response");
											}
											// roles
											ArrayList<Roles> roles = new ArrayList<Roles>();
											roles = totalDataBase.getRoles();
											if (roles != null
													&& roles.size() > 0) {
												for (int i = 0; i < roles
														.size(); i++) {
													auditDataBase
															.insertIntoRoles(roles
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to roles in response");
											}
											// states
											ArrayList<States> states = new ArrayList<States>();
											states = totalDataBase.getStates();
											if (states != null
													&& states.size() > 0) {
												for (int i = 0; i < states
														.size(); i++) {
													auditDataBase
															.insertIntoStates(states
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to states in response");
											}
											// talukas
											ArrayList<Talukas> talukas = new ArrayList<Talukas>();
											talukas = totalDataBase
													.getTalukas();
											if (talukas != null
													&& talukas.size() > 0) {
												for (int i = 0; i < talukas
														.size(); i++) {
													auditDataBase
															.insertIntoTalukas(talukas
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Talukas in response");
											}
											// villages
											ArrayList<Villages> villages = new ArrayList<Villages>();
											villages = totalDataBase
													.getVillages();
											if (villages != null
													&& villages.size() > 0) {
												for (int i = 0; i < villages
														.size(); i++) {
													auditDataBase
															.insertIntoVillages(villages
																	.get(i));
												}
											} else {
												Log.d("Audit",
														"there is no data related to Villages in response");
											}

										
											auditDataBase.close();
											loginscreen();
										}
									} else {
										Log.d("AUDIT", "no data from the server");
									}
								}
							});
							Looper.loop();

						} else {

							progressDialog.dismiss();
							Looper.prepare();
							Intent no_internet_intent = new Intent(
									SplashScreenActivity.this, NoInternet.class);
							startActivityForResult(no_internet_intent, 1);
							Looper.loop();
							finish();
						}

					}
				}
			}
		};
		splashTread.start();

	}

	private void loginscreen() {
		Intent mIntent = new Intent(SplashScreenActivity.this,
				AuditLoginActivity.class);
		startActivityForResult(mIntent, 1);
	}

	public void showProgressDialog(Context mContext) {
		progressDialog = ProgressDialog.show(mContext, "",
				"Loading. Please wait...", true);
	}

	public Dialog getProgressDialog() {
		return progressDialog;
	}

	public static SplashScreenActivity getInstance() {
		if (mSplashScreenIns == null) {
			mSplashScreenIns = new SplashScreenActivity();
		}
		return mSplashScreenIns;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			finish();
		}

	}

}
