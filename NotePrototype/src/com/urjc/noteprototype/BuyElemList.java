package com.urjc.noteprototype;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class BuyElemList extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private BuyDB database;
	private Cursor cursor;
	private long idB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new BuyDB(this);
		setContentView(R.layout.elem_buy_list);
		Bundle extras = getIntent().getExtras();
		idB = extras.getLong(DatabaseHelper.getKeyTitlebuy());
		fillData(idB);
	}

	private void fillData(long idAux) {
		database.open();
		cursor = database.getCursorElements(idAux);
		ArrayList<ElemBuyList> items = new ArrayList<ElemBuyList>();
		if (cursor.moveToFirst()) {
			do {
				long i = cursor.getLong(0);
				String n = cursor.getString(1);
				int a = cursor.getInt(2);
				int c = cursor.getInt(3);
				ElemBuyList elem = new ElemBuyList(i, n, c, a, idB);
				items.add(elem);
			} while (cursor.moveToNext());
		}
		database.close();
		BuyElemAdapter adapter = new BuyElemAdapter(this, items);
		setListAdapter(adapter);
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
			Intent i = new Intent(this, EditElement.class);
			i.putExtra(DatabaseHelper.getKeyTitlebuy(), idB);
			startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case ACTIVITY_CREATE:
			fillData(idB);
			break;
		case ACTIVITY_EDIT:
			fillData(idB);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			Intent i = new Intent(this, BuyList.class);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
