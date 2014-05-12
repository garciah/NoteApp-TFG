package com.urjc.noteprototype.recipe;

import com.urjc.noteprototype.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RecipeDB {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allCols = { DatabaseHelper.getKeyRowid(),
			DatabaseHelper.getKeyTitle(), DatabaseHelper.getKeyIngredients(),
			DatabaseHelper.getKeyInstructions(), DatabaseHelper.getKeyRoute() };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public RecipeDB(Context context) {
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
	 * Create a new recipe using the title, ingredients, instructions (and
	 * image). If the recipe is successfully created return the new rowId for
	 * that recipe, otherwise return a -1 to indicate failure.
	 * 
	 * @param title
	 * @param ingredients
	 * @param instruction
	 * @param route
	 * @return rowId or -1 if failed
	 */
	public long createRecipe(String title, String ingredients,
			String instructions, String route) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseHelper.getKeyTitle(), title);
		initialValues.put(DatabaseHelper.getKeyIngredients(), ingredients);
		initialValues.put(DatabaseHelper.getKeyInstructions(), instructions);
		initialValues.put(DatabaseHelper.getKeyRoute(), route);
		return database.insert(DatabaseHelper.getDatabaseTableRecipe(), null,
				initialValues);
	}

	/**
	 * Delete recipe
	 * 
	 * @param rowId
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRecipe(long rowId) {
		return database.delete(DatabaseHelper.getDatabaseTableRecipe(),
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all recipe in the database
	 * 
	 * @return Cursor over all recipe
	 */
	public Cursor getCursorAllRecipes() {
		return database.query(DatabaseHelper.getDatabaseTableRecipe(), allCols,
				null, null, null, null, null);
	}

	/**
	 * Update recipe using the details provided.
	 * 
	 * @param rowId
	 * @param title
	 * @param pwd
	 * @param user
	 * @param url
	 * @return true if the recipe was successfully updated, false otherwise
	 */
	public boolean updateRecipe(long rowId, String title, String ingredients,
			String instructions, String route) {
		ContentValues args = new ContentValues();
		args.put(DatabaseHelper.getKeyTitle(), title);
		args.put(DatabaseHelper.getKeyIngredients(), ingredients);
		args.put(DatabaseHelper.getKeyInstructions(), instructions);
		args.put(DatabaseHelper.getKeyRoute(), route);
		return database.update(DatabaseHelper.getDatabaseTableRecipe(), args,
				DatabaseHelper.getKeyRowid() + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the recipe with id "id"
	 * 
	 * @param id
	 * @return Cursor over element with id indicated
	 */
	public Cursor getRecipeForId(long id) {
		String cond = "" + DatabaseHelper.getKeyRowid() + "=?";
		String[] args = new String[] { String.valueOf(id) };
		return database.query(DatabaseHelper.getDatabaseTableRecipe(), allCols,
				cond, args, null, null, null);
	}

}
