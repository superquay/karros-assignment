package com.example.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.example.demo.domain.GPS;
import com.example.demo.service.GPSService;
import com.example.demo.service.GPXParser;

@RestController
public class GPSController {
	
	@Autowired
	GPSService gpsService;
	
	@PostMapping("/gps/upload")
	public ResponseEntity<?> uploadGPXFile(@RequestParam("file") MultipartFile file) {
		if(file == null || file.getSize() == 0) {
			return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
		}
		if(!file.getOriginalFilename().endsWith(".gpx")) {
			
			return new ResponseEntity<>("Invalid file extension", HttpStatus.BAD_REQUEST);
		}
		GPS gps = null;
		try {
			gps = GPXParser.parseGPXStream(file.getInputStream());
			gpsService.saveGPSDataFromGPXContent(gps);
		} catch (IOException | ParserConfigurationException | ParseException | SAXException e) {
			return new ResponseEntity<>("Can not parse GPX file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Your file was saved successfully to DB, with gpsId = " + gps.getId(), HttpStatus.OK);
	}
	
	@GetMapping("/gps/{id}")
	public ResponseEntity<?> getGPSById(@PathVariable Long id) {
		GPS gps = gpsService.findByIdWithTracksAndWayPoints(id);
		if(gps == null) {
			return new ResponseEntity<>("GPS not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(gpsService.findByIdWithTracksAndWayPoints(id), HttpStatus.OK);
	}
	
	@GetMapping("/gps/latest")
	public ResponseEntity<List<GPS>> findLatestGPS(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize) {
		Sort defaultSort = new Sort(Direction.DESC, "id");
		List<GPS> results = gpsService.findAllGPS(pageNo, pageSize, defaultSort);
		return new ResponseEntity<List<GPS>>(results, HttpStatus.OK);
	}
}
