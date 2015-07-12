package com.guitar.db.repository;

import com.guitar.db.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LocationJpaRepository extends JpaRepository<Location, Long> {

    /**
     * Custom finder
     */
    List<Location> findByStateLike(String stateName);

    List<Location> findByStateLikeOrderByStateAsc(String stateName);

    Location findFirstByStateLikeOrderByStateAsc(String stateName);

    List<Location> findByStateIgnoreCase(String stateName);

    List<Location> findByStateStartingWith(String stateName);//EndingWith, Containing

    List<Location> findByStateNotLike(String stateName);

    List<Location> findByStateNot(String stateName);

    List<Location> findByStateOrCountry(String stateName, String countryName);

    List<Location> findByStateAndCountry(String stateName, String countryName);
}
