package com.urjc.noteprototype.recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.Option;
import com.urjc.noteprototype.R;

public class FileChooser extends ListActivity {
	private File currentDir;
	private FileArrayAdapter adapter;
	private String route;
	private static String pathAppImgC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File(getString(R.string.sdcardRoute));
		pathAppImgC = getString(R.string.routeImgFilesC);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			route = extras.getString(DatabaseHelper.getKeyRoute());
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
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onFileClick(Option o) {
		String newRoute = pathAppImgC + o.getName();
		if (!route.equals(newRoute)) {
			File file = new File(route);
			file.delete();
		}	
		Intent intent = new Intent();
		intent.putExtra("newRoute", newRoute);
		intent.putExtra("oldRoute",o.getPath());
		setResult(RESULT_OK, intent);
		finish();
	}
}
