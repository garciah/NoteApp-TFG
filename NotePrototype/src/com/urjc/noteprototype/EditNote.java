package com.urjc.noteprototype;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNote extends Activity {

	private TextView bodyText;
	private EditText titleText;
	private Long id;
	private NoteDB database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_note);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveNote);
		titleText = (EditText) findViewById(R.id.editTextTitleNote);
		bodyText = (TextView) findViewById(R.id.editTextBodyNote);
		// Typeface font =
		// Typeface.createFromAsset(getAssets(),"Impregnable_Personal_Use_Only.ttf");
		// bodyText.setTypeface(font);
		database = new NoteDB(this);

		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DatabaseHelper.getKeyTitle());
			String body = extras.getString(DatabaseHelper.getKeyBody());
			id = extras.getLong(DatabaseHelper.getKeyRowid());

			if (title != null) {
				titleText.setText(title);
			}
			if (body != null) {
				bodyText.setText(body);
			}
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String title = titleText.getText().toString();
				String body = bodyText.getText().toString();
				if (title.length() > 0 && body.length() > 0) {
					database.open();
					if (id != null) {
						database.updateNote(id, title, body);
					} else {
						database.createNote(title, body);
					}
					database.close();
					finish();
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditNote, Toast.LENGTH_SHORT);
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
