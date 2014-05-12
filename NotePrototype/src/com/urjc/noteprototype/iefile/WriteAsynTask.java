package com.urjc.noteprototype.iefile;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.urjc.noteprototype.R;
import com.urjc.noteprototype.account.AccountElem;
import com.urjc.noteprototype.shoplist.ElemBuyList;
import com.urjc.noteprototype.task.TaskClass;

public class WriteAsynTask extends AsyncTask<String, Void, Boolean> {

	private static final String COD_NOTE = "0001";
	private static final String COD_PWD = "0002";
	private static final String COD_TASK = "0003";
	private static final String COD_RECIPE = "0004";
	private static final String COD_ACC = "0005";
	private static final String COD_BUY = "0006";
	private Context context;
	private List<?> items;

	public WriteAsynTask(Context context) {
		super();
		this.context = context;
	}

	public WriteAsynTask(Context context, List<?> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String cod = params[0];
		String title;
		String body;
		String route;
		switch (cod) {
		case COD_NOTE:
			title = params[1];
			body = params[2];
			route = params[3];
			try {
				HandlerFileImportExport.writeFileNote(title, body, route);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		case COD_TASK:
			@SuppressWarnings("unchecked")
			List<TaskClass> tasks = (List<TaskClass>) items;
			route = params[1];
			try {
				HandlerFileImportExport.writeFileTask("list", tasks, route);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		case COD_PWD:
			title = params[1];
			String user = params[2];
			String pwd = params[3];
			String url = params[4];
			route = params[5];
			try {
				HandlerFileImportExport.writeFilePwd(title, user, pwd, url,
						route);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		case COD_ACC:
			title = params[1];
			route = params[2];
			@SuppressWarnings("unchecked")
			List<AccountElem> accs = (List<AccountElem>) items;
			try {
				HandlerFileImportExport.writeFileAcc(title, accs, route);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		case COD_BUY:
			title = params[1];
			route = params[2];
			@SuppressWarnings("unchecked")
			List<ElemBuyList> buys = (List<ElemBuyList>) items;
			try {
				HandlerFileImportExport.writeFileShopping(title, buys, route);
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		case COD_RECIPE:
			title = params[1];
			String ing = params[2];
			String inst = params[3];
			String nimg = params[4];
			route = params[5];
			try {
				HandlerFileImportExport.writeFileRecipe(title, ing, inst, nimg,
						route);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		default:
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			Toast.makeText(context, R.string.fileCreateMsg, Toast.LENGTH_SHORT)
					.show();
		}
		super.onPostExecute(result);
	}
}
