package com.guitar.db.repository;

import com.guitar.db.model.Model;
import com.guitar.db.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ModelJpaRepository extends JpaRepository<Model, Long> {

    /**
     * Custom finder
     */
    List<Model> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal lowest, BigDecimal highest);

    List<Model> findByModelTypeNameIn(Collection<String> modelTypes);
}
