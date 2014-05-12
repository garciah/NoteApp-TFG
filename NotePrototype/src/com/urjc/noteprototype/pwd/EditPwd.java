package com.urjc.noteprototype.pwd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class EditPwd extends Activity {

	private EditText title;
	private EditText user;
	private EditText pwd;
	private EditText url;
	private Long id;
	private PwdDB database;
	private boolean impFile;
	private String titleAux;
	private String userAux;
	private String pwdAux;
	private String urlAux;

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
		impFile = false;
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			titleAux = extras.getString(DatabaseHelper.getKeyTitle());
			userAux = extras.getString(DatabaseHelper.getKeyUser());
			pwdAux = extras.getString(DatabaseHelper.getKeyPwd());
			urlAux = extras.getString(DatabaseHelper.getKeyUrl());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			impFile = extras.getBoolean("impFile");
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
					if (impFile) {
						database.createPwd(titleAux, pwdAux, userAux, urlAux);
					} else {
						if (id != null) {
							database.updatePwd(id, titleAux, pwdAux, userAux,
									urlAux);
						} else {
							database.createPwd(titleAux, pwdAux, userAux,
									urlAux);
						}
					}
					database.close();
					if (impFile) {
						Intent i = new Intent(getApplicationContext(),
								PasswordsList.class);
						startActivity(i);
					}
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (impFile) {
				database.open();
				database.createPwd(titleAux, pwdAux, userAux, urlAux);
				database.close();
				Toast.makeText(this, R.string.msgImpPwd, Toast.LENGTH_LONG)
						.show();
				finish();
				Intent i = new Intent(this, PasswordsList.class);
				startActivity(i);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
