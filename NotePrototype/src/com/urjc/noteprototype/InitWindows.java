package com.urjc.noteprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InitWindows extends Activity {

	private final int DURACION_INTWINDOW = 500; // 1/2 seg

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_window);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(InitWindows.this, MenuApp.class);
				startActivity(intent);
				finish();
			};
		}, DURACION_INTWINDOW);
	}
}