package edu.tcd.skillextractor.bean;

import java.util.List;

public class ScoredKeyword {

	private double score;

	private List<String> keywords;

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
