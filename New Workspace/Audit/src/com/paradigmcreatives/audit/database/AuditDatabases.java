package com.paradigmcreatives.audit.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AuditDatabases {
	public static final String MYDATABASE_NAME = "AUDIT";
	public static final String MYDATABASE_TABLE = "QUESTION_ANSWER";
	public static final String MYDATABASE_TABLE1 = "PICTURE_BANNER";
	public static final String MYDATABASE_TABLE2 = "LOGIN";
	public static final String MYDATABASE_TABLE3 = "Questions";
	public static final String MYDATABASE_TABLE4 = "Audit_CONFIGURATION";
	public static final String KEY_CONTENT1 = "question_id";
	public static final String KEY_CONTENT2 = "response_type";
	public static final String KEY_CONTENT7 = "photo";
	public static final String KEY_CONTENT6 = "remark";
	public static final String KEY_CONTENT3 = "operator_names";
	public static final String KEY_CONTENT4 = "superviser_names";
	public static final String KEY_CONTENT5 = "technisation_names";
	public static final String KEY_CONTENT9 = "emp_id";
	public static final String KEY_CONTENT10 = "role_id";
	public static final String KEY_CONTENT11 = "login_id";
	public static final String KEY_CONTENT12 = "password";
	public static final String KEY_CONTENT13 = "question_name";
	public static final String KEY_CONTENT14 = "response_type";
	public static final String KEY_CONTENT15 = "response_values";
	public static final String KEY_CONTENT16 = "response_count";
	public static final String KEY_CONTENT17 = "auditor_number";
	public static final String KEY_CONTENT18 = "date";
	public static final String KEY_CONTENT19 = "location";
	public static final String KEY_CONTENT20 = "info";
	public static final String KEY_CONTENT21 = "sync";
	public static final String KEY_ID4 = "_id";
	public static final String KEY_ID3 = "_id";
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT8 = "banner_picture";
	public static final String KEY_ID1 = "_id";
	public static final String KEY_ID2 = "_id";
	private static final String TAG = "EVENTS";
	public static final int MYDATABASE_VERSION = 1;

	// create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_CONTENT1
			+ " text not null, " + KEY_CONTENT2 + " text not null, "
			+ KEY_CONTENT3 + " text not null, " + KEY_CONTENT4
			+ " text not null, " + KEY_CONTENT5 + " text not null, "
			+ KEY_CONTENT6 + " text, " + KEY_CONTENT7 + " blob);";

	private static final String AUDIT_BANNER_PICTURE_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE1
			+ " ("
			+ KEY_ID1
			+ " integer primary key autoincrement, " + KEY_CONTENT8 + " blob);";
	private static final String LOGIN_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE2 + " (" + KEY_ID2
			+ " integer primary key autoincrement, " + KEY_CONTENT9
			+ " text not null, " + KEY_CONTENT10 + " text not null, "
			+ KEY_CONTENT11 + " text not null, " + KEY_CONTENT12
			+ " text not null);";
	private static final String QUESTION_RESPONSE_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE3
			+ " ("
			+ KEY_ID3 + " integer primary key autoincrement, "
			+ KEY_CONTENT13	+ " text not null, "
			+ KEY_CONTENT14 + " text not null, "
			+ KEY_CONTENT15 + " text not null, "
			+ KEY_CONTENT16 + " text not null);";
	private static final String AUDIT_CONFIGUARATION_CREATE_DATABASE = "create table "
		+ MYDATABASE_TABLE4
		+ " ("
		+ KEY_ID4 + " integer primary key autoincrement, "
		+ KEY_CONTENT17	+ " text not null, "
		+ KEY_CONTENT18 + " text not null, "
		+ KEY_CONTENT19 + " text not null, "
		+ KEY_CONTENT20 + " text not null, "
		+ KEY_CONTENT21 + " text not null);";
	
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	public AuditDatabases(Context c) {
		context = c;
	}

	public AuditDatabases openToRead() throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public AuditDatabases openToWrite() throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {

		sqLiteHelper.close();
	}

	public long insert(String content1, String content2, String content3,
			String content4, String content5, String content6, byte[] content7) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_CONTENT1, content1);
		contentValues.put(KEY_CONTENT2, content2);
		contentValues.put(KEY_CONTENT3, content3);
		contentValues.put(KEY_CONTENT4, content4);
		contentValues.put(KEY_CONTENT5, content5);
		contentValues.put(KEY_CONTENT6, content6);
		contentValues.put(KEY_CONTENT7, content7);

		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
	}

	public long banner_picture_insert(byte[] content1) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_CONTENT8, content1);

		return sqLiteDatabase.insert(MYDATABASE_TABLE1, null, contentValues);
	}

	public long loginInsert(ArrayList<KeyValues> data) {
		long returnVal = -1;
		for (int i = 0; i < data.size(); i++) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_CONTENT9, data.get(i).getEmp_id());
			contentValues.put(KEY_CONTENT10, data.get(i).getRole_id());
			contentValues.put(KEY_CONTENT11, data.get(i).getLogin_id());
			contentValues.put(KEY_CONTENT12, data.get(i).getPassword());
			returnVal = sqLiteDatabase.insert(MYDATABASE_TABLE2, null,
					contentValues);
		}
		return returnVal;
	}
	public long questionsInsert(ArrayList<KeyValues> data) {
		long returnVal = -1;
		for (int i = 0; i < data.size(); i++) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_CONTENT13, data.get(i).getQuestion_name());
			contentValues.put(KEY_CONTENT14, data.get(i).getResponse_type());
			contentValues.put(KEY_CONTENT15, data.get(i).getResponse_values());
			contentValues.put(KEY_CONTENT16, data.get(i).getResponse_count());
			returnVal = sqLiteDatabase.insert(MYDATABASE_TABLE3, null,
					contentValues);
		}
		return returnVal;
	}
	public long configurationInsert(ArrayList<KeyValues> data) {
		long returnVal = -1;
		for (int i = 0; i < data.size(); i++) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(KEY_CONTENT17, data.get(i).getSerial_number());
			contentValues.put(KEY_CONTENT18, data.get(i).getDate());
			contentValues.put(KEY_CONTENT19, data.get(i).getLocation());
			contentValues.put(KEY_CONTENT20, data.get(i).getInfo());
			contentValues.put(KEY_CONTENT21, data.get(i).getSync());

			returnVal = sqLiteDatabase.insert(MYDATABASE_TABLE4, null,
					contentValues);
		}
		return returnVal;
	}

	/**
	 * method to delete all contents in List
	 */
	/*
	 * public int deleteAll(){ return sqLiteDatabase.delete(MYDATABASE_TABLE,
	 * null, null); }
	 */

	/**
	 * Method to delete particular contact
	 * 
	 * @param id
	 * @return int
	 */
	/*
	 * public int delete(int id) {
	 * 
	 * return sqLiteDatabase.delete(MYDATABASE_TABLE, KEY_ID + "=" + id, null);
	 * 
	 * }
	 */
	/**
	 * Method to update
	 * 
	 * @param id
	 * @param v1
	 * @param v2
	 */
	/*
	 * public void update_byID(int id, String v1, String v2){ ContentValues
	 * values = new ContentValues(); values.put(KEY_CONTENT1, v1);
	 * values.put(KEY_CONTENT2, v2); sqLiteDatabase.update(MYDATABASE_TABLE,
	 * values, KEY_ID+"="+id, null); }
	 */

	public Cursor queueAll() {

		String[] columns = new String[] { KEY_ID, KEY_CONTENT1, KEY_CONTENT2,
				KEY_CONTENT3, KEY_CONTENT4, KEY_CONTENT5, KEY_CONTENT6,
				KEY_CONTENT7 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null,
				null, null, null, null);

		return cursor;
	}
	public Cursor retriveQuestionData() {

		String[] columns = new String[] { KEY_ID3, KEY_CONTENT13, KEY_CONTENT14,
				KEY_CONTENT15, KEY_CONTENT16 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE3, columns, null,
				null, null, null, null);

		return cursor;
	}
	public Cursor retriveConfiguarationData() {

		String[] columns = new String[] { KEY_ID4, KEY_CONTENT17, KEY_CONTENT18,
				KEY_CONTENT19, KEY_CONTENT20,KEY_CONTENT21};
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE4, columns, null,
				null, null, null, null);

		return cursor;
	}

	public Cursor retriveLoginData() {

		String[] columns = new String[] { KEY_ID2, KEY_CONTENT9, KEY_CONTENT10,
				KEY_CONTENT11, KEY_CONTENT12 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE2, columns, null,
				null, null, null, null);

		return cursor;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {

			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(LOGIN_CREATE_DATABASE);
				db.execSQL(AUDIT_BANNER_PICTURE_CREATE_DATABASE);
				db.execSQL(SCRIPT_CREATE_DATABASE);
				db.execSQL(QUESTION_RESPONSE_CREATE_DATABASE);
				db.execSQL(AUDIT_CONFIGUARATION_CREATE_DATABASE);				
				Log.d(TAG, "Data base created");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}

}
