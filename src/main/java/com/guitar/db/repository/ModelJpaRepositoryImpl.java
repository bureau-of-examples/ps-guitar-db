package com.guitar.db.repository;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class ModelJpaRepositoryImpl implements ModelJpaRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    @Override
    public void aCustomMethod() {
        System.out.println("Entity manager implementation class is:" + entityManager.getClass());
    }
}
