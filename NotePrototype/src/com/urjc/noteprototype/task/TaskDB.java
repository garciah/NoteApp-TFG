package com.urjc.noteprototype.task;

import com.urjc.noteprototype.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allTask = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyTitle(), DatabaseHelper.getKeyCheck() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public TaskDB(Context context) {
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
	 * Create a new task using the title. If the buy is successfully created
	 * return the new rowId for that element, otherwise return a -1 to indicate
	 * failure.
	 * 
	 * @param title
	 *            the title of the pwd
	 * @return rowId or -1 if failed
	 */
	public long createTask(String title, int check) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		initialValues.put(DatabaseHelper.getKeyCheck(), check);
		return database.insert(DatabaseHelper.getDatabaseTableTask(), null,
				initialValues);
	}

	/**
	 * Delete task
	 * 
	 * @param taskId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTask(long rowId) {
		database.execSQL("PRAGMA foreign_keys = ON");
		return database.delete(DatabaseHelper.getDatabaseTableTask(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all task in the database
	 * 
	 * @return Cursor over all task
	 */
	public Cursor getCursorAllTask() {
		return database.query(DatabaseHelper.getDatabaseTableTask(), allTask,
				null, null, null, null, null);
	}

	/**
	 * Update task using the details provided.
	 * 
	 * @param rowId
	 * @param title
	 * @param check
	 * @return true if the element was successfully updated, false otherwise
	 */
	public boolean updateTask(long buyId, String title, int check) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		args.put(DatabaseHelper.getKeyCheck(), check);
		return database.update(DatabaseHelper.getDatabaseTableTask(), args,
				DatabaseHelper.getKeyRowid() + "=" + buyId, null) > 0;
	}

}