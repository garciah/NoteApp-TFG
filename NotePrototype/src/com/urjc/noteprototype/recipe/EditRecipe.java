package com.urjc.noteprototype.recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

public class EditRecipe extends Activity {

	private static final int CHANGE_IMAGE = 1;
	private EditText title;
	private EditText ingredients;
	private EditText instructions;
	private ImageView image;
	private RecipeDB database;
	private Long id;
	private String route;
	private String titleAux;
	private String ingredientsAux;
	private String instructionsAux;
	private boolean upd;
	private boolean impFile;
	private File imgFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_recipe);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveRecipe);
		ImageButton searchImgButton = (ImageButton) findViewById(R.id.buttonSearch);
		title = (EditText) findViewById(R.id.editTextTitleRecipe);
		ingredients = (EditText) findViewById(R.id.editTextIngredients);
		instructions = (EditText) findViewById(R.id.editTextInstruction);
		image = (ImageView) findViewById(R.id.imageRecipe);
		database = new RecipeDB(this);
		route = " ";
		id = null;
		upd = false;
		impFile = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			impFile = extras.getBoolean("impFile");
			System.out.println("---------> impFile onCreate -> "+impFile);
			titleAux = extras.getString(DatabaseHelper.getKeyTitle());
			ingredientsAux = extras.getString(DatabaseHelper
					.getKeyIngredients());
			instructionsAux = extras.getString(DatabaseHelper
					.getKeyInstructions());
			route = extras.getString(DatabaseHelper.getKeyRoute());
			System.out.println("---------> route onCreate -> "+route);
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			upd = extras.getBoolean("upd");
			if (route != null) {
				imgFile = new File(route);
				if (impFile) {
					String imgName = extras.getString("imgName");
					String pathAppImgC = getString(R.string.routeImgFilesC);
					String newRoute = pathAppImgC + imgName;
					System.out.println("---------> newRoute onCreate -> "+newRoute);
					if (!imgFile.getPath().equals(newRoute)) {
						File auxFile = new File(newRoute);
						if (auxFile.exists()) {
							try {
								File file = new File(
										Environment
												.getExternalStorageDirectory(),
										getString(R.string.routeImgFiles));
								if (!file.exists()) {
									if (!file.mkdirs()) {
										Log.e("TravellerLog :: ",
												"Problem creating Image folder");
									}
								}
								InputStream in = new FileInputStream(imgFile);
								OutputStream out = new FileOutputStream(auxFile);
								byte[] buf = new byte[1024];
								int len;
								while ((len = in.read(buf)) > 0) {
									out.write(buf, 0, len);
								}
								in.close();
								out.close();
							} catch (FileNotFoundException ex) {
								ex.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							route = newRoute;
						}
					}

				}
				if (imgFile.exists()) {
					Bitmap myBitmap = HandlerBitmap
							.decodeScaledBitmapFromSdCard(route, 250, 250);
					image.setImageBitmap(myBitmap);
				}
			}
			if (titleAux != null) {
				title.setText(titleAux);
			}
			if (ingredientsAux != null) {
				ingredients.setText(ingredientsAux);
			}
			if (instructionsAux != null) {
				instructions.setText(instructionsAux);
			}
		}

		final Context c = this;
		searchImgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(c, FileChooser.class);
				startActivityForResult(i, CHANGE_IMAGE);
			}
		});

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String titleAux = title.getText().toString();
				String ingredientsAux = ingredients.getText().toString();
				String instructionsAux = instructions.getText().toString();
				if (titleAux.length() > 0) {
					database.open();
					if (upd) {
						System.out.println("--- UPDATE ---");
						database.updateRecipe(id, titleAux, ingredientsAux,
								instructionsAux, route);
					} else {
						System.out.println("--- CREO RECIPE ---");
						database.createRecipe(titleAux, ingredientsAux,
								instructionsAux, route);
					}
					database.close();
					if (impFile) {
						Intent i = new Intent(getApplicationContext(),
								RecipeList.class);
						startActivity(i);
					}
					finish();
				} else {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							R.string.msgEditRecipe, Toast.LENGTH_SHORT);
					toast1.show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CHANGE_IMAGE:
				String oldRoute = data.getExtras().getString("oldRoute");
				System.out.println("oldRoute -> "+oldRoute);
				String newRoute = data.getExtras().getString("newRoute");
				System.out.println("newRoute -> "+newRoute);
				CopiarImagenAsyncTask ci = new CopiarImagenAsyncTask(image);
				ci.execute(oldRoute, newRoute,
						getString(R.string.routeImgFiles));
				route = newRoute;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (impFile) {
				database.open();
				System.out.println("CREO RECIPE EN ONKEYDOWN");
				database.createRecipe(titleAux, ingredientsAux,
						instructionsAux, route);
				database.close();
				Toast.makeText(this, R.string.msgImpRecipe, Toast.LENGTH_LONG)
						.show();
				finish();
				Intent i = new Intent(this, RecipeList.class);
				startActivity(i);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
