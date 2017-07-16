package edu.tcd.matchmaking;

import java.util.Comparator;

import edu.tcd.matchmaking.bean.MatchedUser;

public class ScoreComparator implements Comparator<MatchedUser> {

	@Override
	public int compare(MatchedUser user1, MatchedUser user2) {
		return user1.getDistance().compareTo(user2.getDistance());
	}

}
