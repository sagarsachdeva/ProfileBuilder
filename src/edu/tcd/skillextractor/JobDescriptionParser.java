package edu.tcd.skillextractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.tcd.skillextractor.bean.JDRequirementList;
import edu.tcd.skillextractor.bean.Requirement;
import edu.tcd.skillextractor.bean.Skill;

public class JobDescriptionParser {

	public List<Requirement> parseJD(List<String> jobDesc, ServletContext servletContext) {
		SkillLoader skillLoader = new SkillLoader();

		List<Requirement> availSkills = skillLoader.loadAllRequirements(servletContext.getRealPath(Constant.skillFile));

		SkillFinder skillFinder = new SkillFinder();
		List<Requirement> requirements = skillFinder.findSkills(availSkills, jobDesc, servletContext);

		skillFinder.assignLineScores(requirements, jobDesc);

		List<String> newReqList = skillLoader.removeStopChars(jobDesc);

		MaxentTagger tagger = null;
		try {
			tagger = new MaxentTagger(servletContext.getRealPath(Constant.taggerFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<JDRequirementList> jdReqList = tagAndFetchJDRequirements(tagger, newReqList);

		QuantifiableExperienceExtractor expExtractor = new QuantifiableExperienceExtractor();
		expExtractor.scoreQuantifiableExperienceSkill(jdReqList, requirements);

		KeywordBasedScore keywordBasedScore = new KeywordBasedScore();
		keywordBasedScore.assignScoreBasedOnKeyword(jdReqList, requirements,servletContext);

		for (Requirement requirement : requirements) {
			for (Skill skill : requirement.getSkills()) {
				double lineScore = skill.getLineScore() == null ? 0.0 : skill.getLineScore();
				double preferenceScore = skill.getPreferenceScore() == null ? 0.0 : skill.getPreferenceScore();
				skill.setFinalScore(((lineScore + preferenceScore) * 10) / 8);
			}
		}

		return requirements;
	}

	private static List<JDRequirementList> tagAndFetchJDRequirements(MaxentTagger tagger, List<String> newReqList) {
		List<JDRequirementList> jdRequirementList = new ArrayList<JDRequirementList>();
		int i = 0;
		for (String req : newReqList) {
			JDRequirementList jdReq = new JDRequirementList();
			jdReq.setRequirementString(req);
			jdReq.setTaggedRequirementString(tagger.tagString(replaceWordWithInt(req)).toLowerCase().trim());
			jdReq.setLineIndex(i);
			jdRequirementList.add(jdReq);
			i++;
		}

		return jdRequirementList;
	}

	private static String replaceWordWithInt(String req) {
		String[] words = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" };
		req = req.toLowerCase();
		for (int i = 0; i < words.length; i++) {
			if (patternMatches(req, words[i])) {
				req = req.replaceAll("\\b(" + words[i] + ")\\b", String.valueOf(i + 1));
			}
		}
		return req;
	}

	private static boolean patternMatches(String reqString, String Skill) {
		String regex = "\\b(" + Skill + ")\\b";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(reqString);
		return matcher.find();
	}
}
