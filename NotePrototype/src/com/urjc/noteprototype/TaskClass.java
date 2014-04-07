package com.urjc.noteprototype;

public class TaskClass {

	protected Boolean check;
	protected String name;
	protected long id;

	public TaskClass(int check, String name, long id) {
		super();
		this.check = (check == 1);
		this.name = name;
		this.id = id;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
