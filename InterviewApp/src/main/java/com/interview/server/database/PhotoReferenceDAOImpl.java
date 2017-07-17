package com.interview.server.database;

import java.util.List;

import javax.persistence.Query;

import com.interview.shared.PhotoReference;
import com.interview.shared.PlaceDetails;

public class PhotoReferenceDAOImpl extends GenericCRUDManagerImpl<PlaceDetails, String> implements PhotoReferenceDAO {

	@Override
	public List<PhotoReference> getPhotosForPlaces(String placeID) {
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM photo_reference WHERE place_details_id = '" + placeID + "'", PhotoReference.class);

		List<PhotoReference> resultList = query.getResultList();
		return resultList;
	}

}
