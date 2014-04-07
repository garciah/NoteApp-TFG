package com.urjc.noteprototype;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class AboutApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_app);
	}

	public void launchAboutApp(View view) {
		Intent i = new Intent(this, AboutApp.class);
		startActivity(i);
	}
}
