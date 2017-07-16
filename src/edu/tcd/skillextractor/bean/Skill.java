package edu.tcd.skillextractor.bean;

public class Skill {
	private String skillName;

	private Double lineScore;

	private Double preferenceScore;

	private Double finalScore;

	private int status;

	private int lineNo;

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public Double getLineScore() {
		return lineScore;
	}

	public void setLineScore(Double lineScore) {
		this.lineScore = lineScore;
	}

	public Double getPreferenceScore() {
		return preferenceScore;
	}

	public void setPreferenceScore(Double preferenceScore) {
		this.preferenceScore = preferenceScore;
	}

	public Double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Double finalScore) {
		this.finalScore = finalScore;
	}

}
