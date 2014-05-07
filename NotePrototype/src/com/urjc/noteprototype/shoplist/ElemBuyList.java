package com.urjc.noteprototype.shoplist;

public class ElemBuyList {

	protected Boolean check;
	protected int amount;
	protected String name;
	protected long id;
	protected long idB;

	public ElemBuyList(long id, String name, int checkNum, int amount, long idB2) {
		super();
		this.check = (checkNum == 1);
		this.amount = amount;
		this.name = name;
		this.id = id;
		this.idB = idB2;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	public long getIdB() {
		return idB;
	}

	public void setIdB(long idB) {
		this.idB = idB;
	}

}
