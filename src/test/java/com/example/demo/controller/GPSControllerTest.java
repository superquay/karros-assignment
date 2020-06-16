package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repository.GPSRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GPSControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private static final String RESOURCE_FOLDER = "src/test/resources/";
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mock(GPSRepository.class, withSettings().verboseLogging());
	}

	@Test
	public void testUploadEmptyFile_thenReturnBadRequest() throws Exception {
		File emptyFile = new File(RESOURCE_FOLDER + "empty-file.gpx");

		MockMultipartFile mockEmptyFile = new MockMultipartFile("file", "empty-file.gpx",
				MediaType.MULTIPART_FORM_DATA.getType(), new FileInputStream(emptyFile));
		this.mockMvc.perform(fileUpload("/gps/upload").file(mockEmptyFile)).andExpect(status().isBadRequest())
				.andExpect(content().string("File is empty"));

	}

	@Test
	public void testUploadWrongExtension_thenReturnBadRequest() throws Exception {
		File testFile = new File(RESOURCE_FOLDER + "wrong-format.gg");

		MockMultipartFile mockFile = new MockMultipartFile("file", "wrong-format.gg",
				MediaType.MULTIPART_FORM_DATA.getType(), new FileInputStream(testFile));
		this.mockMvc.perform(fileUpload("/gps/upload").file(mockFile)).andExpect(status().isBadRequest())
				.andExpect(content().string("Invalid file extension"));
	}

	@Test
	public void testUploadCorruptedFile_thenReturnServerError() throws Exception {
		File testFile = new File(RESOURCE_FOLDER + "can-not-parse.gpx");
		MockMultipartFile mockFile = new MockMultipartFile("file", "can-not-parse.gpx",
				MediaType.MULTIPART_FORM_DATA.getType(), new FileInputStream(testFile));
		this.mockMvc.perform(fileUpload("/gps/upload").file(mockFile)).andExpect(status().is5xxServerError())
				.andExpect(content().string("Can not parse GPX file"));
	}
	
	@Test
	public void testUploadSampleFile_thenReturnOK() throws Exception {
		File testFile = new File(RESOURCE_FOLDER + "sample.gpx");
		MockMultipartFile mockFile = new MockMultipartFile("file", "sample.gpx",
				MediaType.MULTIPART_FORM_DATA.getType(), new FileInputStream(testFile));
		this.mockMvc.perform(fileUpload("/gps/upload").file(mockFile)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Your file was saved successfully to DB, with gpsId =")));
	}
	
	@Test
	public void testGetUnexistedGPS_thenReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/gps/100"))
		.andExpect(status().isNotFound())
		.andExpect(content().string("GPS not found"));
	}
	
}
