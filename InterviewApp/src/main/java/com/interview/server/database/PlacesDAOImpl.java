package com.interview.server.database;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

public class PlacesDAOImpl extends GenericCRUDManagerImpl<Place, String> implements PlacesDAO {

	@Override
	public void savePlaces(List<Place> places) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		for (Place place : places) {
			create(place);
		}
		transaction.commit();
	}

	@Override
	public void updatePlace(String placeId, PlaceDetails placeDetails) {
		Place place = read(placeId);
		place.setPlaceDetails(placeDetails);
		update(place);
	}

	@Override
	public List<Place> getPlaces(String cityName) {

		// TypedQuery<Place>
		// Query query = entityManager.createNativeQuery("SELECT p FROM Place p
		// WHERE p.CITY_NAME = '" + cityName + "'",
		// Place.class);
		Query query = entityManager.createNativeQuery("SELECT * FROM Place WHERE CITY_NAME = '" + cityName + "'",
				Place.class);

		System.out.println(query.toString());

		List<Place> resultList = query.getResultList();
		return resultList;
	}

	@Override
	public void updatePlaces(List<Place> places) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		for (Place place : places) {
			Place p = read(place.getPlaceId());
			if (p != null) {
				p.setName(place.getName());
				p.setDirtyFlag(place.isDirtyFlag());
				p.setLocation(place.getLocation());
				p.setPlaceDetails(place.getPlaceDetails());
			}
		}
		transaction.commit();
	}

	@Override
	public void updatePlace(Place newPlace) {
		Place existingPlace = read(newPlace.getPlaceId());
		entityManager.getTransaction().begin();
		existingPlace.setDirtyFlag(newPlace.isDirtyFlag());
		existingPlace.setLocation(newPlace.getLocation());
		existingPlace.setName(newPlace.getName());
		existingPlace.setPlaceDetails(newPlace.getPlaceDetails());
		entityManager.getTransaction().commit();
	}

	@Override
	public void deletePlace(String placeId) {
		entityManager.getTransaction().begin();
		Place place = read(placeId);
		if (place != null) {
			delete(place);
		}
		entityManager.getTransaction().commit();
	}

}
