package com.interview.server.guice;

import com.google.inject.AbstractModule;
import com.interview.server.database.CityDAO;
import com.interview.server.database.CityDAOImpl;
import com.interview.server.database.PhotoReferenceDAO;
import com.interview.server.database.PhotoReferenceDAOImpl;
import com.interview.server.database.PlaceDetailsDAO;
import com.interview.server.database.PlaceDetailsDAOImpl;
import com.interview.server.database.PlacesDAO;
import com.interview.server.database.PlacesDAOImpl;

public class AppGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CityDAO.class).to(CityDAOImpl.class);
		bind(PlaceDetailsDAO.class).to(PlaceDetailsDAOImpl.class);
		bind(PlacesDAO.class).to(PlacesDAOImpl.class);
		bind(PhotoReferenceDAO.class).to(PhotoReferenceDAOImpl.class);
	}

}
