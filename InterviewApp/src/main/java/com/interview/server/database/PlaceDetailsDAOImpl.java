package com.interview.server.database;

import javax.persistence.EntityTransaction;

import com.interview.shared.PlaceDetails;

public class PlaceDetailsDAOImpl extends GenericCRUDManagerImpl<PlaceDetails, String> implements PlaceDetailsDAO {

	@Override
	public PlaceDetails savePlaceDetails(PlaceDetails placeDetails) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		create(placeDetails);
		transaction.commit();
		return placeDetails;
	}

	@Override
	public PlaceDetails getPlaceDetails(String placeId) {
		return read(placeId);
	}

	@Override
	public void upadtePlaceDetails(PlaceDetails newPlaceDetails) {
		PlaceDetails existingPlace = read(newPlaceDetails.getPlaceId());
		entityManager.getTransaction().begin();
		existingPlace.setAddress(newPlaceDetails.getAddress());
		existingPlace.setPhone(newPlaceDetails.getPhone());
		existingPlace.setPlaceId(newPlaceDetails.getPlaceId());
		existingPlace.setIcon(newPlaceDetails.getIcon());
		entityManager.getTransaction().commit();
	}

	@Override
	public void deletePlaceDetails(String placeId) {
		entityManager.getTransaction().begin();
		PlaceDetails placeDetails = read(placeId);
		delete(placeDetails);
		entityManager.getTransaction().commit();
	}

}
