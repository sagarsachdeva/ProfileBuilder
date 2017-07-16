package edu.tcd.userprofiling.profilebuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import edu.tcd.userprofiling.scoring.ScoreAssigner;

public class UserProfileBuilder {

	private static UserDAO userDAO = new UserDAO();

	private static RepositoryDAO repoDAO = new RepositoryDAO();

	private static ReactionDAO reactionDAO = new ReactionDAO();

	private static UserCommentDAO userCommentDAO = new UserCommentDAO();

	private static UserCommitDAO userCommitDAO = new UserCommitDAO();

	private static UserIssueDAO userIssueDAO = new UserIssueDAO();

	private static UserTypedRepositoryDAO userTypedRepositoryDAO = new UserTypedRepositoryDAO();

	private static RepoLanguageDAO repoLanguageDAO = new RepoLanguageDAO();

	private static List<String> fetchedRepoIdList = new ArrayList<String>();

	private static ScoreAssigner scoreAssigner = new ScoreAssigner();

	public static List<UserProfile> userProfiles;

	static {
		try {
			userProfiles = buildUserProfiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<UserProfile> buildUserProfiles() {
		List<UserProfile> userprofiles = new ArrayList<UserProfile>();

		List<User> users = userDAO.getAllUser();
//		int count = 0;
		for (User user : users) {
			// if (!user.getId().equals("66577"))
			// continue;
//			if (count == 100) {
//				break;
//			}
			UserProfile userProfile = new UserProfile();

			userProfile.setUser(user);

			userProfile.setStarredRepositories(fetchTypedRepositories(user, RepositoryType.Starred.name()));
			userProfile.setOwnedRepositories(fetchTypedRepositories(user, RepositoryType.Owned.name()));

			fetchOtherRepositories(userProfile, user, userTypedRepositoryDAO.getOtherRepositories(user.getId()));

			scoreAssigner.assignScore(userProfile);

			userprofiles.add(userProfile);
//			count++;
			// break;
		}
		return userprofiles;
	}

	private static void fetchOtherRepositories(UserProfile userProfile, User user, List<Repository> repositories) {
		List<UserTypedRepository> userOtherRepositories = new ArrayList<UserTypedRepository>();

		List<Issue> userOpenedIssues = userIssueDAO.getUserIssuesByType(user.getId(), IssueAction.opened.name());
		List<Issue> userReOpenedIssues = userIssueDAO.getUserIssuesByType(user.getId(), IssueAction.reopened.name());
		List<Issue> userClosedIssues = userIssueDAO.getUserIssuesByType(user.getId(), IssueAction.closed.name());

		List<Issue> userNewOpenedIssues = getNotFetchedIssues(userProfile, userOpenedIssues, IssueAction.opened.name());
		addOtherRepoIssues(userNewOpenedIssues, IssueAction.opened.name(), userOtherRepositories);

		List<Issue> userNewReOpenedIssues = getNotFetchedIssues(userProfile, userReOpenedIssues,
				IssueAction.reopened.name());
		addOtherRepoIssues(userNewReOpenedIssues, IssueAction.reopened.name(), userOtherRepositories);

		List<Issue> userNewClosedIssues = getNotFetchedIssues(userProfile, userClosedIssues, IssueAction.closed.name());
		addOtherRepoIssues(userNewClosedIssues, IssueAction.closed.name(), userOtherRepositories);

		List<IssueComment> userComments = userCommentDAO.getUserComments(user.getId());
		List<IssueComment> userNewComments = getNotFetchedComments(userProfile, userComments);
		addOtherRepoComments(userNewComments, userOtherRepositories);

		List<Commit> userCommits = userCommitDAO.getUserCommits(user.getId());
		List<Commit> userNewCommits = getNotFetchedCommits(userProfile, userCommits);
		addOtherRepoCommits(userNewCommits, userOtherRepositories);
		for (UserTypedRepository otherRepo : userOtherRepositories) {
			String repoId = otherRepo.getRepository().getId();
			otherRepo.setRepoLanguages(repoLanguageDAO.getLanguageByRepoId(repoId));
		}

		userProfile.setOtherRepositories(userOtherRepositories);
	}

	private static void addOtherRepoCommits(List<Commit> userNewCommits,
			List<UserTypedRepository> userOtherRepositories) {
		for (Commit commit : userNewCommits) {
			UserTypedRepository existingRepo = null;
			UserCommit userCommit = new UserCommit();
			userCommit.setCommit(commit);
			userCommit.setModifications(userCommitDAO.getModificationByCommitId(commit.getId()));

			for (UserTypedRepository repo : userOtherRepositories) {
				if (commit.getRepoId().equals(repo.getRepository().getId())) {
					repo.getRepoCommits().add(userCommit);
					existingRepo = repo;
					break;
				}
			}
			if (existingRepo != null)
				continue;

			existingRepo = new UserTypedRepository();
			existingRepo.setRepository(repoDAO.getRepositoryById(commit.getRepoId()));
			existingRepo.getRepoCommits().add(userCommit);
			userOtherRepositories.add(existingRepo);
		}

	}

	private static List<Commit> getNotFetchedCommits(UserProfile userProfile, List<Commit> userCommits) {
		List<Commit> userNewCommits = new ArrayList<Commit>();
		List<String> userExistingCommitsId = new ArrayList<String>();

		for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
			for (UserCommit commit : repo.getRepoCommits()) {
				userExistingCommitsId.add(commit.getCommit().getId());
			}
		}
		for (UserTypedRepository repo : userProfile.getStarredRepositories()) {
			for (UserCommit commit : repo.getRepoCommits()) {
				userExistingCommitsId.add(commit.getCommit().getId());
			}
		}

		for (Commit commit : userCommits) {
			if (userExistingCommitsId.contains(commit.getId()))
				continue;

			userNewCommits.add(commit);
		}
		return userNewCommits;
	}

	private static void addOtherRepoComments(List<IssueComment> userNewComments,
			List<UserTypedRepository> userOtherRepositories) {
		for (IssueComment comment : userNewComments) {
			UserTypedRepository existingRepo = null;
			UserComment userComment = new UserComment();
			userComment.setComment(comment);
			for (UserTypedRepository repo : userOtherRepositories) {
				if (comment.getRepoId().equals(repo.getRepository().getId())) {
					repo.getRepoComments().add(userComment);
					existingRepo = repo;
					break;
				}
			}
			if (existingRepo != null)
				continue;

			existingRepo = new UserTypedRepository();
			existingRepo.setRepository(repoDAO.getRepositoryById(comment.getRepoId()));
			existingRepo.getRepoComments().add(userComment);
			userOtherRepositories.add(existingRepo);
		}
	}

	private static List<IssueComment> getNotFetchedComments(UserProfile userProfile, List<IssueComment> userComments) {
		List<IssueComment> userNewComments = new ArrayList<IssueComment>();
		List<String> userExistingCommentsId = new ArrayList<String>();

		for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
			for (UserComment comment : repo.getRepoComments()) {
				userExistingCommentsId.add(comment.getComment().getId());
			}
		}
		for (UserTypedRepository repo : userProfile.getStarredRepositories()) {
			for (UserComment comment : repo.getRepoComments()) {
				userExistingCommentsId.add(comment.getComment().getId());
			}
		}

		for (IssueComment comment : userComments) {
			if (userExistingCommentsId.contains(comment.getId()))
				continue;

			userNewComments.add(comment);
		}
		return userNewComments;
	}

	private static void addOtherRepoIssues(List<Issue> userNewIssues, String type,
			List<UserTypedRepository> userOtherRepositories) {

		for (Issue issue : userNewIssues) {
			UserTypedRepository existingRepo = null;
			UserIssue userIssue = new UserIssue();
			userIssue.setIssue(issue);
			for (UserTypedRepository repo : userOtherRepositories) {
				if (issue.getRepoId().equals(repo.getRepository().getId())) {

					if (type.equals(IssueAction.opened.name())) {
						repo.getRepoOpenedIssues().add(userIssue);
					} else if (type.equals(IssueAction.reopened.name())) {
						repo.getRepoReOpenedIssues().add(userIssue);
					} else if (type.equals(IssueAction.closed.name())) {
						repo.getRepoClosedIssues().add(userIssue);
					}

					existingRepo = repo;
					break;
				}
			}

			if (existingRepo != null)
				continue;
			existingRepo = new UserTypedRepository();
			existingRepo.setRepository(repoDAO.getRepositoryById(issue.getRepoId()));
			existingRepo.getRepoOpenedIssues().add(userIssue);
			userOtherRepositories.add(existingRepo);
		}

	}

	private static List<Issue> getNotFetchedIssues(UserProfile userProfile, List<Issue> userIssues, String type) {
		List<Issue> userNewIssues = new ArrayList<Issue>();
		List<String> userExistingIssuesId = new ArrayList<String>();
		if (type.equals(IssueAction.opened.name())) {
			for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
				for (UserIssue userIssue : repo.getRepoOpenedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}

			for (UserTypedRepository repo : userProfile.getStarredRepositories()) {
				for (UserIssue userIssue : repo.getRepoOpenedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}
		}

		else if (type.equals(IssueAction.reopened.name())) {
			for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
				for (UserIssue userIssue : repo.getRepoReOpenedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}

			for (UserTypedRepository repo : userProfile.getStarredRepositories()) {
				for (UserIssue userIssue : repo.getRepoReOpenedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}
		}

		else if (type.equals(IssueAction.closed.name())) {
			for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
				for (UserIssue userIssue : repo.getRepoClosedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}

			for (UserTypedRepository repo : userProfile.getStarredRepositories()) {
				for (UserIssue userIssue : repo.getRepoClosedIssues()) {
					userExistingIssuesId.add(userIssue.getIssue().getId());
				}
			}
		}

		for (Issue issue : userIssues) {
			if (userExistingIssuesId.contains(issue.getId()))
				continue;

			userNewIssues.add(issue);
		}
		return userNewIssues;
	}

	private static List<UserTypedRepository> fetchTypedRepositories(User user, String type) {
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

			fetchedRepoIdList.add(repository.getId());

			userTypedRepositories.add(userTypedRepository);
		}

		return userTypedRepositories;
	}

	private static List<UserCommit> fetchRepoCommits(User user, Repository repository) {
		List<UserCommit> userCommits = new ArrayList<UserCommit>();

		List<Commit> commits = userCommitDAO.getUserCommitsByRepo(user.getId(), repository.getId());

		for (Commit commit : commits) {
			UserCommit userCommit = new UserCommit();

			userCommit.setCommit(commit);
			userCommit.setModifications(userCommitDAO.getModificationByCommitId(commit.getId()));

			userCommits.add(userCommit);
		}
		return userCommits;
	}

	private static List<UserComment> fetchRepoIssueComments(User user, Repository repository) {
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

	private static List<UserIssue> fetchRepoIssues(User user, Repository repository, String action) {
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
		Date d = new Date();
		buildUserProfiles();
		// List<UserProfile> userProfiles = builder.buildUserProfiles();
		// int count = 0;
		// for (UserProfile userProfile : userProfiles) {
		// for (UserTypedRepository repo : userProfile.getOtherRepositories()) {
		// count += repo.getRepoOpenedIssues().size();
		// }
		// System.out.println("other Issues opened : " + count);
		// count = 0;
		//
		// for (UserTypedRepository repo : userProfile.getOwnedRepositories()) {
		// count += repo.getRepoOpenedIssues().size();
		// }
		// System.out.println("owned Issues opened : " + count);
		// count = 0;
		//
		// for (UserTypedRepository repo : userProfile.getStarredRepositories())
		// {
		// count += repo.getRepoOpenedIssues().size();
		// }
		// System.out.println("starred Issues opened : " + count);
		// count = 0;
		// }
		System.out.println(d);
		System.out.println(new Date());
	}

	public static List<UserProfile> getUserProfiles() {
		return userProfiles;
	}

}
