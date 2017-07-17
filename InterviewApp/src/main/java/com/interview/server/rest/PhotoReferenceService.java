package com.interview.server.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.interview.server.database.PhotoReferenceDAO;
import com.interview.shared.PhotoReference;

@Path("places")
public class PhotoReferenceService {
	@Inject
	private PhotoReferenceDAO photoReferenceDAO;

	@GET
	@Path("/photos/{placeId}")
	@Produces("application/json")
	public List<PhotoReference> getPhotos(@PathParam("placeId") String placeId) {
		return photoReferenceDAO.getPhotosForPlaces(placeId);
	}
}
