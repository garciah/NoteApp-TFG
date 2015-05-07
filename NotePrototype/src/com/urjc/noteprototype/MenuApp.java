package com.urjc.noteprototype;

import com.urjc.noteprototype.account.AccList;
import com.urjc.noteprototype.iefile.FileInputChooser;
import com.urjc.noteprototype.note.NoteList;
import com.urjc.noteprototype.pwd.PasswordsList;
import com.urjc.noteprototype.recipe.RecipeList;
import com.urjc.noteprototype.shoplist.BuyList;
import com.urjc.noteprototype.task.TaskList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_menu);
		Button opNote = (Button) findViewById(R.id.Button01);
		Button opPwd = (Button) findViewById(R.id.Button02);
		Button opBuy = (Button) findViewById(R.id.Button03);
		Button opTask = (Button) findViewById(R.id.Button04);
		Button opRecipe = (Button) findViewById(R.id.Button05);
		Button opAcc = (Button) findViewById(R.id.Button06);
		Button opFile = (Button) findViewById(R.id.Button07);
		Button opAbout = (Button) findViewById(R.id.Button08);
		
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

		opFile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						FileInputChooser.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ini, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = new Intent(getApplicationContext(), HelpActivity.class);
		startActivity(i);
		return super.onOptionsItemSelected(item);
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
