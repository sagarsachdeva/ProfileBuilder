package edu.tcd.userprofiling.bean;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.Issue;
import edu.tcd.repositorycrawler.bean.Reaction;

public class UserIssue {

	private Issue issue;

	private List<Reaction> reactions;

	public UserIssue() {
		reactions = new ArrayList<Reaction>();
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public List<Reaction> getReactions() {
		return reactions;
	}

	public void setReactions(List<Reaction> reactions) {
		this.reactions = reactions;
	}

}
