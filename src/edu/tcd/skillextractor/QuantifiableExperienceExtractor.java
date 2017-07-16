package edu.tcd.skillextractor;

import java.util.List;

import edu.tcd.skillextractor.bean.JDRequirementList;
import edu.tcd.skillextractor.bean.Pattern;
import edu.tcd.skillextractor.bean.Requirement;
import edu.tcd.skillextractor.bean.Skill;

public class QuantifiableExperienceExtractor {

	public void scoreQuantifiableExperienceSkill(List<JDRequirementList> reqList, List<Requirement> foundSkills) {
		int i = 0;
		String expAmountPattern = Constant.expAmountPatternMatcher;
		String expPattern = expAmountPattern.split(",")[1];
		String keyword = expAmountPattern.split(",")[0];

		for (JDRequirementList jdReq : reqList) {
			String req = jdReq.getTaggedRequirementString();
			Pattern[] patterns = new Pattern[req.split(" ").length];
			fillPatterns(patterns, req);

			int quantity = checkAndReturnQuantity(patterns, expPattern, keyword);

			if (quantity == -1)
				continue;

			scoreBasedOnQuantity(quantity, i, foundSkills);
			jdReq.setStatus(1);
			i++;
		}
	}

	private void scoreBasedOnQuantity(int quantity, int i, List<Requirement> foundSkills) {
		double score = 0;

		if (quantity >= 7) {
			score = 5;
		}

		else if (quantity >= 5) {
			score = 4;
		}

		else if (quantity >= 3) {
			score = 3.5;
		}

		else if (quantity >= 2) {
			score = 2.5;
		}

		else {
			score = 1;
		}

		for (Requirement requirement : foundSkills) {
			for (Skill skill : requirement.getSkills()) {
				if (skill.getLineNo() == i) {
					skill.setPreferenceScore(new Double(score));
				}
			}
		}

	}

	private int checkAndReturnQuantity(Pattern[] patterns, String expPatternString, String keyword) {
		/// cd/nn/experience:/cd/nns/experience:/cd/nn/in/experience:/cd/nns/in/experience
		int keywordPos = -1;

		for (Pattern pattern : patterns) {
			if (pattern.getWord().equals(keyword)) {
				keywordPos = pattern.getPosition();
				break;
			}
		}

		if (keywordPos == -1)
			return -1;

		String[] expPatterns = expPatternString.split(":");
		for (String expPattern : expPatterns) {

			int tagVal = -1;
			String[] tags = expPattern.split("/");
			int keyworkPosInTags = -1;

			for (int k = 0; k < tags.length; k++) {
				if (tags[k].equals(keyword)) {
					keyworkPosInTags = k;
					break;
				}
			}

			if (keywordPos < keyworkPosInTags)
				continue;

			int tagsLen = tags.length;
			int i = keywordPos - keyworkPosInTags, j = 0;

			for (; j < tagsLen; i++, j++) {
				if (tags[j].equals(keyword))
					continue;

				// Added for pattern[i] getting null
				if (i >= patterns.length)
					break;

				if (!patterns[i].getPosTag().equals(tags[j])) {
					break;
				}

				if (tags[j].equals("cd")) {
					tagVal = Integer.parseInt(patterns[i].getWord());
				}

			}

			if (j != tagsLen)
				continue;

			return tagVal;

		}

		return -1;
	}

	private void fillPatterns(Pattern[] patterns, String req) {
		String[] vals = req.split(" ");
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = new Pattern();
			patterns[i].setWord(vals[i].split("/")[0]);
			patterns[i].setPosTag(vals[i].split("/")[1]);
			patterns[i].setPosition(i);
		}
	}

	public static void main(String[] args) {
		String req = "having/VBG 25/CD years/NNS of/IN experience/NN in/IN core/NN java/NN java/NN ee/NN and/CC javascript/JJ css/NN mvc/NN technologies/NNS "
				.toLowerCase();
		Pattern[] patterns = new Pattern[req.split(" ").length];
		QuantifiableExperienceExtractor ex = new QuantifiableExperienceExtractor();
		ex.fillPatterns(patterns, req);
		String expAmountPattern = Constant.expAmountPatternMatcher;
		String expPattern = expAmountPattern.split(",")[1];
		String keyword = expAmountPattern.split(",")[0];
		int quantity = ex.checkAndReturnQuantity(patterns, expPattern, keyword);
		System.out.println(quantity);

	}
}
