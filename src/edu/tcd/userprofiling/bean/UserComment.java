package edu.tcd.userprofiling.bean;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.IssueComment;
import edu.tcd.repositorycrawler.bean.Reaction;

public class UserComment {

	private IssueComment comment;

	private List<Reaction> reactions;

	public UserComment() {
		reactions = new ArrayList<Reaction>();
	}

	public IssueComment getComment() {
		return comment;
	}

	public void setComment(IssueComment comment) {
		this.comment = comment;
	}

	public List<Reaction> getReactions() {
		return reactions;
	}

	public void setReactions(List<Reaction> reactions) {
		this.reactions = reactions;
	}
}
