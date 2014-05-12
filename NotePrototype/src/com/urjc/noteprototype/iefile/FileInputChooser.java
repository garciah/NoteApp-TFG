package com.urjc.noteprototype.iefile;

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

import com.urjc.noteprototype.MenuApp;
import com.urjc.noteprototype.Option;
import com.urjc.noteprototype.R;
import com.urjc.noteprototype.recipe.FileArrayAdapter;

public class FileInputChooser extends ListActivity {
	private File currentDir;
	private FileArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File(getString(R.string.sdcardRoute));
		fill(currentDir);
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		adapter = new FileArrayAdapter(FileInputChooser.this,
				R.layout.file_chooser, dir);
		this.setListAdapter(adapter);
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), "Folder", ff
							.getAbsolutePath()));
				else {
					if (ff.getName().contains(".nfh")) {
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
			Intent i = new Intent(this, MenuApp.class);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onFileClick(Option o) {
		String name = o.getName();
		String route = o.getPath();
		ReadAsynTask at = new ReadAsynTask(this);
		at.execute(name, route);
	}

}
