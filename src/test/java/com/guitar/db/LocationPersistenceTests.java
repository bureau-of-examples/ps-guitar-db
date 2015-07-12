package com.guitar.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.LocationJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;

import static org.junit.Assert.*;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {

	@PersistenceContext
	private EntityManager entityManager;

    @Autowired
    private LocationJpaRepository locationJpaRepository;

    @Test
    @Transactional
    public void testJpaFind(){
        List<Location> locations = locationJpaRepository.findAll();
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
    }

    @Test
    @Transactional
    public void testJpaAnd(){
        List<Location> locations = locationJpaRepository.findByStateAndCountry("Utah", "United States");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertEquals("Utah", location.getState());
            assertEquals("United States", location.getCountry());
        }
    }

    @Test
    @Transactional
    public void testJpaOr(){
        List<Location> locations = locationJpaRepository.findByStateOrCountry("Utah", "United States");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertTrue("Utah".equals(location.getState()) || "United States".equals(location.getCountry()));
        }
    }

    @Test
    @Transactional
    public void testJpaNot(){
        List<Location> locations = locationJpaRepository.findByStateNot("Utah");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertNotSame("Utah", location.getState());
        }
    }

    @Test
    @Transactional
    public void testJpaNotLike(){
        List<Location> locations = locationJpaRepository.findByStateNotLike("New%");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertFalse(location.getState().startsWith("New"));
        }
    }

    @Test
    @Transactional
    public void testJpaIgnoreCase(){
        List<Location> locations = locationJpaRepository.findByStateIgnoreCase("utah");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertEquals("Utah", location.getState());
        }
    }

    @Test
    @Transactional
    public void testJpaOrderBy(){
        List<Location> locations = locationJpaRepository.findByStateLikeOrderByStateAsc("New%");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        Location previousLocation = null;
        for(Location location : locations){
            if(previousLocation != null){
                assertTrue(previousLocation.getState().compareTo(location.getState()) <= 0);
            }
            previousLocation = location;
        }
    }

    @Test
    @Transactional
    public void testJpaFirst(){
        Location location = locationJpaRepository.findFirstByStateLikeOrderByStateAsc("New%");
        assertNotNull(location);
        assertEquals("New Hampshire", location.getState());
    }

    @Test
    @Transactional
    public void testJpaStartingWith(){
        List<Location> locations = locationJpaRepository.findByStateStartingWith("New");
        assertNotNull(locations);
        assertTrue(locations.size() > 0);
        for(Location location : locations){
            assertTrue(location.getState().startsWith("New"));
        }
    }

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJpaRepository.saveAndFlush(location);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Location otherLocation = locationJpaRepository.findOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
        locationJpaRepository.delete(otherLocation);
	}

    @Test
    public void testFindWithLike() throws Exception {
        List<Location> locs = locationJpaRepository.findByStateLike("New%");
        assertEquals(4, locs.size());
    }

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJpaRepository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}


}
