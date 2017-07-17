package com.interview.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.interview.server.database.CityDAO;
import com.interview.server.database.PlaceDetailsDAO;
import com.interview.server.database.PlacesDAO;
import com.interview.server.places.GooglePlacesService;
import com.interview.shared.City;
import com.interview.shared.Place;

@Path("places")
public class PlacesServices {

	@Inject
	private CityDAO cityDAO;
	@Inject
	private PlacesDAO placesDAO;
	@Inject
	private PlaceDetailsDAO placesDetailsDAO;

	@GET
	@Path("/{city}")
	@Produces("application/json")
	public List<Place> getPlaces(@PathParam("city") String cityName) {
		City city = cityDAO.findCity(cityName);

		List<Place> googleServicePlaces = GooglePlacesService.getInstance().getPlaces(cityName);

		if (city == null) { // new city and places
			City newCity = new City(cityName);
			cityDAO.saveCity(newCity);

			for (Place place : googleServicePlaces) {
				place.setCity(newCity);
				place.setDirtyFlag(false);
			}
			placesDAO.savePlaces(googleServicePlaces);
		} else {
			List<Place> localPlaces = placesDAO.getPlaces(cityName);
			List<Place> placesToUpdate = new ArrayList<>();
			List<Place> newPlaces = new ArrayList<>();
			for (Place gPlace : googleServicePlaces) {
				boolean saved = false;
				for (Place lPlace : localPlaces) {
					if (lPlace.getPlaceId().equals(gPlace.getPlaceId())) {
						saved = true;
						if (!lPlace.isDirtyFlag()) {
							placesToUpdate.add(gPlace);
						}
					}
				}
				if (!saved) {
					gPlace.setCity(city);
					gPlace.setDirtyFlag(false);
					newPlaces.add(gPlace);
				}
			}
			placesDAO.savePlaces(newPlaces);
			placesDAO.updatePlaces(placesToUpdate);
		}

		return placesDAO.getPlaces(cityName);
	}

	@POST
	@Path("/place/save")
	@Consumes("application/json")
	@Produces("application/json")
	public Place savePlace(Place place) {
		place.setDirtyFlag(true);
		placesDAO.updatePlace(place);
		return place;
	}

	@DELETE
	@Path("/delete/{placeId}/{cityName}")
	public List<Place> deletePlace(@PathParam("cityName") String cityName, @PathParam("placeId") String placeId) {
		if (placesDetailsDAO.getPlaceDetails(placeId) != null) {
			placesDetailsDAO.deletePlaceDetails(placeId);
		}
		placesDAO.deletePlace(placeId);
		return placesDAO.getPlaces(cityName);
	}

}
