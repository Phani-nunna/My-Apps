package com.paradigmcreatives.audit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
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

public class DownloadAsyncTask extends
		AsyncTask<Handler, Void, Boolean> {

	private Handler handler;

	private WeakReference<SplashScreenActivity> activityReference;

	//private ProgressDialog progressDialog;

	private TotalDataBase finalQuestionsList;

	public DownloadAsyncTask(SplashScreenActivity auditDataBaseActivity) {
		activityReference = new WeakReference<SplashScreenActivity>(auditDataBaseActivity);
		finalQuestionsList = new TotalDataBase();
	}

	/*@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(activityReference.get(),
				"Getting the Questions", "Kindly wait...");
		progressDialog.setCancelable(false);
	}*/

	@Override
	protected Boolean doInBackground(Handler... params) {
		handler = params[0];
		//HttpURLConnection connection = getConnection();
		//boolean check = processConnection(connection);
		finalQuestionsList = processJSON(null);
		if (finalQuestionsList != null) {
			return true;
		} else {
			return false;
		}
		//return check;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		/*if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}*/
		if (result) {
			Message message = handler.obtainMessage();
			message.what = 0;
			message.obj = finalQuestionsList;
			handler.sendMessage(message);
		} else {
			handler.sendEmptyMessage(1);
		}
	}

	private boolean processConnection(HttpURLConnection connection) {

		if (connection != null) {
			try {
				int responseCode = connection.getResponseCode();
				Log.d("QUESTION", "response code" + responseCode);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String line = "";
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				Log.d("Audit",
						"response string from the server" + builder.toString());
				finalQuestionsList = processJSON(builder.toString());
				if (finalQuestionsList != null) {
					return true;
				} else {
					return false;
				}

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		} else {
			Log.d("QUESTION", "connection is null");
			return false;
		}
	}

	private TotalDataBase processJSON(String string) {
		try {
			StringBuilder builder = new StringBuilder();
			File file = new File(Environment.getExternalStorageDirectory()+ "/GetAllData_modified");
			if (file.exists()) {

				try {
					FileInputStream inputStream = new FileInputStream(file);
					if (inputStream != null) {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream));
						if (reader != null) {
							String line;
							while ((line = reader.readLine()) != null) {
								builder.append(line);
							}
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					
				}
			}
			JSONObject mainJsonObject = new JSONObject(builder.toString());
			JSONObject jsonObject = new JSONObject(
					mainJsonObject.getString("GetAllTheDataForMobileAppResult"));
			TotalDataBase totalDataBase = new TotalDataBase();

			// Audit_Category
			if (jsonObject.has("Audit_Category")) {
				JSONArray auditCategoryArray = jsonObject
						.getJSONArray("Audit_Category");
				ArrayList<Audit_Category> audit_category_list = new ArrayList<Audit_Category>();
				for (int i = 0; i < auditCategoryArray.length(); i++) {
					Audit_Category audit_category = new Audit_Category();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = auditCategoryArray.getJSONObject(i);
					if (jsonObject2.has("Process_Id")) {
						audit_category
								.setProcess_ID((Integer.parseInt(jsonObject2
										.getString("Process_Id"))));
					}
					if (jsonObject2.has("Category_Id")) {
						audit_category
								.setCategory_ID((Integer.parseInt(jsonObject2
										.getString("Category_Id"))));
					}
					if (jsonObject2.has("Category_Name")) {
						audit_category.setCategory_Name(jsonObject2
								.getString("Category_Name"));
					}
					audit_category_list.add(audit_category);
				}
				totalDataBase.setAuditCategory(audit_category_list);
			} else {
				Log.d("Audit",
						"No Array with the name Audit_category found in the json response");
			}

			// Audit_Questions
			if (jsonObject.has("Audit_Questions")) {
				JSONArray auditQuestionsArray = jsonObject
						.getJSONArray("Audit_Questions");
				System.out.println("audit_questions array"
						+ auditQuestionsArray.toString());
				ArrayList<Audit_Questions> audit_questions_list = new ArrayList<Audit_Questions>();
				for (int i = 0; i < auditQuestionsArray.length(); i++) {
					Audit_Questions audit_questions = new Audit_Questions();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = auditQuestionsArray.getJSONObject(i);
					System.out.println("audit_questions array element"
							+ jsonObject2.toString());
					if (jsonObject2.has("Process_Id")) {
						audit_questions
								.setProcess_Id((Integer.parseInt(jsonObject2
										.getString("Process_Id"))));
					}
					if (jsonObject2.has("Category_Id")) {
						audit_questions
								.setCategory_Id((Integer.parseInt(jsonObject2
										.getString("Category_Id"))));
					}
					if (jsonObject2.has("Question_Id")) {
						audit_questions
								.setQuestion_ID((Integer.parseInt(jsonObject2
										.getString("Question_Id"))));
					}
					if (jsonObject2.has("Question")) {
						audit_questions.setQuestion(jsonObject2
								.getString("Question"));
					}
					if (jsonObject2.has("No_of_Controls")) {
						audit_questions.setNo_of_Controls((Integer
								.parseInt(jsonObject2
										.getString("No_of_Controls"))));
					}
					if (jsonObject2.has("Control_ID")) {
						audit_questions
								.setControl_ID((Integer.parseInt(jsonObject2
										.getString("Control_ID"))));
					}
					audit_questions_list.add(audit_questions);
				}
				totalDataBase.setAuditQuestions(audit_questions_list);
			} else {
				Log.d("Audit",
						"No Array with the name Audit_Questions found in the json response");
			}

			// Auditor_Schedules
			if (jsonObject.has("Auditor_Schedules")) {
				JSONArray auditSchedulesArray = jsonObject
						.getJSONArray("Auditor_Schedules");
				ArrayList<Auditor_Schedules> audit_schedules_list = new ArrayList<Auditor_Schedules>();
				for (int i = 0; i < auditSchedulesArray.length(); i++) {
					Auditor_Schedules audit_schedules = new Auditor_Schedules();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = auditSchedulesArray.getJSONObject(i);
					if (jsonObject2.has("Process")) {
						audit_schedules.setProcess(jsonObject2
								.getString("Process"));
					}
					if (jsonObject2.has("State")) {
						audit_schedules
								.setState(jsonObject2.getString("State"));
					}
					if (jsonObject2.has("District")) {
						audit_schedules.setDistrict(jsonObject2
								.getString("District"));
					}
					if (jsonObject2.has("Taluka")) {
						audit_schedules.setTaluka(jsonObject2
								.getString("Taluka"));
					}
					if (jsonObject2.has("Village")) {
						audit_schedules.setVillage(jsonObject2
								.getString("Village"));
					}
					if (jsonObject2.has("Schedule_Date")) {
						audit_schedules.setSchedule_Date(jsonObject2
								.getString("Schedule_Date"));
					}
					if (jsonObject2.has("Auditor_Emp_ID")) {
						audit_schedules.setAuditor_Emp_ID((Integer
								.parseInt(jsonObject2
										.getString("Auditor_Emp_ID"))));
					}
					if (jsonObject2.has("Status")) {
						audit_schedules.setStatus(jsonObject2
								.getString("Status"));
					}
					if (jsonObject2.has("Created_date")) {
						audit_schedules.setCreated_date(jsonObject2
								.getString("Created_date"));
					}
					audit_schedules_list.add(audit_schedules);
				}
				totalDataBase.setAuditorSchedules(audit_schedules_list);
			} else {
				Log.d("Audit",
						"No Array with the name Auditor_Schedules found in the json response");
			}

			// Banner_Images
			if (jsonObject.has("Banner_Images")) {
				JSONArray banner_ImagesArray = jsonObject
						.getJSONArray("Banner_Images");
				ArrayList<Banner_Images> banner_images_list = new ArrayList<Banner_Images>();
				for (int i = 0; i < banner_ImagesArray.length(); i++) {
					Banner_Images banner_images = new Banner_Images();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = banner_ImagesArray.getJSONObject(i);
					if (jsonObject2.has("Image_Path")) {
						banner_images.setImage_Path(jsonObject2
								.getString("Image_Path"));
					}
					banner_images_list.add(banner_images);
				}
				totalDataBase.setBannerImages(banner_images_list);
			} else {
				Log.d("Audit",
						"No Array with the name Banner_Images found in the json response");
			}

			// Controls
			if (jsonObject.has("Controls")) {
				JSONArray controlsArray = jsonObject.getJSONArray("Controls");
				ArrayList<Controls> controls_list = new ArrayList<Controls>();
				for (int i = 0; i < controlsArray.length(); i++) {
					Controls controls = new Controls();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = controlsArray.getJSONObject(i);
					if (jsonObject2.has("Control_Id")) {
						controls.setControl_Id((Integer.parseInt(jsonObject2
								.getString("Control_Id"))));
					}
					if (jsonObject2.has("Control_Name")) {
						controls.setControl_Name((jsonObject2
								.getString("Control_Name")));
					}
					controls_list.add(controls);
				}
				totalDataBase.setControls(controls_list);
			} else {
				Log.d("Audit",
						"No Array with the name Control_Name found in the json response");
			}

			// districts
			if (jsonObject.has("Districts")) {
				JSONArray districtsArray = jsonObject.getJSONArray("Districts");
				ArrayList<Districts> districts_list = new ArrayList<Districts>();
				for (int i = 0; i < districtsArray.length(); i++) {
					Districts districts = new Districts();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = districtsArray.getJSONObject(i);
					if (jsonObject2.has("district_Id")) {
						districts.setDistrict_id((Integer.parseInt(jsonObject2
								.getString("district_Id"))));
					}
					if (jsonObject2.has("state_id")) {
						districts.setState_id((Integer.parseInt(jsonObject2
								.getString("state_id"))));
					}
					if (jsonObject2.has("district_name")) {
						districts.setDistrict_name(jsonObject2
								.getString("district_name"));
					}
					districts_list.add(districts);
				}
				totalDataBase.setDistricts(districts_list);
			} else {
				Log.d("Audit",
						"No Array with the name districts found in the json response");
			}

			// employees
			if (jsonObject.has("Employees")) {
				JSONArray employeesArray = jsonObject.getJSONArray("Employees");
				ArrayList<Employees> employees_list = new ArrayList<Employees>();
				for (int i = 0; i < employeesArray.length(); i++) {
					Employees employees = new Employees();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = employeesArray.getJSONObject(i);
					if (jsonObject2.has("Emp_ID")) {
						employees.setEmp_ID((Integer.parseInt(jsonObject2
								.getString("Emp_ID"))));
					}
					if (jsonObject2.has("First_Name")) {
						employees.setFirst_Name(jsonObject2
								.getString("First_Name"));
					}
					if (jsonObject2.has("Last_Name")) {
						employees.setLast_Name(jsonObject2
								.getString("Last_Name"));
					}
					if (jsonObject2.has("Gender")) {
						employees.setGender(jsonObject2.getString("Gender"));
					}
					if (jsonObject2.has("Dob")) {
						employees.setDob(jsonObject2.getString("First_Name"));
					}
					if (jsonObject2.has("Father_Name")) {
						employees.setFather_Name(jsonObject2
								.getString("Father_Name"));
					}
					if (jsonObject2.has("Mother_Name")) {
						employees.setMother_Name(jsonObject2
								.getString("Mother_Name"));
					}
					if (jsonObject2.has("Qualification")) {
						employees.setQualification(jsonObject2
								.getString("Qualification"));
					}
					if (jsonObject2.has("Mobile_Number")) {
						employees.setMobile_Number(jsonObject2
								.getString("Mobile_Number"));
					}
					if (jsonObject2.has("Alternative_Mobile_Number")) {
						employees.setAlternative_Mobile_Number(jsonObject2
								.getString("Alternative_Mobile_Number"));
					}
					if (jsonObject2.has("Emergency_Contact_Number")) {
						employees.setEmergency_Contact_Number(jsonObject2
								.getString("Emergency_Contact_Number"));
					}
					if (jsonObject2.has("Email_ID")) {
						employees
								.setEmail_ID(jsonObject2.getString("Email_ID"));
					}
					if (jsonObject2.has("Address1")) {
						employees
								.setAddress1(jsonObject2.getString("Address1"));
					}
					if (jsonObject2.has("Address2")) {
						employees
								.setAddress2(jsonObject2.getString("Address2"));
					}
					if (jsonObject2.has("Referred_By")) {
						employees.setReferred_By(jsonObject2
								.getString("Referred_By"));
					}
					if (jsonObject2.has("Referral_Contact_Number")) {
						employees.setReferral_Contact_Number(jsonObject2
								.getString("Referral_Contact_Number"));
					}
					if (jsonObject2.has("Interview_Attending_Date")) {
						employees.setInterview_Attending_Date(jsonObject2
								.getString("Interview_Attending_Date"));
					}
					if (jsonObject2.has("Doj")) {
						employees.setDoj(jsonObject2.getString("Doj"));
					}
					if (jsonObject2.has("Process_at_the_time_of_joining")) {
						employees.setProcess_at_the_time_of_joining(jsonObject2
								.getString("Process_at_the_time_of_joining"));
					}
					if (jsonObject2.has("Designation_at_the_time_of_joining")) {
						employees
								.setDesignation_at_the_time_of_joining(jsonObject2
										.getString("Designation_at_the_time_of_joining"));
					}
					if (jsonObject2.has("Date_of_Appoitment")) {
						employees.setDate_of_Appoitment(jsonObject2
								.getString("Date_of_Appoitment"));
					}
					if (jsonObject2.has("Salary_Declared")) {
						employees.setSalary_Declared(jsonObject2
								.getString("Salary_Declared"));
					}
					if (jsonObject2.has("Daily_Allowance")) {
						employees.setDaily_Allowance(jsonObject2
								.getString("Daily_Allowance"));
					}
					if (jsonObject2.has("Travel_Allowance")) {
						employees.setTravel_Allowance(jsonObject2
								.getString("Travel_Allowance"));
					}
					if (jsonObject2.has("Status")) {
						employees.setStatus(jsonObject2.getString("Status"));
					}
					if (jsonObject2.has("Remarks")) {
						employees.setRemarks(jsonObject2.getString("Remarks"));
					}
					if (jsonObject2.has("Created_Date")) {
						employees.setCreated_Date(jsonObject2
								.getString("Created_Date"));
					}
					if (jsonObject2.has("Updated_Date")) {
						employees.setUpdated_Date(jsonObject2
								.getString("Updated_Date"));
					}
					employees_list.add(employees);
				}
				totalDataBase.setEmployees(employees_list);
			} else {
				Log.d("Audit",
						"No Array with the name employees found in the json response");
			}

			// logins
			if (jsonObject.has("Logins")) {
				JSONArray loginsArray = jsonObject.getJSONArray("Logins");
				ArrayList<Logins> logins_list = new ArrayList<Logins>();
				for (int i = 0; i < loginsArray.length(); i++) {
					Logins logins = new Logins();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = loginsArray.getJSONObject(i);
					if (jsonObject2.has("emp_id")) {
						logins.setEmp_id((Integer.parseInt(jsonObject2
								.getString("emp_id"))));
					}
					if (jsonObject2.has("role_id")) {
						logins.setRole_id((Integer.parseInt(jsonObject2
								.getString("role_id"))));
					}
					if (jsonObject2.has("login_id")) {
						logins.setLogin_id(jsonObject2.getString("login_id"));
					}
					if (jsonObject2.has("password")) {
						logins.setPassword(jsonObject2.getString("password"));
					}
					logins_list.add(logins);
				}
				totalDataBase.setLogins(logins_list);
			} else {
				Log.d("Audit",
						"No Array with the name logins found in the json response");
			}

			// Processes
			if (jsonObject.has("Processes")) {
				JSONArray processesArray = jsonObject.getJSONArray("Processes");
				ArrayList<Processes> processes_list = new ArrayList<Processes>();
				for (int i = 0; i < processesArray.length(); i++) {
					Processes processes = new Processes();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = processesArray.getJSONObject(i);
					if (jsonObject2.has("Process_ID")) {
						processes.setProcess_ID((Integer.parseInt(jsonObject2
								.getString("Process_ID"))));
					}
					if (jsonObject2.has("Process_Name")) {
						processes.setProcess_Name(jsonObject2
								.getString("Process_Name"));
					}
					processes_list.add(processes);
				}
				totalDataBase.setProcesses(processes_list);
			} else {
				Log.d("Audit",
						"No Array with the name processes found in the json response");
			}

			// Responses
			if (jsonObject.has("Responses")) {
				JSONArray responsesArray = jsonObject.getJSONArray("Responses");
				ArrayList<Responses> responses_list = new ArrayList<Responses>();
				for (int i = 0; i < responsesArray.length(); i++) {
					Responses responses = new Responses();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = responsesArray.getJSONObject(i);
					if (jsonObject2.has("Question_ID")) {
						responses.setQuestion_ID((Integer.parseInt(jsonObject2
								.getString("Question_ID"))));
					}
					if (jsonObject2.has("Control_Label")) {
						responses.setControl_Label(jsonObject2
								.getString("Control_Label"));
					}
					responses_list.add(responses);
				}
				totalDataBase.setResponses(responses_list);
			} else {
				Log.d("Audit",
						"No Array with the name Responses found in the json response");
			}

			// roles
			if (jsonObject.has("Roles")) {
				JSONArray rolesArray = jsonObject.getJSONArray("Roles");
				ArrayList<Roles> roles_list = new ArrayList<Roles>();
				for (int i = 0; i < rolesArray.length(); i++) {
					Roles roles = new Roles();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = rolesArray.getJSONObject(i);
					if (jsonObject2.has("Role_ID")) {
						roles.setRole_ID((Integer.parseInt(jsonObject2
								.getString("Role_ID"))));
					}
					if (jsonObject2.has("Role_Name")) {
						roles.setRole_Name(jsonObject2.getString("Role_Name"));
					}
					if (jsonObject2.has("Description")) {
						roles.setDescription(jsonObject2
								.getString("Description"));
					}
					if (jsonObject2.has("Created_Date")) {
						roles.setCreated_Date(jsonObject2
								.getString("Created_Date"));
					}
					if (jsonObject2.has("Created_Date")) {
						roles.setCreated_Date(jsonObject2
								.getString("Created_Date"));
					}
					if (jsonObject2.has("Status")) {
						roles.setStatus(jsonObject2.getString("Status"));
					}
					roles_list.add(roles);
				}
				totalDataBase.setRoles(roles_list);
			} else {
				Log.d("Audit",
						"No Array with the name roles found in the json response");
			}

			// states
			if (jsonObject.has("States")) {
				JSONArray statesArray = jsonObject.getJSONArray("States");
				ArrayList<States> states_list = new ArrayList<States>();
				for (int i = 0; i < statesArray.length(); i++) {
					States states = new States();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = statesArray.getJSONObject(i);
					if (jsonObject2.has("state_id")) {
						states.setState_id((Integer.parseInt(jsonObject2
								.getString("state_id"))));
					}
					if (jsonObject2.has("state_name")) {
						states.setState_name(jsonObject2
								.getString("state_name"));
					}
					states_list.add(states);
				}
				totalDataBase.setStates(states_list);
			} else {
				Log.d("Audit",
						"No Array with the name states found in the json response");
			}

			// talukas
			if (jsonObject.has("Talukas")) {
				JSONArray talukasArray = jsonObject.getJSONArray("Talukas");
				ArrayList<Talukas> talukas_list = new ArrayList<Talukas>();
				for (int i = 0; i < talukasArray.length(); i++) {
					Talukas states = new Talukas();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = talukasArray.getJSONObject(i);
					if (jsonObject2.has("taluka_id")) {
						states.setTaluka_id((Integer.parseInt(jsonObject2
								.getString("taluka_id"))));
					}
					if (jsonObject2.has("state_id")) {
						states.setState_id((Integer.parseInt(jsonObject2
								.getString("state_id"))));
					}
					if (jsonObject2.has("district_id")) {
						states.setDistrict_id((Integer.parseInt(jsonObject2
								.getString("district_id"))));
					}
					if (jsonObject2.has("taluka_name")) {
						states.setTaluka_name(jsonObject2
								.getString("taluka_name"));
					}
					talukas_list.add(states);
				}
				totalDataBase.setTalukas(talukas_list);
			} else {
				Log.d("Audit",
						"No Array with the name talukas found in the json response");
			}

			// villages
			if (jsonObject.has("Villages")) {
				JSONArray villagesArray = jsonObject.getJSONArray("Villages");
				ArrayList<Villages> villages_list = new ArrayList<Villages>();
				for (int i = 0; i < villagesArray.length(); i++) {
					Villages villages = new Villages();
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2 = villagesArray.getJSONObject(i);
					if (jsonObject2.has("village_id")) {
						String village_id = jsonObject2.getString("village_id");
						if (village_id.length() > 0) {
							villages.setVillage_id((Integer
									.parseInt(jsonObject2
											.getString("village_id"))));
						} else {
							Log.d("AUDIT", "null value in village id");
						}
					}
					if (jsonObject2.has("state_id")) {
						String state_id = jsonObject2.getString("state_id");
						if (state_id.length() > 0) {
							villages.setState_id((Integer.parseInt(jsonObject2
									.getString("state_id"))));
						} else {
							Log.d("AUDIT", "null value in state id");
						}
					}
					if (jsonObject2.has("district_id")) {
						String district_id = jsonObject2
								.getString("district_id");
						if (district_id.length() > 0) {
							villages.setDistrict_id((Integer
									.parseInt(jsonObject2
											.getString("district_id"))));
						} else {
							Log.d("AUDIT", "null value in district id");
						}
					}
					if (jsonObject2.has("taluka_id")) {
						String taluka_id = jsonObject2.getString("taluka_id");
						if (taluka_id.length() > 0) {
							villages.setTaluka_id((Integer.parseInt(jsonObject2
									.getString("taluka_id"))));
						} else {
							Log.d("AUDIT", "null value in taluka id");
						}
					}
					if (jsonObject2.has("village_name")) {
						villages.setVillage_name(jsonObject2
								.getString("village_name"));
					}
					villages_list.add(villages);
				}
				totalDataBase.setVillages(villages_list);
			} else {
				Log.d("Audit",
						"No Array with the name villages found in the json response");
			}

			return totalDataBase;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private HttpURLConnection getConnection() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(
					"http://110.234.129.67/AuditServiceTest/RestServiceImpl.svc/GetAllData");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("content-type", "application/json");
			connection.setRequestProperty("accept", "application/json");
			return connection;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
