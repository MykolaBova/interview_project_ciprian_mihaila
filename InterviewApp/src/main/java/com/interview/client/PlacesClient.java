package com.interview.client;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.interview.shared.Place;
import com.interview.shared.PlaceDetails;

@Path("/api/places")
public interface PlacesClient extends RestService {
	@GET
	@Path("/{city}")
	public void getPlaces(@PathParam("city") String city, MethodCallback<List<Place>> callback);

	@GET
	@Path("/details/{placeId}")
	public void getPlaceDetails(@PathParam("placeId") String placeId, MethodCallback<PlaceDetails> callback);

	@POST
	@Path("/place/save")
	public void savePlace(Place place, MethodCallback<Place> callback);

	@POST
	@Path("/placedetails/save")
	public void savePlaceDetails(PlaceDetails placeDetails, MethodCallback<PlaceDetails> callback);

	@DELETE
	@Path("/delete/{cityName}/{placeId}")
	public void deletePlace(@PathParam("cityName") String cityName, @PathParam("placeId") String placeId,
			MethodCallback<List<Place>> callback);
}
