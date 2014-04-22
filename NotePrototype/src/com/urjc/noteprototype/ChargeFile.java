package com.urjc.noteprototype;


import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ChargeFile extends Activity {
	private static final String COD_NOTE = "0001";
	private static final String COD_PWD = "0002";
	private static final String COD_TASK = "0003";
	private static final String COD_RECIPE = "0004";
	private static final String COD_ACC = "0005";
	private static final String COD_BUY = "0006";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			Uri data = intent.getData();
			String path = data.getPath();
			String cod = "0000";
			Intent i;
			try {
				cod = HandlerFileImportExport.readCode(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			switch (cod) {
			case COD_NOTE:
				try {
					Note n = new Note();
					n = HandlerFileImportExport.readFileNote(path);
					i = new Intent(this, EditNote.class);
					i.putExtra(DatabaseHelper.getKeyTitle(), n.getTitle());
					i.putExtra(DatabaseHelper.getKeyBody(), n.getBody());
					i.putExtra("impFile", true);
					startActivity(i);
					finish();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this, "Error File ", Toast.LENGTH_SHORT).show();
				}
				break;
			case COD_TASK:
				i = new Intent(this, TaskList.class);
				i.putExtra("taskRoute", path);
				startActivity(i);
				finish();
				break;
			case COD_PWD:
				try {
					PwdClass p = new PwdClass();
					p = HandlerFileImportExport.readFilePwd(path);
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
					Toast.makeText(this, "Error File ", Toast.LENGTH_SHORT).show();
				}
				break;
			case COD_ACC:
				i = new Intent(this, TableAccount.class);
				i.putExtra("impFile", true);
				i.putExtra("accRoute", path);
				startActivity(i);
				finish();
				break;
			case COD_BUY:
				i = new Intent(this, BuyElemList.class);
				i.putExtra("impFile", true);
				i.putExtra("accRoute", path);
				startActivity(i);
				finish();
				break;
			case COD_RECIPE:
				try {
					RecipeClass r = new RecipeClass();
					r = HandlerFileImportExport.readFileRecipe(path);
					i = new Intent(this, EditRecipe.class);
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
					startActivity(i);
					finish();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this, "Error File ", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				Toast.makeText(this, "Error File ", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
