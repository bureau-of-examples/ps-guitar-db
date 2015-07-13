package com.guitar.db.repository;

import com.guitar.db.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ModelTypeJpaRepository extends JpaRepository<ModelType, Long>{

    List<ModelType> findByNameNotNull();

    List<ModelType> findByNameIsNull();

    @Modifying
    @Query("update ModelType m set m.name = ?2 where m.name = ?1 or m.name is null and ?1 is null")
    int updateByName(String name, String newName);

}
