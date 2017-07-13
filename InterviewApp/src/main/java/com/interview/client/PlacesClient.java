package com.interview.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.interview.shared.Place;

@Path("/api/places")
public interface PlacesClient extends RestService {
	@GET
	@Path("/{city}")
	public void getPlaces(@PathParam("city") String city, MethodCallback<List<Place>> callback);
}
