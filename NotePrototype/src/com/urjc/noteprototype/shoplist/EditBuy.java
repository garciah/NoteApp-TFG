package com.urjc.noteprototype.shoplist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class EditBuy extends Activity {

	private EditText titleText;
	private Long id;
	private BuyDB database;
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_buy);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveBuy);
		titleText = (EditText) findViewById(R.id.editTextTitleBuy);
		c = this;
		database = new BuyDB(this);
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DatabaseHelper.getKeyTitle());
			id = extras.getLong(DatabaseHelper.getKeyIdbuy());
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
						database.updateBuy(id, title);
						finish();
					} else {
						id = database.createBuy(title);
						database.close();
						finish();
						Intent i = new Intent(c, BuyElemList.class);
						i.putExtra(DatabaseHelper.getKeyTitlebuy(), id);
						startActivity(i);
					}
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditBuy, Toast.LENGTH_SHORT);
					toast1.show();
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent(this, BuyList.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}