package edu.tcd.userprofiling.scoring;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.CommitModification;
import edu.tcd.repositorycrawler.bean.RepoLanguage;
import edu.tcd.userprofiling.bean.UserCommit;
import edu.tcd.userprofiling.bean.UserIssue;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.bean.UserSkill;
import edu.tcd.userprofiling.bean.UserTypedRepository;
import edu.tcd.userprofiling.dao.SkillExtensionDAO;

public class SkillIssuesScoreAssigner {
	private SkillExtensionDAO skillExtensionDao;

	public SkillIssuesScoreAssigner(SkillExtensionDAO skillExtensionDao) {
		this.skillExtensionDao = skillExtensionDao;
	}

	public void assignScore(UserProfile userProfile) {
		List<UserTypedRepository> allRepos = new ArrayList<UserTypedRepository>();
		allRepos.addAll(userProfile.getStarredRepositories());
		allRepos.addAll(userProfile.getOwnedRepositories());
		allRepos.addAll(userProfile.getOtherRepositories());

		for (UserTypedRepository repo : allRepos) {
			List<UserSkill> closedIssueSkills = new ArrayList<UserSkill>();

			if (repo.getRepoClosedIssues().size() == 0)
				continue;

			if (repo.getRepoLanguages() == null) {
				continue;
				
			}
			for (RepoLanguage language : repo.getRepoLanguages()) {
				UserSkill userSkill = new UserSkill();
				userSkill.setName(language.getName().toLowerCase());
				closedIssueSkills.add(userSkill);
			}

			fetchUserSkills(repo.getRepoClosedIssues(), repo.getRepoCommits(), closedIssueSkills);

			for (UserSkill closedIssueSkill : closedIssueSkills) {
				boolean skillFound = false;
				for (UserSkill userSkill : userProfile.getSkills()) {
					if (userSkill.getName().equalsIgnoreCase(closedIssueSkill.getName())) {
						userSkill.setIssueFixedScore(
								userSkill.getIssueFixedScore() + closedIssueSkill.getIssueFixedScore());

						if (userSkill.getIssueFixedScore() > 10)
							userSkill.setIssueFixedScore(10);

						skillFound = true;
						break;
					}
				}
				if (skillFound)
					continue;

				UserSkill userSkill = new UserSkill();
				userSkill.setName(closedIssueSkill.getName());
				userSkill.setIssueFixedScore(closedIssueSkill.getIssueFixedScore());
				userProfile.getSkills().add(userSkill);
			}

		}
	}

	private void fetchUserSkills(List<UserIssue> repoClosedIssues, List<UserCommit> commits,
			List<UserSkill> closedIssueSkills) {
		int score = 0;

		for (UserIssue issue : repoClosedIssues) {
			score += 1;

			if (issue.getIssue().getNoOfComments() > 3)
				score += 1;
		}

		if (score > 10)
			score = 10;

		for (UserSkill skill : closedIssueSkills) {
			skill.setIssueFixedScore(score);
		}

		for (UserCommit commit : commits) {
			for (CommitModification modification : commit.getModifications()) {
				String skill = skillExtensionDao.getExtensionSkillMap().get(modification.getExtension());

				if (skill.equalsIgnoreCase("Other"))
					continue;

				if (isExistingUserSkill(skill, closedIssueSkills, score))
					continue;

				UserSkill userSkill = new UserSkill();
				userSkill.setName(skill);
				userSkill.setIssueFixedScore(score);
				closedIssueSkills.add(userSkill);
			}
		}
	}

	private boolean isExistingUserSkill(String skill, List<UserSkill> closedIssueSkills, int score) {
		for (UserSkill userSkill : closedIssueSkills) {
			if (userSkill.getName().equalsIgnoreCase(skill)) {
				return true;
			}
		}
		return false;
	}
}
