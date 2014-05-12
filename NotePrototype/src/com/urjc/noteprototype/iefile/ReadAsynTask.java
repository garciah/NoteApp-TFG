package com.urjc.noteprototype.iefile;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.urjc.noteprototype.DatabaseHelper;
import com.urjc.noteprototype.MenuApp;
import com.urjc.noteprototype.R;
import com.urjc.noteprototype.account.TableAccount;
import com.urjc.noteprototype.note.EditNote;
import com.urjc.noteprototype.note.Note;
import com.urjc.noteprototype.pwd.EditPwd;
import com.urjc.noteprototype.pwd.PwdClass;
import com.urjc.noteprototype.recipe.EditRecipe;
import com.urjc.noteprototype.recipe.RecipeClass;
import com.urjc.noteprototype.shoplist.BuyElemList;
import com.urjc.noteprototype.task.TaskList;

public class ReadAsynTask extends AsyncTask<String, Void, Boolean> {

	private static final String COD_NOTE = "0001";
	private static final String COD_PWD = "0002";
	private static final String COD_TASK = "0003";
	private static final String COD_RECIPE = "0004";
	private static final String COD_ACC = "0005";
	private static final String COD_BUY = "0006";
	private Context context;

	public ReadAsynTask(Context ctx) {
		super();
		context = ctx;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String name = params[0];
		String path = params[1];
		Intent i = new Intent();
		String cod = "0000";
		try {
			cod = HandlerFileImportExport.readCode(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		switch (cod) {
		case COD_NOTE:
			try {
				Note n = new Note();
				n = HandlerFileImportExport.readFileNote(path);
				i = new Intent(context, EditNote.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), n.getTitle());
				i.putExtra(DatabaseHelper.getKeyBody(), n.getBody());
				i.putExtra("impFile", true);
				context.startActivity(i);
				((Activity) context).finish();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(context, "Error File: " + name,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		case COD_TASK:
			i = new Intent(context, TaskList.class);
			i.putExtra("taskRoute", path);
			context.startActivity(i);
			((Activity) context).finish();
			return true;
		case COD_PWD:
			try {
				PwdClass p = new PwdClass();
				p = HandlerFileImportExport.readFilePwd(path);
				i = new Intent(context, EditPwd.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), p.getTitle());
				i.putExtra(DatabaseHelper.getKeyUser(), p.getUser());
				i.putExtra(DatabaseHelper.getKeyPwd(), p.getPwd());
				i.putExtra(DatabaseHelper.getKeyUrl(), p.getUrl());
				i.putExtra("impFile", true);
				context.startActivity(i);
				((Activity) context).finish();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(context, "Error File: " + name,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		case COD_ACC:
			i = new Intent(context, TableAccount.class);
			i.putExtra("impFile", true);
			i.putExtra("accRoute", path);
			context.startActivity(i);
			((Activity) context).finish();
			return true;
		case COD_BUY:
			i = new Intent(context, BuyElemList.class);
			i.putExtra("impFile", true);
			i.putExtra("accRoute", path);
			context.startActivity(i);
			((Activity) context).finish();
			return true;
		case COD_RECIPE:
			try {
				RecipeClass r = new RecipeClass();
				r = HandlerFileImportExport.readFileRecipe(path);
				i = new Intent(context, EditRecipe.class);
				i.putExtra(DatabaseHelper.getKeyTitle(), r.getTitle());
				i.putExtra(DatabaseHelper.getKeyIngredients(),
						r.getIngredients());
				i.putExtra(DatabaseHelper.getKeyInstructions(),
						r.getInstructions());
				String[] aux = path.split("/");
				String imgPath = "";
				for (int k = 0; k < aux.length - 1; k++) {
					imgPath = imgPath + aux[k] + "/";
				}
				imgPath = imgPath + r.getImageName();
				i.putExtra(DatabaseHelper.getKeyRoute(), imgPath);
				i.putExtra("imgName", r.getImageName());
				i.putExtra("impFile", true);
				context.startActivity(i);
				((Activity) context).finish();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(context, "Error File: " + name,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		default:
			Toast.makeText(context, R.string.errFormat, Toast.LENGTH_SHORT)
					.show();
			i = new Intent(context, MenuApp.class);
			context.startActivity(i);
			((Activity) context).finish();
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (!result) {
			Intent i = new Intent(context, MenuApp.class);
			context.startActivity(i);
		}
	}

}
