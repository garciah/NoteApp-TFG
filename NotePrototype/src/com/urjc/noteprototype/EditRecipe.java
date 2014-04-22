package com.urjc.noteprototype;

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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EditRecipe extends Activity {

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
			titleAux = extras.getString(DatabaseHelper.getKeyTitle());
			ingredientsAux = extras.getString(DatabaseHelper
					.getKeyIngredients());
			instructionsAux = extras.getString(DatabaseHelper
					.getKeyInstructions());
			route = extras.getString(DatabaseHelper.getKeyRoute());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			upd = extras.getBoolean("upd");
			if(route != null){
				File imgFile = new  File(route);
				if(impFile){
					if(imgFile.exists()){
						String imgName = extras.getString("imgName");
						String pathAppImgC= getString(R.string.routeImgFilesC);
						String newRoute = pathAppImgC + imgName;
						if(!imgFile.getPath().equals(newRoute)){
							File auxFile = new  File(newRoute);
							if(auxFile.exists()){
								try {
									File file = new File(Environment.getExternalStorageDirectory(),
											getString(R.string.routeImgFiles));
									if (!file.exists()) {
										if (!file.mkdirs()) {
											Log.e("TravellerLog :: ", "Problem creating Image folder");
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
							}
							
						}
						imgFile = new  File(newRoute);
						route = newRoute;
					}
				}
				if(imgFile.exists()){
				    Bitmap myBitmap = decodeScaledBitmapFromSdCard(route, 250, 250);
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
			if(impFile){
				database.open();
				database.createRecipe(titleAux, ingredientsAux,
						instructionsAux, route);
				database.close();
				Toast.makeText(this, R.string.msgImpRecipe,
						Toast.LENGTH_SHORT).show();
			}
		}
		
		final Context c = this;
		searchImgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				Intent i = new Intent(c, FileChooser.class);
				String titleAux = title.getText().toString();
				String ingredientsAux = ingredients.getText().toString();
				String instructionsAux = instructions.getText().toString();
				i.putExtra(DatabaseHelper.getKeyRowid(), id);
				i.putExtra(DatabaseHelper.getKeyTitle(), titleAux);
				i.putExtra(DatabaseHelper.getKeyIngredients(), ingredientsAux);
				i.putExtra(DatabaseHelper.getKeyInstructions(), instructionsAux);
				i.putExtra(DatabaseHelper.getKeyRoute(), route);
				i.putExtra("upd",upd);
				startActivity(i);
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
						database.updateRecipe(id, titleAux, ingredientsAux,
								instructionsAux, route);
					} else {
						database.createRecipe(titleAux, ingredientsAux,
								instructionsAux, route);
					}
					database.close();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(
	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(impFile){
				finish();
				Intent i = new Intent(this, RecipeList.class);
				startActivity(i);
				return true;	
			}
		}
		return super.onKeyDown(keyCode, event);
	}	
}


