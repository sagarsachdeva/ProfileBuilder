package edu.tcd.userprofiling.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import edu.tcd.userprofiling.bean.UserProfile;
import edu.tcd.userprofiling.profilebuilder.UserProfileBuilder;

@Singleton
@Path("/profile")
public class ProfilingController {

	private List<UserProfile> userProfiles;

	private static UserProfileBuilder userProfileBuilder = new UserProfileBuilder();
	

	public ProfilingController() {
		userProfiles = userProfileBuilder.buildUserProfiles();
	}

	@GET
	@Produces("text/html")
	public Response getLocalCust() {

		if (userProfiles == null)
			return Response.status(200).entity("0").build();

		String output = String.valueOf(userProfiles.size());
		return Response.status(200).entity(output).build();
//		return Response.status(200).entity("hi").build();
	}

}
