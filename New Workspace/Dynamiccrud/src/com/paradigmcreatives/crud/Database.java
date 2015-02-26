package com.paradigmcreatives.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class Database {

	public static final String MYDATABASE_NAME = "MY_DATABASE11";

	public static final String MYDATABASE_TABLE = "MY_TABLE1";

	public static final String KEY_CONTENT1 = "Content1";

	public static final String KEY_CONTENT2 = "Content2";

	public static final String KEY_ID = "_id";

	private static final String TAG = "EVENTS";

	public static final int MYDATABASE_VERSION = 1;

	// create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE = "create table " + MYDATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_CONTENT1 + " text not null, " + KEY_CONTENT2 + " text not null);";

	private SQLiteHelper sqLiteHelper;

	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public Database (Context c) {
		context = c;
	}

	public Database openToRead () throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public Database openToWrite () throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close () {

		sqLiteHelper.close();
	}

	/**
	 * Method to insert .
	 * 
	 * @param content1
	 * @param content2
	 * @return long
	 */
	public long insert (String content1, String content2) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_CONTENT1, content1);
		contentValues.put(KEY_CONTENT2, content2);
		return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
	}

	/**
	 * method to delete all contents in List
	 */
	public int deleteAll () {
		return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
	}

	/**
	 * Method to delete particular contact
	 * 
	 * @param id
	 * @return int
	 */
	public int delete (int id) {
   return sqLiteDatabase.delete(MYDATABASE_TABLE, KEY_ID + "="+id, null);
		//return sqLiteDatabase.delete(MYDATABASE_TABLE, KEY_ID + "=" + id, null);

	}

	/**
	 * Method to update
	 * 
	 * @param id
	 * @param v1
	 * @param v2
	 */
	public void update_byID (int id, String v1, String v2) {
		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT1, v1);
		values.put(KEY_CONTENT2, v2);
		sqLiteDatabase.update(MYDATABASE_TABLE, values, KEY_ID + "=" + id, null);
	}

	public Cursor queueAll () {

		String[] columns = new String[] { KEY_ID, KEY_CONTENT1, KEY_CONTENT2 };
		Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);

		return cursor;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper (Context context, String name, CursorFactory factory, int version) {

			super(context, name, factory, version);
		}

		@Override
		public void onCreate (SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SCRIPT_CREATE_DATABASE);
			Log.d(TAG, "Data base created");
		}

		@Override
		public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
	}

}
