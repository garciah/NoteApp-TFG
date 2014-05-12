package com.urjc.noteprototype.recipe;

public class RecipeClass {
	private long id;
	private String title;
	private String ingredients;
	private String instructions;
	private String imageName;

	public RecipeClass() {
		super();
	}

	public RecipeClass(long id, String title, String ingredients,
			String instructions, String imageName) {
		super();
		this.id = id;
		this.title = title;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.imageName = imageName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
