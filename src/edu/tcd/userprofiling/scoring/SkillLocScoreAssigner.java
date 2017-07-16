package edu.tcd.userprofiling.scoring;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.CommitModification;
import edu.tcd.userprofiling.bean.SkillWithTotalLocChange;
import edu.tcd.userprofiling.bean.UserCommit;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.bean.UserSkill;
import edu.tcd.userprofiling.bean.UserTypedRepository;
import edu.tcd.userprofiling.dao.SkillExtensionDAO;
import edu.tcd.userprofiling.profilebuilder.Constant;

public class SkillLocScoreAssigner {

	private SkillExtensionDAO skillExtensionDao;

	public SkillLocScoreAssigner(SkillExtensionDAO skillExtensionDao) {
		this.skillExtensionDao = skillExtensionDao;
	}

	public void assignScore(UserProfile userProfile) {
		List<SkillWithTotalLocChange> skillWithTotalLocChanges = skillExtensionDao.getSkillsWithLocChanges();

		List<UserTypedRepository> allRepos = new ArrayList<UserTypedRepository>();
		allRepos.addAll(userProfile.getStarredRepositories());
		allRepos.addAll(userProfile.getOwnedRepositories());
		allRepos.addAll(userProfile.getOtherRepositories());

		for (UserTypedRepository repo : allRepos) {
			for (UserCommit commit : repo.getRepoCommits()) {
				for (CommitModification modification : commit.getModifications()) {
					String skill = skillExtensionDao.getExtensionSkillMap().get(modification.getExtension());
					if (skill == null)
						continue;

					if (skill.equals("Other"))
						continue;

					if (updateExistingUserSkill(skill, userProfile, modification))
						continue;

					UserSkill userSkill = new UserSkill();
					userSkill.setName(skill);
					userSkill.setLocChanged(modification.getLinesChanged());
					userProfile.getSkills().add(userSkill);
				}
			}
		}

		for (UserSkill userSkill : userProfile.getSkills()) {
			for (SkillWithTotalLocChange skill : skillWithTotalLocChanges) {
				if (userSkill.getName().equals(skill.getSkill())) {
					userSkill.setLocScore(calculateLocScore(userSkill, skill));
					break;
				}
			}
		}

	}

	private double calculateLocScore(UserSkill userSkill, SkillWithTotalLocChange skill) {
		double meanLocChange = (double) skill.getTotalLocChanged() / (double) skill.getUserCount();
		double score = ((double) userSkill.getLocChanged() / (double) skill.getTotalLocChanged()) * 10;
		double finalScore = 0;

		if (userSkill.getLocChanged() < meanLocChange)
			finalScore = Constant.BASE_LOC_SCORE + score;

		else if (userSkill.getLocChanged() >= meanLocChange)
			finalScore = Constant.INTERMEDIATE_LOC_SCORE + score;

		if (finalScore > 10)
			finalScore = 10;

		return finalScore;
	}

	private boolean updateExistingUserSkill(String skill, UserProfile userProfile, CommitModification modification) {
		for (UserSkill userSkill : userProfile.getSkills()) {
			if (userSkill.getName().equals(skill)) {
				userSkill.setLocChanged(userSkill.getLocChanged() + modification.getLinesChanged());
				return true;
			}
		}
		return false;

	}

}
