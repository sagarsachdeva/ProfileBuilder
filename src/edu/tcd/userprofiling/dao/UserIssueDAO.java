package edu.tcd.userprofiling.dao;

import java.util.List;

import org.hibernate.Session;

import edu.tcd.repositorycrawler.bean.Issue;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class UserIssueDAO {

	@SuppressWarnings("unchecked")
	public List<Issue> getUserIssuesByRepoAndType(String userId, String repoId, String action) {
		List<Issue> userIssues = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			userIssues = session.createQuery("from Issue where userId=:userId and repoId=:repoId and action=:action")
					.setParameter("userId", userId).setParameter("repoId", repoId).setParameter("action", action)
					.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return userIssues;

	}
}
