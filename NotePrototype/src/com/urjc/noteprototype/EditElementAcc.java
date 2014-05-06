package com.urjc.noteprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditElementAcc extends Activity {

	private EditText tagEt;
	private EditText numEt;
	private Long id;
	private AccountDB database;
	private String tag;
	private float num;
	private long idAcc;
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_elem_acc);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveElemAcc);
		tagEt = (EditText) findViewById(R.id.editTextTagE);
		numEt = (EditText) findViewById(R.id.editTextNumE);
		database = new AccountDB(this);
		flag = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			tag = extras.getString(DatabaseHelper.getKeyTag());
			flag = (tag != null);
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			num = extras.getFloat(DatabaseHelper.getKeyNum());
			idAcc = extras.getLong(DatabaseHelper.getKeyAcc());
			
			if (tag != null) {
				tagEt.setText(tag);
			}
			numEt.setText(Float.toString(num));			
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tag = tagEt.getText().toString();
				num = Float.parseFloat(numEt.getText().toString());
				if (tag.length() > 0) {
					database.open();
					if (flag) {
						database.updateElement(id, tag, num);
					} else {
						database.createAccountElement(tag, num, idAcc);
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