package com.interview.server.database;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class GenericCRUDManagerImpl<T, PK extends Serializable> implements GenericCRUDManager<T, PK> {
	protected Class<T> entityClass;

	// @PersistenceContext(unitName = "pu1")
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public GenericCRUDManagerImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu1");
		entityManager = emf.createEntityManager();
	}

	@Override
	public T create(T t) {
		this.entityManager.persist(t);
		return t;
	}

	@Override
	public T read(PK id) {
		return this.entityManager.find(entityClass, id);
	}

	@Override
	public T update(T t) {
		return this.entityManager.merge(t);
	}

	@Override
	public void delete(T t) {
		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
	}
}
