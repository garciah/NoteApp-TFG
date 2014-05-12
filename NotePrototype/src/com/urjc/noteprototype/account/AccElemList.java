package com.urjc.noteprototype.account;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class AccElemList extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private AccountDB database;
	private Cursor cursor;
	private long idA;
	private String titleA;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new AccountDB(this);
		setContentView(R.layout.elem_acc_list);
		Bundle extras = getIntent().getExtras();
		idA = extras.getLong(DatabaseHelper.getKeyAcc());
		titleA = extras.getString(DatabaseHelper.getKeyTitle());
		fillData(idA);
	}

	private void fillData(long idAux) {
		database.open();
		cursor = database.getCursorElements(idAux);
		List<AccountElem> items = new ArrayList<AccountElem>();
		if (cursor.moveToFirst()) {
			do {
				long id = cursor.getLong(0);
				String tag = cursor.getString(1);
				float num = cursor.getFloat(2);
				AccountElem elem = new AccountElem(id, tag, num, idAux);
				items.add(elem);
			} while (cursor.moveToNext());
		}
		database.close();
		AccElemAdapter adapter = new AccElemAdapter(this, items);
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
			Intent i = new Intent(this, EditElementAcc.class);
			i.putExtra(DatabaseHelper.getKeyAcc(), idA);
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
			fillData(idA);
			break;
		case ACTIVITY_EDIT:
			fillData(idA);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			Intent i = new Intent(this, TableAccount.class);
			i.putExtra(DatabaseHelper.getKeyAcc(), idA);
			i.putExtra(DatabaseHelper.getKeyTitle(), titleA);
			startActivity(i);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
