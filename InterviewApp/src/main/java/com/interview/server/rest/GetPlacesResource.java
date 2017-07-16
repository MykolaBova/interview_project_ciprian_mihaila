package com.interview.server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.maps.errors.ApiException;
import com.interview.server.database.CityDAO;
import com.interview.server.database.CityDAOImpl;
import com.interview.server.database.PlaceDetailsDAO;
import com.interview.server.database.PlaceDetailsDAOImpl;
import com.interview.server.database.PlacesDAO;
import com.interview.server.database.PlacesDAOImpl;
import com.interview.server.places.GooglePlacesService;
import com.interview.shared.City;
import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

@Path("places")
public class GetPlacesResource {

	@GET
	@Path("/{city}")
	@Produces("application/json")
	public List<Place> getPlaces(@PathParam("city") String cityName) {
		System.out.println("here " + cityName);

		CityDAO cityDAO = new CityDAOImpl();
		PlacesDAO placesDao = new PlacesDAOImpl();

		City city = cityDAO.findCity(cityName);

		List<Place> googleServicePlaces = GooglePlacesService.getInstance().getPlaces(cityName);

		if (city == null) { // new city and places
			City newCity = new City(cityName);
			cityDAO.saveCity(newCity);

			for (Place place : googleServicePlaces) {
				place.setCity(newCity);
				place.setDirtyFlag(false);
			}
			placesDao.savePlaces(googleServicePlaces);
		} else {
			List<Place> localPlaces = placesDao.getPlaces(cityName);
			List<Place> placesToUpdate = new ArrayList<>();
			for (Place gPlace : googleServicePlaces) {
				for (Place lPlace : localPlaces) {
					if (lPlace.getPlaceId().equals(gPlace.getPlaceId()) && !lPlace.isDirtyFlag()) {
						placesToUpdate.add(gPlace);
					}
				}
			}
			placesDao.updatePlaces(placesToUpdate);
		}

		return placesDao.getPlaces(cityName);
	}

	@GET
	@Path("/details/{placeId}")
	@Produces("application/json")
	public PlaceDetails getPlaceDetails(@PathParam("placeId") String placeId) {
		PlacesDAO placesDAO = new PlacesDAOImpl();
		PlaceDetailsDAO placesDetailsDAO = new PlaceDetailsDAOImpl();

		PlaceDetails placeDetails = null;
		try {
			placeDetails = placesDetailsDAO.getPlaceDetails(placeId);
			if (placeDetails == null) {
				PlaceDetails googlePlaceDetails = GooglePlacesService.getInstance().getPlaceDetails(placeId);
				placesDetailsDAO.savePlaceDetails(googlePlaceDetails);
				placesDAO.updatePlace(placeId, googlePlaceDetails);
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

	@DELETE
	@Path("/delete/{placeId}")
	public String deletePlace(@PathParam("placeId") String placeId) {
		PlacesDAO placesDAO = new PlacesDAOImpl();
		PlaceDetailsDAO placesDetailsDAO = new PlaceDetailsDAOImpl();

		placesDetailsDAO.deletePlaceDetails(placeId);
		placesDAO.deletePlace(placeId);

		return "deleted";
	}

}
