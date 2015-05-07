package com.urjc.noteprototype.recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.R;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class EditRecipe extends Activity {

	private static final int PICK_IMAGE = 1;
	private static final int CAPTURE_IMAGE = 2;
	public static final int MEDIA_TYPE_IMAGE = 3;
	private EditText title;
	private EditText ingredients;
	private EditText instructions;
	private ScrollView scroll;
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
	private String nameFile;
	private final Context c = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_recipe);
		Button confirmButton = (Button) findViewById(R.id.buttonSaveRecipe);
		ImageButton searchImgButton = (ImageButton) findViewById(R.id.buttonSearch);
		ImageButton buttonImgButton = (ImageButton) findViewById(R.id.buttonPhoto);
		title = (EditText) findViewById(R.id.editTextTitleRecipe);
		ingredients = (EditText) findViewById(R.id.editTextIngredients);
		instructions = (EditText) findViewById(R.id.editTextInstruction);
		image = (ImageView) findViewById(R.id.imageRecipe);
		scroll = (ScrollView) findViewById(R.id.scrollRecipe);
		
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
		

		
		buttonImgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				File direct = new File(Environment.getExternalStorageDirectory()+getString(R.string.routeImgFiles));
			    if (!direct.exists()) {
			        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+getString(R.string.routeImgFiles));
			        wallpaperDirectory.mkdirs();
			    }
			    nameFile = Environment.getExternalStorageDirectory()+getString(R.string.routeImgFiles)+"imgSan_"+new Date().getTime()+".png";
				File f = new File(nameFile);
				Uri uri = Uri.fromFile(f);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, CAPTURE_IMAGE);
			}
		});	
		
		searchImgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, ""),
						PICK_IMAGE);
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
			case PICK_IMAGE:
				if (data != null && data.getData() != null) {
					CopiarImagenAsyncTask ci = new CopiarImagenAsyncTask(image);
					String name = data.getData().toString().substring(data.getData().toString().lastIndexOf("/")+ 1,data.getData().toString().length());
					name = "noteRecipImage_"+name.substring(6);
					String newRoute = getString(R.string.routeImgFilesC) + name +".png";
					Bitmap myBitmap = HandlerBitmap.decodeScaledBitmapFromSdCard(getPath(c, data.getData()), 250, 250);
					ci.execute(getPath(c, data.getData()), newRoute,getString(R.string.routeImgFiles));
					route = newRoute;
					image.setImageBitmap(myBitmap);
					image.setRotation(obtainRotate());
				}
				break;
			case CAPTURE_IMAGE:
				route = nameFile;
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
				Bitmap myBitmap = HandlerBitmap.decodeScaledBitmapFromSdCard(nameFile, 250, 250);
				image.setImageBitmap(myBitmap);
				image.setRotation(obtainRotate());
				int id_photo = getLastImageId();
				ContentResolver cr = getContentResolver();
				cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{ Long.toString(id_photo) } );
				break;
			default:
				break;
			}
		}
	}

	private int obtainRotate() {
		int rotate =0;
		try
		  {
		      ExifInterface exif = new ExifInterface(route); 
		      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		      switch(orientation) {
		          case ExifInterface.ORIENTATION_ROTATE_90:
		              System.out.println("-----------------> 90");
		              rotate = 90;
		              break;
		          case ExifInterface.ORIENTATION_ROTATE_180:
		              System.out.println("-----------------> 180");
		              rotate = 180;
		              break;
		          case ExifInterface.ORIENTATION_ROTATE_270:
		              System.out.println("-----------------> 270");
		              rotate = 270;
		              break;
		          case ExifInterface.ORIENTATION_NORMAL:
		              System.out.println("-----------------> NORMAL");
		              break;
		      }
		      
		  }
		  catch (OutOfMemoryError | IOException e)
		  {
		      Toast.makeText(getApplicationContext(),e+"\"memory exception occured\"",Toast.LENGTH_LONG).show();
		  }
		return rotate;
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
	
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	/**
	 * Gets the last image id from the media store
	 * @return
	 */
	private int getLastImageId(){
	    final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
	    final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
	    Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
	    if(imageCursor.moveToFirst()){
	        int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
	        String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
	        imageCursor.close();
	        return id;
	    }else{
	        return 0;
	    }
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	        // Checks whether a hardware keyboard is available
	        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
	            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
	        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
	            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
	        }
	}
	
}
