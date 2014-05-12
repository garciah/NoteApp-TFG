package com.urjc.noteprototype.pwd;

public class PwdClass {
	private String title;
	private String user;
	private String pwd;
	private String url;
	private long id;

	public PwdClass(String title, String user, String pwd, String url, long id) {
		super();
		this.title = title;
		this.user = user;
		this.pwd = pwd;
		this.url = url;
		this.id = id;
	}

	public PwdClass() {
		super();
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
