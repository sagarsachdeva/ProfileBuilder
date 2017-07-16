package edu.tcd.skillextractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import edu.tcd.skillextractor.bean.Requirement;
import edu.tcd.skillextractor.bean.Skill;

public class SkillFinder {

	private List<Requirement> foundRequirements;

	public SkillFinder() {
		foundRequirements = new ArrayList<Requirement>();
	}

	public List<Requirement> findSkills(List<Requirement> requirements, List<String> jobDesc,
			ServletContext servletContext) {
		SkillLoader skillLoader = new SkillLoader();
		List<Requirement> specialCharSkillReq = skillLoader
				.loadAllRequirements(servletContext.getRealPath(Constant.specialCharSkillFile));
		int i = 0;
		for (String reqString : jobDesc) {
			reqString = fetchSpecialCharSkillRequirements(specialCharSkillReq, reqString.trim(), i);
			fetchRequirements(requirements, (reqString).trim(), i);
			i++;
		}
		return foundRequirements;
	}

	public void assignLineScores(List<Requirement> requirements, List<String> reqList) {
		int lineCount = reqList.size();
		for (Requirement requirement : requirements) {
			for (Skill skill : requirement.getSkills()) {
				int score = lineCount - skill.getLineNo();
				double lineScore = (3.0 * score) / lineCount;
				skill.setLineScore(lineScore);
			}
		}
	}

	private String fetchSpecialCharSkillRequirements(List<Requirement> requirements, String reqString, int i) {
		for (Requirement requirement : requirements) {
			for (Skill skill : requirement.getSkills()) {
				if (skill.getStatus() == 1) {
					reqString = reqString.replace(skill.getSkillName(), "");
					continue;
				}

				if (reqString.contains(skill.getSkillName())) {
					reqString = reqString.replace(skill.getSkillName(), "");
					skill.setStatus(1);
					skill.setLineNo(i);
					addSkilltoFoundRequirements(skill, requirement);
				}
			}
		}
		return reqString;
	}

	private void fetchRequirements(List<Requirement> requirements, String reqString, int i) {
		for (Requirement requirement : requirements) {
			for (Skill skill : requirement.getSkills()) {
				if (skill.getStatus() == 1)
					continue;

				Skill foundSkill = fillSkillStatus(skill, reqString, i);
				if (foundSkill != null)
					addSkilltoFoundRequirements(foundSkill, requirement);
			}
		}
	}

	private void addSkilltoFoundRequirements(Skill foundSkill, Requirement requirement) {
		for (Requirement existingReq : foundRequirements) {
			if (existingReq.getCategoryName().equals(requirement.getCategoryName())) {
				existingReq.getSkills().add(foundSkill);
				return;
			}
		}

		Requirement req = new Requirement();
		req.setCategoryName(requirement.getCategoryName());
		List<Skill> skills = new ArrayList<Skill>();
		skills.add(foundSkill);
		req.setSkills(skills);
		foundRequirements.add(req);
	}

	private Skill fillSkillStatus(Skill skill, String reqString, int i) {
		String skillName = skill.getSkillName();
		Skill foundSkill = new Skill();
		if (skillName.contains("/")) {
			String[] dupSkills = skillName.split("/");
			String chosenSkill = "";

			for (String dupSkill : dupSkills) {
				if (!patternMatches(reqString, dupSkill))
					continue;

				if (dupSkill.split(" ").length > chosenSkill.length()) {
					chosenSkill = dupSkill;
				}
			}
			if (chosenSkill.isEmpty())
				return null;

			foundSkill.setSkillName(chosenSkill);
			foundSkill.setStatus(1);
			skill.setStatus(1);
			foundSkill.setLineNo(i);
			return foundSkill;
		}

		if (!patternMatches(reqString, skillName))
			return null;

		foundSkill.setSkillName(skill.getSkillName());
		foundSkill.setStatus(1);
		foundSkill.setLineNo(i);
		skill.setStatus(1);
		return foundSkill;
	}

	private boolean patternMatches(String reqString, String skill) {
		String regex = "\\b" + skill + "\\b";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(reqString);
		return matcher.find();
	}
}
