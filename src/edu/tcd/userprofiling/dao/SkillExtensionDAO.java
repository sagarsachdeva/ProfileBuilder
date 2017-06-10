package edu.tcd.userprofiling.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.tcd.repositorycrawler.dao.CommitModificationsDAO;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;
import edu.tcd.userprofiling.bean.SkillWithTotalLocChange;

public class SkillExtensionDAO {

	private Map<String, String> extensionSkillMap = new HashMap<String, String>();

	private CommitModificationsDAO commitModificationsDAO;

	public SkillExtensionDAO() {
		commitModificationsDAO = new CommitModificationsDAO();
	}

	public List<SkillWithTotalLocChange> getSkillsWithLocChanges() {
		List<SkillWithTotalLocChange> skillWithTotalLocChanges = new ArrayList<SkillWithTotalLocChange>();

		List<Object[]> extensions = commitModificationsDAO.getExtensionsWithLocChanged();

		List<Object[]> skills = getSkills();

		for (Object[] skill : skills) {
			extensionSkillMap.put(skill[0].toString(), skill[1].toString());

			if (checkIfSkillPresent(skillWithTotalLocChanges, skill[1].toString()))
				continue;

			SkillWithTotalLocChange skillExtension = new SkillWithTotalLocChange();
			skillExtension.setSkill(skill[1].toString());
			skillExtension.setUserCount(getNoOfUsersForSkill(skill[1].toString()));
			skillWithTotalLocChanges.add(skillExtension);
		}

		for (Object[] extension : extensions) {
			String skill = extensionSkillMap.get(extension[0].toString());
			if (skill.equals("Other")) {
				System.out.println();
			}
			for (SkillWithTotalLocChange skillExtension : skillWithTotalLocChanges) {
				if (skillExtension.getSkill().equals(skill)) {
					skillExtension.setTotalLocChanged(
							skillExtension.getTotalLocChanged() + Integer.parseInt(extension[1].toString()));
				}
			}
		}

		return skillWithTotalLocChanges;
	}

	private boolean checkIfSkillPresent(List<SkillWithTotalLocChange> skillWithTotalLocChanges, String skill) {
		for (SkillWithTotalLocChange skillWithTotalLocChange : skillWithTotalLocChanges) {
			if (skillWithTotalLocChange.getSkill().equals(skill.toString())) {
				return true;
			}
		}
		return false;
	}

	private int getNoOfUsersForSkill(String skill) {
		Session session = null;
		Number o = 0;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			String query = "select count(distinct commit.author_id) " + "from commit_modifications,commit "
					+ "where commit.id=commit_modifications.commit_id "
					+ "and extension in (select extension from skill_extension where skill = :skill)";
			o = (Number) session.createSQLQuery(query).setParameter("skill", skill).uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return o.intValue();
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getSkills() {
		Session session = null;
		List<Object[]> o = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			o = session.createSQLQuery("select extension,skill from skill_extension").list();

			if (o == null) {
				session.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}
		return o;
	}

	public static void main(String[] args) {
		SkillExtensionDAO skillExtensionDAO = new SkillExtensionDAO();
		skillExtensionDAO.getNoOfUsersForSkill("java");
	}

	public Map<String, String> getExtensionSkillMap() {
		return extensionSkillMap;
	}
}
