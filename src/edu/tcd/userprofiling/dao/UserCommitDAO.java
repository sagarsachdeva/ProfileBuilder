package edu.tcd.userprofiling.dao;

import java.util.List;

import org.hibernate.Session;

import edu.tcd.repositorycrawler.bean.Commit;
import edu.tcd.repositorycrawler.bean.CommitModification;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class UserCommitDAO {

	@SuppressWarnings("unchecked")
	public List<Commit> getCommitsByRepo(String authorId, String repoId) {
		List<Commit> userCommits = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			userCommits = session.createQuery("from Commit where authorId= :authorId and repoId = :repoId")
					.setParameter("authorId", authorId).setParameter("repoId", repoId).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return userCommits;
	}

	@SuppressWarnings("unchecked")
	public List<CommitModification> getModificationByCommitId(String commitId) {
		List<CommitModification> commitModifications = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			commitModifications = session.createQuery("from CommitModification where commitId=:commitId")
					.setParameter("commitId", commitId).list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return commitModifications;
	}
}
