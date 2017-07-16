package com.interview.server.database;

import javax.persistence.EntityTransaction;

import com.interview.shared.City;

public class CityDAOImpl extends GenericCRUDManagerImpl<City, String> implements CityDAO {

	@Override
	public void saveCity(City city) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		create(city);
		transaction.commit();
	}

	@Override
	public City findCity(String cityName) {
		// EntityManagerFactory emf =
		// Persistence.createEntityManagerFactory("pu1");
		// EntityManager em = emf.createEntityManager();
		// EntityTransaction transaction = em.getTransaction();
		// transaction.begin();
		// City result = em.find(City.class, cityName);
		// transaction.commit();
		// em.close();
		// emf.close();
		return read(cityName);
	}

}
