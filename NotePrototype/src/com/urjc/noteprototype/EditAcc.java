package com.urjc.noteprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditAcc extends Activity {

	private EditText titleText;
	private Long id;
	private String title;
	private AccountDB database;
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_acc);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveAcc);
		titleText = (EditText) findViewById(R.id.editTextTitleAcc);
		c = this;
		database = new AccountDB(this);
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			title = extras.getString(DatabaseHelper.getKeyTitle());
			id = extras.getLong(DatabaseHelper.getKeyIdbuy());
			if (title != null) {
				titleText.setText(title);
			}
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String title = titleText.getText().toString();
				if (title.length() > 0) {
					database.open();
					if (id != null) {
						database.updateAccount(id, title);
						finish();
					} else {
						id = database.createAccount(title);
						database.close();
						finish();
						Intent i = new Intent(c, TableAccount.class);
						i.putExtra(DatabaseHelper.getKeyAcc(), id);
						i.putExtra(DatabaseHelper.getKeyTitle(), title);
						startActivity(i);
					}
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditBuy, Toast.LENGTH_SHORT);
					toast1.show();
				}
			}
		});
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
			Intent intent = new Intent(this, AccList.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}