package edu.tcd.userprofiling.scoring;

import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.bean.UserSkill;
import edu.tcd.userprofiling.dao.SkillExtensionDAO;

public class ScoreAssigner {

	private SkillLocScoreAssigner skillLocScoreAssigner;

	private SkillExtensionDAO skillExtensionDao;

	private SkillIssuesScoreAssigner skillIssuesScoreAssigner;

	private PopularityScoreAssigner popularityScoreAssigner;

	public ScoreAssigner() {
		skillExtensionDao = new SkillExtensionDAO();
		skillLocScoreAssigner = new SkillLocScoreAssigner(skillExtensionDao);
		skillIssuesScoreAssigner = new SkillIssuesScoreAssigner(skillExtensionDao);
		popularityScoreAssigner = new PopularityScoreAssigner();
	}

	public void assignScore(UserProfile userProfile) {
		skillLocScoreAssigner.assignScore(userProfile);
		skillIssuesScoreAssigner.assignScore(userProfile);
		popularityScoreAssigner.assignScore(userProfile);
		assignFinalScore(userProfile);
	}

	private void assignFinalScore(UserProfile userProfile) {
		double popularityScore = userProfile.getPopularityScore();

		for (UserSkill skill : userProfile.getSkills()) {
			double locScore = skill.getLocScore();
			double issueFixedScore = skill.getIssueFixedScore();

			double finalScore = (locScore * 0.5) + (issueFixedScore * 0.3) + (popularityScore * 0.2);

			skill.setFinalScore(finalScore);
		}
	}
}
