package com.urjc.noteprototype.account;

import com.urjc.noteprototype.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AccountDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allAccs = { DatabaseHelper.getKeyIdacc(),
			DatabaseHelper.getKeyTitle() };
	private String[] allTags = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyTag(), DatabaseHelper.getKeyNum(),
			DatabaseHelper.getKeyAcc() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public AccountDB(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Open the notes database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close the pwds database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new account using the title. If the element is successfully
	 * created return the new rowId for that element, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param title
	 * @return rowId or -1 if failed
	 */
	public long createAccount(String title) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		return database.insert(DatabaseHelper.getDatabaseTableAccount(), null,
				initialValues);
	}

	/**
	 * Delete account
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteAccount(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTableAccount(),
				DatabaseHelper.getKeyIdacc() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all list in the database
	 * 
	 * @return Cursor over all list
	 */
	public Cursor getCursorAllAccs() {
		return database.query(DatabaseHelper.getDatabaseTableAccount(),
				allAccs, null, null, null, null, null);
	}

	/**
	 * Update account using the details provided.
	 * 
	 * @param rowId
	 * @param title
	 * @return true if the element was successfully updated, false otherwise
	 */
	public boolean updateAccount(long accId, String title) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		return database.update(DatabaseHelper.getDatabaseTableAccount(), args,
				DatabaseHelper.getKeyIdacc() + "=" + accId, null) > 0;
	}

	/*****************************************/
	/**
	 * Create a new buy element using the name. If the buy is successfully
	 * created return the new rowId for that element, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param name
	 * @param tag
	 * @param num
	 * @param idAcc
	 * @return rowId or -1 if failed
	 */
	public long createAccountElement(String tag, float number, long idAcc) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTag(), tag);
		initialValues.put(DatabaseHelper.getKeyNum(), number);
		initialValues.put(DatabaseHelper.getKeyAcc(), idAcc);
		return database.insert(DatabaseHelper.getDatabaseTableElemsAccount(),
				null, initialValues);
	}

	/**
	 * Delete element
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteElement(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTableElemsAccount(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all list in the database
	 * 
	 * @param idAcc
	 * @return Cursor over all list
	 */
	public Cursor getCursorElements(long idAcc) {
		String condition = "" + DatabaseHelper.getKeyAcc() + "=?";
		String[] args = new String[] { String.valueOf(idAcc) };
		return database.query(DatabaseHelper.getDatabaseTableElemsAccount(),
				allTags, condition, args, null, null, null);
	}

	/**
	 * Update element using the details provided.
	 * 
	 * @param rowId
	 * @param tag
	 * @param num
	 * @return true if the element was successfully updated, false otherwise
	 */
	public boolean updateElement(long rowId, String tag, float num) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTag(), tag);
		args.put(DatabaseHelper.getKeyNum(), num);
		return database.update(DatabaseHelper.getDatabaseTableElemsAccount(),
				args, DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

}
