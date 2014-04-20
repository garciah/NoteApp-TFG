package com.urjc.noteprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditBuy extends Activity {

	private EditText titleText;
	private Long id;
	private BuyDB database;
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_buy);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveBuy);
		titleText = (EditText) findViewById(R.id.editTextTitleBuy);
		c = this;
		database = new BuyDB(this);
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DatabaseHelper.getKeyTitle());
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
						database.updateBuy(id, title);
						finish();
					} else {
						id = database.createBuy(title);
						database.close();
						finish();
						Intent i = new Intent(c, BuyElemList.class);
						i.putExtra(DatabaseHelper.getKeyTitlebuy(), id);
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
}