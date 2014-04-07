package com.urjc.noteprototype;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableAccount extends Activity {

	private Long idAcc;
	private String titleAcc;
	private AccountDB database;
	private TableLayout tl;
	private TextView tit;
	private Cursor cursor;
	private Button editTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_account);
		tl = (TableLayout) findViewById(R.id.tableAcc);
		tit = (TextView) findViewById(R.id.textViewTitleAcc);
		editTable = (Button) findViewById(R.id.buttonEditTable);
		tl.removeAllViews();
		idAcc = null;
		float result = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idAcc = extras.getLong(DatabaseHelper.getKeyAcc());
			titleAcc = extras.getString(DatabaseHelper.getKeyTitle());
			tit.setText(titleAcc);
		}
		if (idAcc != null) {
			database = new AccountDB(this);
			database.open();
			cursor = database.getCursorElements(idAcc);
			ArrayList<AccountElem> items = new ArrayList<AccountElem>();
			if (cursor.moveToFirst()) {
				do {
					long id = cursor.getLong(0);
					String tag = cursor.getString(1);
					float num = cursor.getFloat(2);
					AccountElem elem = new AccountElem(id, tag, num, idAcc);
					items.add(elem);
				} while (cursor.moveToNext());
			}
			database.close();
			// head
			TableRow row = new TableRow(this);
			row.setPadding(1, 1, 1, 1);
			row.setGravity(Gravity.CENTER);
			TextView tag = new TextView(this);
			TextView value = new TextView(this);
			configTViewTag(tag);
			tag.setTextColor(getResources().getColor(R.color.black));
			configTViewValue(value);
			value.setTextColor(getResources().getColor(R.color.black));
			tag.setText("TAG");
			value.setText("VALUE");
			row.addView(tag);
			row.addView(value);
			tl.addView(row, 0);
			// content
			for (int i = 0; i < items.size(); i++) {
				row = new TableRow(this);
				row.setGravity(Gravity.CENTER);
				tag = new TextView(this);
				value = new TextView(this);
				configTViewTag(tag);
				tag.setTextColor(getResources().getColor(R.color.Blue));
				configTViewValue(value);
				value.setTextColor(getResources().getColor(R.color.Blue));
				tag.setText(items.get(i).getTag());
				value.setText(String.valueOf(items.get(i).getNum()));
				result = result + items.get(i).getNum();
				row.addView(tag);
				row.addView(value);
				tl.addView(row, i + 1);
			}
			// final
			row = new TableRow(this);
			row.setPadding(1, 1, 1, 1);
			row.setGravity(Gravity.CENTER);
			tag = new TextView(this);
			configTViewTag(tag);
			tag.setTextColor(getResources().getColor(R.color.BlueDark));
			value = new TextView(this);
			configTViewValue(value);
			value.setTextColor(getResources().getColor(R.color.BlueDark));
			tag.setText("TOTAL");
			result = (float) (Math.rint(result*1000)/1000);
			value.setText(String.valueOf(result));
			row.addView(tag);
			row.addView(value);
			tl.addView(row, items.size() + 1);
		}
		editTable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AccElemList.class);
				i.putExtra(DatabaseHelper.getKeyAcc(), idAcc);
				i.putExtra(DatabaseHelper.getKeyTitle(), titleAcc);
				startActivity(i);
				finish();
			}
		});
	}
	
	private void configTViewTag(TextView t){
		t.setTypeface(Typeface.DEFAULT_BOLD);
		t.setPadding(10, 5, 10, 5);
		t.setBackgroundColor(getResources().getColor(R.color.Yellow));
	}
	
	private void configTViewValue(TextView t){
		t.setGravity(Gravity.CENTER);
		t.setTypeface(Typeface.DEFAULT_BOLD);
		t.setPadding(10, 5, 10, 5);
		t.setBackgroundColor(getResources().getColor(R.color.Yellow));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(getApplicationContext(), AccList.class);
			startActivityForResult(intent,1);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
