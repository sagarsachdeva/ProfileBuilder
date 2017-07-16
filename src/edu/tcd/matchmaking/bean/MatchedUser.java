package edu.tcd.matchmaking.bean;

import edu.tcd.repositorycrawler.bean.User;

public class MatchedUser {
	private User user;

	private Double distance;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
