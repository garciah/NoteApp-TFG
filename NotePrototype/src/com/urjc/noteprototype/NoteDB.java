package com.urjc.noteprototype;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NoteDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allCols = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyTitle(), DatabaseHelper.getKeyBody() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public NoteDB(Context context) {
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
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createNote(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		initialValues.put(DatabaseHelper.getKeyBody(), body);
		return database.insert(DatabaseHelper.getDatabaseTableNote(), null,
				initialValues);
	}

	/**
	 * Delete the note
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNote(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTableNote(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return list of all notes id in the database
	 * 
	 * @return list of notes id
	 */
	public List<Note> getAllNotes() {
		List<Note> notesId = new ArrayList<Note>();
		Cursor cursor = database.query(DatabaseHelper.getDatabaseTableNote(),
				allCols, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			notesId.add(cursorToNote(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return notesId;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor getCursorAllNotes() {
		return database.query(DatabaseHelper.getDatabaseTableNote(), allCols,
				null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchNote(long rowId) throws SQLException {
		Cursor cursor = database.query(DatabaseHelper.getDatabaseTableNote(),
				allCols, DatabaseHelper.getKeyRowid() + "=" + rowId, null,
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNote(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		args.put(DatabaseHelper.getKeyBody(), body);
		return database.update(DatabaseHelper.getDatabaseTableNote(), args,
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	private Note cursorToNote(Cursor cursor) {
		Note n = new Note();
		n.setId(Long.parseLong(cursor.getString(0)));
		n.setTitle(cursor.getString(1));
		n.setBody(cursor.getString(2));
		return n;
	}
}
