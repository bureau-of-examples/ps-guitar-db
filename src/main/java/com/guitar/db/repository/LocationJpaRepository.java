package com.guitar.db.repository;

import com.guitar.db.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LocationJpaRepository extends JpaRepository<Location, Long> {

    /**
     * Custom finder
     */
    List<Location> findByStateLike(String stateName);
}
