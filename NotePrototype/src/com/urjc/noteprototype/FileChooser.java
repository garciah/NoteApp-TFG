package com.urjc.noteprototype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

public class FileChooser extends ListActivity {
	private File currentDir;
	private FileArrayAdapter adapter;
	private Long id;
	private String route;
	private String title;
	private String ingredients;
	private String instructions;
	private Boolean upd;
	private static String pathAppImg;
	private static String pathAppImgC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File(getString(R.string.sdcardRoute));
		pathAppImg = getString(R.string.routeImgFiles);
		pathAppImgC = getString(R.string.routeImgFilesC);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			title = extras.getString(DatabaseHelper.getKeyTitle());
			ingredients = extras.getString(DatabaseHelper.getKeyIngredients());
			instructions = extras
					.getString(DatabaseHelper.getKeyInstructions());
			route = extras.getString(DatabaseHelper.getKeyRoute());
			id = extras.getLong(DatabaseHelper.getKeyRowid());
			upd = extras.getBoolean("upd");
		}
		fill(currentDir);
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_chooser,
				dir);
		this.setListAdapter(adapter);
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), "Folder", ff
							.getAbsolutePath()));
				else {
					boolean im = false;
					if (ff.getName().contains(".jpg")) {
						im = true;
					} else if (ff.getName().contains(".png")) {
						im = true;
					} else if (ff.getName().contains(".jpeg")) {
						im = true;
					}

					if (im) {
						fls.add(new Option(ff.getName(), "File Size: "
								+ ff.length(), ff.getAbsolutePath()));
					}
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", "Parent Directory", f.getParent()));
	}

	Stack<File> dirStack = new Stack<File>();

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getData().equalsIgnoreCase("folder")) {
			dirStack.push(currentDir);
			currentDir = new File(o.getPath());
			fill(currentDir);
		} else if (o.getData().equalsIgnoreCase("parent directory")) {
			currentDir = dirStack.pop();
			fill(currentDir);
		} else {
			onFileClick(o);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			Intent i = new Intent(this, EditRecipe.class);
			i.putExtra(DatabaseHelper.getKeyRowid(), id);
			i.putExtra(DatabaseHelper.getKeyTitle(), title);
			i.putExtra(DatabaseHelper.getKeyIngredients(), ingredients);
			i.putExtra(DatabaseHelper.getKeyInstructions(), instructions);
			i.putExtra(DatabaseHelper.getKeyRoute(), route);
			i.putExtra("upd", upd);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onFileClick(Option o) {
		String newRoute = pathAppImgC + o.getName();
		copyfile(o.getPath(), newRoute);
		if (!route.equals(newRoute)) {
			File file = new File(route);
			file.delete();
		}
		finish();
		Intent i = new Intent(this, EditRecipe.class);
		i.putExtra(DatabaseHelper.getKeyRowid(), id);
		i.putExtra(DatabaseHelper.getKeyTitle(), title);
		i.putExtra(DatabaseHelper.getKeyIngredients(), ingredients);
		i.putExtra(DatabaseHelper.getKeyInstructions(), instructions);
		i.putExtra(DatabaseHelper.getKeyRoute(), newRoute);
		i.putExtra("upd", upd);
		startActivity(i);
	}

	public static void copyfile(String srFile, String dtFile) {
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					pathAppImg);
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.e("TravellerLog :: ", "Problem creating Image folder");
				}
			}
			if (!srFile.equals(dtFile)) {
				File f1 = new File(srFile);
				File f2 = new File(dtFile);
				InputStream in = new FileInputStream(f1);
				OutputStream out = new FileOutputStream(f2);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
