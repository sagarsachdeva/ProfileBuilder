package edu.tcd.userprofiling.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import edu.tcd.repositorycrawler.bean.Repository;
import edu.tcd.repositorycrawler.bean.UserRepo;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class UserTypedRepositoryDAO {

	@SuppressWarnings("unchecked")
	public List<UserRepo> getUserReposByType(String userId, String type) {
		List<UserRepo> userRepos = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			userRepos = session.createQuery("from UserRepo where userId=:userId and type=:type")
					.setParameter("userId", userId).setParameter("type", type).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return userRepos;

	}

	@SuppressWarnings("unchecked")
	public List<Repository> getOtherRepositories(String userId) {
		List<Repository> repositories = new ArrayList<Repository>();
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			repositories = session
					.createQuery("from Repository where id not in (select repoId from UserRepo where userId=:userId)")
					.setParameter("userId", userId).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return repositories;
	}
}
