package edu.tcd.matchmaking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tcd.matchmaking.bean.MatchedUser;
import edu.tcd.matchmaking.bean.SkillRequirement;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.bean.UserSkill;

public class MatchMakingAlgorithm {

	public List<MatchedUser> matchMakingAlgorithm(List<SkillRequirement> reqToBeProcessed,
			List<UserProfile> userProfiles) {
		List<MatchedUser> matchedUsers = new ArrayList<MatchedUser>();
		Map<String, Double> userScoreMap = new HashMap<String, Double>();
		List<SkillRequirement> matchingSkills = new ArrayList<SkillRequirement>();
		for (SkillRequirement skillRequirement : reqToBeProcessed) {
			matchingSkills.add(new SkillRequirement(skillRequirement.getSkill()));
		}

		double maxDistance = Double.MAX_VALUE;
		for (UserProfile profile : userProfiles) {
			for (UserSkill userSkill : profile.getSkills()) {
				for (SkillRequirement skillRequirement : matchingSkills) {
					if (skillRequirement.getSkill().equalsIgnoreCase(userSkill.getName())) {
						skillRequirement.setReqScore(userSkill.getFinalScore());
					}
				}
			}
			double distance = findEucledianDistance(reqToBeProcessed, matchingSkills);

			if (distance < maxDistance || userScoreMap.size() < 10) {
				updateMatchedUsers(distance, userScoreMap, profile);
				maxDistance = fetchMaxDistance(userScoreMap);
			}

			SkillRequirement.clearSkillRequirementScore(matchingSkills);
		}

		for (UserProfile profile : userProfiles) {
			for (String userId : userScoreMap.keySet()) {
				if (userId.equalsIgnoreCase(profile.getUser().getId())) {
					MatchedUser matchedUser = new MatchedUser();
					matchedUser.setUser(profile.getUser());
					matchedUser.setDistance(userScoreMap.get(userId));
					matchedUsers.add(matchedUser);
					break;
				}
			}
		}
		Collections.sort(matchedUsers, new ScoreComparator());
		return matchedUsers;
	}

	private double fetchMaxDistance(Map<String, Double> userScoreMap) {
		double max = userScoreMap.get(0) == null ? 0 : userScoreMap.get(0);
		for (double d : userScoreMap.values()) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	private void updateMatchedUsers(double distance, Map<String, Double> userScoreMap, UserProfile profile) {
		if (userScoreMap.size() < 10) {
			userScoreMap.put(profile.getUser().getId(), distance);
			return;
		}

		double maxDistance = 0;
		String remUserId = "";
		for (String key : userScoreMap.keySet()) {
			if (userScoreMap.get(key) > maxDistance) {
				maxDistance = userScoreMap.get(key);
				remUserId = key;
			}
		}

		userScoreMap.remove(remUserId);
		userScoreMap.put(profile.getUser().getId(), distance);
	}

	private double findEucledianDistance(List<SkillRequirement> reqToBeProcessed,
			List<SkillRequirement> matchingSkills) {
		int distance = 0;
		for (SkillRequirement skillReq : reqToBeProcessed) {
			for (SkillRequirement matchedReq : matchingSkills) {
				if (matchedReq.getSkill().equalsIgnoreCase(skillReq.getSkill())) {
					distance += Math.pow(skillReq.getReqScore() - matchedReq.getReqScore(), 2);
					break;
				}
			}
		}
		return Math.sqrt(distance);
	}

}
