package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.domain.GPS;
import com.example.demo.domain.Track;
import com.example.demo.domain.WayPoint;
import com.example.demo.repository.GPSRepository;
import com.example.demo.repository.TrackRepository;
import com.example.demo.repository.WayPointRepository;

@Service
public class GPSService {

	@Autowired
	GPSRepository gpsRepository;
	
	@Autowired
	TrackRepository trackRepository;
	
	@Autowired
	WayPointRepository wayPointRepository;

	@Transactional
	public GPS saveGPSDataFromGPXContent(GPS gps) {
		
		GPS savedGPS = gpsRepository.save(gps);
		List<Track> tracks = gps.getTracks();
		List<WayPoint> waypoints = gps.getWaypoints();
		
		if(!CollectionUtils.isEmpty(tracks)) {
			tracks.forEach(track -> track.setGps(savedGPS));
			trackRepository.save(tracks);
		}
		
		if(!CollectionUtils.isEmpty(waypoints)) {
			waypoints.forEach(wp -> wp.setGps(savedGPS));
			wayPointRepository.save(waypoints);
		}
		return savedGPS;
	}
	
	public GPS findByIdWithTracksAndWayPoints(Long id) {
		GPS gps = gpsRepository.findOne(id);
		if(gps != null) {
			gps.setWaypoints(wayPointRepository.findByGpsId(id));
			gps.setTracks(trackRepository.findByGpsId(id));
		}
		return gps;
	}
	
	public List<GPS> findAllGPS(Integer pageNo, Integer pageSize, Sort sort) {
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		Page<GPS> result = gpsRepository.findAll(pageable);
		if(result.hasContent()) {
			return result.getContent();
		}
		return new ArrayList<>();
	}
}
