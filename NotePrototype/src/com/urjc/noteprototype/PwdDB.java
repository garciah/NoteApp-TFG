package com.urjc.noteprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PwdDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allCols = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyTitle(), DatabaseHelper.getKeyPwd(),
			DatabaseHelper.getKeyUser(), DatabaseHelper.getKeyUrl() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public PwdDB(Context context) {
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
	 * Create a new password using the title, password and user. If the pwd is
	 * successfully created return the new rowId for that pwd, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the pwd
	 * @param password
	 * @param user
	 * @param url
	 * @return rowId or -1 if failed
	 */
	public long createPwd(String title, String pwd, String user, String url) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		initialValues.put(DatabaseHelper.getKeyPwd(), pwd);
		initialValues.put(DatabaseHelper.getKeyUrl(), url);
		initialValues.put(DatabaseHelper.getKeyUser(), user);
		return database.insert(DatabaseHelper.getDatabaseTablePwd(), null,
				initialValues);
	}

	/**
	 * Delete pasword
	 * 
	 * @param rowId
	 *            id of pwd to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePwd(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTablePwd(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all password in the database
	 * 
	 * @return Cursor over all pwds
	 */
	public Cursor getCursorAllPwds() {
		return database.query(DatabaseHelper.getDatabaseTablePwd(), allCols,
				null, null, null, null, null);
	}

	/**
	 * Update password using the details provided. The pwd to be updated is
	 * specified using the rowId, and it is altered to use the title and
	 * characters values passed in
	 * 
	 * @param rowId
	 *            id of pwd to update
	 * @param title
	 *            value to set note title to
	 * @param pwd
	 *            value to set pwd body to
	 * @param user
	 *            value to set user text to
	 * @param url
	 *            value to set user text to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updatePwd(long rowId, String title, String pwd, String user,
			String url) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		args.put(DatabaseHelper.getKeyPwd(), pwd);
		args.put(DatabaseHelper.getKeyUser(), user);
		args.put(DatabaseHelper.getKeyUrl(), url);
		return database.update(DatabaseHelper.getDatabaseTablePwd(), args,
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

}
