package com.urjc.noteprototype;

import java.io.File;
import java.io.IOException;
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
import android.widget.Toast;

public class FileInputChooser extends ListActivity {
	private File currentDir;
	private FileArrayAdapter adapter;
	private static final String COD_NOTE = "0001";
	private static final String COD_PWD = "0002";
	private static final String COD_TASK = "0003";
	private static final String COD_RECIPE = "0004";
	private static final String COD_ACC = "0005";
	private static final String COD_BUY = "0006";

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
		String cod = "0000";
		Intent i;
		try {
			cod = HandlerFileImportExport.readCode(o.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (cod) {
		case COD_NOTE:
			try {
				Note n = new Note();
				n = HandlerFileImportExport.readFileNote(o.getPath());
				i = new Intent(this, EditNote.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), n.getTitle());
				i.putExtra(DatabaseHelper.getKeyBody(), n.getBody());
				i.putExtra("impFile", true);
				startActivity(i);
				finish();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Error File: " + o.getName(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case COD_TASK:
			i = new Intent(this, TaskList.class);
			i.putExtra("taskRoute", o.getPath());
			startActivity(i);
			finish();
			break;
		case COD_PWD:
			try {
				PwdClass p = new PwdClass();
				p = HandlerFileImportExport.readFilePwd(o.getPath());
				i = new Intent(this, EditPwd.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), p.getTitle());
				i.putExtra(DatabaseHelper.getKeyUser(), p.getUser());
				i.putExtra(DatabaseHelper.getKeyPwd(), p.getPwd());
				i.putExtra(DatabaseHelper.getKeyUrl(), p.getUrl());
				i.putExtra("impFile", true);
				startActivity(i);
				finish();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Error File: " + o.getName(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case COD_ACC:
			i = new Intent(this, TableAccount.class);
			i.putExtra("impFile", true);
			i.putExtra("accRoute", o.getPath());
			startActivity(i);
			finish();
			break;
		case COD_BUY:
			i = new Intent(this, BuyElemList.class);
			i.putExtra("impFile", true);
			i.putExtra("accRoute", o.getPath());
			startActivity(i);
			finish();
			break;
		case COD_RECIPE:
			try {
				RecipeClass r = new RecipeClass();
				r = HandlerFileImportExport.readFileRecipe(o.getPath());
				i = new Intent(this, EditRecipe.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), r.getTitle());
				i.putExtra(DatabaseHelper.getKeyIngredients(),
						r.getIngredients());
				i.putExtra(DatabaseHelper.getKeyInstructions(),
						r.getInstructions());
				String[] aux = o.getPath().split("/");
				String imgPath = "";
				for (int k = 0; k < aux.length - 1; k++) {
					imgPath = imgPath + aux[k] + "/";
				}
				imgPath = imgPath + r.getImageName();
				i.putExtra(DatabaseHelper.getKeyRoute(), imgPath);
				i.putExtra("imgName", r.getImageName());
				i.putExtra("impFile", true);
				startActivity(i);
				finish();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Error File: " + o.getName(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			Toast.makeText(this, "Error File: " + o.getName(),
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
