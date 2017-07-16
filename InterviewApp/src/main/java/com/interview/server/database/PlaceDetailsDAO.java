package com.interview.server.database;

import com.interview.shared.PlaceDetails;

public interface PlaceDetailsDAO {
	public PlaceDetails savePlaceDetails(PlaceDetails placeDetails);

	public PlaceDetails getPlaceDetails(String placeId);

	public void upadtePlaceDetails(PlaceDetails newPlaceDetails);

	public void deletePlaceDetails(String placeId);
}
