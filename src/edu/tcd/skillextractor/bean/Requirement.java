package edu.tcd.skillextractor.bean;

import java.util.List;

public class Requirement {
	private String categoryName;

	private List<Skill> skills;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
}
