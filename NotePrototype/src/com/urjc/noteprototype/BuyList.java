package com.urjc.noteprototype;

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
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private BuyDB database;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_list);
		registerForContextMenu(getListView());
		database = new BuyDB(this);
		database.open();
		fillData();
	}

	private void fillData() {

		cursor = database.getCursorAllBuys();
		String[] from = new String[] { DatabaseHelper.getKeyTitle() };
		int[] to = new int[] { R.id.titleBuy };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.row_buy_list, cursor, from, to, 0);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = cursor;
		c.moveToPosition(position);
		long idAux = Integer.parseInt(cursor.getString(0));
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
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_OP1:
			database.deleteBuy(info.id);
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelElem, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		case MENU_OP2:
			Cursor c = cursor;
			c.moveToPosition(info.position);
			Intent i = new Intent(this, EditBuy.class);
			i.putExtra(DatabaseHelper.getKeyIdbuy(), info.id);
			i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
			startActivityForResult(i, ACTIVITY_EDIT);
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