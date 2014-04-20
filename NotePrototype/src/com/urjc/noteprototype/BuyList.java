package com.urjc.noteprototype;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;

public class BuyList extends ListActivity {

	private static final int MENU_OP1 = 1;
	private static final int MENU_OP2 = 2;
	private static final int MENU_OP3 = 3;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_EXPORT = 2;
	private BuyDB database;
	private Cursor cursor;
	private File f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_list);
		registerForContextMenu(getListView());
		database = new BuyDB(this);
		fillData();
	}

	private void fillData() {
		database.open();
		cursor = database.getCursorAllBuys();
		String[] from = new String[] { DatabaseHelper.getKeyTitle() };
		int[] to = new int[] { R.id.titleBuy };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.row_buy_list, cursor, from, to, 0);
		setListAdapter(adapter);
		database.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		database.open();
		Cursor c = cursor;
		c.moveToPosition(position);
		long idAux = Integer.parseInt(c.getString(0));
		database.close();
		Intent i = new Intent(this, BuyElemList.class);
		i.putExtra(DatabaseHelper.getKeyTitlebuy(), idAux);
		startActivity(i);
		finish();
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
			Intent i = new Intent(this, EditBuy.class);
			startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, MENU_OP2, Menu.NONE, R.string.menuList2);
		menu.add(Menu.NONE, MENU_OP1, Menu.NONE, R.string.menuList1);
		menu.add(Menu.NONE, MENU_OP3, Menu.NONE, R.string.exportFile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_OP1:
			database.open();
			database.deleteBuy(info.id);
			database.close();
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelElem, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		case MENU_OP2:
			database.open();
			Cursor c = cursor;
			c.moveToPosition(info.position);
			Intent i = new Intent(this, EditBuy.class);
			i.putExtra(DatabaseHelper.getKeyIdbuy(), info.id);
			i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
			database.close();
			startActivityForResult(i, ACTIVITY_EDIT);
		case MENU_OP3:
			c = cursor;
			c.moveToPosition(info.position);
			try {
				String t = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle()));
				database.open();
				Cursor cursor = database.getCursorElements(info.id);
				List<ElemBuyList> items = new ArrayList<ElemBuyList>();
				if (cursor.moveToFirst()) {
					do {
						long id = cursor.getLong(0);
						String n = cursor.getString(1);
						int a = cursor.getInt(2);
						int ch = cursor.getInt(3);
						ElemBuyList elem = new ElemBuyList(id, n, ch, a, info.id);
						items.add(elem);
					} while (cursor.moveToNext());
				}
				database.close();
				String file = HandlerFileImportExport.writeFileShopping(t, items, getString(R.string.routeExportFile));
				if (file != "") {
					f = new File(file);
					Uri path = Uri.fromFile(f);
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File NoteForHome");
					shareIntent.putExtra(Intent.EXTRA_STREAM, path);
					shareIntent.setType("application/octet-stream");
					startActivityForResult(Intent.createChooser(shareIntent, "ShoppingList"),ACTIVITY_EXPORT);
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
			finish();
			break;
		case ACTIVITY_EDIT:
			fillData();
			break;
		case ACTIVITY_EXPORT:
			//f.delete();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(this, MenuApp.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
