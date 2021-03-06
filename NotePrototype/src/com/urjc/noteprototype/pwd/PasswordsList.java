package com.urjc.noteprototype.pwd;

import java.io.File;
import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.MenuApp;
import com.urjc.noteprototype.R;
import com.urjc.noteprototype.iefile.HandlerFileImportExport;
import com.urjc.noteprototype.iefile.WriteAsynTask;

public class PasswordsList extends ListActivity {

	private static final int MENU_OP1 = 1;
	private static final int MENU_OP2 = 2;
	private static final int MENU_OP3 = 3;
	private static final int MENU_OP4 = 4;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_EXPORT = 2;
	private PwdDB database;
	private Cursor cursor;
	private File f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwds_list);
		registerForContextMenu(getListView());
		database = new PwdDB(this);
		fillData();
	}

	private void fillData() {
		database.open();
		cursor = database.getCursorAllPwds();
		String[] from = new String[] { DatabaseHelper.getKeyTitle(),
				DatabaseHelper.getKeyUser() };
		int[] to = new int[] { R.id.title, R.id.amountText };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.element_file_pwd, cursor, from, to, 0);
		setListAdapter(adapter);
		database.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		database.open();
		Cursor c = cursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, ViewPwd.class);
		i.putExtra(DatabaseHelper.getKeyRowid(), id);
		i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
		i.putExtra(DatabaseHelper.getKeyUser(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyUser())));
		i.putExtra(DatabaseHelper.getKeyPwd(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyPwd())));
		i.putExtra(DatabaseHelper.getKeyUrl(), c.getString(c
				.getColumnIndexOrThrow(DatabaseHelper.getKeyUrl())));
		database.close();
		startActivity(i);
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
			Intent i = new Intent(this, EditPwd.class);
			startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.titleContextMenu);
		menu.add(Menu.NONE, MENU_OP2, Menu.NONE, R.string.menuList2);
		menu.add(Menu.NONE, MENU_OP1, Menu.NONE, R.string.menuList1);
		menu.add(Menu.NONE, MENU_OP3, Menu.NONE, R.string.shareFile);
		menu.add(Menu.NONE, MENU_OP4, Menu.NONE, R.string.exportFile);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Cursor c;
		String t, u, p, ur;
		switch (item.getItemId()) {
		case MENU_OP1:
			database.open();
			database.deletePwd(info.id);
			database.close();
			fillData();
			Toast toast1 = Toast.makeText(getApplicationContext(),
					R.string.msgDelPwd, Toast.LENGTH_SHORT);
			toast1.show();
			return true;
		case MENU_OP2:
			database.open();
			c = cursor;
			c.moveToPosition(info.position);
			Intent i = new Intent(this, EditPwd.class);
			i.putExtra(DatabaseHelper.getKeyRowid(), info.id);
			i.putExtra(DatabaseHelper.getKeyTitle(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyTitle())));
			i.putExtra(DatabaseHelper.getKeyUser(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyUser())));
			i.putExtra(DatabaseHelper.getKeyPwd(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyPwd())));
			i.putExtra(DatabaseHelper.getKeyUrl(), c.getString(c
					.getColumnIndexOrThrow(DatabaseHelper.getKeyUrl())));
			database.close();
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		case MENU_OP3:
			database.open();
			c = cursor;
			c.moveToPosition(info.position);
			t = c.getString(c.getColumnIndexOrThrow(DatabaseHelper
					.getKeyTitle()));
			u = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyUser()));
			p = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyPwd()));
			ur = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyUrl()));
			database.close();
			try {
				String file = HandlerFileImportExport.writeFilePwd(t, u, p, ur,
						getString(R.string.routeSharingFile));
				if (file != "") {
					f = new File(file);
					Uri path = Uri.fromFile(f);
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_TEXT,
							"Sharing File NoteForHome");
					shareIntent.putExtra(Intent.EXTRA_STREAM, path);
					shareIntent.setType("application/octet-stream");
					startActivityForResult(
							Intent.createChooser(shareIntent, "Pwd"),
							ACTIVITY_EXPORT);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case MENU_OP4:
			database.open();
			c = cursor;
			c.moveToPosition(info.position);
			t = c.getString(c.getColumnIndexOrThrow(DatabaseHelper
					.getKeyTitle()));
			u = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyUser()));
			p = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyPwd()));
			ur = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.getKeyUrl()));
			database.close();
			WriteAsynTask writeAsynTask = new WriteAsynTask(this);
			writeAsynTask.execute("0002", t, u, p, ur,
					getString(R.string.routeExportFile));
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
