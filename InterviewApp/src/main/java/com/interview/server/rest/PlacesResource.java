package com.interview.server.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.interview.server.places.GooglePlacesService;
import com.interview.shared.Place;

@Path("places")
public class PlacesResource {
	
	@GET
	@Path("/{city}")
	@Produces("application/json")
	public List<Place> getPlaces(@PathParam("city") String city) {
		System.out.println("here" + city);
		GooglePlacesService service = new GooglePlacesService();
		return service.getPlaces(city);
	}
	
}
