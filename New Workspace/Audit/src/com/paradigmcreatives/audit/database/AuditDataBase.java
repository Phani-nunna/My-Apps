package com.paradigmcreatives.audit.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.paradigmcreatives.audit.beans.Audit_Category;
import com.paradigmcreatives.audit.beans.Audit_Questions;
import com.paradigmcreatives.audit.beans.Auditor_Schedules;
import com.paradigmcreatives.audit.beans.Banner_Images;
import com.paradigmcreatives.audit.beans.Controls;
import com.paradigmcreatives.audit.beans.Districts;
import com.paradigmcreatives.audit.beans.Employees;
import com.paradigmcreatives.audit.beans.Logins;
import com.paradigmcreatives.audit.beans.Processes;
import com.paradigmcreatives.audit.beans.Question_Answers;
import com.paradigmcreatives.audit.beans.Responses;
import com.paradigmcreatives.audit.beans.Roles;
import com.paradigmcreatives.audit.beans.States;
import com.paradigmcreatives.audit.beans.Talukas;
import com.paradigmcreatives.audit.beans.Villages;

public class AuditDataBase {
	public final static String MYDATABASE_NAME = "Audit";
	public final int MYDATABASE_VERSION = 1;

	/**
	 * TODO
	 */
	private SQLiteHelper sqLiteHelper;

	/**
	 * TODO
	 */
	private SQLiteDatabase sqLiteDatabase;

	/**
	 * TODO
	 */
	private Context context;

	private final String SCRIPT_CREATE_TABLE = "create table if not exists Audit_Category "
			+ "(Process_ID INTEGER,Category_ID NUMBER,Category_Name VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE1 = "create table if not exists Audit_Questions "
			+ "(Process_Id INTEGER,Category_Id INTEGER,Question_ID INTEGER,"
			+ "Question VARCHAR(150),No_of_Controls INTEGER,Control_ID INTEGER);";

	private final String SCRIPT_CREATE_TABLE2 = "create table if not exists Auditor_Schedules "
			+ "(Sno INTEGER PRIMARY KEY AUTOINCREMENT,Process VARCHAR(50),State VARCHAR(50),District VARCHAR(50),"
			+ "Taluka VARCHAR(50),Village VARCHAR(50),Schedule_Date VARCHAR(50),Auditor_Emp_ID INTEGER,"
			+ "Status VARCHAR(50),Created_date VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE3 = "create table if not exists Banner_Images ("
			+ "Sno INTEGER PRIMARY KEY AUTOINCREMENT,Image_Path VARCHAR(150));";

	private final String SCRIPT_CREATE_TABLE4 = "create table if not exists Controls("
			+ "Control_ID INTEGER ,Control_Name VARCHAR(100));";

	private final String SCRIPT_CREATE_TABLE5 = "create table if not exists districts("
			+ "Sno INTEGER PRIMARY KEY AUTOINCREMENT,district_id INTEGER,state_id INTEGER,district_name VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE6 = "create table if not exists employees("
			+ "Emp_ID INTEGER,First_Name VARCHAR(50),Last_Name VARCHAR(50),Gender VARCHAR(10),Dob VARCHAR(50),"
			+ "Father_Name VARCHAR(50),Mother_Name VARCHAR(50),Qualification VARCHAR(20),Mobile_Number VARCHAR(20),"
			+ "Alternative_Mobile_Number VARCHAR(20),Emergency_Contact_Number VARCHAR(20),Email_ID VARCHAR(40),"
			+ "Address1 VARCHAR(100),Address2 VARCHAR(100),Referred_By VARCHAR(30),Referral_Contact_Number VARCHAR(30),"
			+ "Interview_Attending_Date VARCHAR(50),Doj VARCHAR(50),Process_at_the_time_of_joining VARCHAR(30),"
			+ "Designation_at_the_time_of_joining VARCHAR(30),Date_of_Appoitment VARCHAR(50),Salary_Declared VARCHAR(30),"
			+ "Daily_Allowance VARCHAR(30),Travel_Allowance VARCHAR(30),Status VARCHAR(30),Remarks VARCHAR(50)"
			+ ",Created_Date VARCHAR(50),Updated_Date VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE7 = "create table if not exists logins("
			+ "emp_id INTEGER,role_id INTEGER,login_id VARCHAR(50),password VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE8 = "create table if not exists Processes("
			+ "Process_ID NUMBER,Process_Name VARCHAR(60));";

	private final String SCRIPT_CREATE_TABLE9 = "create table if not exists Responses("
			+ "Question_ID NUMBER,Control_Label VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE10 = "create table if not exists roles("
			+ "Role_ID INTEGER,Role_Name VARCHAR(60),Description VARCHAR(100),"
			+ "Created_Date VARCHAR(50),Updated_Date VARCHAR(50),Status VARCHAR(40));";

	private final String SCRIPT_CREATE_TABLE11 = "create table if not exists states("
			+ "Sno INTEGER PRIMARY KEY AUTOINCREMENT,state_id INTEGER,state_name VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE12 = "create table if not exists talukas("
			+ "Sno INTEGER PRIMARY KEY AUTOINCREMENT,taluka_id INTEGER,state_id INTEGER,district_id INTEGER,"
			+ "taluka_name VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE13 = "create table if not exists villages("
			+ "Sno INTEGER PRIMARY KEY AUTOINCREMENT,village_id NUMBER,state_id INTEGER,district_id INTEGER,"
			+ "taluka_id INTEGER,village_name VARCHAR(50));";

	private final String SCRIPT_CREATE_TABLE14 = "create table if not exists question_answers("
			+ "question_id INTEGER,response VARCHAR(100),operators_names VARCHAR(200),supervisors_names VARCHAR(200)"
			+ ",technicians_names VARCHAR(200),remark TEXT,category_name VARCHAR(50),process_id INTEGER,image_path TEXT)";

	/**
	 * TODO
	 * 
	 * @param c
	 */
	public AuditDataBase(Context c) {// constructor
		context = c;
	}

	/**
	 * TODO
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public AuditDataBase openToRead() throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	/**
	 * TODO
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public AuditDataBase openToWrite() throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	/**
	 * TODO
	 */
	public void close() {

		sqLiteHelper.close();
	}

	public long insertIntoAudit_Category(Audit_Category audit_category) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Process_ID", audit_category.getProcess_ID());
		System.out.println("category_id in the insertion"
				+ audit_category.getCategory_ID());
		contentValues.put("Category_ID", audit_category.getCategory_ID());
		contentValues.put("Category_Name", audit_category.getCategory_Name());
		return sqLiteDatabase.insert("Audit_Category", null, contentValues);
	}

	public long insertIntoAudit_Questions(Audit_Questions audit_questions) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Process_ID", audit_questions.getProcess_Id());
		contentValues.put("Category_ID", audit_questions.getCategory_Id());
		contentValues.put("Question_ID", audit_questions.getQuestion_ID());
		contentValues
				.put("No_of_Controls", audit_questions.getNo_of_Controls());
		contentValues.put("Question", audit_questions.getQuestion());
		contentValues.put("Control_ID", audit_questions.getControl_ID());
		return sqLiteDatabase.insert("Audit_Questions", null, contentValues);
	}

	public long insertIntoAuditor_Schedules(Auditor_Schedules auditor_schedules) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Process", auditor_schedules.getProcess());
		contentValues.put("State", auditor_schedules.getState());
		contentValues.put("District", auditor_schedules.getDistrict());
		contentValues.put("Taluka", auditor_schedules.getTaluka());
		contentValues.put("Village", auditor_schedules.getVillage());
		contentValues.put("Schedule_Date",
				String.valueOf(auditor_schedules.getSchedule_Date()));
		contentValues.put("Auditor_Emp_ID",
				auditor_schedules.getAuditor_Emp_ID());
		contentValues.put("Status", auditor_schedules.getStatus());
		contentValues.put("Created_date",
				String.valueOf(auditor_schedules.getCreated_date()));
		return sqLiteDatabase.insert("Auditor_Schedules", null, contentValues);
	}

	public long insertIntoBanner_Images(Banner_Images banner_images) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Image_Path", banner_images.getImage_Path());
		return sqLiteDatabase.insert("Banner_Images", null, contentValues);
	}

	public long insertIntoControls(Controls controls) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Control_ID", controls.getControl_Id());
		contentValues.put("Control_Name", controls.getControl_Name());
		return sqLiteDatabase.insert("Controls", null, contentValues);
	}

	public long insertIntoDistricts(Districts districts) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("district_id", districts.getDistrict_id());
		contentValues.put("state_id", districts.getState_id());
		contentValues.put("district_name", districts.getDistrict_name());
		return sqLiteDatabase.insert("districts", null, contentValues);
	}

	public long insertIntoEmployees(Employees employees) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Emp_ID", employees.getEmp_ID());
		contentValues.put("First_Name", employees.getFirst_Name());
		contentValues.put("Last_Name", employees.getLast_Name());
		contentValues.put("Gender", employees.getGender());
		contentValues.put("Dob", String.valueOf(employees.getDob()));
		contentValues.put("Father_Name", employees.getFather_Name());
		contentValues.put("Mother_Name", employees.getMother_Name());
		contentValues.put("Qualification", employees.getQualification());
		contentValues.put("Mobile_Number", employees.getMobile_Number());
		contentValues.put("Alternative_Mobile_Number",
				employees.getAlternative_Mobile_Number());
		contentValues.put("Emergency_Contact_Number",
				employees.getEmergency_Contact_Number());
		contentValues.put("Email_ID", employees.getEmail_ID());
		contentValues.put("Address1", employees.getAddress1());
		contentValues.put("Address2", employees.getAddress2());
		contentValues.put("Referred_By", employees.getReferred_By());
		contentValues.put("Referral_Contact_Number",
				employees.getReferral_Contact_Number());
		contentValues.put("Interview_Attending_Date",
				String.valueOf(employees.getInterview_Attending_Date()));
		contentValues.put("Doj", String.valueOf(employees.getDoj()));
		contentValues.put("Process_at_the_time_of_joining",
				employees.getProcess_at_the_time_of_joining());
		contentValues.put("Designation_at_the_time_of_joining",
				employees.getDesignation_at_the_time_of_joining());
		contentValues.put("Date_of_Appoitment",
				String.valueOf(employees.getDate_of_Appoitment()));
		contentValues.put("Salary_Declared", employees.getSalary_Declared());
		contentValues.put("Daily_Allowance", employees.getDaily_Allowance());
		contentValues.put("Travel_Allowance", employees.getTravel_Allowance());
		contentValues.put("Status", employees.getStatus());
		contentValues.put("Remarks", employees.getRemarks());
		contentValues.put("Created_Date",
				String.valueOf(employees.getCreated_Date()));
		contentValues.put("Updated_Date",
				String.valueOf(employees.getUpdated_Date()));
		return sqLiteDatabase.insert("employees", null, contentValues);
	}

	public long insertIntoLogins(Logins logins) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("emp_id", logins.getEmp_id());
		contentValues.put("role_id", logins.getRole_id());
		contentValues.put("login_id", logins.getLogin_id());
		contentValues.put("password", logins.getPassword());
		return sqLiteDatabase.insert("logins", null, contentValues);
	}

	public long insertIntoProcesses(Processes processes) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Process_ID", processes.getProcess_ID());
		contentValues.put("Process_Name", processes.getProcess_Name());
		return sqLiteDatabase.insert("Processes", null, contentValues);
	}

	public long insertIntoResponses(Responses responses) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Question_ID", responses.getQuestion_ID());
		contentValues.put("Control_Label", responses.getControl_Label());
		return sqLiteDatabase.insert("Responses", null, contentValues);
	}

	public long insertIntoRoles(Roles roles) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Role_ID", roles.getRole_ID());
		contentValues.put("Role_Name", roles.getRole_Name());
		contentValues.put("Description", roles.getDescription());
		contentValues.put("Created_Date",
				String.valueOf(roles.getCreated_Date()));
		contentValues.put("Updated_Date",
				String.valueOf(roles.getUpdated_Date()));
		contentValues.put("Status", roles.getStatus());
		return sqLiteDatabase.insert("roles", null, contentValues);
	}

	public long insertIntoStates(States states) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("state_id", states.getState_id());
		contentValues.put("state_name", states.getState_name());
		return sqLiteDatabase.insert("states", null, contentValues);
	}

	public long insertIntoTalukas(Talukas talukas) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("taluka_id", talukas.getTaluka_id());
		contentValues.put("state_id", talukas.getState_id());
		contentValues.put("district_id", talukas.getDistrict_id());
		contentValues.put("taluka_name", talukas.getTaluka_name());
		return sqLiteDatabase.insert("talukas", null, contentValues);
	}

	public long insertIntoVillages(Villages villages) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("village_id", villages.getVillage_id());
		contentValues.put("state_id", villages.getState_id());
		contentValues.put("district_id", villages.getDistrict_id());
		contentValues.put("taluka_id", villages.getTaluka_id());
		contentValues.put("village_name", villages.getVillage_name());
		return sqLiteDatabase.insert("villages", null, contentValues);
	}

	public long insertIntoQuestionAnswers(Question_Answers quAnswers) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("question_id", quAnswers.getQuestion_id());
		contentValues.put("response", quAnswers.getResponse());
		contentValues.put("operators_names", quAnswers.getOperators_names());
		contentValues
				.put("supervisors_names", quAnswers.getSupervisors_names());
		contentValues
				.put("technicians_names", quAnswers.getTechnicians_names());
		contentValues.put("remark", quAnswers.getRemark());
		contentValues.put("category_name", quAnswers.getCategory_name());
		contentValues.put("process_id", quAnswers.getProcess_id());
		contentValues.put("image_path", quAnswers.getImage_path());
		return sqLiteDatabase.insert("question_answers", null, contentValues);
	}

	public boolean getLoginAvailability(String userName, String pwd) {
		Cursor cursor = sqLiteDatabase.query("logins", null, "login_id=" + "'"
				+ userName + "' and password=" + "'" + pwd + "'", null, null,
				null, null);
		if (cursor != null && cursor.getCount() == 1) {
			cursor.close();
			return true;
		} else {
			if (cursor != null) {
				cursor.close();
			}
			return false;
		}
	}

	public ArrayList<Auditor_Schedules> getAuditorSchedules() {
		Cursor cursor = sqLiteDatabase.query("Auditor_Schedules",
				new String[] { "Auditor_Emp_ID", "State", "Schedule_Date",
						"Status", "Process" }, null, null, null, null, null);
		ArrayList<Auditor_Schedules> auditor_scheduleslist = new ArrayList<Auditor_Schedules>();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Auditor_Schedules auditor_schedules = new Auditor_Schedules();
				auditor_schedules.setAuditor_Emp_ID(cursor.getInt(cursor
						.getColumnIndex("Auditor_Emp_ID")));
				auditor_schedules.setState(cursor.getString(cursor
						.getColumnIndex("State")));
				auditor_schedules.setSchedule_Date(cursor.getString(cursor
						.getColumnIndex("Schedule_Date")));
				auditor_schedules.setStatus(cursor.getString(cursor
						.getColumnIndex("Status")));
				auditor_schedules.setProcess(cursor.getString(cursor
						.getColumnIndex("Process")));
				auditor_scheduleslist.add(auditor_schedules);
			} while (cursor.moveToNext());
			cursor.close();
		} else {
			if (cursor != null) {
				cursor.close();
			}
		}
		return auditor_scheduleslist;
	}

	public ArrayList<Audit_Questions> getAllQuestions(String processName) {
		if (processName != null) {
			String getQuestions = "select Category_Id,Question_ID,No_of_Controls,Question,Process_Id,"
					+ "Control_ID from Audit_Questions where Process_ID = "
					+ "(select Process_ID from Processes where Process_Name="
					+ "'" + processName + "') ORDER BY Category_Id";
			Cursor cursor = sqLiteDatabase.rawQuery(getQuestions, null);
			System.out.println("cursor count" + cursor.getCount());
			System.out.println("cursor column count" + cursor.getColumnCount());
			ArrayList<Audit_Questions> questionsList = new ArrayList<Audit_Questions>();
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					do {
						Audit_Questions audit_Questions = new Audit_Questions();
						audit_Questions.setCategory_Id(cursor.getInt(cursor
								.getColumnIndex("Category_Id")));
						audit_Questions.setQuestion_ID(cursor.getInt(cursor
								.getColumnIndex("Question_ID")));
						audit_Questions.setNo_of_Controls(cursor.getInt(cursor
								.getColumnIndex("No_of_Controls")));
						audit_Questions.setQuestion(cursor.getString(cursor
								.getColumnIndex("Question")));
						audit_Questions.setControl_ID(cursor.getInt(cursor
								.getColumnIndex("Control_ID")));
						audit_Questions.setProcess_Id(cursor.getInt(cursor
								.getColumnIndex("Process_Id")));
						questionsList.add(audit_Questions);
					} while (cursor.moveToNext());
					cursor.close();
					return questionsList;
				}
			}
		}
		return null;
	}

	public String getResponseType(int id) {
		if (id != -1) {
			Cursor cursor = sqLiteDatabase.query("Controls",
					new String[] { "Control_Name" }, "Control_ID=" + "'" + id
							+ "'", null, null, null, null);
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					System.out.println(cursor.getCount());
					cursor.moveToFirst();
					String name = cursor.getString(cursor
							.getColumnIndex("Control_Name"));
					cursor.close();
					return name;
				}
			}

		}
		return null;
	}

	public String getCategoryName(int id) {
		if (id != -1) {
			Cursor cursor = sqLiteDatabase.query("Audit_Category",
					new String[] { "Category_Name" }, "Category_ID=" + "'" + id
							+ "'", null, null, null, null);
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					System.out.println(cursor.getCount());
					cursor.moveToFirst();
					String name = cursor.getString(cursor
							.getColumnIndex("Category_Name"));
					cursor.close();
					return name;
				}
			}

		}
		return null;
	}

	public ArrayList<String> getResponseVales(int id) {
		if (id != -1) {
			Cursor cursor = sqLiteDatabase.query("Responses",
					new String[] { "Control_Label" }, "Question_ID=" + "'" + id
							+ "'", null, null, null, null);
			if (cursor != null) {
				ArrayList<String> responses = new ArrayList<String>();
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					do {
						responses.add(cursor.getString(cursor
								.getColumnIndex("Control_Label")));
					} while (cursor.moveToNext());
					cursor.close();
					return responses;
				}
			}
		}
		return null;
	}

	public ArrayList<Question_Answers> getQuestionAnswers() {
		Cursor cursor = sqLiteDatabase.query("question_answers", new String[] {
				"question_id", "response", "operators_names",
				"supervisors_names", "technicians_names", "remark",
				"category_name", "process_id", "image_path" }, null, null,
				null, null, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				ArrayList<Question_Answers> questionAnswersList = new ArrayList<Question_Answers>();
				cursor.moveToFirst();
				do {
					Question_Answers questionAnswer = new Question_Answers();
					questionAnswer.setCategory_name(cursor.getString(cursor
							.getColumnIndex("category_name")));
					questionAnswer.setProcess_id(cursor.getInt(cursor
							.getColumnIndex("process_id")));
					questionAnswer.setQuestion_id(cursor.getInt(cursor
							.getColumnIndex("question_id")));
					questionAnswer.setResponse(cursor.getString(cursor
							.getColumnIndex("response")));
					String supervisorNames = cursor.getString(cursor
							.getColumnIndex("supervisors_names"));
					String technicianNames = cursor.getString(cursor
							.getColumnIndex("technicians_names"));
					String operatorNames = cursor.getString(cursor
							.getColumnIndex("operators_names"));
					String remark = cursor.getString(cursor
							.getColumnIndex("remark"));
					String image_path = cursor.getString(cursor
							.getColumnIndex("image_path"));
					
					if (supervisorNames != null && supervisorNames.length() > 0) {
						questionAnswer.setSupervisors_names(supervisorNames);
					}
					if (technicianNames != null && technicianNames.length() > 0) {
						questionAnswer.setTechnicians_names(technicianNames);
					}
					if (operatorNames != null && operatorNames.length() > 0) {
						questionAnswer.setOperators_names(operatorNames);
					}
					if (remark != null && remark.length() > 0) {
						questionAnswer.setRemark(remark);
					}
					if (image_path != null && image_path.length() > 0) {
						questionAnswer.setImage_path(image_path);
					}
					questionAnswersList.add(questionAnswer);
				} while (cursor.moveToNext());
				cursor.close();
				return questionAnswersList;
			}
		}
		return null;
	}
	public ArrayList<Banner_Images>getBannerImages(){
		Cursor cursor = sqLiteDatabase.query("Banner_Images", new String[]{"Image_Path"}, null, null, null, null, null);
		ArrayList<Banner_Images>banner_images = new ArrayList<Banner_Images>();
		if(cursor !=null && cursor.getCount()>0){
			cursor.moveToFirst();
			do{
				Banner_Images images = new Banner_Images();
				images.setImage_Path(cursor.getString(cursor.getColumnIndex("Image_Path")));
				banner_images.add(images);
			}while(cursor.moveToNext());
				cursor.close();

				return banner_images;
		}
	
		return null;
		
	}

	/**
	 * TODO
	 * 
	 * @author paradigm
	 * 
	 */
	private class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SCRIPT_CREATE_TABLE);
			db.execSQL(SCRIPT_CREATE_TABLE1);
			db.execSQL(SCRIPT_CREATE_TABLE2);
			db.execSQL(SCRIPT_CREATE_TABLE3);
			db.execSQL(SCRIPT_CREATE_TABLE4);
			db.execSQL(SCRIPT_CREATE_TABLE5);
			db.execSQL(SCRIPT_CREATE_TABLE6);
			db.execSQL(SCRIPT_CREATE_TABLE7);
			db.execSQL(SCRIPT_CREATE_TABLE8);
			db.execSQL(SCRIPT_CREATE_TABLE9);
			db.execSQL(SCRIPT_CREATE_TABLE10);
			db.execSQL(SCRIPT_CREATE_TABLE11);
			db.execSQL(SCRIPT_CREATE_TABLE12);
			db.execSQL(SCRIPT_CREATE_TABLE13);
			db.execSQL(SCRIPT_CREATE_TABLE14);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + "Audit_Category");
			db.execSQL("DROP TABLE IF EXISTS" + "Audit_Questions");
			db.execSQL("DROP TABLE IF EXISTS" + "Auditor_Schedules");
			db.execSQL("DROP TABLE IF EXISTS" + "Banner_Images");
			db.execSQL("DROP TABLE IF EXISTS" + "Controls");
			db.execSQL("DROP TABLE IF EXISTS" + "districts");
			db.execSQL("DROP TABLE IF EXISTS" + "employees");
			db.execSQL("DROP TABLE IF EXISTS" + "logins");
			db.execSQL("DROP TABLE IF EXISTS" + "Processes");
			db.execSQL("DROP TABLE IF EXISTS" + "Responses");
			db.execSQL("DROP TABLE IF EXISTS" + "roles");
			db.execSQL("DROP TABLE IF EXISTS" + "states");
			db.execSQL("DROP TABLE IF EXISTS" + "talukas");
			db.execSQL("DROP TABLE IF EXISTS" + "villages");
			db.execSQL("DROP TABLE IF EXISTS" + "question_answers");
			onCreate(db);
		}
	}

}
