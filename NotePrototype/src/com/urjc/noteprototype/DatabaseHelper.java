package com.urjc.noteprototype;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "NotesDbAdapter";
	private static final String DATABASE_NAME = "data.sqlite";
	private static final String DATABASE_TABLE_NOTE = "notes";
	private static final String DATABASE_TABLE_PWD = "passwords";
	private static final String DATABASE_TABLE_BUY = "buy_list";
	private static final String DATABASE_TABLE_ELEMS_BUY = "buy_elems";
	private static final String DATABASE_TABLE_TASK = "task";
	private static final String DATABASE_TABLE_RECIPE = "recipe";
	private static final String DATABASE_TABLE_ACCOUNT = "account_list";
	private static final String DATABASE_TABLE_ELEMS_ACCOUNT = "account_els";
	private static final int DATABASE_VERSION = 9;
	private static final String KEY_TITLE = "title";
	private static final String KEY_NAME = "name";
	private static final String KEY_BODY = "body";
	private static final String KEY_PWD = "password";
	private static final String KEY_USER = "user";
	private static final String KEY_URL = "url";
	private static final String KEY_ROWID = "_id";
	private static final String KEY_IDBUY = "_id";
	private static final String KEY_ACCOUNT = "account";
	private static final String KEY_IDACC = "_id";
	private static final String KEY_CHECK = "mark";
	private static final String KEY_TAG = "tag";
	private static final String KEY_NUM = "num";
	private static final String KEY_ACC = "acc";
	private static final String KEY_TITLEBUY = "titleBuy";
	private static final String KEY_INGREDIENTS = "ingredients";
	private static final String KEY_INSTRUCTIONS = "instructions";
	private static final String KEY_ROUTE = "route";

	/**
	 * Database creation sql statement
	 */

	private static final String DATABASE_CREATE_1 = "create table "
			+ DATABASE_TABLE_NOTE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_BODY + " text not null);";

	private static final String DATABASE_CREATE_2 = "create table "
			+ DATABASE_TABLE_PWD + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_PWD + " text not null, " + KEY_URL
			+ " text, " + KEY_USER + " text);";

	private static final String DATABASE_CREATE_3 = "create table "
			+ DATABASE_TABLE_BUY + " (" + KEY_IDBUY
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null);";

	private static final String DATABASE_CREATE_4 = "create table "
			+ DATABASE_TABLE_ELEMS_BUY + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_NAME
			+ " text not null, " + KEY_ACCOUNT + " integer, " + KEY_CHECK
			+ " integer, " + KEY_TITLEBUY + " integer, " + "FOREIGN KEY ("
			+ KEY_TITLEBUY + ") references " + DATABASE_TABLE_BUY + " ("
			+ KEY_IDBUY + ") ON DELETE CASCADE);";

	private static final String DATABASE_CREATE_5 = "create table "
			+ DATABASE_TABLE_TASK + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_CHECK + " integer);";

	private static final String DATABASE_CREATE_6 = "create table "
			+ DATABASE_TABLE_RECIPE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null, " + KEY_INGREDIENTS + " text, "
			+ KEY_INSTRUCTIONS + " text, " + KEY_ROUTE + " text);";

	private static final String DATABASE_CREATE_7 = "create table "
			+ DATABASE_TABLE_ACCOUNT + " (" + KEY_IDACC
			+ " integer primary key autoincrement, " + KEY_TITLE
			+ " text not null);";

	private static final String DATABASE_CREATE_8 = "create table "
			+ DATABASE_TABLE_ELEMS_ACCOUNT + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_TAG
			+ " text not null, " + KEY_NUM + " real, " + KEY_ACC + " integer, "
			+ "FOREIGN KEY (" + KEY_ACC + ") references "
			+ DATABASE_TABLE_ACCOUNT + " (" + KEY_IDACC
			+ ") ON DELETE CASCADE);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_1);
		db.execSQL(DATABASE_CREATE_2);
		db.execSQL(DATABASE_CREATE_3);
		db.execSQL(DATABASE_CREATE_4);
		db.execSQL(DATABASE_CREATE_5);
		db.execSQL(DATABASE_CREATE_6);
		db.execSQL(DATABASE_CREATE_7);
		db.execSQL(DATABASE_CREATE_8);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NOTE);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PWD);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BUY);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ELEMS_BUY);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TASK);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_RECIPE);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ACCOUNT);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ELEMS_ACCOUNT);
		onCreate(db);
	}

	public static String getDatabaseTableNote() {
		return DATABASE_TABLE_NOTE;
	}

	public static String getDatabaseTablePwd() {
		return DATABASE_TABLE_PWD;
	}

	public static String getDatabaseTableBuy() {
		return DATABASE_TABLE_BUY;
	}

	public static String getDatabaseTableElemsBuy() {
		return DATABASE_TABLE_ELEMS_BUY;
	}

	public static String getDatabaseTableTask() {
		return DATABASE_TABLE_TASK;
	}

	public static String getDatabaseTableRecipe() {
		return DATABASE_TABLE_RECIPE;
	}

	public static String getDatabaseTableAccount() {
		return DATABASE_TABLE_ACCOUNT;
	}

	public static String getDatabaseTableElemsAccount() {
		return DATABASE_TABLE_ELEMS_ACCOUNT;
	}

	public static int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	public static String getKeyTitle() {
		return KEY_TITLE;
	}

	public static String getKeyName() {
		return KEY_NAME;
	}

	public static String getKeyBody() {
		return KEY_BODY;
	}

	public static String getKeyPwd() {
		return KEY_PWD;
	}

	public static String getKeyUser() {
		return KEY_USER;
	}

	public static String getKeyUrl() {
		return KEY_URL;
	}

	public static String getKeyRowid() {
		return KEY_ROWID;
	}

	public static String getKeyIdbuy() {
		return KEY_IDBUY;
	}

	public static String getKeyAccount() {
		return KEY_ACCOUNT;
	}

	public static String getKeyCheck() {
		return KEY_CHECK;
	}

	public static String getKeyTitlebuy() {
		return KEY_TITLEBUY;
	}

	public static String getKeyIngredients() {
		return KEY_INGREDIENTS;
	}

	public static String getKeyInstructions() {
		return KEY_INSTRUCTIONS;
	}

	public static String getKeyRoute() {
		return KEY_ROUTE;
	}

	public static String getKeyTag() {
		return KEY_TAG;
	}

	public static String getKeyNum() {
		return KEY_NUM;
	}

	public static String getKeyIdacc() {
		return KEY_IDACC;
	}

	public static String getKeyAcc() {
		return KEY_ACC;
	}

}