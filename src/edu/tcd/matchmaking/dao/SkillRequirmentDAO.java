package edu.tcd.matchmaking.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import edu.tcd.matchmaking.bean.SkillRequirement;
import edu.tcd.repositorycrawler.hibernate.HibernateUtil;

public class SkillRequirmentDAO {

	public List<SkillRequirement> fetchSkillRequirement() {
		List<SkillRequirement> skillRequirements = new ArrayList<SkillRequirement>();
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			String query = "select * from skill_requirement";
			@SuppressWarnings("unchecked")
			List<Object[]> rows = session.createSQLQuery(query).list();
			for (Object[] row : rows) {
				SkillRequirement skillRequirement = new SkillRequirement();
				skillRequirement.setId(row[0].toString());
				skillRequirement.setSkill(row[1].toString());
				skillRequirement.setRequirements(Arrays.asList(row[2].toString().split(",")));

				skillRequirements.add(skillRequirement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen())
				session.close();
		}

		return skillRequirements;
	}

}
