package com.urjc.noteprototype.iefile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.urjc.noteprototype.account.AccountElem;
import com.urjc.noteprototype.note.Note;
import com.urjc.noteprototype.pwd.PwdClass;
import com.urjc.noteprototype.recipe.RecipeClass;
import com.urjc.noteprototype.shoplist.ElemBuyList;
import com.urjc.noteprototype.task.TaskClass;

import android.os.Environment;
import android.util.Log;

public class HandlerFileImportExport {

	private static final String COD_NOTE = "0001";
	private static final String COD_PWD = "0002";
	private static final String COD_TASK = "0003";
	private static final String COD_RECIPE = "0004";
	private static final String COD_ACC = "0005";
	private static final String COD_BUY = "0006";

	public HandlerFileImportExport() {
		super();
	}

	public static String writeFileNote(String title, String body, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_note.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_NOTE + "\n");
		salida.write(title + "\n");
		salida.write(body + "\n");
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static Note readFileNote(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		salida.readLine();
		String title = salida.readLine();
		String body = "";
		String aux = "";
		while ((aux = salida.readLine()) != null) {
			body = body + aux + "\n";
		}
		fis.close();
		Note n = new Note();
		n.setTitle(title);
		n.setBody(body);
		return n;
	}

	/**********************************************************************/
	public static String writeFilePwd(String title, String user, String pwd,
			String url, String route) throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_pwd.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_PWD + "\n");
		salida.write(title + "\n");
		salida.write(user + "\n");
		salida.write(pwd + "\n");
		salida.write(url + "\n");
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static PwdClass readFilePwd(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		salida.readLine();
		String title = salida.readLine();
		String user = salida.readLine();
		String pwd = salida.readLine();
		String url = salida.readLine();
		fis.close();
		PwdClass p = new PwdClass(title, user, pwd, url, 0);
		return p;
	}

	/**********************************************************************/

	public static String writeFileTask(String title, List<TaskClass> elems,
			String route) throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_task.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_TASK + "\n");
		salida.write(title + "\n");
		for (int i = 0; i < elems.size(); i++) {
			salida.write(elems.get(i).getName() + "\n");
			if (elems.get(i).getCheck()) {
				salida.write(1 + "\n");
			} else {
				salida.write(0 + "\n");
			}
		}
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static List<TaskClass> readFileTask(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		ArrayList<TaskClass> list = new ArrayList<TaskClass>();
		TaskClass t;
		salida.readLine();
		salida.readLine();// title
		String name;
		while ((name = salida.readLine()) != null) {
			int ch = Integer.parseInt(salida.readLine());
			t = new TaskClass(ch, name, 0);
			list.add(t);
		}
		fis.close();
		return list;
	}

	/**********************************************************************/
	public static String writeFileAcc(String title, List<AccountElem> elems,
			String route) throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_acc.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_ACC + "\n");
		salida.write(title + "\n");
		for (int i = 0; i < elems.size(); i++) {
			salida.write(elems.get(i).getTag() + "\n");
			salida.write(elems.get(i).getNum() + "\n");
		}
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static List<AccountElem> readFileAcc(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		List<AccountElem> list = new ArrayList<AccountElem>();
		AccountElem a;
		salida.readLine();
		String title = salida.readLine();
		a = new AccountElem(0, title, 0, 0);
		list.add(a);
		String tag;
		while ((tag = salida.readLine()) != null) {
			float num = Float.parseFloat(salida.readLine());
			a = new AccountElem(0, tag, num, 0);
			list.add(a);
		}
		fis.close();
		return list;
	}

	/**********************************************************************/
	public static String writeFileShopping(String title,
			List<ElemBuyList> elems, String route) throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_buy.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_BUY + "\n");
		salida.write(title + "\n");
		for (int i = 0; i < elems.size(); i++) {
			salida.write(elems.get(i).getName() + "\n");
			salida.write(elems.get(i).getAmount() + "\n");
			if (elems.get(i).getCheck()) {
				salida.write(1 + "\n");
			} else {
				salida.write(0 + "\n");
			}
		}
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static List<ElemBuyList> readFileShopping(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		List<ElemBuyList> list = new ArrayList<ElemBuyList>();
		ElemBuyList a;
		salida.readLine();
		String title = salida.readLine();
		a = new ElemBuyList(0, title, 0, 0, 0);
		list.add(a);
		String name;
		while ((name = salida.readLine()) != null) {
			int num = Integer.parseInt(salida.readLine());
			int ch = Integer.parseInt(salida.readLine());
			a = new ElemBuyList(0, name, ch, num, 0);
			list.add(a);
		}
		fis.close();
		return list;
	}

	/**********************************************************************/

	public static String writeFileRecipe(String title, String ingredients,
			String instructions, String imageName, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(), route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x = 0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
				tAux += title.charAt(x);
		}
		File f = new File(folder, tAux + "_recipe.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(COD_RECIPE + "\n");
		salida.write(title + "\n");
		salida.write(ingredients + "\n");
		salida.write(instructions + "\n");
		salida.write(imageName + "\n");
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static RecipeClass readFileRecipe(String titleFile)
			throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		salida.readLine();
		String title = salida.readLine();
		String ingredients = salida.readLine();
		String instructions = salida.readLine();
		String imageName = salida.readLine();
		fis.close();
		RecipeClass r = new RecipeClass(0, title, ingredients, instructions,
				imageName);
		return r;
	}

	/**********************************************************************/
	public static String readCode(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		String s = salida.readLine();
		fis.close();
		return s;
	}
}
