package com.kosuke.todo;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.kosuke.image.Image;
import com.kosuke.image.ImageService;

@SpringBootTest
class TodoControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
	TodoController todoController;
	
	@Autowired
	ImageService imageService;
	
	@BeforeEach
	void setUpBeforeClass() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
	}

	@Test
	@Disabled
	public void download() {
		HttpServletResponse response = mock(HttpServletResponse.class); 
//		TodoController todo = new TodoController();
		ResponseEntity<StreamingResponseBody> responseEntity = todoController.download(8, response);
		Assertions.assertNotNull(responseEntity);
	}
	
	@Test
	@Disabled
	public void saveImageTest() throws Exception {
		
		MockMultipartFile file1 = new MockMultipartFile(
				"multipartFiles", "file1.txt", "text/plain", "text1".getBytes());
		MockMultipartFile file2 = new MockMultipartFile(
				"multipartFiles", "file2.txt", "text/plain", "text2".getBytes());
		
		List<MultipartFile> files = new ArrayList<>();
		files.add(file1);
		files.add(file2);
		
		Image image = new Image(files);
//		mockMvc.perform(
//				multipart("/task/image/1")
//				.file("file", file.getBytes())
//				)
		mockMvc.perform(
				post("/image/save/1")
				.flashAttr("image", image)
				)
		.andExpect(status().isOk());
	}
	
	@Test
	@Disabled
	public void getAllImageTest() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/image/getAll/1")
				)
		.andExpect(status().isOk())
		.andReturn()
		;
		Assertions.assertNotNull(result.getResponse());
	}
	
	@Test
	public void getImageFindNameTest() throws Exception {
//		MvcResult result = 
				mockMvc.perform(
				get("/image/file/1")
				)
		.andExpect(status().isOk())
//		.andReturn()
		;		
	}
	

}
