package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.GPS;
import com.example.demo.domain.Track;
import com.example.demo.domain.WayPoint;
import com.example.demo.repository.GPSRepository;
import com.example.demo.repository.TrackRepository;
import com.example.demo.repository.WayPointRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GPSServiceTest {
	
	@Autowired
	GPSService gpsService;
	
	@Autowired
	GPSRepository gpsRepository;
	
	@Autowired
	WayPointRepository wayPointRepository;
	
	@Autowired
	TrackRepository trackRepository;

	@Before
	public void beforeTest() {
		wayPointRepository.deleteAll();
		trackRepository.deleteAll();
		gpsRepository.deleteAll();
	}
	
	@Test
	public void testSaveGPSDataFromGPXContent() {
		GPS gps = createTestData();
		gpsService.saveGPSDataFromGPXContent(gps);
		assertNotNull(gps.getId());
		assertEquals(1, wayPointRepository.findByGpsId(gps.getId()).size());
		assertEquals(1, trackRepository.findByGpsId(gps.getId()).size());
	}
	
	@Test
	public void testFindByIdWithTracksAndWayPoints() {
		GPS gps = createTestData();
		gpsService.saveGPSDataFromGPXContent(gps);
		assertNotNull(gps.getId());
		
		GPS foundGPS = gpsService.findByIdWithTracksAndWayPoints(gps.getId());
		assertNotNull(foundGPS);
		assertEquals("Test Author", foundGPS.getAuthor());
		assertEquals("Test Name", foundGPS.getName());
		assertEquals(1, foundGPS.getWaypoints().size());
		assertEquals(1, foundGPS.getTracks().size());
		
		foundGPS = gpsService.findByIdWithTracksAndWayPoints(10000L);
		assertNull(foundGPS);
	}

	@Test
	public void testFindAllGPS() {
		GPS gps1 = new GPS();
		gps1.setAuthor("Test 1");
		GPS gps2 = new GPS();
		gps2.setAuthor("Test 2");
		gpsRepository.save(Arrays.asList(gps1, gps2));
		
		Sort defaultSort = new Sort(Direction.DESC, "id");
		List<GPS> foundGPSs = gpsService.findAllGPS(0, 10, defaultSort);
		assertEquals(2, foundGPSs.size());
		foundGPSs = gpsService.findAllGPS(1, 1, defaultSort);
		assertEquals(1, foundGPSs.size());
		foundGPSs = gpsService.findAllGPS(2, 1, defaultSort);
		assertEquals(0, foundGPSs.size());
	}
	
	private GPS createTestData() {
		GPS gps = new GPS();
		gps.setAuthor("Test Author");
		gps.setName("Test Name");
		
		WayPoint wp = new WayPoint();
		wp.setLattitude("1.000");
		wp.setLongtitude("2.000");
		gps.setWaypoints(Arrays.asList(wp));
		
		Track track = new Track();
		track.setElevation("3.000");
		track.setLattitude("4.000");
		track.setLongtitude("5.000");
		gps.setTracks(Arrays.asList(track));
		return gps;
	}
}
