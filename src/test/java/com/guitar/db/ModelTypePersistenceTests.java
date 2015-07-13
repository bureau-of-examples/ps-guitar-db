package com.guitar.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.ModelTypeJpaRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.ModelType;

import java.util.List;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelTypePersistenceTests {
	@Autowired
	private ModelTypeJpaRepository modelTypeRepository;
	//private ModelTypeRepository modelTypeRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		ModelType mt = new ModelType();
		mt.setName("Test Model Type");
		mt = modelTypeRepository.save(mt);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		ModelType otherModelType = modelTypeRepository.findOne(mt.getId());
		assertEquals("Test Model Type", otherModelType.getName());
		
		modelTypeRepository.delete(otherModelType);
	}

	@Test
	public void testFind() throws Exception {
		ModelType mt = modelTypeRepository.findOne(1L);
		assertEquals("Dreadnought Acoustic", mt.getName());
	}

	@Test
	public void testNullNotNull(){
		List<ModelType> all = modelTypeRepository.findAll();
		List<ModelType> nullOnes = modelTypeRepository.findByNameIsNull();
		List<ModelType> notNullOnes = modelTypeRepository.findByNameNotNull();

		assertTrue(nullOnes.size() > 0);
        for(ModelType modelType : nullOnes){
            assertNull(modelType.getName());
        }
		assertEquals(all.size(), nullOnes.size() + notNullOnes.size());
	}

	@Test
	@Transactional
	public void testJpaModifying(){
		int rowsUpdated = modelTypeRepository.updateByName(null, "RunningMan");
		assertEquals(1, rowsUpdated);

		rowsUpdated = modelTypeRepository.updateByName("RunningMan", null);
		assertEquals(1, rowsUpdated);
	}
}
