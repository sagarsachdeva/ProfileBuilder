package edu.tcd.userprofiling.bean;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.User;

public class UserProfile {

	private List<UserTypedRepository> starredRepositories;

	private List<UserTypedRepository> ownedRepositories;

	private List<UserTypedRepository> otherRepositories;

	private User user;

	public UserProfile() {
		starredRepositories = new ArrayList<UserTypedRepository>();
		ownedRepositories = new ArrayList<UserTypedRepository>();
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
}
