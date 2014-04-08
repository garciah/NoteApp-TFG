package com.urjc.noteprototype;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

public class HandlerFileImportExport {

	public HandlerFileImportExport() {
		super();
	}
	
	public static String determineType(String titleFile){
		String[] aux = titleFile.split("\\_");
		String t = aux[1];
		switch (t) {
		case "_note.nfh":
			t = "Note";
			break;
		default:
			break;
		}
		return t;
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
		salida.write(body);
		salida.flush();
		fos.close();
		return f.toString();
	}

	public static Note readFileNote(String titleFile) throws java.io.IOException {
		FileInputStream fis = new FileInputStream(titleFile);
		InputStreamReader isw = new InputStreamReader(fis, "UTF-8");
		BufferedReader salida = new BufferedReader(isw);
		salida.readLine();
		String title = salida.readLine();
		String body = salida.readLine();
		fis.close();
		Note n = new Note();
		n.setTitle(title);
		n.setBody(body);
		return n;
	}

}
