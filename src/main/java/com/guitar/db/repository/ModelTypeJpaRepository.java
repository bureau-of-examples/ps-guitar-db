package com.guitar.db.repository;

import com.guitar.db.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ModelTypeJpaRepository extends JpaRepository<ModelType, Long>{

    List<ModelType> findByNameNotNull();

    List<ModelType> findByNameIsNull();

}
