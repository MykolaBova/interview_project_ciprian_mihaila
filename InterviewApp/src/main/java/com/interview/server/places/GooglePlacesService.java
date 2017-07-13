package com.interview.server.places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.interview.shared.Place;


public class GooglePlacesService {
	private static final String API_KEY = "AIzaSyAQhlhowY8CxaIZHA4JrTVnOLuO1FrduI0";
	
	private static int RADIUS = 5;
    
	private GeoApiContext context;
	
	public GooglePlacesService() {
		context = new GeoApiContext().setApiKey(API_KEY);
	}
	
	private LatLng findCityLocation(String cityName) throws ApiException, InterruptedException, IOException{
		TextSearchRequest textSearchRequest = new TextSearchRequest(context);
		textSearchRequest.query(cityName);
		PlacesSearchResponse response = textSearchRequest.await();
		if (response.results.length == 0){
			return null;
		}
		return response.results[0].geometry.location;
	}
	
	private List<Place> getPlaces(LatLng location, int radius) throws ApiException, InterruptedException, IOException{
		List<Place> places = new ArrayList<>();
		NearbySearchRequest nearByRequest = new NearbySearchRequest(context);
		nearByRequest.location(location);
		nearByRequest.radius(radius);
		PlacesSearchResponse response = nearByRequest.await();
		
		for (PlacesSearchResult placeResult : response.results){
			places.add(new Place(placeResult.placeId, placeResult.name, placeResult.geometry.location.toString()));
		}
		
		return places;
	}
	
	public List<Place> getPlaces(String cityName){
		List<Place> places = new ArrayList<>();
		try {
			LatLng cityLocation = findCityLocation(cityName);
			if (cityLocation != null){
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