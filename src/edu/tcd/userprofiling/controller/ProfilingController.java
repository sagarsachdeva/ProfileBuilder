package edu.tcd.userprofiling.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/profile")
public class ProfilingController {
	@GET
	@Produces("text/html")
	public Response getLocalCust() {

		String output = "I am from 'getLocalCust' method";
		return Response.status(200).entity(output).build();
	}

}
