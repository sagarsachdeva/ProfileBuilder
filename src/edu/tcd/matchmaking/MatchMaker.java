package edu.tcd.matchmaking;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import edu.tcd.matchmaking.bean.MatchedUser;
import edu.tcd.matchmaking.bean.SkillRequirement;
import edu.tcd.matchmaking.dao.SkillRequirmentDAO;
import edu.tcd.skillextractor.JobDescriptionParser;
import edu.tcd.skillextractor.bean.Requirement;
import edu.tcd.skillextractor.bean.Skill;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.profilebuilder.UserProfileBuilder;

public class MatchMaker {

	private JobDescriptionParser jobDescriptionParser = new JobDescriptionParser();

	private SkillRequirmentDAO skillRequirmentDAO = new SkillRequirmentDAO();

	private List<SkillRequirement> skillRequirements;

	private List<UserProfile> userProfiles;

	private MatchMakingAlgorithm matchMakingAlgorithm;

	public MatchMaker() {
		skillRequirements = skillRequirmentDAO.fetchSkillRequirement();
		userProfiles = UserProfileBuilder.getUserProfiles();
		matchMakingAlgorithm = new MatchMakingAlgorithm();

	}

	public List<MatchedUser> findMatches(List<String> jd, ServletContext servletContext) {

		List<Requirement> requirements = jobDescriptionParser.parseJD(jd, servletContext);

		List<SkillRequirement> reqToBeProcessed = fetchRequirementsToBeProcessed(skillRequirements, requirements);

		return matchMakingAlgorithm.matchMakingAlgorithm(reqToBeProcessed, userProfiles);
	}

	private List<SkillRequirement> fetchRequirementsToBeProcessed(List<SkillRequirement> skillRequirements,
			List<Requirement> requirements) {
		List<SkillRequirement> reqToBeProcessed = new ArrayList<SkillRequirement>();

		for (Requirement req : requirements) {
			for (Skill skill : req.getSkills()) {
				if (skill.getSkillName().equalsIgnoreCase("c")) {
					System.out.println();
				}
				for (SkillRequirement skillRequirement : skillRequirements) {
					if (skillRequirement.getRequirements().contains(skill.getSkillName().toLowerCase())) {
						if (!checkIfRequirementExists(skill, reqToBeProcessed, skillRequirement)) {

							SkillRequirement reqSkill = new SkillRequirement();
							reqSkill.setId(skillRequirement.getId());
							reqSkill.setSkill(skillRequirement.getSkill());
							reqSkill.setReqScore(skill.getFinalScore());
							reqSkill.setMappedRequirementCount(1);

							reqToBeProcessed.add(reqSkill);
						}
						break;
					}
				}
			}
		}

		for (SkillRequirement reqSkill : reqToBeProcessed) {
			reqSkill.setReqScore(reqSkill.getReqScore() / reqSkill.getMappedRequirementCount());
		}

		return reqToBeProcessed;
	}

	private boolean checkIfRequirementExists(Skill skill, List<SkillRequirement> reqToBeProcessed,
			SkillRequirement skillRequirement) {
		for (SkillRequirement req : reqToBeProcessed) {
			if (req.getSkill().equalsIgnoreCase(skillRequirement.getSkill())) {
				req.setReqScore(skill.getFinalScore() + req.getReqScore());
				req.setMappedRequirementCount(req.getMappedRequirementCount() + 1);
				return true;
			}
		}
		return false;
	}

}
