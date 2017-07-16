package edu.tcd.matchmaking.bean;

import java.util.List;

public class SkillRequirement {

	private String id;

	private String skill;

	private List<String> requirements;

	private double reqScore;

	private int mappedRequirementCount;

	public SkillRequirement() {
	}

	public SkillRequirement(String skill) {
		this.skill = skill;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public List<String> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<String> requirements) {
		this.requirements = requirements;
	}

	public double getReqScore() {
		return reqScore;
	}

	public void setReqScore(double reqScore) {
		this.reqScore = reqScore;
	}

	public int getMappedRequirementCount() {
		return mappedRequirementCount;
	}

	public void setMappedRequirementCount(int mappedRequirementCount) {
		this.mappedRequirementCount = mappedRequirementCount;
	}

	public static void clearSkillRequirementScore(List<SkillRequirement> requirements) {
		for (SkillRequirement req : requirements) {
			req.setReqScore(0);
		}
	}

}
