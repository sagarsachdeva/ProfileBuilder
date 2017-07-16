package edu.tcd.skillextractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.tcd.skillextractor.bean.Requirement;
import edu.tcd.skillextractor.bean.Skill;

public class SkillLoader {

	public List<Requirement> loadAllRequirements(String file) {
		List<Requirement> requirements = new ArrayList<Requirement>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				Requirement requirement = new Requirement();
				String[] lineSkill = line.split(",");
				requirement.setCategoryName(lineSkill[0]);
				requirement.setSkills(loadSkills(lineSkill[1]));
				requirements.add(requirement);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requirements;
	}

	public List<String> removeStopChars(List<String> reqList) {
		List<String> newReqList = new ArrayList<String>();
		for (String req : reqList) {
			String newReq = req.replaceAll("[^a-z0-9\\s]", " ");
			newReqList.add(newReq);
		}
		return newReqList;
	}

	private List<Skill> loadSkills(String lineSkill) {
		List<Skill> skills = new ArrayList<Skill>();
		String[] skillsArray = lineSkill.split(":");
		for (String s : skillsArray) {
			if (s.equals("c#")) {
				System.out.println();
			}
			Skill skill = new Skill();
			skill.setSkillName(s);
			skills.add(skill);
		}
		return skills;
	}

	public static void main(String args[]) {
		SkillLoader skillFinder = new SkillLoader();
		String text = "5+ year of experience in java";
		text = text.toLowerCase();
		List<String> reqList = Arrays.asList(text.split("\n"));
		reqList = skillFinder.removeStopChars(reqList);
		System.out.println();

	}

}
