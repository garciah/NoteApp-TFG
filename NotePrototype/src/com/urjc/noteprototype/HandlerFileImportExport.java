package com.urjc.noteprototype;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

public class HandlerFileImportExport {

	public HandlerFileImportExport() {
		super();
	}

	public static String writeFileNote(String title, String body, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(),
				route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x=0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
			    tAux += title.charAt(x);
			}
		File f = new File(folder, tAux+"_note.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}	
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(title+"\n");
		salida.write(body+"\n");
		salida.flush();
		fos.close();
		return f.toString();
	}
	
	public static Note readFileNote(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		String title = salida.readLine();
		String body = salida.readLine();
		fis.close();
		Note n = new Note();
		n.setTitle(title);
		n.setBody(body);
		return n;
	}
	
	/**********************************************************************/
	public static String writeFilePwd(String title, String user,String pwd, String url, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(),
				route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x=0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
			    tAux += title.charAt(x);
			}
		File f = new File(folder, tAux+"_pwd.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}	
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(title+"\n");
		salida.write(user+"\n");
		salida.write(pwd+"\n");
		salida.write(url+"\n");
		salida.flush();
		fos.close();
		return f.toString();
	}
	
	public static PwdClass readFilePwd(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		String title = salida.readLine();
		String user = salida.readLine();
		String pwd = salida.readLine(); 
		String url = salida.readLine();
		fis.close();
		PwdClass p = new PwdClass(title,user,pwd,url,0);
		return p;
	}
	
	/**********************************************************************/
	
	public static String writeFileTask(String title, List<TaskClass> elems, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(),
				route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x=0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
			    tAux += title.charAt(x);
			}
		File f = new File(folder, tAux+"_task.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}	
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(title+"\n");
		for(int i=0;i<elems.size();i++){
			salida.write(elems.get(i).getName()+"\n");
			if(elems.get(i).getCheck()){
				salida.write(1+"\n");
			}else{
				salida.write(0+"\n");
			}
		}
		salida.flush();
		fos.close();
		return f.toString();
	}
	
	public static List<TaskClass> readFileTask(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		ArrayList<TaskClass> list = new ArrayList<TaskClass>();
		TaskClass t;
		salida.readLine();//title 
		String name;
        while((name=salida.readLine())!=null){
        	int ch = Integer.parseInt(salida.readLine());
        	t = new TaskClass(ch, name,0);
        	list.add(t);
        }
		fis.close();
		return list;
	}	
	
	/**********************************************************************/
	public static String writeFileAcc(String title, List<AccountElem> elems, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(),
				route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x=0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
			    tAux += title.charAt(x);
			}
		File f = new File(folder, tAux+"_acc.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}	
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(title+"\n");
		for(int i=0;i<elems.size();i++){
			salida.write(elems.get(i).getTag()+"\n");
			salida.write(elems.get(i).getNum()+"\n");
		}
		salida.flush();
		fos.close();
		return f.toString();
	}
	
	public static List<AccountElem> readFileAcc(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		List<AccountElem> list = new ArrayList<AccountElem>();
		AccountElem a;
		String title = salida.readLine();
		a = new AccountElem(0,title,0,0);
		list.add(a);
		String tag;
        while((tag=salida.readLine())!=null){
        	float num = Float.parseFloat(salida.readLine());
        	a = new AccountElem(0,tag,num,0);
        	list.add(a);
        }
		fis.close();
		return list;
	}
	
	/**********************************************************************/
	public static String writeFileShopping(String title, List<ElemBuyList> elems, String route)
			throws java.io.IOException {
		File folder = new File(Environment.getExternalStorageDirectory(),
				route);
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
			}
		}
		String tAux = "";
		for (int x=0; x < title.length(); x++) {
			if (title.charAt(x) != ' ')
			    tAux += title.charAt(x);
			}
		File f = new File(folder, tAux+"_buy.nfh");
		if (!f.exists()) {
			f.createNewFile();
		}	
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter salida = new BufferedWriter(osw);
		salida.write(title+"\n");
		for(int i=0;i<elems.size();i++){
			salida.write(elems.get(i).getName()+"\n");
			salida.write(elems.get(i).getAmount()+"\n");
			if(elems.get(i).getCheck()){
				salida.write(1+"\n");
			}else{
				salida.write(0+"\n");
			}
		}
		salida.flush();
		fos.close();
		return f.toString();
	}
	
	public static List<ElemBuyList> readFileShopping(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		List<ElemBuyList> list = new ArrayList<ElemBuyList>();
		ElemBuyList a;
		String title = salida.readLine();
		a = new ElemBuyList(0, title, 0, 0, 0);
		list.add(a);
		String name;
        while((name=salida.readLine())!=null){
        	int num = Integer.parseInt(salida.readLine());
        	int ch = Integer.parseInt(salida.readLine());
        	a = new ElemBuyList(0, name, ch, num, 0);
        	list.add(a);
        }
		fis.close();
		return list;
	}
	
	/**********************************************************************/
}