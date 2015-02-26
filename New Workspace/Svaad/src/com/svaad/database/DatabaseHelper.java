package com.svaad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	public static final String DATABASE_NAME = "svaad";

	// Table Names
	public static final String TABLE_PROFILE = "profile";
	public static final String TABLE_SOCIALMENU = "socailmenu";
	public static final String TABLE_BRANCH_MENU = "branchmenu";
	public static final String TABLE_BRANCHIDS = "branchids";
	public static final String TABLE_RESHISTORY = "reshistory";

	// Profile Table - column names
	public static final String PROFILE_USERNAME = "userName";
	public static final String PROFILE_USEREMAIL = "userEmail";
	public static final String PROFILE_USERPIC = "UserPic";
	public static final String PROFILE_USEROBJECTID = "userObjectId";
	public static final String PROFILE_USERFBID = "userFbId";
	public static final String PROFILE_USERUNAME = "userUname";
	public static final String PROFILE_USERROLE = "userRole";

	// SocailMenu Table -column names
	public static final String SOCIALMENU_SNO = "sNo";
	public static final String SOCIALMENU_OBJECTID = "objectId";
	public static final String SOCIALMENU_USERID = "userId";
	public static final String SOCIALMENU_UNAME = "uname";
	public static final String SOCIALMENU_USERPIC = "userPic";
	public static final String SOCIALMENU_DISHPHOTO = "dishPhoto";
	public static final String SOCIALMENU_DISHPIC = "dishPic";
	public static final String SOCIALMENU_COMMENTTEXT = "commentText";
	public static final String SOCIALMENU_DISHNAME = "dishName";
	public static final String SOCIALMENU_BRANCHDISHID = "branchDishId";
	public static final String SOCIALMENU_BRANCHID = "branchId";
	public static final String SOCIALMENU_DISHID = "dishId";
	public static final String SOCIALMENU_CITYID = "cityId";
	public static final String SOCIALMENU_POINTLATITUDE = "latitude";
	public static final String SOCIALMENU_POINTLONGITUDE = "longitude";
	public static final String SOCIALMENU_LOCATIONID = "locationId";
	public static final String SOCIALMENU_PLACEID = "placeId";
	public static final String SOCIALMENU_REGULARPRICE = "regularPrice";
	public static final String SOCIALMENU_ONETAG = "oneTag";
	public static final String SOCIALMENU_TWOTAG = "twoTag";
	public static final String SOCIALMENU_THREETAG = "threeTag";
	public static final String SOCIALMENU_FOURTAG = "fourTag";
	public static final String SOCIALMENU_DISHTAG = "dishTag";
	public static final String SOCIALMENU_CREATEDAT = "createdAt";
	public static final String SOCIALMENU_ISBOOKMARK = "isBookmark";

	// BRANCH_MENU Table - column names
	public static final String KEY_BRANCH_MENU_BRANCHID = "branchId";
	public static final String KEY_BRANCH_MENU_BRANCHNAME = "branchName";
	public static final String KEY_BRANCH_MENU_OBJECTID = "objectId";
	public static final String KEY_BRANCH_MENU_DISHID = "dishId";
	public static final String KEY_BRANCH_MENU_DISHNAME = "dishName";
	public static final String KEY_BRANCH_MENU_DISHDESC = "dishDesc";
	public static final String KEY_BRANCH_MENU_DISHTAGS = "dishTags";
	public static final String KEY_BRANCH_MENU_DISHPIC = "dishPic";
	public static final String KEY_BRANCH_MENU_NONVEG = "nonVeg";
	public static final String KEY_BRANCH_MENU_PUBLISH = "publish";
	public static final String KEY_BRANCH_MENU_PRICE1 = "price1";
	public static final String KEY_BRANCH_MENU_PRICE2 = "price2";
	public static final String KEY_BRANCH_MENU_PRICE3 = "price3";
	public static final String KEY_BRANCH_MENU_PRICE4 = "price4";
	public static final String KEY_BRANCH_MENU_PRICE5 = "price5";
	public static final String KEY_BRANCH_MENU_PRICE6 = "price6";
	public static final String KEY_BRANCH_MENU_CATID = "catId";
	public static final String KEY_BRANCH_MENU_CATNAME = "catName";
	public static final String KEY_BRANCH_MENU_PRICENAME1 = "priceName1";
	public static final String KEY_BRANCH_MENU_PRICENAME2 = "priceName2";
	public static final String KEY_BRANCH_MENU_PRICENAME3 = "priceName3";
	public static final String KEY_BRANCH_MENU_PRICENAME4 = "priceName4";
	public static final String KEY_BRANCH_MENU_PRICENAME5 = "priceName5";
	public static final String KEY_BRANCH_MENU_PRICENAME6 = "priceName6";
	public static final String KEY_BRANCH_MENU_REGULAR = "regular";
	public static final String KEY_BRANCH_MENU_HOMEDELIVERY = "homeDeliveryPrice";
	public static final String KEY_BRANCH_MENU_HAPPYHOUR = "happyHour";
	public static final String KEY_BRANCH_MENU_LIKES = "likes";
	public static final String KEY_BRANCH_MENU_LOCATIONID = "locationID";
	public static final String KEY_BRANCH_MENU_CITYID = "cityID";
	public static final String KEY_BRANCH_MENU_POINT_LATITUDE = "latitude";
	public static final String KEY_BRANCH_MENU_POINT_LONGITUDE = "longitude";
	public static final String KEY_BRANCH_MENU_COMMENTS = "comments";
	public static final String KEY_BRANCH_MENU_ONETAG = "oneTag";
	public static final String KEY_BRANCH_MENU_TWOTAG = "twoTag";
	public static final String KEY_BRANCH_MENU_THREETAG = "threeTag";
	public static final String KEY_BRANCH_MENU_FOURTAG = "fourTag";
	public static final String KEY_BRANCH_MENU_LOCATIONNAME = "locationName";

	// BRANCHIDS Table - column names
	public static final String KEY_BRANCHIDS_BRANCH_ID = "branchId";
	public static final String KEY_BRANCHIDS_TIME = "time";
	public static final String KEY_BRANCHIDS_BOOKMARKS = "bookmark";

	// History for ResHistory Table - column names
	public static final String KEY_RES_BRANCHID = "branchId";
	public static final String KEY_RES_BRANCHNAME = "branchName";
	public static final String KEY_RES_RESNAME = "name";

	// Table Create Statements
	// Profile table create statement
	private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "
			+ TABLE_PROFILE + " (" + PROFILE_USERNAME + " TEXT ,"
			+ PROFILE_USEREMAIL + " TEXT," + PROFILE_USERPIC + " TEXT,"
			+ PROFILE_USEROBJECTID + " TEXT," + PROFILE_USERFBID + " TEXT,"
			+ PROFILE_USERUNAME + " TEXT," + PROFILE_USERROLE + " TEXT" + ")";

	// Socail menu table create statement
	public static final String CREATE_TABLE_SOCIALMENU = "CREATE TABLE "
			+ TABLE_SOCIALMENU + " (" + SOCIALMENU_SNO
			+ " INTEGER PRIMARY KEY AUTOINCREMENT ," + SOCIALMENU_OBJECTID
			+ " TEXT ," + SOCIALMENU_USERID + " TEXT," + SOCIALMENU_UNAME
			+ " TEXT," + SOCIALMENU_USERPIC + " TEXT," + SOCIALMENU_DISHPHOTO
			+ " TEXT,"

			+ SOCIALMENU_DISHPIC + " TEXT," + SOCIALMENU_COMMENTTEXT + " TEXT,"
			+ SOCIALMENU_DISHNAME + " TEXT," + SOCIALMENU_BRANCHDISHID
			+ " TEXT,"

			+ SOCIALMENU_BRANCHID + " TEXT," + SOCIALMENU_DISHID + " TEXT,"
			+ SOCIALMENU_CITYID + " TEXT," + SOCIALMENU_POINTLATITUDE
			+ " TEXT," + SOCIALMENU_POINTLONGITUDE + " TEXT,"
			+ SOCIALMENU_LOCATIONID + " TEXT," + SOCIALMENU_PLACEID + " TEXT,"
			+ SOCIALMENU_REGULARPRICE + " TEXT," + SOCIALMENU_ONETAG + " TEXT,"

			+ SOCIALMENU_TWOTAG + " TEXT," + SOCIALMENU_THREETAG + " TEXT,"
			+ SOCIALMENU_FOURTAG + " TEXT," + SOCIALMENU_DISHTAG + " TEXT,"
			+ SOCIALMENU_CREATEDAT + " TEXT," + SOCIALMENU_ISBOOKMARK + " TEXT"
			+ ")";

	// BranchMenu table create statement

	private static final String CREATE_TABLE_BRANCH_MENU = "CREATE TABLE "
			+ TABLE_BRANCH_MENU + " (" + KEY_BRANCH_MENU_OBJECTID
			+ " TEXT PRIMARY KEY ," + KEY_BRANCH_MENU_BRANCHID + " TEXT,"
			+ KEY_BRANCH_MENU_BRANCHNAME + " TEXT," + KEY_BRANCH_MENU_DISHID
			+ " TEXT," + KEY_BRANCH_MENU_DISHNAME + " TEXT,"
			+ KEY_BRANCH_MENU_DISHDESC + " TEXT," + KEY_BRANCH_MENU_DISHTAGS
			+ " TEXT," + KEY_BRANCH_MENU_PUBLISH + " TEXT,"
			+ KEY_BRANCH_MENU_NONVEG + " TEXT," + KEY_BRANCH_MENU_DISHPIC
			+ " TEXT," + KEY_BRANCH_MENU_PRICE1 + " INTEGER,"
			+ KEY_BRANCH_MENU_PRICE2 + " INTEGER," + KEY_BRANCH_MENU_PRICE3
			+ " INTEGER," + KEY_BRANCH_MENU_PRICE4 + " INTEGER,"
			+ KEY_BRANCH_MENU_PRICE5 + " INTEGER," + KEY_BRANCH_MENU_PRICE6
			+ " INTEGER," + KEY_BRANCH_MENU_CATID + " TEXT,"
			+ KEY_BRANCH_MENU_CATNAME + " TEXT," + KEY_BRANCH_MENU_PRICENAME1
			+ " TEXT," + KEY_BRANCH_MENU_PRICENAME2 + " TEXT,"
			+ KEY_BRANCH_MENU_PRICENAME3 + " TEXT,"
			+ KEY_BRANCH_MENU_PRICENAME4 + " TEXT,"
			+ KEY_BRANCH_MENU_PRICENAME5 + " TEXT,"
			+ KEY_BRANCH_MENU_PRICENAME6 + " TEXT," + KEY_BRANCH_MENU_HAPPYHOUR
			+ " TEXT," + KEY_BRANCH_MENU_HOMEDELIVERY + " TEXT,"
			+ KEY_BRANCH_MENU_REGULAR + " TEXT," + KEY_BRANCH_MENU_LIKES
			+ " INTEGER," + KEY_BRANCH_MENU_COMMENTS + " INTEGER,"
			+ KEY_BRANCH_MENU_POINT_LATITUDE + " TEXT,"
			+ KEY_BRANCH_MENU_POINT_LONGITUDE + " TEXT,"
			+ KEY_BRANCH_MENU_LOCATIONID + " TEXT," + KEY_BRANCH_MENU_CITYID
			+ " TEXT," + KEY_BRANCH_MENU_ONETAG + " INTEGER,"
			+ KEY_BRANCH_MENU_TWOTAG + " INTEGER," + KEY_BRANCH_MENU_THREETAG
			+ " INTEGER," + KEY_BRANCH_MENU_FOURTAG + " INTEGER,"
			+ KEY_BRANCH_MENU_LOCATIONNAME + " TEXT" + ")";

	// branchids table create statement
	private static final String CREATE_TABLE_BRANCHIDS = "CREATE TABLE "
			+ TABLE_BRANCHIDS + " (" + KEY_BRANCHIDS_BRANCH_ID + " TEXT ,"
			+ KEY_BRANCHIDS_TIME + " TEXT," + KEY_BRANCHIDS_BOOKMARKS + " TEXT"
			+ ")";

	// ResHistory table create statement
	private static final String CREATE_TABLE_RES_HISTORY = "CREATE TABLE "
			+ TABLE_RESHISTORY + " (" + KEY_RES_BRANCHID + " TEXT PRIMARY KEY ,"
			+ KEY_RES_BRANCHNAME + " TEXT," + KEY_RES_RESNAME + " TEXT"
			+ ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_PROFILE);
		db.execSQL(CREATE_TABLE_SOCIALMENU);
		db.execSQL(CREATE_TABLE_BRANCH_MENU);
		db.execSQL(CREATE_TABLE_BRANCHIDS);
		db.execSQL(CREATE_TABLE_RES_HISTORY);

		System.out.println("Create table:" + CREATE_TABLE_PROFILE);
		System.out.println("Create table:" + CREATE_TABLE_SOCIALMENU);
		System.out.println("Create table:" + CREATE_TABLE_BRANCH_MENU);
		System.out.println("Create table:" + CREATE_TABLE_BRANCHIDS);
		System.out.println("Create table:" + CREATE_TABLE_RES_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIALMENU);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH_MENU);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCHIDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESHISTORY);
		// create new tables
		onCreate(db);
	}
}
