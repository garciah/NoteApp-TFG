package com.urjc.noteprototype.task;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class EditTask extends Activity {

	private EditText titleText;
	private Long id;
	private int check;
	private TaskDB database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveTask);
		titleText = (EditText) findViewById(R.id.editTextTitleTask);
		database = new TaskDB(this);
		id = null;
		check = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DatabaseHelper.getKeyTitle());
			check = extras.getInt(DatabaseHelper.getKeyCheck());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
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
						database.updateTask(id, title, check);
					} else {
						database.createTask(title, 0);
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
