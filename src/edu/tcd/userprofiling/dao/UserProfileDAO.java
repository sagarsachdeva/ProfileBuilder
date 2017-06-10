package edu.tcd.userprofiling.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.tcd.repositorycrawler.bean.User;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class UserProfileDAO {

	@SuppressWarnings("unchecked")
	public List<User> getSortedUsersList() {
		List<User> users = new ArrayList<User>();
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from User order by numberOfFollowers");
			users = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return users;

	}
}
