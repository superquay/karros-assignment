package com.example.demo.controller;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.GPS;
import com.example.demo.service.GPSService;
import com.example.demo.service.GPXParser;

@RestController
public class GPSController {
	
	private static final Logger logger = Logger.getLogger(GPSController.class.getName());
	@Autowired
	GPSService gpsService;
	
	@PostMapping("/gps/upload")
	public ResponseEntity<String> uploadGPXFile(@RequestParam("file") MultipartFile file) {
		if(file == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		GPS gps = null;
		try {
			gps = GPXParser.parseGPXStream(file.getInputStream());
			gpsService.saveGPSDataFromGPXContent(gps);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Your file was saved successfully to DB, with gpsId = " + gps.getId(), HttpStatus.OK);
	}
	
	@GetMapping("/gps/{id}")
	public ResponseEntity<GPS> getGPSById(@PathVariable Long id) {
		return new ResponseEntity<GPS>(gpsService.findById(id), HttpStatus.OK);
	}
}
