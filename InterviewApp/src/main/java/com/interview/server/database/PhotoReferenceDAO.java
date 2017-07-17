package com.interview.server.database;

import java.util.List;

import com.interview.shared.PhotoReference;

public interface PhotoReferenceDAO {
	public List<PhotoReference> getPhotosForPlaces(String placeID);
}
