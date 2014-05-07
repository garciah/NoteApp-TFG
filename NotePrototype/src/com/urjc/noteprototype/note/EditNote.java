package com.urjc.noteprototype.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.MenuApp;
import com.urjc.noteprototype.R;

public class EditNote extends Activity {

	private TextView bodyText;
	private EditText titleText;
	private Long id;
	private NoteDB database;
	private boolean impFile;
	private String title;
	private String body;

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
		impFile = false;
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			title = extras.getString(DatabaseHelper.getKeyTitle());
			body = extras.getString(DatabaseHelper.getKeyBody());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			impFile = extras.getBoolean("impFile");
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
					if(impFile){
						database.createNote(title, body);
					}else{
						if (id != null) {
							database.updateNote(id, title, body);
						} else {
							database.createNote(title, body);
						}
					}
					database.close();
					if(impFile){
						Intent i = new Intent(getApplicationContext(), NoteList.class);
						startActivity(i);
					}
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(impFile){
				database.open();
				database.createNote(title, body);
				database.close();
				Toast.makeText(this, R.string.msgImpNote,
						Toast.LENGTH_LONG).show();
				finish();
				Intent i = new Intent(this, MenuApp.class);
				startActivity(i);
				return true;	
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
