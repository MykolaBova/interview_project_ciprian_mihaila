package com.interview.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.interview.server.database.PlaceDetailsDAO;
import com.interview.server.database.PlaceDetailsDAOImpl;
import com.interview.server.database.PlacesDAO;
import com.interview.server.database.PlacesDAOImpl;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

@Path("places")
public class PostPlacesResource {
	@POST
	@Path("/place/save")
	@Consumes("application/json")
	@Produces("application/json")
	public Place savePlace(Place place) {
		PlacesDAO placesDAO = new PlacesDAOImpl();
		place.setDirtyFlag(true);
		placesDAO.updatePlace(place);
		return place;
	}

	@POST
	@Path("/placedetails/save")
	@Consumes("application/json")
	@Produces("application/json")
	public PlaceDetails savePlaceDetails(PlaceDetails placeDetails) {
		PlaceDetailsDAO placesDetailsDAO = new PlaceDetailsDAOImpl();
		placesDetailsDAO.upadtePlaceDetails(placeDetails);
		return placeDetails;
	}
}
