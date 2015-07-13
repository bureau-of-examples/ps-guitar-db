package com.guitar.db.repository;

import com.guitar.db.model.Model;
import com.guitar.db.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface ModelJpaRepository extends JpaRepository<Model, Long> {

    /**
     * Custom finder
     */
    List<Model> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal lowest, BigDecimal highest);

    List<Model> findByModelTypeNameIn(Collection<String> modelTypes);

    @Query("select  m from Model m where m.price between :lowest and :highest and m.woodType like :wood")
    List<Model> queryByPriceRangeAndWoodType(@Param("lowest") BigDecimal lowest, @Param("highest") BigDecimal highest, @Param("wood") String wood);

    List<Model> findAllModelsByType(@Param("name") String typeName);
}
