package com.urjc.noteprototype;

import android.annotation.SuppressLint;
import java.util.Locale;

public class Option implements Comparable<Option> {
	private String name;
	private String data;
	private String path;

	public Option(String n, String d, String p) {
		name = n;
		data = d;
		path = p;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}

	public String getPath() {
		return path;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int compareTo(Option o) {
		if (this.name != null) {
			Locale loc = Locale.getDefault();
			return this.name.toLowerCase(loc).compareTo(
					o.getName().toLowerCase());
		} else
			throw new IllegalArgumentException();
	}
}
