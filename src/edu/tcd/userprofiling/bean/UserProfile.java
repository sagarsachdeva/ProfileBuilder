package edu.tcd.userprofiling.bean;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.User;

public class UserProfile {

	private List<UserTypedRepository> starredRepositories;

	private List<UserTypedRepository> ownedRepositories;

	private List<UserTypedRepository> otherRepositories;

	private List<UserSkill> skills;

	private double popularityScore;

	private User user;

	public UserProfile() {
		skills = new ArrayList<UserSkill>();
	}

	public List<UserTypedRepository> getStarredRepositories() {
		return starredRepositories;
	}

	public void setStarredRepositories(List<UserTypedRepository> starredRepositories) {
		this.starredRepositories = starredRepositories;
	}

	public List<UserTypedRepository> getOwnedRepositories() {
		return ownedRepositories;
	}

	public void setOwnedRepositories(List<UserTypedRepository> ownedRepositories) {
		this.ownedRepositories = ownedRepositories;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserTypedRepository> getOtherRepositories() {
		return otherRepositories;
	}

	public void setOtherRepositories(List<UserTypedRepository> otherRepositories) {
		this.otherRepositories = otherRepositories;
	}

	public List<UserSkill> getSkills() {
		return skills;
	}

	public void setSkills(List<UserSkill> skills) {
		this.skills = skills;
	}

	public double getPopularityScore() {
		return popularityScore;
	}

	public void setPopularityScore(double popularityScore) {
		this.popularityScore = popularityScore;
	}
}
