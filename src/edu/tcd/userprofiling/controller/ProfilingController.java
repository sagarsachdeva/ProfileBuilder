package edu.tcd.userprofiling.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

import edu.tcd.repositorycrawler.dao.UserDAO;
import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.profilebuilder.UserProfileBuilder;

@Singleton
@Path("/profile")
public class ProfilingController {

	private List<UserProfile> userProfiles;

	private static UserProfileBuilder userProfileBuilder = new UserProfileBuilder();

	private UserDAO userDAO = new UserDAO();

	private Gson gson = new Gson();

	public ProfilingController() {
		userProfiles = userProfileBuilder.buildUserProfiles();
	}

	@GET
	@Path("/getUserProfiles")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserProfiles() {
		return gson.toJson(userProfiles);

	}

	@GET
	@Path("/getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllUsers() {
		return gson.toJson(userDAO.getAllUser());
	}

	@GET
	@Path("/getUserProfile/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserProfile(@PathParam("id") String id) {
		for (UserProfile profile : userProfiles) {
			if (profile.getUser().getId().equals(id))
				return gson.toJson(profile);
		}
		return gson.toJson(null);
	}
}
