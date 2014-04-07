package com.urjc.noteprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MenuApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		Button opNote = (Button) findViewById(R.id.buttonView);
		Button opPwd = (Button) findViewById(R.id.butPwd);
		Button opBuy = (Button) findViewById(R.id.butBuy);
		Button opAbout = (Button) findViewById(R.id.butAbout);
		Button opTask = (Button) findViewById(R.id.butTasks);
		Button opRecipe = (Button) findViewById(R.id.buttonRecipe);
		Button opAcc = (Button) findViewById(R.id.buttonAcc);
		opNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), NoteList.class);
				startActivity(i);
				finish();
			}
		});

		opPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						PasswordsList.class);
				startActivity(i);
				finish();
			}
		});

		opBuy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), BuyList.class);
				startActivity(i);
				finish();
			}
		});

		opAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AboutApp.class);
				startActivity(i);
			}
		});

		opTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), TaskList.class);
				startActivity(i);
				finish();
			}
		});
		
		opRecipe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), RecipeList.class);
				startActivity(i);
				finish();
			}
		});
		
		opAcc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AccList.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
