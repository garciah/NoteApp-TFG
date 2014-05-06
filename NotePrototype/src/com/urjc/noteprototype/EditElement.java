package com.urjc.noteprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditElement extends Activity {

	private EditText titleText;
	private Long id;
	private BuyDB database;
	private int account;
	private int check;
	private long idBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_element);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveNewElement);
		titleText = (EditText) findViewById(R.id.editTextTitleElement);
		database = new BuyDB(this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String name = extras.getString(DatabaseHelper.getKeyTitle());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			account = extras.getInt(DatabaseHelper.getKeyAccount());
			check = extras.getInt(DatabaseHelper.getKeyCheck());
			idBuy = extras.getLong(DatabaseHelper.getKeyTitlebuy());
			if (name != null) {
				titleText.setText(name);
			}
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = titleText.getText().toString();
				if (name.length() > 0) {
					database.open();
					if (account != 0) {
						database.updateElement(id, name, account, check);
					} else {
						account = 1;
						check = 0;
						database.createBuyElement(name, account, check, idBuy);
					}
					database.close();
					finish();
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditElem, Toast.LENGTH_SHORT);
					toast1.show();
				}
			}
		});
	}
}