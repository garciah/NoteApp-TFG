package com.urjc.noteprototype;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ViewPwd extends Activity {

	private TextView title;
	private TextView user;
	private TextView pwd;
	private TextView url;
	private String pwdAux;
	private String urlAux;
	private ImageButton imBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_password);
		final ToggleButton but = (ToggleButton) findViewById(R.id.buttonView);
		title = (TextView) findViewById(R.id.textViewT);
		user = (TextView) findViewById(R.id.textViewU);
		pwd = (TextView) findViewById(R.id.textViewP);
		url = (TextView) findViewById(R.id.textViewUrl);
		imBut = (ImageButton) findViewById(R.id.imageButInternet);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String titleAux = extras.getString(DatabaseHelper.getKeyTitle());
			String userAux = extras.getString(DatabaseHelper.getKeyUser());
			urlAux = extras.getString(DatabaseHelper.getKeyUrl());
			pwdAux = extras.getString(DatabaseHelper.getKeyPwd());
			if (titleAux != null) {
				title.setText(titleAux);
			}
			if (userAux != null) {
				user.setText(userAux);
			}
			if (urlAux != null) {
				url.setText(urlAux);
			}
		}

		but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (but.isChecked())
					pwd.setText(pwdAux);
				else
					pwd.setText(R.string.PwdSecret);
			}
		});

		imBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!urlAux.startsWith("http://")
						&& !urlAux.startsWith("https://"))
					urlAux = "http://" + urlAux;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(urlAux));
				startActivity(browserIntent);
			}
		});

	}
}
