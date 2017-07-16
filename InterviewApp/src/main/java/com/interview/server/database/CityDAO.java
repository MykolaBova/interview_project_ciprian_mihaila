package com.interview.server.database;

import com.interview.shared.City;

public interface CityDAO {
	public void saveCity(City city);

	public City findCity(String cityName);
}
