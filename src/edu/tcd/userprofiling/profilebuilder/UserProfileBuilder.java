package edu.tcd.userprofiling.profilebuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.tcd.repositorycrawler.bean.Commit;
import edu.tcd.repositorycrawler.bean.Issue;
import edu.tcd.repositorycrawler.bean.IssueComment;
import edu.tcd.repositorycrawler.bean.Repository;
import edu.tcd.repositorycrawler.bean.User;
import edu.tcd.repositorycrawler.bean.UserRepo;
import edu.tcd.repositorycrawler.dao.ReactionDAO;
import edu.tcd.repositorycrawler.dao.RepoLanguageDAO;
import edu.tcd.repositorycrawler.dao.RepositoryDAO;
import edu.tcd.repositorycrawler.dao.UserDAO;
import edu.tcd.repositorycrawler.util.Constants.IssueAction;
import edu.tcd.repositorycrawler.util.Constants.ReactionType;
import edu.tcd.repositorycrawler.util.Constants.RepositoryType;
import edu.tcd.userprofiling.bean.UserComment;
import edu.tcd.userprofiling.bean.UserCommit;
import edu.tcd.userprofiling.bean.UserIssue;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.bean.UserTypedRepository;
import edu.tcd.userprofiling.dao.UserCommentDAO;
import edu.tcd.userprofiling.dao.UserCommitDAO;
import edu.tcd.userprofiling.dao.UserIssueDAO;
import edu.tcd.userprofiling.dao.UserTypedRepositoryDAO;

public class UserProfileBuilder {

	private static UserDAO userDAO = new UserDAO();

	private static RepositoryDAO repoDAO = new RepositoryDAO();

	private static ReactionDAO reactionDAO = new ReactionDAO();

	private static UserCommentDAO userCommentDAO = new UserCommentDAO();

	private static UserCommitDAO userCommitDAO = new UserCommitDAO();

	private static UserIssueDAO userIssueDAO = new UserIssueDAO();

	private static UserTypedRepositoryDAO userTypedRepositoryDAO = new UserTypedRepositoryDAO();

	private static RepoLanguageDAO repoLanguageDAO = new RepoLanguageDAO();

	public List<UserProfile> buildUserProfiles() {
		List<UserProfile> userprofiles = new ArrayList<UserProfile>();

		List<User> users = userDAO.getAllUser();

		for (User user : users) {
			UserProfile userProfile = new UserProfile();

			userProfile.setUser(user);

			userProfile.setStarredRepositories(fetchTypedRepositories(user, RepositoryType.Starred.name()));
			userProfile.setOwnedRepositories(fetchTypedRepositories(user, RepositoryType.Owned.name()));

			userProfile
					.setOtherRepositories(fetchOtherRepositories(user, userTypedRepositoryDAO.getOtherRepositories()));

			userprofiles.add(userProfile);
		}
		return userprofiles;
	}

	private List<UserTypedRepository> fetchOtherRepositories(User user, List<Repository> repositories) {
		List<UserTypedRepository> userOtherRepositories = new ArrayList<UserTypedRepository>();

		for (Repository repository : repositories) {
			boolean hasRelation = false;
			UserTypedRepository otherRepository = new UserTypedRepository();

			List<UserIssue> openedIssues = fetchRepoIssues(user, repository, IssueAction.opened.name());
			if (openedIssues.size() > 0) {
				hasRelation = true;
				otherRepository.setRepoOpenedIssues(openedIssues);
			}

			List<UserIssue> reoOpenedIssues = fetchRepoIssues(user, repository, IssueAction.reopened.name());
			if (reoOpenedIssues.size() > 0) {
				hasRelation = true;
				otherRepository.setRepoReOpenedIssues(reoOpenedIssues);
			}

			List<UserIssue> closedIssues = fetchRepoIssues(user, repository, IssueAction.closed.name());
			if (closedIssues.size() > 0) {
				hasRelation = true;
				otherRepository.setRepoClosedIssues(closedIssues);
			}

			List<UserComment> userComments = fetchRepoIssueComments(user, repository);
			if (userComments.size() > 0) {
				hasRelation = true;
				otherRepository.setRepoComments(userComments);
			}

			List<UserCommit> userCommits = fetchRepoCommits(user, repository);
			if (userCommits.size() > 0) {
				hasRelation = true;
				otherRepository.setRepoCommits(userCommits);
			}

			if (hasRelation) {
				otherRepository.setRepoLanguages(repoLanguageDAO.getLanguageByRepoId(repository.getId()));
				userOtherRepositories.add(otherRepository);
			}

		}
		return userOtherRepositories;
	}

	private List<UserTypedRepository> fetchTypedRepositories(User user, String type) {
		List<UserTypedRepository> userTypedRepositories = new ArrayList<UserTypedRepository>();

		List<UserRepo> userRepos = userTypedRepositoryDAO.getUserReposByType(user.getId(), type);

		for (UserRepo userRepo : userRepos) {
			UserTypedRepository userTypedRepository = new UserTypedRepository();
			Repository repository = repoDAO.getRepositoryById(userRepo.getRepoId());

			userTypedRepository.setRepository(repository);

			userTypedRepository.setRepoOpenedIssues((fetchRepoIssues(user, repository, IssueAction.opened.name())));
			userTypedRepository.setRepoReOpenedIssues((fetchRepoIssues(user, repository, IssueAction.reopened.name())));
			userTypedRepository.setRepoClosedIssues((fetchRepoIssues(user, repository, IssueAction.closed.name())));

			userTypedRepository.setRepoComments(fetchRepoIssueComments(user, repository));

			userTypedRepository.setRepoCommits(fetchRepoCommits(user, repository));

			userTypedRepository.setRepoLanguages(repoLanguageDAO.getLanguageByRepoId(repository.getId()));

			userTypedRepositories.add(userTypedRepository);
		}

		return userTypedRepositories;
	}

	private List<UserCommit> fetchRepoCommits(User user, Repository repository) {
		List<UserCommit> userCommits = new ArrayList<UserCommit>();

		List<Commit> commits = userCommitDAO.getCommitsByRepo(user.getId(), repository.getId());

		for (Commit commit : commits) {
			UserCommit userCommit = new UserCommit();

			userCommit.setCommit(commit);
			userCommit.setModifications(userCommitDAO.getModificationByCommitId(commit.getId()));

			userCommits.add(userCommit);
		}
		return userCommits;
	}

	private List<UserComment> fetchRepoIssueComments(User user, Repository repository) {
		List<UserComment> userComments = new ArrayList<UserComment>();

		List<IssueComment> issueComments = userCommentDAO.getUserCommentsByRepo(user.getId(), repository.getId());

		for (IssueComment comment : issueComments) {
			UserComment userComment = new UserComment();
			userComment.setComment(comment);
			userComment.setReactions(reactionDAO.getReactionByType(comment.getId(), ReactionType.IssueComment.name()));

			userComments.add(userComment);
		}
		return userComments;
	}

	private List<UserIssue> fetchRepoIssues(User user, Repository repository, String action) {
		List<UserIssue> userIssues = new ArrayList<UserIssue>();

		List<Issue> issues = userIssueDAO.getUserIssuesByRepoAndType(user.getId(), repository.getId(), action);

		for (Issue issue : issues) {
			UserIssue userIssue = new UserIssue();
			userIssue.setIssue(issue);
			userIssue.setReactions(reactionDAO.getReactionByType(issue.getId(), ReactionType.Issue.name()));

			userIssues.add(userIssue);
		}
		return userIssues;
	}

	public static void main(String[] args) {
		UserProfileBuilder builder = new UserProfileBuilder();
		List<UserProfile> profiles = builder.buildUserProfiles();
		List<String> idList = new ArrayList<String>();
		Set<String> idSet = new LinkedHashSet<String>();
		for (UserProfile profile : profiles) {
			for (UserTypedRepository repo : profile.getOtherRepositories()) {
				for (UserCommit commit : repo.getRepoCommits()) {
					idList.add(commit.getCommit().getId());
					idSet.add(commit.getCommit().getId());
				}
			}

			for (UserTypedRepository repo : profile.getOwnedRepositories()) {
				for (UserCommit commit : repo.getRepoCommits()) {
					idList.add(commit.getCommit().getId());
					idSet.add(commit.getCommit().getId());
				}
			}

			for (UserTypedRepository repo : profile.getStarredRepositories()) {
				for (UserCommit commit : repo.getRepoCommits()) {
					idList.add(commit.getCommit().getId());
					idSet.add(commit.getCommit().getId());
				}
			}
		}
		System.out.println();
		System.out.println("Total commits = " + idList.size());
		System.out.println("Total unique commits = " + idSet.size());
	}
}
