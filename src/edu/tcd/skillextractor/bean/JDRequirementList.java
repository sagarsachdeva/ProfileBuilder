package edu.tcd.skillextractor.bean;

public class JDRequirementList {

	private String requirementString;

	private String taggedRequirementString;

	private int lineIndex;

	private int status;

	public String getRequirementString() {
		return requirementString;
	}

	public void setRequirementString(String requirementString) {
		this.requirementString = requirementString;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTaggedRequirementString() {
		return taggedRequirementString;
	}

	public void setTaggedRequirementString(String taggedRequirementString) {
		this.taggedRequirementString = taggedRequirementString;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}
}
