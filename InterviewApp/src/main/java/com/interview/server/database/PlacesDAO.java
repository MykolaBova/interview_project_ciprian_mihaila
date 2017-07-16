package com.interview.server.database;

import java.util.List;

import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public interface PlacesDAO {
	public void savePlaces(List<Place> places);

	public void updatePlace(String placeId, PlaceDetails placeDetails);

	public List<Place> getPlaces(String cityName);

	public void updatePlaces(List<Place> places);

	public void updatePlace(Place newPlace);

	public void deletePlace(String placeId);

}
