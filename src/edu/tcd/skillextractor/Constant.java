package edu.tcd.skillextractor;

public class Constant {

	public static String expAmountPatternMatcher = "experience,cd/nn/experience:cd/nns/experience:cd/nn/in/experience:cd/nns/in/experience:experience/in/cd/nns:cd/nns/in/jj/nn/experience:cd/nns/in/nn/experience:cd/nns/nn/nn/nn:cd/nns/jj/experience:cd/nns/pos/jj/experience:cd/cc/jjr/nns/pos/experience:cd/cc/jjr/nns/in/experience:cd/nns/pos/experience";

	public static String[] specialSymbolsInSkill = { "(", ")", ".", "-", ",", "/", ";" };

	public static String skillFile = "resources/skills.csv";

	public static String specialCharSkillFile = "resources/specialCharSkills.csv";
	
	public static String scoredKeywordsFile = "resources/scoredKeywords";
	
	public static String taggerFile = "resources/taggers/left3words-wsj-0-18.tagger";
}
