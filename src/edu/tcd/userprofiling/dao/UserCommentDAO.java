package edu.tcd.userprofiling.dao;

import java.util.List;

import org.hibernate.Session;

import edu.tcd.repositorycrawler.bean.IssueComment;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class UserCommentDAO {

	@SuppressWarnings("unchecked")
	public List<IssueComment> getUserCommentsByRepo(String userId, String repoId) {
		List<IssueComment> userComments = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			userComments = session.createQuery("from IssueComment where userId= :userId and repoId = :repoId")
					.setParameter("userId", userId).setParameter("repoId", repoId).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return userComments;

	}
	
	@SuppressWarnings("unchecked")
	public List<IssueComment> getUserComments(String userId) {
		List<IssueComment> userComments = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			userComments = session.createQuery("from IssueComment where userId= :userId")
					.setParameter("userId", userId).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return userComments;

	}
}
