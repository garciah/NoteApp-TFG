package com.urjc.noteprototype.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.urjc.noteprototype.MenuApp;
import com.urjc.noteprototype.R;
import com.urjc.noteprototype.iefile.HandlerFileImportExport;
import com.urjc.noteprototype.iefile.WriteAsynTask;

public class TaskList extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_EXPORT = 2;
	private TaskDB database;
	private Cursor cursor;
	private File f;
	private List<TaskClass> items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new TaskDB(this);
		setContentView(R.layout.task_list);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String route = extras.getString("taskRoute");
			try {
				List<TaskClass> auxList = new ArrayList<TaskClass>();
				auxList = HandlerFileImportExport.readFileTask(route);
				database.open();
				for (int i = 0; i < auxList.size(); i++) {
					int aux = 0;
					if (auxList.get(i).getCheck()) {
						aux = 1;
					}
					database.createTask(auxList.get(i).getName(), aux);
				}
				database.close();
				Toast.makeText(this, R.string.msgImpTask,
						Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Error File", Toast.LENGTH_SHORT).show();
			}
		}
		fillData();
	}

	private void fillData() {
		database.open();
		cursor = database.getCursorAllTask();
		items = new ArrayList<TaskClass>();
		if (cursor.moveToFirst()) {
			do {
				long i = cursor.getLong(0);
				String n = cursor.getString(1);
				int c = cursor.getInt(2);
				TaskClass elem = new TaskClass(c, n, i);
				items.add(elem);
			} while (cursor.moveToNext());
		}
		database.close();
		TaskElemAdapter adapter = new TaskElemAdapter(this, items);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_new:
			Intent i = new Intent(this, EditTask.class);
			startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		case R.id.menu_share:
			try {
				String file = HandlerFileImportExport.writeFileTask("list",
						items, getString(R.string.routeSharingFile));
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
							Intent.createChooser(shareIntent, "Tasks"),
							ACTIVITY_EXPORT);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case R.id.menu_export:
			WriteAsynTask writeAsynTask = new WriteAsynTask(this, items);
			writeAsynTask.execute("0003", getString(R.string.routeExportFile));
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
