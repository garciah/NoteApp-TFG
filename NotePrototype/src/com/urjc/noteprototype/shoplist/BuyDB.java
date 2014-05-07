package com.urjc.noteprototype.shoplist;

import com.urjc.noteprototype.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BuyDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allBuys = { DatabaseHelper.getKeyIdbuy(),
			DatabaseHelper.getKeyTitle() };
	private String[] allElements = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyName(), DatabaseHelper.getKeyAccount(),
			DatabaseHelper.getKeyCheck() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public BuyDB(Context context) {
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
	 * Close the notes database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new buy using the title. If the buy is successfully created
	 * return the new rowId for that element, otherwise return a -1 to indicate
	 * failure.
	 * 
	 * @param title
	 *            the title of the pwd
	 * @return rowId or -1 if failed
	 */
	public long createBuy(String title) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		return database.insert(DatabaseHelper.getDatabaseTableBuy(), null,
				initialValues);
	}

	/**
	 * Delete buy
	 * 
	 * @param shopId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteBuy(long rowId) {
		database.execSQL("PRAGMA foreign_keys = ON");
		return database.delete(DatabaseHelper.getDatabaseTableBuy(),
				DatabaseHelper.getKeyIdbuy() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all list in the database
	 * 
	 * @return Cursor over all list
	 */
	public Cursor getCursorAllBuys() {
		return database.query(DatabaseHelper.getDatabaseTableBuy(), allBuys,
				null, null, null, null, null);
	}

	/**
	 * Update buy using the details provided.
	 * 
	 * @param rowId
	 * @param title
	 * @return true if the element was successfully updated, false otherwise
	 */
	public boolean updateBuy(long buyId, String title) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		return database.update(DatabaseHelper.getDatabaseTableBuy(), args,
				DatabaseHelper.getKeyIdbuy() + "=" + buyId, null) > 0;
	}

	/*****************************************/
	/**
	 * Create a new buy element using the name. If the buy is successfully
	 * created return the new rowId for that element, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param name
	 *            the title of the pwd
	 * @param account
	 *            number of elements
	 * @param check
	 *            check on/off
	 * @param idbuy
	 *            id of buy associated
	 * @return rowId or -1 if failed
	 */
	public long createBuyElement(String nameElement, int account, int check,
			long idBuy) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyName(), nameElement);
		initialValues.put(DatabaseHelper.getKeyAccount(), account);
		initialValues.put(DatabaseHelper.getKeyCheck(), check);
		initialValues.put(DatabaseHelper.getKeyTitlebuy(), idBuy);
		return database.insert(DatabaseHelper.getDatabaseTableElemsBuy(), null,
				initialValues);
	}

	/**
	 * Delete element
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteElement(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTableElemsBuy(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all list in the database
	 * 
	 * @param idBuy
	 *            id associated to buy of elements
	 * @return Cursor over all list
	 */
	public Cursor getCursorElements(long idBuy) {
		String condition = "" + DatabaseHelper.getKeyTitlebuy() + "=?";
		String[] args = new String[] { String.valueOf(idBuy) };
		return database.query(DatabaseHelper.getDatabaseTableElemsBuy(),
				allElements, condition, args, null, null, null);
	}

	/**
	 * Update element using the details provided.
	 * 
	 * @param rowId
	 * @param name
	 * @param account
	 * @param check
	 * @return true if the element was successfully updated, false otherwise
	 */
	public boolean updateElement(long rowId, String name, int account, int check) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyName(), name);
		args.put(DatabaseHelper.getKeyAccount(), account);
		args.put(DatabaseHelper.getKeyCheck(), check);
		return database.update(DatabaseHelper.getDatabaseTableElemsBuy(), args,
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

}
