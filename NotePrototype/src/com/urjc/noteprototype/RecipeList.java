package com.urjc.noteprototype;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RecipeList extends ListActivity {

	private static final int MENU_OP1 = 1;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private RecipeDB database;
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		registerForContextMenu(getListView());
		database = new RecipeDB(this);
		database.open();
		fillData();
	}

	private void fillData() {

		cursor = database.getCursorAllRecipes();
		String[] from = new String[] { DatabaseHelper.getKeyTitle() };
		int[] to = new int[] { R.id.title };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.element_file_recipe, cursor, from, to, 0);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Cursor c = cursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, EditRecipe.class);
		i.putExtra(DatabaseHelper.getKeyRowid(), id);
		i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
		i.putExtra(DatabaseHelper.getKeyIngredients(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyIngredients())));
		i.putExtra(DatabaseHelper.getKeyInstructions(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyInstructions())));
		i.putExtra(DatabaseHelper.getKeyRoute(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyRoute())));
		i.putExtra("upd",true);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_new:
			Intent i = new Intent(this, EditRecipe.class);
			startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, MENU_OP1, Menu.NONE, R.string.menuList1);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_OP1:
			database.deleteRecipe(info.id);
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelRecipe, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case ACTIVITY_CREATE:
			fillData();
			break;
		case ACTIVITY_EDIT:
			fillData();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			Intent i = new Intent(this, MenuApp.class);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
