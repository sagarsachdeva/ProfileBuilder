package edu.tcd.userprofiling.bean;

import java.util.ArrayList;
import java.util.List;

import edu.tcd.repositorycrawler.bean.RepoLanguage;
import edu.tcd.repositorycrawler.bean.Repository;

public class UserTypedRepository {

	private Repository repository;

	private List<UserCommit> repoCommits;

	private List<UserComment> repoComments;

	private List<UserIssue> repoOpenedIssues;

	private List<UserIssue> repoReOpenedIssues;

	private List<UserIssue> repoClosedIssues;

	private List<RepoLanguage> repoLanguages;

	public UserTypedRepository() {
		repoCommits = new ArrayList<UserCommit>();
		repoComments = new ArrayList<UserComment>();
		repoOpenedIssues = new ArrayList<UserIssue>();
		repoReOpenedIssues = new ArrayList<UserIssue>();
		repoClosedIssues = new ArrayList<UserIssue>();
		repoLanguages = new ArrayList<RepoLanguage>();
	}

	public List<UserComment> getRepoComments() {
		return repoComments;
	}

	public void setRepoComments(List<UserComment> repoComments) {
		this.repoComments = repoComments;
	}

	public List<UserIssue> getRepoOpenedIssues() {
		return repoOpenedIssues;
	}

	public void setRepoOpenedIssues(List<UserIssue> repoOpenedIssues) {
		this.repoOpenedIssues = repoOpenedIssues;
	}

	public List<UserIssue> getRepoReOpenedIssues() {
		return repoReOpenedIssues;
	}

	public void setRepoReOpenedIssues(List<UserIssue> repoReOpenedIssues) {
		this.repoReOpenedIssues = repoReOpenedIssues;
	}

	public List<UserIssue> getRepoClosedIssues() {
		return repoClosedIssues;
	}

	public void setRepoClosedIssues(List<UserIssue> repoClosedIssues) {
		this.repoClosedIssues = repoClosedIssues;
	}

	public List<RepoLanguage> getRepoLanguages() {
		return repoLanguages;
	}

	public void setRepoLanguages(List<RepoLanguage> repoLanguages) {
		this.repoLanguages = repoLanguages;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public List<UserCommit> getRepoCommits() {
		return repoCommits;
	}

	public void setRepoCommits(List<UserCommit> repoCommits) {
		this.repoCommits = repoCommits;
	}

}
