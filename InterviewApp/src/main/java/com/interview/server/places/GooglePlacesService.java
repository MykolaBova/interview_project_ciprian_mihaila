package com.interview.server.places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.interview.shared.PhotoReference;
import com.interview.shared.Place;

public class GooglePlacesService {
	private static final String API_KEY = "AIzaSyAQhlhowY8CxaIZHA4JrTVnOLuO1FrduI0";

	private static int RADIUS = 5;

	private GeoApiContext context;

	private static GooglePlacesService instance;

	private GooglePlacesService() {
		context = new GeoApiContext().setApiKey(API_KEY);
	}

	public static GooglePlacesService getInstance() {
		if (instance == null) {
			instance = new GooglePlacesService();
		}
		return instance;
	}

	private LatLng findCityLocation(String cityName) throws ApiException, InterruptedException, IOException {
		TextSearchRequest textSearchRequest = new TextSearchRequest(context);
		textSearchRequest.query(cityName);
		PlacesSearchResponse response = textSearchRequest.await();
		if (response.results.length == 0) {
			return null;
		}
		return response.results[0].geometry.location;
	}

	private List<Place> getPlaces(LatLng location, int radius) throws ApiException, InterruptedException, IOException {
		List<Place> places = new ArrayList<>();
		NearbySearchRequest nearByRequest = new NearbySearchRequest(context);
		nearByRequest.location(location);
		nearByRequest.radius(radius);
		PlacesSearchResponse response = nearByRequest.await();

		for (PlacesSearchResult placeResult : response.results) {
			places.add(new Place(placeResult.placeId, placeResult.name, placeResult.geometry.location.toString()));
		}

		return places;
	}

	public com.interview.shared.PlaceDetails getPlaceDetails(String placeId)
			throws ApiException, InterruptedException, IOException {
		PlaceDetailsRequest placeDetailsRequest = new PlaceDetailsRequest(context);
		placeDetailsRequest.placeId(placeId);
		PlaceDetails result = placeDetailsRequest.await();

		com.interview.shared.PlaceDetails placeDetailsDB = new com.interview.shared.PlaceDetails();
		placeDetailsDB.setPlaceId(result.placeId);
		placeDetailsDB.setAddress(result.formattedAddress);
		placeDetailsDB.setPhone(result.formattedPhoneNumber);
		placeDetailsDB.setIcon(result.icon.toString());

		if (result.photos != null) {
			List<PhotoReference> photoRefs = new ArrayList<>();
			for (Photo photo : result.photos) {
				photoRefs.add(new PhotoReference(photo.photoReference, placeDetailsDB));
			}
			placeDetailsDB.setPhotosReferences(photoRefs);
		}

		return placeDetailsDB;
	}

	public List<Place> getPlaces(String cityName) {
		List<Place> places = new ArrayList<>();
		try {
			LatLng cityLocation = findCityLocation(cityName);
			if (cityLocation != null) {
				return getPlaces(cityLocation, RADIUS);
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
		return places;
	}

}
