package com.interview.server.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.google.maps.errors.ApiException;
import com.interview.server.database.PlaceDetailsDAO;
import com.interview.server.database.PlacesDAO;
import com.interview.server.places.GooglePlacesService;
import com.interview.shared.PlaceDetails;

@Path("places")
public class PlaceDetailsServices {

	@Inject
	private PlaceDetailsDAO placesDetailsDAO;
	@Inject
	private PlacesDAO placesDAO;

	@POST
	@Path("/placedetails/save")
	@Consumes("application/json")
	@Produces("application/json")
	public PlaceDetails savePlaceDetails(PlaceDetails placeDetails) {
		placesDetailsDAO.upadtePlaceDetails(placeDetails);
		return placeDetails;
	}

	@GET
	@Path("/details/{placeId}")
	@Produces("application/json")
	public PlaceDetails getPlaceDetails(@PathParam("placeId") String placeId) {
		PlaceDetails placeDetails = null;
		try {
			placeDetails = placesDetailsDAO.getPlaceDetails(placeId);
			if (placeDetails == null) {
				PlaceDetails googlePlaceDetails = GooglePlacesService.getInstance().getPlaceDetails(placeId);
				placesDetailsDAO.savePlaceDetails(googlePlaceDetails);
				placesDAO.updatePlace(placeId, googlePlaceDetails);
				placeDetails = googlePlaceDetails;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return placeDetails;
	}
}
