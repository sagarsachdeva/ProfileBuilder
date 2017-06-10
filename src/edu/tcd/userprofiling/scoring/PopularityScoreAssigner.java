package edu.tcd.userprofiling.scoring;

import java.util.List;

import edu.tcd.repositorycrawler.bean.User;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.dao.UserProfileDAO;

public class PopularityScoreAssigner {

	private List<User> sortedUsers;

	private UserProfileDAO userProfileDAO;

	public PopularityScoreAssigner() {
		userProfileDAO = new UserProfileDAO();
		sortedUsers = userProfileDAO.getSortedUsersList();
	}

	public void assignScore(UserProfile userProfile) {
		int index = 1;
		for (User user : sortedUsers) {
			if (user.getId().equals(userProfile.getUser().getId())) {
				break;
			}
			index++;
		}
		userProfile.setPopularityScore(((double) index / (double) sortedUsers.size()) * 10);
	}
}
