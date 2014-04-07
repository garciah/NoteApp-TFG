package com.urjc.noteprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPwd extends Activity {

	private EditText title;
	private EditText user;
	private EditText pwd;
	private EditText url;
	private Long id;
	private PwdDB database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_pwd);
		Button confirmButton = (Button) findViewById(R.id.buttonSavePwd);
		title = (EditText) findViewById(R.id.editTextTitlePwd);
		user = (EditText) findViewById(R.id.editTextUser);
		pwd = (EditText) findViewById(R.id.editTextBodyPwd);
		url = (EditText) findViewById(R.id.editTextUrl);
		database = new PwdDB(this);

		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String titleAux = extras.getString(DatabaseHelper.getKeyTitle());
			String userAux = extras.getString(DatabaseHelper.getKeyUser());
			String pwdAux = extras.getString(DatabaseHelper.getKeyPwd());
			String urlAux = extras.getString(DatabaseHelper.getKeyUrl());
			id = extras.getLong(DatabaseHelper.getKeyRowid());

			if (titleAux != null) {
				title.setText(titleAux);
			}
			if (userAux != null) {
				user.setText(userAux);
			}
			if (pwdAux != null) {
				pwd.setText(pwdAux);
			}
			if (urlAux != null) {
				url.setText(urlAux);
			}
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String titleAux = title.getText().toString();
				String userAux = user.getText().toString();
				String pwdAux = pwd.getText().toString();
				String urlAux = url.getText().toString();
				if (titleAux.length() > 0 && pwdAux.length() > 0) {
					database.open();
					if (id != null) {
						database.updatePwd(id, titleAux, pwdAux, userAux,
								urlAux);
					} else {
						database.createPwd(titleAux, pwdAux, userAux, urlAux);
					}
					database.close();
					finish();
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditPwd, Toast.LENGTH_SHORT);
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
