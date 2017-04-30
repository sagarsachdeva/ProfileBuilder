package edu.tcd.userprofiling.bean;

import java.util.List;

import edu.tcd.repositorycrawler.bean.Commit;
import edu.tcd.repositorycrawler.bean.CommitModification;

public class UserCommit {

	private Commit commit;

	private List<CommitModification> modifications;

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public List<CommitModification> getModifications() {
		return modifications;
	}

	public void setModifications(List<CommitModification> modifications) {
		this.modifications = modifications;
	}
}
