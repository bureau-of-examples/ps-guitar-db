package com.guitar.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.ModelJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Model;
import com.guitar.db.repository.ModelRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelPersistenceTests {
	@Autowired
	private ModelRepository modelRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Model m = new Model();
		m.setFrets(10);
		m.setName("Test Model");
		m.setPrice(BigDecimal.valueOf(55L));
		m.setWoodType("Maple");
		m.setYearFirstMade(new Date());
		m = modelRepository.create(m);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Model otherModel = modelRepository.find(m.getId());
		assertEquals("Test Model", otherModel.getName());
		assertEquals(10, otherModel.getFrets());
		
		//delete BC location now
		modelRepository.delete(otherModel);
	}

    @Autowired
	private ModelJpaRepository modelJpaRepository;

    @Test
	public void testGetModelsInPriceRange() throws Exception {
		List<Model> mods = modelJpaRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(new BigDecimal("1000"), new BigDecimal("2000")); //modelRepository.getModelsInPriceRange(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L));
		assertEquals(4, mods.size());
	}

	@Test
	public void testGetModelsByPriceRangeAndWoodType() throws Exception {
		List<Model> mods = modelRepository.getModelsByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "Maple");
		assertEquals(3, mods.size());
	}

	@Test
	public void testGetModelsByType() throws Exception {
		List<Model> mods = modelRepository.getModelsByType("Electric");
		assertEquals(4, mods.size());
	}

    @Test
    public void testFindAllModelsByType() throws Exception {
        List<Model> mods = modelJpaRepository.findAllModelsByType("Electric");
        assertEquals(4, mods.size());
    }


    @Transactional
    @Test
    public void testFindModelTypeIn(){
        List<String> types = Arrays.asList("Electric", "Acoustic");
        List<Model> mods = modelJpaRepository.findByModelTypeNameIn(types);
        assertTrue(mods.size() > 0);
        for (Model model : mods){
            assertTrue(types.contains(model.getModelType().getName()));
        }
    }

	@Transactional
	@Test
	public void testQueryByPriceRangeAndWoodType(){
        List<Model> mods = modelJpaRepository.queryByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "%Maple%");
        assertEquals(3, mods.size());
	}
}
