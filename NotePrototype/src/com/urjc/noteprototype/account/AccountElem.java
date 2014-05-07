package com.urjc.noteprototype.account;

public class AccountElem {
	private long id;
	private String tag;
	private float num;
	private long idAcc;

	public AccountElem() {
		super();
	}

	public AccountElem(long id, String tag, float num, long idAcc) {
		super();
		this.id = id;
		this.tag = tag;
		this.num = num;
		this.idAcc = idAcc;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public float getNum() {
		return num;
	}

	public void setNum(float num) {
		this.num = num;
	}

	public long getIdAcc() {
		return idAcc;
	}

	public void setIdAcc(long idAcc) {
		this.idAcc = idAcc;
	}

}
