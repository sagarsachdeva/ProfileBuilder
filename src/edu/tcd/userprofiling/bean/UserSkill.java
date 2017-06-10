package edu.tcd.userprofiling.bean;

public class UserSkill {

	private String name;

	private int locChanged;

	private double locScore;

	private double issueFixedScore;

	private double finalScore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLocChanged() {
		return locChanged;
	}

	public void setLocChanged(int locChanged) {
		this.locChanged = locChanged;
	}

	public double getLocScore() {
		return locScore;
	}

	public void setLocScore(double locScore) {
		this.locScore = locScore;
	}

	public double getIssueFixedScore() {
		return issueFixedScore;
	}

	public void setIssueFixedScore(double issueFixedScore) {
		this.issueFixedScore = issueFixedScore;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}

}
