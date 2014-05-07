package com.urjc.noteprototype.recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class CopiarImagenAsyncTask extends AsyncTask<String, Void, String> {

	private ImageView imageView;

	public CopiarImagenAsyncTask(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected String doInBackground(String... params) {
		String srFile = params[0];
		String dtFile = params[1];
		String pathAppImg = params[2];
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
			return dtFile;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		imageView.setImageBitmap(HandlerBitmap.decodeScaledBitmapFromSdCard(result, 250, 250));
	}

}
