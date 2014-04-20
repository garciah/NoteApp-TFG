package com.urjc.noteprototype;

import java.io.File;
import java.io.IOException;
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

public class NoteList extends ListActivity {

	private static final int MENU_OP1 = 1;
	private static final int MENU_OP2 = 2;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_EXPORT = 2;
	private NoteDB database;
	private Cursor cursor;
	private File f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_list);
		registerForContextMenu(getListView());
		database = new NoteDB(this);
		fillData();
	}

	private void fillData() {
		database.open();
		cursor = database.getCursorAllNotes();
		String[] from = new String[] { DatabaseHelper.getKeyTitle(),
				DatabaseHelper.getKeyBody() };
		int[] to = new int[] { R.id.title, R.id.amountText };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.element_file_note, cursor, from, to, 0);
		setListAdapter(adapter);
		database.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		database.open();
		Cursor c = cursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, EditNote.class);
		i.putExtra(DatabaseHelper.getKeyRowid(), id);
		i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
		i.putExtra(DatabaseHelper.getKeyBody(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyBody())));
		database.open();
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
			Intent i = new Intent(this, EditNote.class);
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
		menu.add(Menu.NONE, MENU_OP2, Menu.NONE, R.string.exportFile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_OP1:
			database.open();
			database.deleteNote(info.id);
			database.close();
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelete, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		case MENU_OP2:
			database.open();
			Cursor c = database.getNoteForId(info.id);
			c.moveToFirst();
			String t = c.getString(0);
			String b = c.getString(1);
			database.close();
			try {
				String file = HandlerFileImportExport.writeFileNote(t, b,
						getString(R.string.routeExportFile));
				if (file != "") {
					f = new File(file);
					Uri path = Uri.fromFile(f);
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File NoteForHome");
					shareIntent.putExtra(Intent.EXTRA_STREAM, path);
					shareIntent.setType("application/octet-stream");
					startActivityForResult(Intent.createChooser(shareIntent, "Note"),ACTIVITY_EXPORT);
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
			Intent i = new Intent(this, NoteList.class);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
