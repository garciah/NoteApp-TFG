package com.urjc.noteprototype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
	private static final int MENU_OP2 = 2;
	private static final int MENU_OP3 = 3;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_EXPORT = 2;
	private RecipeDB database;
	private Cursor cursor;
	private File f;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		registerForContextMenu(getListView());
		database = new RecipeDB(this);
		fillData();
	}

	private void fillData() {
		database.open();
		cursor = database.getCursorAllRecipes();
		String[] from = new String[] { DatabaseHelper.getKeyTitle() };
		int[] to = new int[] { R.id.title };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.element_file_recipe, cursor, from, to, 0);
		setListAdapter(adapter);
		database.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		database.open();
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
		database.close();
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
		menu.add(Menu.NONE, MENU_OP2, Menu.NONE, R.string.shareFile);
		menu.add(Menu.NONE, MENU_OP3, Menu.NONE, R.string.exportFile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Cursor c;
		String t, ing, inst, pimg,nimg;
		String[] aux;
		switch (item.getItemId()) {
		case MENU_OP1:
			database.open();
			c = database.getRecipeForId(info.id);
			c.moveToFirst();
			String routeImg = c.getString(4);
			File file = new File(routeImg);
			file.delete();
			database.deleteRecipe(info.id);
			database.close();
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelRecipe, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		case MENU_OP3:
			database.open();
			c = database.getRecipeForId(info.id);
			c.moveToFirst();
			t = c.getString(1)+"Temp";
			ing =c.getString(2); 
			inst =c.getString(3);
			pimg = c.getString(4);
			aux = pimg.split("/");
			nimg = aux[aux.length-1];
			database.close();
			try {
				HandlerFileImportExport.writeFileRecipe(t, ing, inst,nimg ,getString(R.string.routeExportFile));
				Toast.makeText(getApplicationContext(),
						R.string.fileCreateMsg, Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case MENU_OP2:	
			database.open();
			c = database.getRecipeForId(info.id);
			c.moveToFirst();
			t = c.getString(1)+"Temp";
			ing =c.getString(2); 
			inst =c.getString(3);
			pimg = c.getString(4);
			aux = pimg.split("/");
			nimg = aux[aux.length-1];
			database.close();
			try {
				String fi = HandlerFileImportExport.writeFileRecipe(t, ing, inst,nimg ,getString(R.string.routeExportFile));
				if (fi != "") {
					f = new File(fi);
					File imgFile = new  File(pimg);
					Uri path = Uri.fromFile(f);
					Uri pImg = Uri.fromFile(imgFile);
					ArrayList<Uri> files = new ArrayList<Uri>();
					files.add(path);
					files.add(pImg);
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
					shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File NoteForHome");
					shareIntent.putExtra(Intent.EXTRA_STREAM, files);
					shareIntent.setType("application/octet-stream");
					startActivityForResult(Intent.createChooser(shareIntent, "Recipe"),ACTIVITY_EXPORT);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
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
		case ACTIVITY_EXPORT:
			f.delete();
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
