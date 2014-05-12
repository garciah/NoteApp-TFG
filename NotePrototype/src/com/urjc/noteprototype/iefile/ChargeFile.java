package com.urjc.noteprototype.iefile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ChargeFile extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			Uri data = intent.getData();
			String path = data.getPath();
			String[] aux = path.split("/");
			String name = aux[aux.length - 1];
			ReadAsynTask at;
			at = new ReadAsynTask(this);
			at.execute(name, path);
		}
	}

}
