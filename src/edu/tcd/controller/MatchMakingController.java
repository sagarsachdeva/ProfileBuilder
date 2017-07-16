package edu.tcd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import edu.tcd.matchmaking.MatchMaker;
import edu.tcd.matchmaking.bean.MatchedUser;

@Path("/match")
public class MatchMakingController {

	MatchMaker matchMaker = new MatchMaker();

	@Context
	ServletContext servletContext;

	@POST
	@Path("/findMatches")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String findMatches(String jd) {
		Gson gson = new Gson();
		 @SuppressWarnings("unchecked")
		List<String> jobDesc = gson.fromJson(jd, ArrayList.class);
		List<MatchedUser> matchedUsers = matchMaker.findMatches(jobDesc, servletContext);

		return gson.toJson(matchedUsers);
	}

	@GET
	@Path("/findMatches")
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchMatches() {
		List<String> jd = new ArrayList<String>();
		jd.add("substantial experience in java");
		jd.add("knowledge of c and c++");
		Gson gson = new Gson();
		return gson.toJson(matchMaker.findMatches(jd, servletContext));
	}
}
